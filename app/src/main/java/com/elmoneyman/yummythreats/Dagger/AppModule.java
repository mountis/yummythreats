package com.elmoneyman.yummythreats.Dagger;

import android.app.Application;
import android.content.Context;

import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotifications;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application=application;
    }

    @Singleton
    @Provides
    Context provideWithContext(){
        return application;
    }

    @Singleton
    @Provides
    MessageNotificationProvider provideMessageNotifications(MessageNotifications messageNotifications){
        return messageNotifications;
    }

    @Singleton
    @Provides
    RxBus provideBus(){
        return new RxBus();
    }
}
