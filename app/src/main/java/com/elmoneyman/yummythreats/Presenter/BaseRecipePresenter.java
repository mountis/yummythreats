package com.elmoneyman.yummythreats.Presenter;


import android.support.annotation.NonNull;

import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import rx.subscriptions.CompositeSubscription;

class BaseRecipePresenter {

    protected final RecipeService<Recipe> recipeService;
    protected final BaseSchedulerProvider schedulerProvider;
    protected final CompositeSubscription subscriptions;
    protected final MessageNotificationProvider messageNotificationProvider;

    BaseRecipePresenter(@NonNull RecipeService<Recipe> recipeService,
                               @NonNull BaseSchedulerProvider schedulerProvider,
                               @NonNull MessageNotificationProvider messageNotificationProvider){
        this.recipeService = recipeService;
        this.schedulerProvider=schedulerProvider;
        this.messageNotificationProvider = messageNotificationProvider;
        this.subscriptions=new CompositeSubscription();
    }
}
