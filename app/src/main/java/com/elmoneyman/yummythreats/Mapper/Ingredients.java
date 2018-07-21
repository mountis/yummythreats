package com.elmoneyman.yummythreats.Mapper;

import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Ingredients implements Mapper<Ingredient,IngredientSerialized> {

    @Inject
    public Ingredients(){}

    @Override
    public Ingredient map(IngredientSerialized ingredientSerialized) {
        if(ingredientSerialized ==null) return null;
        Ingredient ingredient=new Ingredient();
        ingredient.setId( ingredientSerialized.getId());
        ingredient.setIngredient( ingredientSerialized.getIngredient());
        ingredient.setMeasure( ingredientSerialized.getMeasure());
        ingredient.setQuantity( ingredientSerialized.getQuantity());
        return ingredient;
    }

    @Override
    public IngredientSerialized reverseMap(Ingredient ingredient) {
        if(ingredient==null) return null;
        IngredientSerialized ingredientSerialized=new IngredientSerialized();
        ingredientSerialized.setId(ingredient.getId());
        ingredientSerialized.setQuantity(ingredient.getQuantity());
        ingredientSerialized.setMeasure(ingredient.getMeasure());
        ingredientSerialized.setIngredient(ingredient.getIngredient());
        return ingredientSerialized;
    }

    @Override
    public List<Ingredient> map(List<IngredientSerialized> ingredientSerializedList) {
        if(ingredientSerializedList==null||ingredientSerializedList.isEmpty()) return null;
        List<Ingredient> result=new ArrayList<>(ingredientSerializedList.size());
        for (IngredientSerialized ingredientEntity : ingredientSerializedList) {
            result.add( map( ingredientEntity ) );
        }
        return result;
    }
}
