package com.elmoneyman.yummythreats.Presenter;


import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Contract.DetailsRecipeContract;
import com.elmoneyman.yummythreats.Dagger.ViewScope;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import javax.inject.Inject;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;


@ViewScope
public class DetailPresenter extends BaseRecipePresenter
        implements DetailsRecipeContract.Presenter{

    private DetailsRecipeContract.View view;

    @Inject
    public DetailPresenter(@NonNull RecipeService<Recipe> recipeService,
                           @NonNull BaseSchedulerProvider schedulerProvider,
                           @NonNull MessageNotificationProvider messageNotificationProvider){
        super(recipeService,schedulerProvider, messageNotificationProvider );
    }

    @Override
    public void attachView(@NonNull DetailsRecipeContract.View view) {
        checkNotNull(view);
        this.view=view;
    }

    @Override
    public void stop() {
        subscriptions.clear();
    }

    @Override
    public void fetchById(int recipeId) {
        subscriptions.add( recipeService.getRecipeById(recipeId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::recipeProcessor,this::errorHandler,()->{}));
    }

    private void recipeProcessor(Recipe recipe){
        if(recipe==null){
            view.showMessage( messageNotificationProvider.emptyMessage());
            return;
        }
        view.showRecipe(recipe);
    }

    private void errorHandler(Throwable error){
        error.printStackTrace();
        view.showMessage( messageNotificationProvider.errorMessage());
    }
}
