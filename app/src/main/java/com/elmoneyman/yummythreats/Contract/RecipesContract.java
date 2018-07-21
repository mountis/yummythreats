package com.elmoneyman.yummythreats.Contract;

import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Presenter.BasePresenter;
import com.elmoneyman.yummythreats.View.BaseView;

import java.util.List;

public interface RecipesContract {

    interface Presenter extends BasePresenter<View> {
        void attachView(@NonNull View view);
        void start();
        void stop();
        void queryRecipes();
    }

    interface View extends BaseView<Presenter> {
        void attachPresenter(@NonNull Presenter presenter);
        void showRecipes(@NonNull List<Recipe> recipes);
        void showErrorMessage(@NonNull String message);
        void setLoading(boolean isLoading);
    }
}
