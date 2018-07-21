package com.elmoneyman.yummythreats.Model;


import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
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

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
