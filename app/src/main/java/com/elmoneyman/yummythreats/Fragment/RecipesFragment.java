package com.elmoneyman.yummythreats.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmoneyman.yummythreats.Adapter.RecipesAdapter;
import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Contract.RecipesContract;
import com.elmoneyman.yummythreats.Dagger.DaggerViewComponent;
import com.elmoneyman.yummythreats.Dagger.PresenterModule;
import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.R;
import com.elmoneyman.yummythreats.View.Decorations;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class RecipesFragment extends BaseFragment
        implements RecipesContract.View{

    private RecipesContract.Presenter presenter;
    private RecipesAdapter adapter;

    @BindView(R.id.recipe_list)
    protected RecyclerView recipeList;

    @BindView(R.id.refresher)
    protected SwipeRefreshLayout refresher;

    @Inject
    protected RxBus rxBus;

    @BindView(R.id.message)
    protected TextView messageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_recipes,container,false);
        bind(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            refresher.setOnRefreshListener(()->presenter.queryRecipes());
            adapter=new RecipesAdapter(getContext(),rxBus);
            recipeList.addItemDecoration(new Decorations(getContext()));
            recipeList.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null) adapter.resume();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Inject
    @Override
    public void attachPresenter(@NonNull RecipesContract.Presenter presenter) {
        this.presenter=presenter;
        this.presenter.attachView(this);
    }

    @Override
    public void setLoading(boolean isLoading) {
        refresher.setRefreshing(isLoading);
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            messageView.setAlpha(0.f);
        }
        initiateVisibility(false);
        messageView.setText(message);
        ViewCompat.animate(messageView)
                .alpha(1.f)
                .setDuration(R.integer.fade_in_duration)
                .start();
    }

    @Override
    public void showRecipes(@NonNull List<Recipe> recipes) {
        initiateVisibility(true);
        adapter.setData(recipes);
    }

    private void initiateVisibility(boolean initiateList){
        if(initiateList){
            recipeList.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
        }else{
            recipeList.setVisibility(View.GONE);
            messageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    void initializeDependencies() {
        DaggerViewComponent.builder()
                .presenterModule(new PresenterModule())
                .appComponent( App.appInstance().appComponent())
                .build().inject(this);
    }
}
