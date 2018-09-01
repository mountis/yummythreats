package com.elmoneyman.yummythreats.FakeData;


import android.net.Uri;

import com.elmoneyman.yummythreats.Data.RecipeContract;
import com.elmoneyman.yummythreats.Data.RecipeMatchEnum;
import com.elmoneyman.yummythreats.Data.RecipeUriMatcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(RobolectricTestRunner.class)
public class RecipeUriMatcherTest {

    private RecipeUriMatcher matcher;
    private Map<RecipeMatchEnum, Uri> matchMap;

    @Before
    public void setUp() {
        matcher = new RecipeUriMatcher();
        matchMap = new HashMap<>();
        matchMap.put( RecipeMatchEnum.RECIPES, RecipeContract.Recipes.CONTENT_URI );
        matchMap.put( RecipeMatchEnum.RECIPE_ID, RecipeContract.Recipes.buildRecipeUri( "1" ) );
        matchMap.put( RecipeMatchEnum.RECIPE_STEP_ID, RecipeContract.Recipes.buildRecipeWithStepsUri( "2" ) );
        matchMap.put( RecipeMatchEnum.RECIPE_INGREDIENTS_ID, RecipeContract.Recipes.buildRecipeWithIngredientsUri( "3" ) );
        matchMap.put( RecipeMatchEnum.INGREDIENT_ID, RecipeContract.Ingredients.buildIngredientUri( "1" ) );
        matchMap.put( RecipeMatchEnum.INGREDIENTS, RecipeContract.Ingredients.CONTENT_URI );
        matchMap.put( RecipeMatchEnum.INGREDIENT_RECIPES_ID, RecipeContract.Ingredients.buildIngredientWithRecipesUri( "1" ) );
        matchMap.put( RecipeMatchEnum.STEPS, RecipeContract.Steps.CONTENT_URI );
        matchMap.put( RecipeMatchEnum.STEP_ID, RecipeContract.Steps.buildStepUri( "1" ) );
    }

    @Test
    public void matchesToRecipeMatchEnumOnPassedUri() {
        for (RecipeMatchEnum value : matchMap.keySet()) {
            Uri uri = matchMap.get( value );
            assertThat( value, is( matcher.match( uri ) ) );
        }
    }

    @Test
    public void returnsContentTypeOfEnumOnPassedUri() {
        for (RecipeMatchEnum value : RecipeMatchEnum.values()) {
            Uri uri = matchMap.get( value );
            assertThat( value.contentType, is( matcher.getType( uri ) ) );
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throwsAnExceptionOnPassedUri() {
        Uri fakeUri = RecipeContract.Recipes.CONTENT_URI.buildUpon().appendPath( "fake_path" ).build();
        matcher.match( fakeUri );
        matcher.getType( fakeUri );
    }
}
