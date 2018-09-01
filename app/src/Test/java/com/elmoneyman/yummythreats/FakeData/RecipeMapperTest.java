package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Mapper.Recipes;
import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_IMAGE_URL;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_NAME;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_SERVINGS;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideIngredientList;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideIngredientSerialized;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideRecipe;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideRecipeESerializedList;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideRecipeSerialized;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideStepList;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideStepSerialized;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RecipeMapperTest {

    @Mock
    private Mapper<Step, StepSerialized> stepMapper;

    @Mock
    private Mapper<Ingredient, IngredientSerialized> ingredientMapper;

    private Mapper<Recipe, RecipeSerialized> mapper;

    @Before
    public void setUp() {
        this.mapper = new Recipes( ingredientMapper, stepMapper );
    }

    @Test
    public void testMapTo() {
        when( stepMapper.map( anyListOf( StepSerialized.class ) ) ).thenReturn( provideStepList() );
        when( ingredientMapper.map( anyListOf( IngredientSerialized.class ) ) ).thenReturn( provideIngredientList() );
        RecipeSerialized serialized = provideRecipeSerialized();
        Recipe recipe = mapper.map( serialized );
        assertThat( recipe.getId(), is( FAKE_ID ) );
        assertThat( recipe.getImageUrl(), is( FAKE_IMAGE_URL ) );
        assertThat( recipe.getName(), is( FAKE_NAME ) );
        assertThat( recipe.getServings(), is( FAKE_SERVINGS ) );
        verify( stepMapper ).map( anyListOf( StepSerialized.class ) );
        verify( ingredientMapper ).map( anyListOf( IngredientSerialized.class ) );
    }

    @Test
    public void testMapFrom() {
        when( stepMapper.reverseMap( any( Step.class ) ) ).thenReturn( provideStepSerialized() );
        when( ingredientMapper.reverseMap( any( Ingredient.class ) ) ).thenReturn( provideIngredientSerialized() );
        Recipe recipe = provideRecipe();
        RecipeSerialized recipeSerialized = mapper.reverseMap( recipe );
        assertThat( recipeSerialized.getId(), is( FAKE_ID ) );
        assertThat( recipeSerialized.getImageUrl(), is( FAKE_IMAGE_URL ) );
        assertThat( recipeSerialized.getName(), is( FAKE_NAME ) );
        assertThat( recipeSerialized.getServings(), is( FAKE_SERVINGS ) );
        verify( stepMapper, times( recipe.getSteps().size() ) ).reverseMap( any( Step.class ) );
        verify( ingredientMapper, times( recipe.getIngredients().size() ) ).reverseMap( any( Ingredient.class ) );
    }

    @Test
    public void testMappingList() {
        when( stepMapper.map( anyListOf( StepSerialized.class ) ) ).thenReturn( provideStepList() );
        when( ingredientMapper.map( anyListOf( IngredientSerialized.class ) ) ).thenReturn( provideIngredientList() );
        List<RecipeSerialized> list = provideRecipeESerializedList();
        List<Recipe> result = mapper.map( list );
        for (Recipe recipe : result) {
            assertThat( recipe.getId(), is( FAKE_ID ) );
            assertThat( recipe.getImageUrl(), is( FAKE_IMAGE_URL ) );
            assertThat( recipe.getName(), is( FAKE_NAME ) );
            assertThat( recipe.getServings(), is( FAKE_SERVINGS ) );
        }
        verify( stepMapper, times( list.size() ) ).map( anyListOf( StepSerialized.class ) );
        verify( ingredientMapper, times( list.size() ) ).map( anyListOf( IngredientSerialized.class ) );
    }
}
