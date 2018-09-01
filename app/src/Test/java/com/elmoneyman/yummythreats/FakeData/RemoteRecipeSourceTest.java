package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Network.RecipeAPIManager;
import com.elmoneyman.yummythreats.Network.RemoteRecipeSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class RemoteRecipeSourceTest {

    @Mock
    private RecipeAPIManager recipeAPIManager;

    @InjectMocks
    private RemoteRecipeSource remoteRecipeSource;

    @Test
    public void shouldRequestRecipesFromProvidedApi() {
        Mockito.when( recipeAPIManager.queryRecipes() ).thenReturn( Observable.empty() );
        remoteRecipeSource.getRecipes();
        Mockito.verify( recipeAPIManager ).queryRecipes();
    }
}
