package com.elmoneyman.yummythreats.Data;

public enum RecipeMatchEnum {

    RECIPES(100, RecipeContract.Recipes.CONTENT_DIR_TYPE, RecipeHelper.Tables.RECIPES, RecipeContract.PATH_RECIPE),
    RECIPE_ID(101, RecipeContract.Recipes.CONTENT_ITEM_TYPE,null, RecipeContract.PATH_RECIPE+"/#"),
    RECIPE_INGREDIENTS_ID(103, RecipeContract.Recipes.CONTENT_ITEM_TYPE, RecipeHelper.Tables.RECIPES_INGREDIENTS,RecipeContract.PATH_RECIPE+"/#/"+RecipeContract.PATH_INGREDIENT),
    RECIPE_STEP_ID(105, RecipeContract.Recipes.CONTENT_ITEM_TYPE,null,RecipeContract.PATH_RECIPE+"/#/"+RecipeContract.PATH_STEP),

    STEPS(200, RecipeContract.Steps.CONTENT_DIR_TYPE, RecipeHelper.Tables.STEPS,RecipeContract.PATH_STEP),
    STEP_ID(201, RecipeContract.Steps.CONTENT_ITEM_TYPE,null,RecipeContract.PATH_STEP+"/#"),

    INGREDIENTS(300, RecipeContract.Ingredients.CONTENT_DIR_TYPE, RecipeHelper.Tables.INGREDIENTS,RecipeContract.PATH_INGREDIENT),
    INGREDIENT_ID(301, RecipeContract.Ingredients.CONTENT_ITEM_TYPE,null,RecipeContract.PATH_INGREDIENT+"/#"),
    INGREDIENT_RECIPES_ID(302, RecipeContract.Ingredients.CONTENT_ITEM_TYPE, RecipeHelper.Tables.RECIPES_INGREDIENTS,RecipeContract.PATH_INGREDIENT+"/#/"+RecipeContract.PATH_RECIPE);

    public int code;
    public String contentType;
    public String table;
    public String path;

    RecipeMatchEnum(int code, String contentType,String table,String path){
        this.code=code;
        this.contentType=contentType;
        this.table=table;
        this.path=path;
    }
}
