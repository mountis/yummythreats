package com.elmoneyman.yummythreats.FakeData;


import android.content.ContentResolver;
import android.os.Build;

import com.elmoneyman.yummythreats.BuildConfig;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Network.LocalRecipeSource;
import com.elmoneyman.yummythreats.Network.RecipeManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class LocalRecipeSourceTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ContentResolver mockContentResolver;

    @Mock
    private RecipeManager recipeManager;

    @InjectMocks
    private LocalRecipeSource localRecipeSource;

    @Test
    public void returnsAllRecipes() {
        Mockito.when( recipeManager.queryAll() ).thenReturn( new ArrayList<>() );
        localRecipeSource.getRecipes()
                .subscribeOn( AndroidSchedulers.mainThread() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( this::shouldNotBeNull );
        Mockito.verify( recipeManager ).queryAll();
    }

    @Test
    public void insertsRecipeIfRecipeIsNotNull() {
        RecipeSerialized serialized = RecipeTestUtils.provideRecipeSerialized();
        Mockito.when( recipeManager.insert( eq( serialized ) ) ).thenReturn( recipeManager );
        Mockito.when( recipeManager.insertIngredients( eq( serialized.getId() ), any() ) ).thenReturn( recipeManager );
        Mockito.when( recipeManager.insertSteps( eq( serialized.getId() ), any() ) ).thenReturn( recipeManager );

        localRecipeSource.insert( serialized );

        Mockito.verify( recipeManager ).insert( eq( serialized ) );
        Mockito.verify( recipeManager ).insertIngredients( eq( serialized.getId() ), any() );
        Mockito.verify( recipeManager ).insertSteps( eq( serialized.getId() ), any() );
    }

    private void shouldNotBeNull(Object object) {
        assertTrue( object != null );
    }
}

