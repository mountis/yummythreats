package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Contract.RecipesContract;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Presenter.RecipesPresenter;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.ImmediateSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import rx.Observable;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.provideRecipeList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RecipesPresenterTest {

    @Mock
    private RecipesContract.View view;

    @Mock
    private RecipeService<Recipe> recipeService;

    @Mock
    private MessageNotificationProvider messageProvider;

    private RecipesContract.Presenter presenter;

    @Before
    public void setUp() {
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();
        presenter = new RecipesPresenter( recipeService, schedulerProvider, messageProvider );
        presenter.attachView( view );
    }

    @Test
    public void showsRecipesOnQueryRecipesMethod() {
        List<Recipe> recipes = provideRecipeList();
        when( recipeService.getRecipes() ).thenReturn( Observable.just( recipes ) );

        presenter.queryRecipes();

        verify( recipeService ).getRecipes();
        verify( view ).setLoading( eq( true ) );
        verify( view ).setLoading( eq( false ) );
        verify( view ).showRecipes( recipes );
    }

    @Test
    public void showsEmptyMessageWhenThereIsNoData() {
        when( recipeService.getRecipes() ).thenReturn( Observable.just( null ) );

        presenter.queryRecipes();

        verify( recipeService ).getRecipes();
        verify( messageProvider ).emptyMessage();
        verify( view ).setLoading( eq( true ) );
        verify( view ).setLoading( eq( false ) );
        verify( view ).showErrorMessage( anyString() );
    }
}
