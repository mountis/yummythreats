package com.elmoneyman.yummythreats.Contract;

import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Presenter.BasePresenter;
import com.elmoneyman.yummythreats.View.BaseView;

public interface DetailsRecipeContract {

    interface Presenter extends BasePresenter<View> {
        void attachView(@NonNull View view);
        void stop();
        void fetchById(int recipeId);
    }

    interface View extends BaseView<Presenter> {
        void attachPresenter(@NonNull Presenter presenter);
        void showRecipe(@NonNull Recipe recipe);
        void showMessage(@NonNull String message);
    }
}
