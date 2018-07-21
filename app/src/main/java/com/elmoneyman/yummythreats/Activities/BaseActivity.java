package com.elmoneyman.yummythreats.Activities;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.elmoneyman.yummythreats.Listeners.RecipeItem;
import com.elmoneyman.yummythreats.Listeners.RxBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected RecipeItem recipeItem;

    @Inject
    protected RxBus eventBus;

    protected CompositeDisposable disposables;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposables=new CompositeDisposable();
        initializeDependencies();
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        disposables.add(eventBus.asFlowable()
                .subscribe(this::eventProcessing ));
    }

    private void eventProcessing(Object object){
        if(object!=null){
            handleEvent(object);
        }
    }

    @CallSuper
    @Override
    protected void onStop(){
        super.onStop();
        disposables.clear();
    }

    abstract void handleEvent(@NonNull Object event);

    abstract void initializeDependencies();
}
