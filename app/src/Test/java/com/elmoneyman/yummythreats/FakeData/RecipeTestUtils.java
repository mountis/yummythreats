package com.elmoneyman.yummythreats.FakeData;

import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import java.util.Arrays;
import java.util.List;

public class RecipeTestUtils {

    public static final int FAKE_ID = 123;
    public static final int FAKER_ID = 1234;
    public static final int FAKE_SERVINGS = 45;
    public static final double FAKE_QUANTITY = 1.5F;
    public static final String FAKE_NAME = "fake_name";
    public static final String FAKE_TEXT = "fake_text";
    public static final String FAKE_IMAGE_URL = "fake_image_url";
    public static final String FAKE_VIDEO_URL = "fake_video_url";
    public static final String FAKE_DESCRIPTION = "fake_description";

    public static IngredientSerialized provideIngredientSerialized() {
        IngredientSerialized serialized = new IngredientSerialized();
        serialized.setId( FAKE_ID );
        serialized.setQuantity( FAKE_QUANTITY );
        serialized.setIngredient( FAKE_NAME );
        serialized.setMeasure( FAKE_TEXT );
        return serialized;
    }

    public static StepSerialized provideStepSerialized() {
        StepSerialized stepSerialized = new StepSerialized();
        stepSerialized.setImageUrl( FAKE_IMAGE_URL );
        stepSerialized.setVideoUrl( FAKE_VIDEO_URL );
        stepSerialized.setDescription( FAKE_TEXT );
        stepSerialized.setId( FAKE_ID );
        stepSerialized.setShortDescription( FAKE_DESCRIPTION );
        return stepSerialized;
    }

    public static Ingredient provideIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId( FAKE_ID );
        ingredient.setQuantity( FAKE_QUANTITY );
        ingredient.setIngredient( FAKE_NAME );
        ingredient.setMeasure( FAKE_TEXT );
        return ingredient;
    }

    public static Step provideStep() {
        Step step = new Step();
        step.setImageUrl( FAKE_IMAGE_URL );
        step.setVideoUrl( FAKE_VIDEO_URL );
        step.setDescription( FAKE_TEXT );
        step.setStepId( FAKE_ID );
        step.setShortDescription( FAKE_DESCRIPTION );
        return step;
    }

    public static List<Step> provideStepList() {
        return Arrays.asList( provideStep(), provideStep(),
                provideStep(), provideStep(), provideStep() );
    }

    public static List<StepSerialized> provideStepSerializedList() {
        return Arrays.asList( provideStepSerialized(), provideStepSerialized(),
                provideStepSerialized(), provideStepSerialized(), provideStepSerialized() );
    }

    public static List<IngredientSerialized> provideIngredientSerializedList() {
        return Arrays.asList( provideIngredientSerialized(), provideIngredientSerialized(),
                provideIngredientSerialized(), provideIngredientSerialized() );
    }

    public static List<Ingredient> provideIngredientList() {
        return Arrays.asList( provideIngredient(), provideIngredient(),
                provideIngredient(), provideIngredient() );
    }

    public static List<RecipeSerialized> provideRecipeESerializedList() {
        return Arrays.asList( provideRecipeSerialized(), provideRecipeSerialized(),
                provideRecipeSerialized(), provideRecipeSerialized(), provideRecipeSerialized() );
    }

    public static List<Recipe> provideRecipeList() {
        return Arrays.asList( provideRecipe(), provideRecipe(), provideRecipe(),
                provideRecipe(), provideRecipe(), provideRecipe() );
    }

    public static RecipeSerialized provideRecipeSerialized() {
        RecipeSerialized recipeSerialized = new RecipeSerialized();
        recipeSerialized.setIngredients( provideIngredientSerializedList() );
        recipeSerialized.setSteps( provideStepSerializedList() );
        recipeSerialized.setName( FAKE_NAME );
        recipeSerialized.setServings( FAKE_SERVINGS );
        recipeSerialized.setId( FAKE_ID );
        recipeSerialized.setImageUrl( FAKE_IMAGE_URL );
        return recipeSerialized;
    }

    public static Recipe provideRecipe() {
        Recipe recipe = new Recipe();
        recipe.setIngredients( provideIngredientList() );
        recipe.setSteps( provideStepList() );
        recipe.setName( FAKE_NAME );
        recipe.setServings( FAKE_SERVINGS );
        recipe.setId( FAKE_ID );
        recipe.setImageUrl( FAKE_IMAGE_URL );
        return recipe;
    }

}
