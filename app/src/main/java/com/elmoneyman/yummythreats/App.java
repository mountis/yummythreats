package com.elmoneyman.yummythreats;

import android.app.Application;

import com.elmoneyman.yummythreats.Dagger.AppComponent;
import com.elmoneyman.yummythreats.Dagger.AppModule;
import com.elmoneyman.yummythreats.Dagger.DaggerAppComponent;
import com.elmoneyman.yummythreats.Dagger.DataModule;
import com.elmoneyman.yummythreats.Dagger.NetworkModule;

public class App extends Application {

    private static App INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
        installAppComponent();
    }

    private void installAppComponent(){
        appComponent= DaggerAppComponent.builder()
                .dataModule(new DataModule())
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule()).build();
    }

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;

    }

    public AppComponent appComponent(){
        return appComponent;
    }

    public static App appInstance(){
        return INSTANCE;
    }

}
