package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Mapper.Steps;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_DESCRIPTION;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_IMAGE_URL;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_TEXT;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_VIDEO_URL;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideStep;
import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideStepSerialized;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StepMapperTest {

    private Mapper<Step, StepSerialized> mapper;

    @Before
    public void setUp() {
        mapper = new Steps();
    }

    @Test
    public void testMapToIsNull() {
        StepSerialized serialized = null;
        Step step = mapper.map( serialized );
        assertThat( step, nullValue() );
    }

    @Test
    public void testMapFromIsNull() {
        Step step = null;
        StepSerialized serialized = mapper.reverseMap( step );
        assertThat( serialized, nullValue() );
    }

    @Test
    public void testMapListIsNull() {
        List<StepSerialized> list = null;
        List<Step> result = mapper.map( list );
        assertThat( result, nullValue() );
    }

    @Test
    public void testMapTo() {
        StepSerialized serialized = provideStepSerialized();
        Step step = mapper.map( serialized );
        assertThat( step.getStepId(), is( FAKE_ID ) );
        assertThat( step.getDescription(), is( FAKE_TEXT ) );
        assertThat( step.getImageUrl(), is( FAKE_IMAGE_URL ) );
        assertThat( step.getVideoUrl(), is( FAKE_VIDEO_URL ) );
        assertThat( step.getShortDescription(), is( FAKE_DESCRIPTION ) );
    }

    @Test
    public void testMapFrom() {
        Step step = provideStep();
        StepSerialized serialized = mapper.reverseMap( step );
        assertThat( serialized.getId(), is( FAKE_ID ) );
        assertThat( serialized.getDescription(), is( FAKE_TEXT ) );
        assertThat( serialized.getImageUrl(), is( FAKE_IMAGE_URL ) );
        assertThat( serialized.getVideoUrl(), is( FAKE_VIDEO_URL ) );
        assertThat( serialized.getShortDescription(), is( FAKE_DESCRIPTION ) );
    }

    @Test
    public void testListMapping() {
        List<StepSerialized> inputList = Arrays.asList( provideStepSerialized(), provideStepSerialized(),
                provideStepSerialized(), provideStepSerialized(), provideStepSerialized() );
        List<Step> result = mapper.map( inputList );
        for (Step step : result) {
            assertThat( step.getStepId(), is( FAKE_ID ) );
            assertThat( step.getDescription(), is( FAKE_TEXT ) );
            assertThat( step.getImageUrl(), is( FAKE_IMAGE_URL ) );
            assertThat( step.getVideoUrl(), is( FAKE_VIDEO_URL ) );
            assertThat( step.getShortDescription(), is( FAKE_DESCRIPTION ) );
        }
    }

}
