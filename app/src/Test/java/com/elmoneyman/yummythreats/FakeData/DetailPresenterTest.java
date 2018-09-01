package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Contract.DetailsRecipeContract;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Presenter.DetailPresenter;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.ImmediateSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

import static com.elmoneyman.yummythreats.FakeData.RecipeTestUtils.FAKE_ID;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterTest {

    @Mock
    private DetailsRecipeContract.View view;

    @Mock
    private RecipeService<Recipe> recipeService;

    @Mock
    private MessageNotificationProvider messageProvider;

    private DetailsRecipeContract.Presenter presenter;

    @Before
    public void setUp() {
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();
        presenter = new DetailPresenter( recipeService, schedulerProvider, messageProvider );
        presenter.attachView( view );
    }

    @Test
    public void showsRecipeById() {
        Recipe recipe = new Recipe();
        when( recipeService.getRecipeById( FAKE_ID ) ).thenReturn( Observable.just( recipe ) );

        presenter.fetchById( FAKE_ID );

        verify( recipeService ).getRecipeById( eq( FAKE_ID ) );
        verify( view ).showRecipe( recipe );
    }

    @Test
    public void showsEmptyMessageWhenThereIsNoData() {
        when( recipeService.getRecipeById( FAKE_ID ) ).thenReturn( Observable.just( null ) );

        presenter.fetchById( FAKE_ID );

        verify( recipeService ).getRecipeById( eq( FAKE_ID ) );
        verify( messageProvider ).emptyMessage();
        verify( view ).showMessage( anyString() );
    }
}
