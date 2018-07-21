package com.elmoneyman.yummythreats.Presenter;


import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Contract.RecipesContract;
import com.elmoneyman.yummythreats.Dagger.ViewScope;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import java.util.List;

import javax.inject.Inject;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

@ViewScope
public class RecipesPresenter extends BaseRecipePresenter
        implements RecipesContract.Presenter {

    private RecipesContract.View view;

    @Inject
    public RecipesPresenter(@NonNull RecipeService<Recipe> recipeService,
                            @NonNull BaseSchedulerProvider schedulerProvider,
                            @NonNull MessageNotificationProvider messageNotificationProvider){
        super(recipeService,schedulerProvider, messageNotificationProvider );
    }

    @Override
    public void attachView(@NonNull RecipesContract.View view) {
        checkNotNull(view);
        this.view=view;
    }

    @Override
    public void queryRecipes() {
        subscriptions.clear();
        loadRecipes();
    }

    private void loadRecipes(){
        view.setLoading(true);
        subscriptions.add( recipeService.getRecipes()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::recipeProcessing,this::errorHandling,this::complete));
    }

    private void recipeProcessing(List<Recipe> recipes){
        if(recipes==null||recipes.isEmpty()){
            view.showErrorMessage( messageNotificationProvider.emptyMessage());
            return;
        }
        view.showRecipes(recipes);
    }

    private void errorHandling(Throwable error){
        subscriptions.clear();
        error.printStackTrace();
        view.setLoading(false);
        view.showErrorMessage( messageNotificationProvider.noConnectionMessage());
    }

    private void complete(){
        view.setLoading(false);
    }

    @Override
    public void start() {
        loadRecipes();
    }

    @Override
    public void stop() {
        subscriptions.clear();
    }
}
