package com.elmoneyman.yummythreats.Model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeSerialized {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("ingredients")
    private List<IngredientSerialized> ingredients;

    @SerializedName("steps")
    private List<StepSerialized> steps;

    @SerializedName("servings")
    private int servings;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<IngredientSerialized> getIngredients() {
        return ingredients;
    }

    public List<StepSerialized> getSteps() {
        return steps;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<IngredientSerialized> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<StepSerialized> steps) {
        this.steps = steps;
    }
}
