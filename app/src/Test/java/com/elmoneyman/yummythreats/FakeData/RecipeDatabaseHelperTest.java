package com.elmoneyman.yummythreats.FakeData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Data.RecipeContract;
import com.elmoneyman.yummythreats.Data.RecipeHelper;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.StepSerialized;
import com.elmoneyman.yummythreats.Utils.RecipeDatabaseUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKER_ID;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class RecipeDatabaseHelperTest {

    private RecipeHelper recipeDatabaseHelper;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application;
        recipeDatabaseHelper = new RecipeHelper( context );
    }

    @After
    public void cleanUp() {
        if (recipeDatabaseHelper != null) recipeDatabaseHelper.close();
    }

    @Test
    public void returnsCorrectDatabaseName() {
        String databaseName = RecipeHelper.DATABASE_NAME;
        assertThat( databaseName, is( recipeDatabaseHelper.getDatabaseName() ) );
    }


    @Test
    public void insertsRecipeSerializedIntoDatabase() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( serialized ) );

        Cursor cursor = db.query( RecipeHelper.Tables.RECIPES, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertTrue( cursor.moveToNext() );
        cursor.close();
    }

    @Test
    public void insertsIngredientSerializedIntoDatabase() {
        IngredientSerialized entity = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( entity ) );

        Cursor cursor = db.query( RecipeHelper.Tables.INGREDIENTS, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertTrue( cursor.moveToNext() );
        assertThat( cursor.getCount(), is( 1 ) );
        cursor.close();
    }

    @Test
    public void insertsStepSerializedIntoDatabase() {
        StepSerialized entity = RecipeTestUtils.provideStepSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( entity, FAKE_ID ) );

        Cursor cursor = db.query( RecipeHelper.Tables.STEPS, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertTrue( cursor.moveToNext() );
        assertThat( cursor.getCount(), is( 1 ) );
        cursor.close();
    }

    @Test
    public void insertsRecipesAndIngredientsIntoJunctionTable() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized ) );

        Cursor cursor = db.query( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertTrue( cursor.moveToNext() );
        assertThat( cursor.getCount(), is( 1 ) );
        cursor.close();
    }

    @Test(expected = SQLiteConstraintException.class)
    public void failsIfInsertIngredientWithSameId() {
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insertOrThrow( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized ) );
        db.insertOrThrow( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized ) );
    }

    @Test(expected = SQLiteConstraintException.class)
    public void failsIfInsertRecipeWithSameId() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insertOrThrow( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( recipeSerialized ) );
        db.insertOrThrow( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( recipeSerialized ) );
    }

    @Test(expected = SQLiteConstraintException.class)
    public void failsIfInsertStepThatHasNotUniqueRecipeId() {
        StepSerialized stepEntity = RecipeTestUtils.provideStepSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insertOrThrow( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( stepEntity, FAKE_ID ) );
        db.insertOrThrow( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( stepEntity, FAKE_ID ) );

    }

    @Test
    public void failsIfInsertRecipeAndIngredientWithSameId() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insertOrThrow( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized ) );
        db.insertOrThrow( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized ) );
    }


    @Test
    public void queryAllRecipesFromDatabase() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( serialized ) );
        serialized.setId( FAKER_ID );
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( serialized ) );

        Cursor cursor = db.query( RecipeHelper.Tables.RECIPES, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 2 ) );

        while (cursor.moveToNext()) {
            RecipeSerialized result = RecipeDatabaseUtils.toRecipe( cursor );
            assertThat( result, notNullValue() );
            assertTrue( result.getId() == FAKER_ID || result.getId() == FAKE_ID );
            assertThat( result.getName(), is( serialized.getName() ) );
            assertThat( result.getImageUrl(), is( serialized.getImageUrl() ) );
            assertThat( result.getServings(), is( serialized.getServings() ) );
        }
        cursor.close();
    }

    @Test
    public void queryAllIngredientsFromDatabase() {
        IngredientSerialized serialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( serialized ) );
        serialized.setId( FAKER_ID );
        db.insert( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( serialized ) );

        Cursor cursor = db.query( RecipeHelper.Tables.INGREDIENTS, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 2 ) );

        while (cursor.moveToNext()) {
            IngredientSerialized result = RecipeDatabaseUtils.toIngredient( cursor );
            assertThat( result, notNullValue() );
            assertThat( result.getIngredient(), is( serialized.getIngredient() ) );
            assertThat( result.getQuantity(), is( serialized.getQuantity() ) );
            assertThat( result.getMeasure(), is( serialized.getMeasure() ) );
            assertTrue( result.getId() == FAKER_ID || result.getId() == FAKE_ID );
        }
    }

    @Test
    public void queryAllStepsFromDatabase() {
        StepSerialized serialized = RecipeTestUtils.provideStepSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( serialized, FAKE_ID ) );
        serialized.setId( FAKER_ID );
        db.insert( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( serialized, FAKER_ID ) );

        Cursor cursor = db.query( RecipeHelper.Tables.STEPS, null, null, null, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 2 ) );

        while (cursor.moveToNext()) {
            StepSerialized result = RecipeDatabaseUtils.toStep( cursor );
            assertThat( result, notNullValue() );
            assertThat( result.getVideoUrl(), is( serialized.getVideoUrl() ) );
            assertThat( result.getShortDescription(), is( serialized.getShortDescription() ) );
            assertThat( result.getDescription(), is( serialized.getDescription() ) );
            assertThat( result.getImageUrl(), is( serialized.getImageUrl() ) );
            assertTrue( result.getId() == FAKER_ID || result.getId() == FAKE_ID );
        }
    }

    @Test
    public void shouldJoinRecipeWithSteps() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        StepSerialized stepEntity = RecipeTestUtils.provideStepSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( recipeSerialized ) );
        db.insert( RecipeHelper.Tables.STEPS, null, RecipeDatabaseUtils.toValues( stepEntity, recipeSerialized.getId() ) );

        String selection = RecipeHelper.Tables.RECIPES + "." + RecipeContract.Recipes.RECIPE_ID + "=?";
        String[] selectionArgs = {Long.toString( recipeSerialized.getId() )};
        Cursor cursor = db.query( RecipeHelper.Tables.RECIPE_JOIN_STEPS, null, selection, selectionArgs, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 1 ) );

        while (cursor.moveToNext()) {
            StepSerialized resultStep = RecipeDatabaseUtils.toStep( cursor );
            RecipeSerialized resultRecipe = RecipeDatabaseUtils.toRecipe( cursor );
            assertThat( resultStep, notNullValue() );
            assertThat( resultStep.getVideoUrl(), is( stepEntity.getVideoUrl() ) );
            assertThat( resultStep.getShortDescription(), is( stepEntity.getShortDescription() ) );
            assertThat( resultStep.getDescription(), is( stepEntity.getDescription() ) );
            assertThat( resultStep.getImageUrl(), is( stepEntity.getImageUrl() ) );
            assertTrue( resultStep.getId() == recipeSerialized.getId() );
            assertThat( resultRecipe, notNullValue() );
            assertTrue( resultRecipe.getId() == recipeSerialized.getId() );
            assertThat( resultRecipe.getName(), is( recipeSerialized.getName() ) );
            assertThat( resultRecipe.getImageUrl(), is( recipeSerialized.getImageUrl() ) );
            assertThat( resultRecipe.getServings(), is( recipeSerialized.getServings() ) );

        }
    }

    @Test
    public void shouldJoinRecipeWithIngredients() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized ) );
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( recipeSerialized ) );
        db.insert( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized ) );

        String selection = RecipeHelper.Tables.RECIPES + "." + RecipeContract.Recipes.RECIPE_ID + "=?";
        String[] selectionArgs = {Long.toString( recipeSerialized.getId() )};
        Cursor cursor = db.query( RecipeHelper.Tables.RECIPE_JOIN_INGREDIENTS, null, selection, selectionArgs, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 1 ) );

        while (cursor.moveToNext()) {
            RecipeSerialized resultRecipe = RecipeDatabaseUtils.toRecipe( cursor );
            assertThat( resultRecipe, notNullValue() );
            assertTrue( resultRecipe.getId() == recipeSerialized.getId() );
            assertThat( resultRecipe.getName(), is( recipeSerialized.getName() ) );
            assertThat( resultRecipe.getImageUrl(), is( recipeSerialized.getImageUrl() ) );
            assertThat( resultRecipe.getServings(), is( recipeSerialized.getServings() ) );
        }

    }

    @Test
    public void shouldJoinIngredientWithRecipes() {
        RecipeSerialized recipeSerialized = RecipeTestUtils.provideRecipeSerialized();
        IngredientSerialized ingredientSerialized = RecipeTestUtils.provideIngredientSerialized();
        SQLiteDatabase db = recipeDatabaseHelper.getWritableDatabase();
        db.insert( RecipeHelper.Tables.INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized ) );
        db.insert( RecipeHelper.Tables.RECIPES, null, RecipeDatabaseUtils.toValues( recipeSerialized ) );
        db.insert( RecipeHelper.Tables.RECIPES_INGREDIENTS, null, RecipeDatabaseUtils.toValues( ingredientSerialized, recipeSerialized ) );

        String selection = RecipeHelper.Tables.INGREDIENTS + "." + RecipeContract.Ingredients.INGREDIENT_ID + "=?";
        String[] selectionArgs = {Long.toString( ingredientSerialized.getId() )};
        Cursor cursor = db.query( RecipeHelper.Tables.INGREDIENTS_JOIN_RECIPE, null, selection, selectionArgs, null, null, null );
        assertThat( cursor, notNullValue() );
        assertThat( cursor.getCount(), is( 1 ) );

        while (cursor.moveToNext()) {
            IngredientSerialized resultIngredient = RecipeDatabaseUtils.toIngredient( cursor );
            assertThat( resultIngredient, notNullValue() );
            assertThat( resultIngredient.getIngredient(), is( ingredientSerialized.getIngredient() ) );
            assertThat( resultIngredient.getQuantity(), is( ingredientSerialized.getQuantity() ) );
            assertThat( resultIngredient.getMeasure(), is( ingredientSerialized.getMeasure() ) );
            assertTrue( resultIngredient.getId() == ingredientSerialized.getId() );
        }
    }
}
