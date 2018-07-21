package com.elmoneyman.yummythreats.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmoneyman.yummythreats.Adapter.StepsAdapter;
import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Contract.DetailsRecipeContract;
import com.elmoneyman.yummythreats.Dagger.DaggerViewComponent;
import com.elmoneyman.yummythreats.Dagger.PresenterModule;
import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Listeners.StepsOnClick;
import com.elmoneyman.yummythreats.Listeners.ToolbarChangeEvent;
import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.R;
import com.elmoneyman.yummythreats.Utils.RecipeConstants;
import com.elmoneyman.yummythreats.View.Decorations;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class DetailFragment extends BaseFragment
        implements DetailsRecipeContract.View{

    private DetailsRecipeContract.Presenter presenter;
    private StepsAdapter adapter;
    private int recipeId;

    @BindView(R.id.ingredient_list)
    protected TextView ingredients;

    @BindView(R.id.recipe_steps)
    protected RecyclerView recipeSteps;

    @Inject
    protected RxBus rxBus;

    public static DetailFragment newInstance(Bundle bundle){
        DetailFragment fragment=new DetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            savedInstanceState=getArguments();
        }
        this.recipeId=savedInstanceState.getInt( RecipeConstants.EXTRA_RECIPE_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_details,container,false);
        bind(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null) adapter.resume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            adapter=new StepsAdapter(getContext(),rxBus);
            recipeSteps.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            recipeSteps.addItemDecoration(new Decorations(getContext()));
            recipeSteps.setAdapter(adapter);
            recipeSteps.setNestedScrollingEnabled(false);
            presenter.fetchById(recipeId);
        }
    }

    @Override
    void initializeDependencies() {
        DaggerViewComponent.builder()
                .presenterModule(new PresenterModule())
                .appComponent( App.appInstance().appComponent())
                .build().inject(this);
    }

    @Inject
    @Override
    public void attachPresenter(@NonNull DetailsRecipeContract.Presenter presenter) {
        this.presenter=presenter;
        this.presenter.attachView(this);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void showRecipe(@NonNull com.elmoneyman.yummythreats.Model.Recipe recipe) {
        rxBus.send( ToolbarChangeEvent.change(recipe.getName()));
        viewSteps(recipe.getSteps());
        viewIngredients(recipe.getIngredients());
        if(isTablet) rxBus.send( StepsOnClick.click(recipe.getSteps(),0));
    }

    private void viewIngredients(@NonNull List<Ingredient> ingredientList){
        StringBuilder builder=new StringBuilder();
        final String BLANK=" ";
        final String bullet="\u25CF";
        for(Ingredient ingredient:ingredientList){
            builder.append(bullet);
            builder.append(BLANK);
            builder.append(ingredient.getQuantity());
            builder.append(BLANK);
            builder.append(ingredient.getMeasure());
            builder.append(BLANK);
            builder.append(ingredient.getIngredient());
            builder.append('\n');
            builder.append('\n');
        }
        ingredients.setText(builder.toString());
    }

    public void highlightStep(int step){
        adapter.resume();
        adapter.highlightPosition(step);
    }

    private void viewSteps(@NonNull List<Step> steps){
        adapter.setData(steps);
    }
}
