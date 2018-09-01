package com.elmoneyman.yummythreats.FakeData;


import com.elmoneyman.yummythreats.Dagger.AppModule;
import com.elmoneyman.yummythreats.Dagger.NetworkModule;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        DataModuleTest.class,
        NetworkModule.class,
        AppModule.class
})
public interface AppComponentTest extends com.elmoneyman.yummythreats.Dagger.AppComponent {
    RecipeService<Recipe> service();

    BaseSchedulerProvider schedulerProvider();
}
