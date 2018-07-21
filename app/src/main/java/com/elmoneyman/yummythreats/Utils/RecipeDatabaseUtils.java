package com.elmoneyman.yummythreats.Utils;


import android.content.ContentValues;
import android.database.Cursor;

import com.elmoneyman.yummythreats.Data.RecipeContract;
import com.elmoneyman.yummythreats.Data.RecipeHelper;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.StepSerialized;

public class RecipeDatabaseUtils {

    public static ContentValues toValues(RecipeSerialized recipe){
        if(recipe==null) return null;
        ContentValues values=new ContentValues();
        values.put( RecipeContract.Recipes.RECIPE_ID,recipe.getId());
        values.put( RecipeContract.Recipes.RECIPE_NAME,recipe.getName());
        values.put( RecipeContract.Recipes.RECIPE_SERVINGS,recipe.getServings());
        values.put( RecipeContract.Recipes.RECIPE_IMAGE_URL,recipe.getImageUrl());
        return values;
    }

    public static ContentValues toValues(IngredientSerialized ingredient){
        if(ingredient==null) return null;
        ContentValues values=new ContentValues();
        values.put( RecipeContract.Ingredients.INGREDIENT_ID,ingredient.getId());
        values.put( RecipeContract.Ingredients.INGREDIENT_NAME,ingredient.getIngredient());
        values.put( RecipeContract.Ingredients.INGREDIENT_QUANTITY,ingredient.getQuantity());
        values.put( RecipeContract.Ingredients.INGREDIENT_MEASURE,ingredient.getMeasure());
        return values;
    }

    public static ContentValues toValues(IngredientSerialized ingredientSerialized,
                                         RecipeSerialized recipeSerialized){
        if(ingredientSerialized ==null|| recipeSerialized ==null) return null;
        ContentValues values=new ContentValues();
        values.put( RecipeHelper.RecipesIngredients.RECIPE_ID, recipeSerialized.getId());
        values.put( RecipeHelper.RecipesIngredients.INGREDIENT_ID, ingredientSerialized.getId());
        return values;
    }

    public static ContentValues toValues(StepSerialized step, int recipeId){
        if(step==null) return null;
        ContentValues values=new ContentValues();
        values.put( RecipeContract.Steps.STEP_ID,step.getId());
        values.put( RecipeContract.Steps.STEP_DESCRIPTION,step.getDescription());
        values.put( RecipeContract.Steps.STEP_SHORT_DESCRIPTION,step.getShortDescription());
        values.put( RecipeContract.Steps.STEP_VIDEO_URL,step.getVideoUrl());
        values.put( RecipeContract.Steps.STEP_IMAGE_URL,step.getImageUrl());
        values.put( RecipeContract.Steps.STEP_RECIPE_ID,recipeId);
        return values;
    }

    public static RecipeSerialized toRecipe(Cursor cursor){
        if(cursor==null) return null;
        RecipeSerialized recipeSerialized=new RecipeSerialized();
        String imageUrl=cursor.getString(cursor.getColumnIndex( RecipeContract.Recipes.RECIPE_IMAGE_URL));
        String recipeName=cursor.getString(cursor.getColumnIndex( RecipeContract.Recipes.RECIPE_NAME));
        int recipeId=cursor.getInt(cursor.getColumnIndex( RecipeContract.Recipes.RECIPE_ID));
        int recipeServings=cursor.getInt(cursor.getColumnIndex( RecipeContract.Recipes.RECIPE_SERVINGS));
        recipeSerialized.setImageUrl(imageUrl);
        recipeSerialized.setId(recipeId);
        recipeSerialized.setServings(recipeServings);
        recipeSerialized.setName(recipeName);
        return recipeSerialized;
    }

    public static IngredientSerialized toIngredient(Cursor cursor){
        if(cursor==null) return null;
        int ingredientId=cursor.getInt(cursor.getColumnIndex( RecipeContract.Ingredients.INGREDIENT_ID));
        String ingredientName=cursor.getString(cursor.getColumnIndex( RecipeContract.Ingredients.INGREDIENT_NAME));
        String measure=cursor.getString(cursor.getColumnIndex( RecipeContract.Ingredients.INGREDIENT_MEASURE));
        float quantity=cursor.getFloat(cursor.getColumnIndex( RecipeContract.Ingredients.INGREDIENT_QUANTITY));
        IngredientSerialized ingredientSerialized=new IngredientSerialized();
        ingredientSerialized.setIngredient(ingredientName);
        ingredientSerialized.setMeasure(measure);
        ingredientSerialized.setId(ingredientId);
        ingredientSerialized.setQuantity(quantity);
        return ingredientSerialized;
    }

    public static StepSerialized toStep(Cursor cursor){
        if(cursor==null) return null;
        int stepId=cursor.getInt(cursor.getColumnIndex( RecipeContract.Steps.STEP_ID));
        String videoUrl=cursor.getString(cursor.getColumnIndex( RecipeContract.Steps.STEP_VIDEO_URL));
        String imageUrl=cursor.getString(cursor.getColumnIndex( RecipeContract.Steps.STEP_IMAGE_URL));
        String description=cursor.getString(cursor.getColumnIndex( RecipeContract.Steps.STEP_DESCRIPTION));
        String shortDescription=cursor.getString(cursor.getColumnIndex( RecipeContract.Steps.STEP_SHORT_DESCRIPTION));
        StepSerialized stepSerialized=new StepSerialized();
        stepSerialized.setId(stepId);
        stepSerialized.setDescription(description);
        stepSerialized.setShortDescription(shortDescription);
        stepSerialized.setImageUrl(imageUrl);
        stepSerialized.setVideoUrl(videoUrl);
        return stepSerialized;
    }

}
