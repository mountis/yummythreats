package com.elmoneyman.yummythreats.Listeners;

public class RecipeOnClick {

    public final int recipeId;

    private RecipeOnClick(int recipeId){
        this.recipeId=recipeId;
    }

    public static RecipeOnClick click(int recipeId){
        return new RecipeOnClick(recipeId);
    }
}
