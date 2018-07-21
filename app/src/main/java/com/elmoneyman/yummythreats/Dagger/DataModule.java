package com.elmoneyman.yummythreats.Dagger;

import android.content.Context;

import com.elmoneyman.yummythreats.Mapper.Ingredients;
import com.elmoneyman.yummythreats.Mapper.Mapper;
import com.elmoneyman.yummythreats.Mapper.Recipes;
import com.elmoneyman.yummythreats.Mapper.Steps;
import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.Model.IngredientSerialized;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Model.RecipeSerialized;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;
import com.elmoneyman.yummythreats.Network.RecipeConnectionManager;
import com.elmoneyman.yummythreats.Network.RecipeDataSource;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Network.Local;
import com.elmoneyman.yummythreats.Network.LocalRecipeSource;
import com.elmoneyman.yummythreats.Network.RecipeManager;
import com.elmoneyman.yummythreats.Network.Remote;
import com.elmoneyman.yummythreats.Network.RemoteRecipeSource;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Singleton
    @Provides
    Mapper<Ingredient,IngredientSerialized> ingredientMapper(Ingredients mapper){
        return mapper;
    }

    @Singleton
    @Provides
    Mapper<Recipe,RecipeSerialized> recipeMapper(Recipes mapper){
        return mapper;
    }

    @Singleton
    @Provides
    Mapper<Step,StepSerialized> stepMapper(Steps mapper){
        return mapper;
    }

    @Singleton
    @Provides
    RecipeService<Recipe> recipeRepository(RecipeConnectionManager recipeConnectionManager){
        return recipeConnectionManager;
    }

    @Singleton
    @Provides
    RecipeManager provideRecipeHandler(Context context){
        return RecipeManager.start(context.getContentResolver());
    }

    @Singleton
    @Provides
    @Local
    RecipeDataSource<RecipeSerialized> localRecipeDataSource(LocalRecipeSource recipeSource){
        return recipeSource;
    }

    @Singleton
    @Provides
    @Remote
    RecipeDataSource<RecipeSerialized> remoteRecipeDataSource(RemoteRecipeSource recipeSource){
        return recipeSource;
    }

    @Singleton
    @Provides
    BaseSchedulerProvider provideScheduler(){
        return new SchedulerProvider();
    }
}
