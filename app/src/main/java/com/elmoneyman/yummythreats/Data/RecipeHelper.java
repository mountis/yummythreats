package com.elmoneyman.yummythreats.Data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

public class RecipeHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="yummyThreats.db";
    private static final int DATABASE_VERSION=10;

    public interface Tables {

        String RECIPES="recipes";

        String INGREDIENTS="ingredients";

        String STEPS="steps";

        String RECIPES_INGREDIENTS="recipes_ingredients";

        String RECIPE_JOIN_STEPS="recipes "+
                "INNER JOIN steps ON recipes.recipe_id=steps.ref_recipe_id";

        String RECIPE_JOIN_INGREDIENTS="recipes "+
                "INNER JOIN recipes_ingredients ON recipes.recipe_id=recipes_ingredients.ref_recipe_id";

        String INGREDIENTS_JOIN_RECIPE="ingredients "+
                "LEFT OUTER JOIN recipes_ingredients ON ingredients.ingredient_id=recipes_ingredients.ref_ingredient_id";
    }

    interface References {
        String RECIPE_ID="REFERENCES "+ Tables.RECIPES+"("+ RecipeContract.Recipes.RECIPE_ID+")";
        String INGREDIENT_ID="REFERENCES "+ Tables.INGREDIENTS+"("+ RecipeContract.Ingredients.INGREDIENT_ID+")";
    }

    public interface RecipesIngredients {
        String RECIPE_ID="ref_recipe_id";
        String INGREDIENT_ID="ref_ingredient_id";
    }

    public RecipeHelper(@NonNull Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Tables.RECIPES+" ("+
                RecipeContract.Recipes.RECIPE_ID+" INTEGER PRIMARY KEY NOT NULL,"+
                RecipeContract.Recipes.RECIPE_NAME+" TEXT NOT NULL,"+
                RecipeContract.Recipes.RECIPE_IMAGE_URL+" TEXT NOT NULL,"+
                RecipeContract.Recipes.RECIPE_SERVINGS+" INTEGER NOT NULL,"+
                " UNIQUE (" + RecipeContract.Recipes.RECIPE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+ Tables.STEPS+" ("+
                RecipeContract.Steps.STEP_ID+" INTEGER PRIMARY KEY NOT NULL,"+
                RecipeContract.Steps.STEP_DESCRIPTION+" TEXT,"+
                RecipeContract.Steps.STEP_SHORT_DESCRIPTION+" TEXT,"+
                RecipeContract.Steps.STEP_IMAGE_URL+" TEXT,"+
                RecipeContract.Steps.STEP_RECIPE_ID+" INTEGER NOT NULL "+ References.RECIPE_ID+","+
                RecipeContract.Steps.STEP_VIDEO_URL+" TEXT,"+
                " UNIQUE (" + RecipeContract.Steps.STEP_RECIPE_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+ Tables.INGREDIENTS+" ("+
                RecipeContract.Ingredients.INGREDIENT_ID+" INTEGER PRIMARY KEY NOT NULL,"+
                RecipeContract.Ingredients.INGREDIENT_NAME+" TEXT,"+
                RecipeContract.Ingredients.INGREDIENT_MEASURE+" TEXT,"+
                RecipeContract.Ingredients.INGREDIENT_QUANTITY+" REAL,"+
                " UNIQUE (" + RecipeContract.Ingredients.INGREDIENT_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+ Tables.RECIPES_INGREDIENTS+" ("+
                BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                RecipesIngredients.RECIPE_ID+" INTEGER NOT NULL "+ References.RECIPE_ID+","+
                RecipesIngredients.INGREDIENT_ID+" INTEGER NOT NULL "+ References.INGREDIENT_ID+","+
                 " UNIQUE (" + RecipesIngredients.RECIPE_ID + "," + RecipesIngredients.INGREDIENT_ID + ") ON CONFLICT REPLACE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Tables.INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS "+ Tables.STEPS);
        db.execSQL("DROP TABLE IF EXISTS "+ Tables.RECIPES);
        db.execSQL("DROP TABLE IF EXISTS "+ Tables.RECIPES_INGREDIENTS);
        onCreate(db);
    }


    static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
