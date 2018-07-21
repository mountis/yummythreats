package com.elmoneyman.yummythreats.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Activities.DetailsActivity;
import com.elmoneyman.yummythreats.Utils.RecipeConstants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeItem {

    @Inject
    public RecipeItem(){}

    public void goToRecipeDetails(@NonNull Activity activity,
                                  @NonNull RecipeOnClick recipeOnClick){
        Intent intent=new Intent(activity, DetailsActivity.class);
        intent.putExtra( RecipeConstants.EXTRA_RECIPE_ID, recipeOnClick.recipeId);
        activity.startActivity(intent);
    }
}