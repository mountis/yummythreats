package com.elmoneyman.yummythreats.Network;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.elmoneyman.yummythreats.Data.RecipeContract;
import com.elmoneyman.yummythreats.Data.RecipeHelper;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.StepSerialized;
import com.elmoneyman.yummythreats.Utils.RecipeDatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class RecipeManager {

    private ContentResolver contentResolver;

    private static final String TAG=RecipeManager.class.getSimpleName();

    private RecipeManager(ContentResolver resolver){
        this.contentResolver=resolver;
    }

    public static RecipeManager start(ContentResolver resolver){
        return new RecipeManager(resolver);
    }

    public RecipeManager insert(RecipeSerialized recipeSerialized){
        if(recipeSerialized !=null){
            ContentValues values= RecipeDatabaseUtils.toValues( recipeSerialized );
            contentResolver.insert( RecipeContract.Recipes.CONTENT_URI,values);
        }
        return this;
    }

    public RecipeManager insertIngredients(int recipeId, List<IngredientSerialized> ingredients){
        if(ingredients!=null) {
            ContentValues values = new ContentValues();
            for (IngredientSerialized ingredient : ingredients) {
                values.clear();
                values.put( RecipeHelper.RecipesIngredients.RECIPE_ID, recipeId );
                values.put( RecipeHelper.RecipesIngredients.INGREDIENT_ID, ingredient.getId() );
                Uri contentUri = RecipeContract.Recipes.buildRecipeWithIngredientsUri( Integer.toString( recipeId ) );
                contentResolver.insert( contentUri, values );
                contentUri = RecipeContract.Ingredients.buildIngredientWithRecipesUri( Integer.toString( ingredient.getId() ) );
                contentResolver.insert( contentUri, values );
                contentResolver.insert( RecipeContract.Ingredients.CONTENT_URI, RecipeDatabaseUtils.toValues( ingredient ) );
            }
        }
        return this;
    }

    public RecipeSerialized queryById(int recipeId){
        Uri uri= RecipeContract.Recipes.buildRecipeUri(Integer.toString(recipeId));
        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        RecipeSerialized entity= RecipeDatabaseUtils.toRecipe(cursor);
        if(cursor!=null){
            if(!cursor.isClosed()) cursor.close();
        }
        return entity;
    }

    public List<RecipeSerialized> queryAll(){
        Cursor cursor=contentResolver.query( RecipeContract.Recipes.CONTENT_URI,null,null,null,null);
        if(cursor!=null){
            Log.d(TAG,Integer.toString(cursor.getCount()));
            List<RecipeSerialized> recipes=new ArrayList<>(cursor.getCount());
            while(cursor.moveToNext()){
                RecipeSerialized recipeSerialized = RecipeDatabaseUtils.toRecipe(cursor);
                queryStepsFor( recipeSerialized ).queryIngredientsFor( recipeSerialized );
                recipes.add( recipeSerialized );
            }
            return recipes;
        }
        return null;
    }

    public RecipeManager queryStepsFor(RecipeSerialized entity){
        if(entity!=null){
            Uri contentUri= RecipeContract.Recipes.buildRecipeWithStepsUri(Integer.toString(entity.getId()));
            Cursor cursor=contentResolver.query(contentUri,null,null,null,null);
            if(cursor!=null){
                List<StepSerialized> steps=new ArrayList<>(cursor.getCount());
                while(cursor.moveToNext()){
                    steps.add( RecipeDatabaseUtils.toStep(cursor));
                }
                entity.setSteps(steps);
                if(!cursor.isClosed()) cursor.close();
            }
        }
        return this;
    }

    public RecipeManager queryIngredientsFor(RecipeSerialized recipe){
        if(recipe!=null){
            Uri contentUri= RecipeContract.Recipes.buildRecipeWithIngredientsUri(Integer.toString(recipe.getId()));
            Cursor cursor=contentResolver.query(contentUri,null,null,null,null);
            if(cursor!=null){
                List<IngredientSerialized> ingredients=new ArrayList<>(cursor.getCount());
                while(cursor.moveToNext()){
                    ingredients.add(queryIngredient(cursor.getInt(cursor.getColumnIndex( RecipeHelper.RecipesIngredients.INGREDIENT_ID))));
                }
                if(!cursor.isClosed()) cursor.close();
                recipe.setIngredients(ingredients);
            }
        }
        return this;
    }

    private IngredientSerialized queryIngredient(int id){
        Uri contentUri= RecipeContract.Ingredients.buildIngredientUri(Integer.toString(id));
        Cursor cursor=contentResolver.query(contentUri,null,null,null,null);
        if(cursor!=null){
            IngredientSerialized entity= RecipeDatabaseUtils.toIngredient(cursor);
            if(!cursor.isClosed()) cursor.close();
            return entity;
        }
        return null;
    }

    public RecipeManager insertSteps(int recipeId, List<StepSerialized> steps){
        if (steps != null) {
            for (StepSerialized stepEntity : steps) {
                ContentValues values = RecipeDatabaseUtils.toValues( stepEntity, recipeId );
                contentResolver.insert( RecipeContract.Steps.CONTENT_URI, values );
                values.clear();
            }
        }
        return this;
    }

}
