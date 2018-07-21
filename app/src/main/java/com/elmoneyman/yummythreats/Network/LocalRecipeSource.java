package com.elmoneyman.yummythreats.Network;


import com.elmoneyman.yummythreats.Model.RecipeSerialized;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class LocalRecipeSource extends RecipeDataSource<RecipeSerialized> {

    private RecipeManager handler;

    @Inject
    public LocalRecipeSource(RecipeManager handler){
        this.handler=handler;
    }

    @Override
    public Observable<List<RecipeSerialized>> getRecipes() {
        return Observable.fromCallable(()->handler.queryAll());
    }

    @Override
    public void insert(RecipeSerialized item) {
        if(item!=null) {
            int recipeId=item.getId();
            handler.insert(item)
                    .insertIngredients(recipeId, item.getIngredients())
                    .insertSteps(recipeId, item.getSteps());
        }
    }
}
