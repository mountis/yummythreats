package com.elmoneyman.yummythreats.Mapper;

import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Recipes implements Mapper<Recipe,RecipeSerialized> {

    private Mapper<Ingredient,IngredientSerialized> ingredientMapper;
    private Mapper<Step,StepSerialized> stepMapper;

    @Inject
    public Recipes(@NonNull Mapper<Ingredient,IngredientSerialized>  ingredientMapper,
                   @NonNull Mapper<Step,StepSerialized> stepMapper){
        this.ingredientMapper=ingredientMapper;
        this.stepMapper=stepMapper;
    }


    @Override
    public Recipe map(RecipeSerialized recipeSerialized) {
        if(recipeSerialized ==null) return null;
        Recipe recipe=new Recipe();
        recipe.setId( recipeSerialized.getId());
        recipe.setImageUrl( recipeSerialized.getImageUrl());
        recipe.setName( recipeSerialized.getName());
        recipe.setServings( recipeSerialized.getServings());
        if(recipeSerialized.getIngredients()!=null) {
            recipe.setIngredients(ingredientMapper.map( recipeSerialized.getIngredients()));
        }
        if(recipeSerialized.getSteps()!=null){
            recipe.setSteps(stepMapper.map( recipeSerialized.getSteps()));
        }
        return recipe;
    }

    @Override
    public RecipeSerialized reverseMap(Recipe recipe) {
        if(recipe==null) return null;
        RecipeSerialized recipeSerialized=new RecipeSerialized();
        recipeSerialized.setId(recipe.getId());
        recipeSerialized.setName(recipe.getName());
        recipeSerialized.setServings(recipe.getServings());
        recipeSerialized.setImageUrl(recipe.getImageUrl());
        if(recipe.getSteps()!=null){
            List<StepSerialized> stepList=new ArrayList<>(recipe.getSteps().size());
            for (Step step : recipe.getSteps()) {
                stepList.add( stepMapper.reverseMap( step ) );
            }
            recipeSerialized.setSteps(stepList);
        }

        if(recipe.getIngredients()!=null){
            List<IngredientSerialized> ingredientList=new ArrayList<>(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredientList.add( ingredientMapper.reverseMap( ingredient ) );
            }
            recipeSerialized.setIngredients(ingredientList);
        }
        return recipeSerialized;
    }

    @Override
    public List<Recipe> map(List<RecipeSerialized> recipeSerializedList) {
        if(recipeSerializedList==null||recipeSerializedList.isEmpty()) return null;
        List<Recipe> result=new ArrayList<>(recipeSerializedList.size());
        for (RecipeSerialized recipeSerialized : recipeSerializedList) {
            result.add( map( recipeSerialized ) );
        }
        return result;
    }
}
