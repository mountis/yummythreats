package com.elmoneyman.yummythreats.Data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class RecipeProvider extends ContentProvider {

    private RecipeUriMatcher uriMatcher;
    private RecipeHelper recipeHelper;


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db= recipeHelper.getReadableDatabase();
        final RecipeMatchEnum recipeMatchEnum=uriMatcher.match(uri);

        QueryBuilder builder=buildQuery(uri,recipeMatchEnum);
        Cursor cursor=builder
                .where(selection,selectionArgs)
                .query(db,projection,sortOrder);

        Context context = getContext();
        if (null != context) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return uriMatcher.getType(uri);
    }

    private void notifyChange(Uri uri) {
        if (getContext()!=null) {
            Context context = getContext();
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    private void deleteDatabase(){
        recipeHelper.close();
        Context context=getContext();
        if(context!=null){
            RecipeHelper.deleteDatabase(context);
            recipeHelper =new RecipeHelper(context);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uri == RecipeContract.BASE_CONTENT_URI) {
            deleteDatabase();
            notifyChange(uri);
            return 1;
        }
        final SQLiteDatabase db= recipeHelper.getWritableDatabase();
        final RecipeMatchEnum recipeMatchEnum=uriMatcher.match(uri);
        final QueryBuilder builder=buildQuery(uri,recipeMatchEnum);

        int retVal = builder.where(selection, selectionArgs).delete(db);
        notifyChange(uri);
        return retVal;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db= recipeHelper.getWritableDatabase();
        final RecipeMatchEnum recipeMatchEnum=uriMatcher.match(uri);
        final QueryBuilder builder=buildQuery(uri,recipeMatchEnum);

        int retVal=builder
                .where(selection,selectionArgs)
                .update(db,values);
        notifyChange(uri);
        return retVal;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(values==null) throw new IllegalArgumentException("Values are null");
        final SQLiteDatabase db= recipeHelper.getWritableDatabase();
        final RecipeMatchEnum recipeMatchEnum=uriMatcher.match(uri);
        if(recipeMatchEnum.table!=null){
            db.insertWithOnConflict(recipeMatchEnum.table,null,values,SQLiteDatabase.CONFLICT_REPLACE);
            notifyChange(uri);
        }
        switch (recipeMatchEnum){
            case RECIPES:
                return RecipeContract.Recipes.buildRecipeUri(values.getAsString( RecipeContract.Recipes.RECIPE_ID));
            case RECIPE_STEP_ID:
                return RecipeContract.Recipes.buildRecipeWithStepsUri(values.getAsString( RecipeContract.Recipes.RECIPE_ID));
            case RECIPE_INGREDIENTS_ID:
                return RecipeContract.Recipes.buildRecipeWithIngredientsUri(values.getAsString( RecipeContract.Recipes.RECIPE_ID));
            case INGREDIENTS:
                return RecipeContract.Ingredients.buildIngredientUri(values.getAsString( RecipeContract.Ingredients.INGREDIENT_ID));
            case INGREDIENT_RECIPES_ID:
                return RecipeContract.Ingredients.buildIngredientWithRecipesUri(values.getAsString( RecipeContract.Ingredients.INGREDIENT_ID));
            case STEPS:
                return RecipeContract.Steps.buildStepUri(values.getAsString( RecipeContract.Steps.STEP_ID));
            default:
                throw new UnsupportedOperationException("Unknown URI"+uri);
        }
    }

    @Override
    public boolean onCreate() {
        if(getContext()!=null) {
            this.recipeHelper = new RecipeHelper(getContext());
            this.uriMatcher=new RecipeUriMatcher();
        }
        return recipeHelper !=null;
    }


    private QueryBuilder buildQuery(Uri uri, RecipeMatchEnum matchEnum){
        QueryBuilder builder=new QueryBuilder();
        String id;
        switch (matchEnum){
            case RECIPES:
                return builder.table( RecipeHelper.Tables.RECIPES);
            case INGREDIENTS:
                return builder.table( RecipeHelper.Tables.INGREDIENTS);
            case STEPS:
                return builder.table( RecipeHelper.Tables.STEPS);
            case RECIPE_ID:
                id= RecipeContract.Recipes.getRecipeId(uri);
                return builder.table( RecipeHelper.Tables.RECIPES)
                        .where( RecipeContract.Recipes.RECIPE_ID+"=?",id);
            case INGREDIENT_ID:
                id= RecipeContract.Ingredients.getIngredientId(uri);
                return builder.table( RecipeHelper.Tables.INGREDIENTS)
                        .where( RecipeContract.Ingredients.INGREDIENT_ID+"=?",id);
            case RECIPE_INGREDIENTS_ID:
                id= RecipeContract.Recipes.getRecipeId(uri);
                return builder.table( RecipeHelper.Tables.RECIPE_JOIN_INGREDIENTS)
                        .mapToTable( RecipeContract.Recipes.RECIPE_ID, RecipeHelper.Tables.RECIPES)
                        .mapToTable( RecipeContract.Ingredients.INGREDIENT_ID, RecipeHelper.Tables.INGREDIENTS)
                        .where( RecipeContract.Recipes.RECIPE_ID+"=?",id);
            case RECIPE_STEP_ID:
                id= RecipeContract.Recipes.getRecipeId(uri);
                return builder.table( RecipeHelper.Tables.RECIPE_JOIN_STEPS)
                        .mapToTable( RecipeContract.Recipes.RECIPE_ID, RecipeHelper.Tables.RECIPES)
                        .mapToTable( RecipeContract.Steps.STEP_ID, RecipeHelper.Tables.STEPS)
                        .where( RecipeContract.Recipes.RECIPE_ID+"=?",id);
            case INGREDIENT_RECIPES_ID:
                id= RecipeContract.Ingredients.getIngredientId(uri);
                return builder.table( RecipeHelper.Tables.INGREDIENTS_JOIN_RECIPE)
                        .mapToTable( RecipeContract.Ingredients.INGREDIENT_ID, RecipeHelper.Tables.INGREDIENTS)
                        .mapToTable( RecipeContract.Recipes.RECIPE_ID, RecipeHelper.Tables.RECIPES)
                        .where( RecipeContract.Ingredients.INGREDIENT_ID+"=?",id);
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);

        }
    }
}
