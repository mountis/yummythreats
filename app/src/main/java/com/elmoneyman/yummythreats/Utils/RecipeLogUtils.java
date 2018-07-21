package com.elmoneyman.yummythreats.Utils;


import android.util.Log;

import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import java.util.List;

public class RecipeLogUtils {

    private RecipeLogUtils(){
        throw new UnsupportedOperationException();
    }

    public static void logD(Object object,Class<?> aClass){
        Log.d(aClass.getSimpleName(),object.toString());
    }

    public static void logD(List<Recipe> recipes, Object aClass){
        final String TAG=aClass.getClass().getSimpleName()+":";
        if(recipes==null){
            Log.d(TAG,"Recipes are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(recipes.size()));
        for (Recipe recipe : recipes) {
            logD( recipe, TAG );
        }
    }

    public static void log(List<RecipeSerialized> recipes, Object aClass){
        final String TAG=aClass.getClass().getSimpleName()+":";
        if(recipes==null){
            Log.d(TAG,"Recipes are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(recipes.size()));
        for (RecipeSerialized recipeSerialized : recipes) {
            logD( recipeSerialized, TAG );
        }
    }

    public static void logD(Recipe recipe, String TAG){
        if(recipe==null) {
            Log.d(TAG,"#recipe is null");
            return;
        }
        Log.d(TAG,"#recipe id:"+Integer.toString(recipe.getId()));
        Log.d(TAG,"#recipe name:"+recipe.getName());
        Log.d(TAG,"#recipe image url:"+recipe.getImageUrl());
        Log.d(TAG,"#recipe servings:"+Integer.toString(recipe.getServings()));
        Log.d(TAG,"-------------------------------------------");
        logDI(recipe.getIngredients(),TAG);
        logD(recipe.getSteps(),TAG);
    }

    public static void logD(RecipeSerialized recipe, String TAG){
        if(recipe==null) {
            Log.d(TAG,"#recipe is null");
            return;
        }
        Log.d(TAG,"#recipe id:"+Integer.toString(recipe.getId()));
        Log.d(TAG,"#recipe name:"+recipe.getName());
        Log.d(TAG,"#recipe image url:"+recipe.getImageUrl());
        Log.d(TAG,"#recipe servings:"+Integer.toString(recipe.getServings()));
        log(recipe.getIngredients(),TAG);
        logDS(recipe.getSteps(),TAG);
    }

    public static void logD(Ingredient ingredient, String TAG){
        if(ingredient==null){
            Log.d(TAG,"#ingredient is null");
            return;
        }
        Log.d(TAG,"#ingredient id:"+Integer.toString(ingredient.getId()));
        Log.d(TAG,"#ingredient ingredient:"+ingredient.getIngredient());
        Log.d(TAG,"#ingredient measure:"+ingredient.getMeasure());
        Log.d(TAG,"#ingredient quantity:"+ingredient.getQuantity());
    }

    public static void logD(IngredientSerialized ingredient, String TAG){
        if(ingredient==null){
            Log.d(TAG,"#ingredient is null");
            return;
        }
        Log.d(TAG,"#ingredient id:"+Integer.toString(ingredient.getId()));
        Log.d(TAG,"#ingredient ingredient:"+ingredient.getIngredient());
        Log.d(TAG,"#ingredient measure:"+ingredient.getMeasure());
        Log.d(TAG,"#ingredient quantity:"+ingredient.getQuantity());
    }

    public static void logDI(List<Ingredient> ingredients, String TAG){
        if(ingredients==null){
            Log.d(TAG,"Ingredients are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(ingredients.size()));
        for (Ingredient ingredient : ingredients) {
            logD( ingredient, TAG );
        }
    }

    public static void log(List<IngredientSerialized> ingredients, String TAG){
        if(ingredients==null){
            Log.d(TAG,"Ingredients are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(ingredients.size()));
        for (IngredientSerialized ingredientEntity : ingredients) {
            logD( ingredientEntity, TAG );
        }
    }

    public static void logD(List<Step> steps, String TAG){
        if(steps==null){
            Log.d(TAG,"Steps are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(steps.size()));
        for (Step step : steps) {
            logD( step, TAG );
        }
    }

    public static void logDS(List<StepSerialized> steps, String TAG){
        if(steps==null){
            Log.d(TAG,"Steps are null");
            return;
        }
        Log.d(TAG,"Size is:"+Integer.toString(steps.size()));
        for (StepSerialized step : steps) {
            logD( step, TAG );
        }
    }

    public static void logD(Step step,String TAG){
        if(step==null){
            Log.d(TAG,"#step is null");
            return;
        }
        Log.d(TAG,"#step id:"+step.getStepId());
        Log.d(TAG,"#step image url:"+step.getImageUrl());
        Log.d(TAG,"#step description:"+step.getDescription());
        Log.d(TAG,"#step short desc:"+step.getShortDescription());
        Log.d(TAG,"#step video url:"+step.getVideoUrl());
    }

    public static void logD(StepSerialized step, String TAG){
        if(step==null){
            Log.d(TAG,"#step is null");
            return;
        }
        Log.d(TAG,"#step id:"+step.getId());
        Log.d(TAG,"#step image url:"+step.getImageUrl());
        Log.d(TAG,"#step description:"+step.getDescription());
        Log.d(TAG,"#step short desc:"+step.getShortDescription());
        Log.d(TAG,"#step video url:"+step.getVideoUrl());
    }

}
