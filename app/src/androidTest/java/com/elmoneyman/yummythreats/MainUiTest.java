package com.elmoneyman.yummythreats;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.elmoneyman.yummythreats.Activities.MainActivity;
import com.elmoneyman.yummythreats.Dagger.AppComponent;
import com.elmoneyman.yummythreats.Dagger.AppModule;
import com.elmoneyman.yummythreats.FakeData.AppComponentTest;
import com.elmoneyman.yummythreats.FakeData.DataModuleTest;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;
import static rx.Observable.OnSubscribe;
import static rx.Observable.error;
import static rx.Observable.fromCallable;
import static rx.Observable.just;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainUiTest {

    private AppComponent appComponent;
    private CountingIdlingResource countingIdlingResource;
    private final int DELAYED_TIMEOUT=1000;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule=new ActivityTestRule<>(MainActivity.class,true,false);

    @Before
    public void setUp(){
        countingIdlingResource = new CountingIdlingResource("countingIdlingResource");
        Instrumentation instrumentation= InstrumentationRegistry.getInstrumentation();
        App app=App.class.cast(instrumentation.getTargetContext().getApplicationContext());
        AppComponentTest component = DaggerAppComponentTest.builder()
                .dataModuleTest( new DataModuleTest() )
                .appModule(new AppModule(app))
                .build();
        appComponent =component;
        app.setAppComponent(component);
    }

    @Test
    public void loadsRecipesFromNetworkAndShowsAsList() {
        List<Recipe> recipeList = (List<Recipe>) OnSubscribe.class.cast( Recipe.class );
        when( appComponent.service() ).thenAnswer( new Answer<Observable<List<Recipe>>>() {
            @Override
            public Observable<List<Recipe>> answer(InvocationOnMock invocationOnMock) {
                countingIdlingResource.increment();
                return fromCallable(() -> {
                    Thread.sleep(DELAYED_TIMEOUT);
                    countingIdlingResource.decrement();
                    return recipeList;
                });
            }
        });
        activityTestRule.launchActivity(null);
        onView(withId(R.id.recipe_list)).check(matches(isDisplayed()));
        //check out if provided items are visible
        for (int index = 0; index < recipeList.size(); index++) {
            Recipe recipe = recipeList.get(index);
            String steps = Integer.toString(recipe.getSteps().size());
            String servings = Integer.toString(recipe.getServings());

            onView(withId(R.id.recipe_list))
                    .perform(RecyclerViewActions.scrollToPosition(index));
            Matcher<View> visibleSibling = hasSibling(allOf(withId(R.id.recipe_title), withText(recipe.getName())));
            onView(allOf(withId(R.id.recipe_title), withText(recipe.getName())))
                    .check(matches( TestRecipeMatchesApiData.textAndDisplayMatch(recipe.getName())));
            onView(allOf(withId(R.id.step_label), visibleSibling))
                    .check(matches(allOf( TestRecipeMatchesApiData.coloredTextMatch(R.string.steps_label, steps),isDisplayed())));
            onView(allOf(withId(R.id.servings_label), visibleSibling))
                    .check(matches(allOf( TestRecipeMatchesApiData.coloredTextMatch(R.string.servings_label, servings),isDisplayed())));
            if (recipe.getImageUrl() == null) {
                onView(allOf(withId(R.id.recipe_image), visibleSibling))
                        .check( matches( allOf( TestRecipeMatchesApiData.withDrawable( R.drawable.ic_pastry ), isDisplayed() ) ) );
            }
        }
    }


    @Test
    public void showsErrorMessageWhenLoadingRecipesFailed(){
        when( appComponent.service() ).thenReturn( (RecipeService<Recipe>) error( new Exception() ) );
        activityTestRule.launchActivity(null);

        onView(withId(R.id.message)).check(matches(allOf(isDisplayed(),
                    TestRecipeMatchesApiData.compoundDrawableMatch(R.drawable.ic_empty_box),
                    withText(R.string.message_no_network))));
    }

    @Test
    public void showsErrorMessageWhenGetsEmptyData(){
        when( appComponent.service() ).thenReturn( (RecipeService<Recipe>) just( null ) );
        activityTestRule.launchActivity(null);

        onView(withId(R.id.message)).check(matches(allOf(isDisplayed(),
                TestRecipeMatchesApiData.compoundDrawableMatch(R.drawable.ic_empty_box),
                withText(R.string.message_empty_query))));
    }

    @After
    public void tearDown(){
        Espresso.unregisterIdlingResources(countingIdlingResource);
    }
}
