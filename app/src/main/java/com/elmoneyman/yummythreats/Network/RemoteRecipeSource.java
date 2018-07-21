package com.elmoneyman.yummythreats.Network;

import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Utils.RecipeLogUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class RemoteRecipeSource extends RecipeDataSource<RecipeSerialized> {

    private final RecipeAPIManager recipeAPIManager;

    @Inject
    public RemoteRecipeSource(@NonNull RecipeAPIManager recipeAPIManager){
        this.recipeAPIManager = recipeAPIManager;
    }

    @Override
    public Observable<List<RecipeSerialized>> getRecipes() {
        return recipeAPIManager.queryRecipes()
                .doOnNext(list-> RecipeLogUtils.log(list,this));
    }

    @Override
    public void insert(RecipeSerialized item) {}
}
