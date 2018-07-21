package com.elmoneyman.yummythreats.Dagger;

import android.content.Context;

import com.elmoneyman.yummythreats.Activities.BaseActivity;
import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;
import com.elmoneyman.yummythreats.Widget.Widget;
import com.elmoneyman.yummythreats.Widget.Service;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        DataModule.class,
        NetworkModule.class,
        AppModule.class
})
public interface AppComponent {
    void inject(BaseActivity activity);
    void inject(Widget widget);
    void inject(Service widgetService);
    RecipeService<Recipe> repository();
    BaseSchedulerProvider schedulerProvider();
    MessageNotificationProvider messageProvider();
    RxBus bus();
    Context context();
}
