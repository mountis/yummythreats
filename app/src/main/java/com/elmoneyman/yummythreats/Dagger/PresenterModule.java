package com.elmoneyman.yummythreats.Dagger;

import android.content.Context;

import com.elmoneyman.yummythreats.Contract.DetailsRecipeContract;
import com.elmoneyman.yummythreats.Contract.RecipesContract;
import com.elmoneyman.yummythreats.Exoplayer.ExoPlayback;
import com.elmoneyman.yummythreats.Exoplayer.ExoMediaPlayback;
import com.elmoneyman.yummythreats.Presenter.RecipesPresenter;
import com.elmoneyman.yummythreats.Presenter.DetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {


    @ViewScope
    @Provides
    RecipesContract.Presenter recipesPresenter(RecipesPresenter presenter){
        return presenter;
    }

    @ViewScope
    @Provides
    DetailsRecipeContract.Presenter recipeDetailsPresenter(DetailPresenter presenter){
        return presenter;
    }

    @ViewScope
    @Provides
    ExoPlayback<?> providePlayback(Context context){
        return new ExoMediaPlayback(context);
    }

}
