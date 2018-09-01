package com.elmoneyman.yummythreats.FakeData;

import com.elmoneyman.yummythreats.Data.RecipeContract;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class RecipeContractTest {

    @Test
    public void returnsNotNullUri() {
        String id = "1";
        assertThat( RecipeContract.Recipes.CONTENT_URI, notNullValue() );
        assertThat( RecipeContract.Recipes.buildRecipeUri( id ), notNullValue() );
        assertThat( RecipeContract.Recipes.buildRecipeWithIngredientsUri( id ), notNullValue() );
        assertThat( RecipeContract.Recipes.buildRecipeWithStepsUri( id ), notNullValue() );
        assertThat( RecipeContract.Steps.CONTENT_URI, notNullValue() );
        assertThat( RecipeContract.Steps.buildStepUri( id ), notNullValue() );
        assertThat( RecipeContract.Ingredients.CONTENT_URI, notNullValue() );
        assertThat( RecipeContract.Ingredients.buildIngredientUri( id ), notNullValue() );
        assertThat( RecipeContract.Ingredients.buildIngredientWithRecipesUri( id ), notNullValue() );
    }

}
