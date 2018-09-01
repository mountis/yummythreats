package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Mapper.Ingredients;
import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_NAME;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_QUANTITY;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_TEXT;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideIngredient;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideIngredientSerialized;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class IngredientMapperTest {

    private Mapper<Ingredient, IngredientSerialized> mapper;

    @Before
    public void setUp() {
        mapper = new Ingredients();
    }

    @Test
    public void testMapTo() {
        IngredientSerialized serialized = provideIngredientSerialized();
        Ingredient result = mapper.map( serialized );
        assertThat( result.getId(), is( FAKE_ID ) );
        assertThat( result.getIngredient(), is( FAKE_NAME ) );
        assertThat( result.getMeasure(), is( FAKE_TEXT ) );
        assertThat( result.getQuantity(), is( FAKE_QUANTITY ) );
    }

    @Test
    public void testMapToNull() {
        IngredientSerialized serialized = null;
        Ingredient ingredient = mapper.map( serialized );
        assertThat( ingredient, nullValue() );
    }

    @Test
    public void testMapFromNull() {
        Ingredient ingredient = null;
        IngredientSerialized serialized = mapper.reverseMap( ingredient );
        assertThat( serialized, nullValue() );
    }

    @Test
    public void testMapFrom() {
        Ingredient ingredient = provideIngredient();
        IngredientSerialized serialized = mapper.reverseMap( ingredient );
        assertThat( serialized.getId(), is( FAKE_ID ) );
        assertThat( serialized.getIngredient(), is( FAKE_NAME ) );
        assertThat( serialized.getMeasure(), is( FAKE_TEXT ) );
        assertThat( serialized.getQuantity(), is( FAKE_QUANTITY ) );
    }

    @Test
    public void testMapListNull() {
        List<IngredientSerialized> list = null;
        List<Ingredient> result = mapper.map( list );
        assertThat( result, nullValue() );
    }

    @Test
    public void testMapList() {
        List<IngredientSerialized> entityList = Arrays.asList( provideIngredientSerialized(), provideIngredientSerialized(),
                provideIngredientSerialized(), provideIngredientSerialized(), provideIngredientSerialized() );
        List<Ingredient> resultList = mapper.map( entityList );
        for (Ingredient result : resultList) {
            assertThat( result.getId(), is( FAKE_ID ) );
            assertThat( result.getIngredient(), is( FAKE_NAME ) );
            assertThat( result.getMeasure(), is( FAKE_TEXT ) );
            assertThat( result.getQuantity(), is( FAKE_QUANTITY ) );
        }
    }
}
