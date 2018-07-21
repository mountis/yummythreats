package com.elmoneyman.yummythreats;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import com.elmoneyman.yummythreats.Activities.DetailsActivity;
import com.elmoneyman.yummythreats.Dagger.AppComponent;
import com.elmoneyman.yummythreats.Dagger.AppModule;
import com.elmoneyman.yummythreats.Dagger.DaggerAppComponent;
import com.elmoneyman.yummythreats.Dagger.DataModule;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Network.RecipeService;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailsUi {

    private RecipeService<Recipe> recipeService;

    private final static int EXTRA_ID=1;

    @Rule
    public ActivityTestRule<DetailsActivity> activityTestRule=new ActivityTestRule<DetailsActivity>(DetailsActivity.class,true,false){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent intent = new Intent(targetContext, DetailsActivity.class);
            return intent;
        }
    };

    @Before
    public void setUp(){
        Instrumentation instrumentation= InstrumentationRegistry.getInstrumentation();
        App app=App.class.cast(instrumentation.getTargetContext().getApplicationContext());
        AppComponent component= DaggerAppComponent.builder()
                .dataModule(new DataModule())
                .appModule(new AppModule(app))
                .build();
        recipeService =component.repository();
        app.setAppComponent(component);
        Intents.init();
    }

    @Test
    public void recipeDetail(){
        Recipe recipe= Recipe.class.cast( recipeService );
        when( recipeService.getRecipeById(eq(EXTRA_ID))).thenReturn(Observable.just(recipe));
        activityTestRule.launchActivity(null);
        Espresso.onView(withId(R.id.scrollView)).perform(swipeDown());
        onView(withId(R.id.ingredient_caption))
                .check(matches(allOf(isDisplayed(),withText(R.string.ingredients_headline),
                        TestRecipeMatchesApiData.compoundDrawableMatch(R.drawable.ic_recipe))));
        onView(withId(R.id.ingredient_list))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps))
                .check(matches(isDisplayed()));

        List<Step> steps=recipe.getSteps();
        Espresso.onView(withId(R.id.scrollView)).perform(swipeUp());
        for(int index=0;index<steps.size();index++){
            Step step=steps.get(index);
            onView(withId(R.id.recipe_steps))
                    .perform(RecyclerViewActions.scrollToPosition(index));
            onView(allOf(withText(step.getShortDescription()),
                    withId(R.id.step_short_description)))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void recipeSteps(){
        Recipe recipe=Recipe.class.cast( recipeService );
        when( recipeService.getRecipeById(eq(EXTRA_ID))).thenReturn(Observable.just(recipe));
        activityTestRule.launchActivity(null);

        List<Step> steps=recipe.getSteps();
        for(int index=0;index<steps.size();index++) {
            Step step = steps.get(index);
            onView(withId(R.id.recipe_steps))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(index, click()));
            onView(withId(R.id.action_bar))
                    .check(matches(not(isDisplayed())));
            onView(withId(R.id.step_short_description))
                    .check(matches(allOf(withText(step.getShortDescription()), isDisplayed())));
            if (step.getDescription() != null) {
                onView(withId(R.id.step_description))
                        .check(matches(allOf(withText(step.getDescription()), isDisplayed())));
            }
            Matcher<View> visibility=isDisplayed();
            if(index==0) visibility=not(visibility);
            onView(withId(R.id.step_prev))
                    .check(matches(visibility));
            visibility=isDisplayed();
            if(index==steps.size()-1) visibility=not(visibility);
            onView(withId(R.id.step_next))
                    .check(matches(visibility));
            Espresso.pressBack();

            onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.action_bar))))
                    .check(matches(withText(recipe.getName())));
        }
    }


    @After
    public  void tearDown(){
        Intents.release();
    }
}
