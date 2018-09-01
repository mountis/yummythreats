package com.elmoneyman.yummythreats.FakeData;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Data.RecipeContract;
import com.elmoneyman.yummythreats.Data.RecipeHelper;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.StepSerialized;
import com.elmoneyman.yummythreats.Utils.RecipeDatabaseUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class DatabaseUtilsTest {

    @Test
    public void shouldConvertRecipeSerializedToContentValues() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        ContentValues values = RecipeDatabaseUtils.toValues( serialized );
        assertThat( values, notNullValue() );
        assertThat( values.getAsInteger( RecipeContract.Recipes.RECIPE_ID ), is( serialized.getId() ) );
        assertThat( values.getAsString( RecipeContract.Recipes.RECIPE_IMAGE_URL ), is( serialized.getImageUrl() ) );
        assertThat( values.getAsString( RecipeContract.Recipes.RECIPE_NAME ), is( serialized.getName() ) );
        assertThat( values.getAsInteger( RecipeContract.Recipes.RECIPE_SERVINGS ), is( serialized.getServings() ) );
    }

    @Test
    public void shouldConvertIngredientEntityToContentValues() {
        IngredientSerialized serialized = RecipeTestUtils.provideIngredientSerialized();
        ContentValues values = RecipeDatabaseUtils.toValues( serialized );
        assertThat( values, notNullValue() );
        assertThat( values.getAsInteger( RecipeContract.Ingredients.INGREDIENT_ID ), is( serialized.getId() ) );
        assertThat( values.getAsString( RecipeContract.Ingredients.INGREDIENT_MEASURE ), is( serialized.getMeasure() ) );
        assertThat( values.getAsDouble( RecipeContract.Ingredients.INGREDIENT_QUANTITY ), is( serialized.getQuantity() ) );
        assertThat( values.getAsString( RecipeContract.Ingredients.INGREDIENT_NAME ), is( serialized.getIngredient() ) );
    }

    @Test
    public void returnsContentValuesForJunctionTable() {
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        ContentValues values = RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized );
        assertThat( values, notNullValue() );
        assertThat( values.getAsInteger( RecipeHelper.RecipesIngredients.RECIPE_ID ), is( recipeSerialized.getId() ) );
        assertThat( values.getAsInteger( RecipeHelper.RecipesIngredients.INGREDIENT_ID ), is( ingredientSerialized.getId() ) );
    }

    @Test
    public void shouldConvertStepSerializedToContentValues() {
        StepSerialized serialized = RecipeTestUtils.provideStepSerialized();
        ContentValues values = RecipeDatabaseUtils.toValues( serialized, RecipeTestUtils.FAKE_ID );
        assertThat( values, notNullValue() );
        assertThat( values.getAsInteger( RecipeContract.Steps.STEP_ID ), is( serialized.getId() ) );
        assertThat( values.getAsString( RecipeContract.Steps.STEP_DESCRIPTION ), is( serialized.getDescription() ) );
        assertThat( values.getAsString( RecipeContract.Steps.STEP_SHORT_DESCRIPTION ), is( serialized.getShortDescription() ) );
        assertThat( values.getAsString( RecipeContract.Steps.STEP_IMAGE_URL ), is( serialized.getImageUrl() ) );
        assertThat( values.getAsString( RecipeContract.Steps.STEP_VIDEO_URL ), is( serialized.getVideoUrl() ) );
        assertThat( values.getAsInteger( RecipeContract.Steps.STEP_RECIPE_ID ), is( RecipeTestUtils.FAKE_ID ) );
    }

    @Test
    public void shouldConvertCursorToRecipeSerialized() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        Cursor cursor = createMockCursorFor( serialized );
        RecipeSerialized result = RecipeDatabaseUtils.toRecipe( cursor );
        assertThat( result, notNullValue() );
        assertThat( result.getId(), is( serialized.getId() ) );
        assertThat( result.getServings(), is( serialized.getServings() ) );
        assertThat( result.getName(), is( serialized.getName() ) );
        assertThat( result.getImageUrl(), is( serialized.getImageUrl() ) );

    }

    @Test
    public void shouldConvertCursorToStepSerialized() {
        StepSerialized serialized = RecipeTestUtils.provideStepSerialized();
        Cursor cursor = createMockCursorFor( serialized, RecipeTestUtils.FAKE_ID );
        StepSerialized result = RecipeDatabaseUtils.toStep( cursor );
        assertThat( result, notNullValue() );
        assertThat( result.getId(), is( serialized.getId() ) );
        assertThat( result.getImageUrl(), is( serialized.getImageUrl() ) );
        assertThat( result.getDescription(), is( serialized.getDescription() ) );
        assertThat( result.getShortDescription(), is( serialized.getShortDescription() ) );
        assertThat( result.getVideoUrl(), is( serialized.getVideoUrl() ) );
    }

    @Test
    public void shouldConvertCursorToIngredientSerialized() {
        IngredientSerialized serialized = RecipeTestUtils.provideIngredientSerialized();
        Cursor cursor = createMockCursorFor( serialized );
        IngredientSerialized result = RecipeDatabaseUtils.toIngredient( cursor );
        assertThat( result, notNullValue() );
        assertThat( result.getId(), is( serialized.getId() ) );
        assertThat( result.getIngredient(), is( serialized.getIngredient() ) );
        assertThat( result.getMeasure(), is( serialized.getMeasure() ) );
        assertThat( result.getQuantity(), is( serialized.getQuantity() ) );
    }

    private Cursor createMockCursorFor(StepSerialized serialized, int recipeId) {
        Cursor cursor = Mockito.mock( Cursor.class );
        for (int index = 0; index < RecipeContract.Steps.COLUMNS.length; index++) {
            given( cursor.getColumnIndex( RecipeContract.Steps.COLUMNS[index] ) ).willReturn( index );
        }

        when( cursor.getInt( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Steps.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Steps.STEP_ID:
                    return serialized.getId();
                case RecipeContract.Steps.STEP_RECIPE_ID:
                    return recipeId;
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );

        when( cursor.getString( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Steps.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Steps.STEP_DESCRIPTION:
                    return serialized.getDescription();
                case RecipeContract.Steps.STEP_SHORT_DESCRIPTION:
                    return serialized.getShortDescription();
                case RecipeContract.Steps.STEP_IMAGE_URL:
                    return serialized.getImageUrl();
                case RecipeContract.Steps.STEP_VIDEO_URL:
                    return serialized.getVideoUrl();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );
        return cursor;
    }

    private Cursor createMockCursorFor(RecipeSerialized serialized) {
        Cursor cursor = Mockito.mock( Cursor.class );
        for (int index = 0; index < RecipeContract.Recipes.COLUMNS.length; index++) {
            given( cursor.getColumnIndex( RecipeContract.Recipes.COLUMNS[index] ) ).willReturn( index );
        }

        when( cursor.getString( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Recipes.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Recipes.RECIPE_NAME:
                    return serialized.getName();
                case RecipeContract.Recipes.RECIPE_IMAGE_URL:
                    return serialized.getImageUrl();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );

        when( cursor.getInt( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Recipes.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Recipes.RECIPE_ID:
                    return serialized.getId();
                case RecipeContract.Recipes.RECIPE_SERVINGS:
                    return serialized.getServings();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );
        return cursor;
    }

    private Cursor createMockCursorFor(IngredientSerialized serialized) {
        Cursor cursor = Mockito.mock( Cursor.class );
        for (int index = 0; index < RecipeContract.Ingredients.COLUMNS.length; index++) {
            given( cursor.getColumnIndex( RecipeContract.Ingredients.COLUMNS[index] ) ).willReturn( index );
        }
        when( cursor.getInt( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Ingredients.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Ingredients.INGREDIENT_ID:
                    return serialized.getId();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );

        when( cursor.getFloat( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Ingredients.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Ingredients.INGREDIENT_QUANTITY:
                    return serialized.getQuantity();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );

        when( cursor.getString( anyInt() ) ).thenAnswer( invocation -> {
            Integer columnIndex = invocation.getArgumentAt( 0, Integer.class );
            String argument = RecipeContract.Ingredients.COLUMNS[columnIndex];
            switch (argument) {
                case RecipeContract.Ingredients.INGREDIENT_MEASURE:
                    return serialized.getMeasure();
                case RecipeContract.Ingredients.INGREDIENT_NAME:
                    return serialized.getIngredient();
                default:
                    throw new UnsupportedOperationException( "Unknown argument:" + argument );
            }
        } );
        return cursor;
    }
}
