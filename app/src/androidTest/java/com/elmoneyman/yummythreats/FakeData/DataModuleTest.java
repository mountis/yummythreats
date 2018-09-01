package com.elmoneyman.yummythreats.FakeData;

import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeConnectionManager;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.ImmediateSchedulerProvider;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModuleTest {

    @Singleton
    @Provides
    RecipeService<Recipe> recipeRepository() {
        return Mockito.mock( RecipeConnectionManager.class );
    }

    @Singleton
    @Provides
    BaseSchedulerProvider baseSchedulerProvider() {
        return new ImmediateSchedulerProvider();
    }
}
