package com.elmoneyman.yummythreats.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Contract.StepsContract;
import com.elmoneyman.yummythreats.Fragment.DetailFragment;
import com.elmoneyman.yummythreats.Fragment.StepsFragment;
import com.elmoneyman.yummythreats.Listeners.StepNavigation;
import com.elmoneyman.yummythreats.Listeners.StepsOnClick;
import com.elmoneyman.yummythreats.Listeners.ToolbarChangeEvent;
import com.elmoneyman.yummythreats.Listeners.Visibility;
import com.elmoneyman.yummythreats.Presenter.StepsPresenter;
import com.elmoneyman.yummythreats.R;
import com.elmoneyman.yummythreats.Utils.MessageNotifications;
import com.elmoneyman.yummythreats.Utils.RecipeConstants;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends BaseActivity {

    @BindView(R.id.action_bar)
    protected Toolbar actionBar;

    @BindBool(R.bool.is_tablet)
    protected boolean isTablet;

    private StepsContract.Presenter stepsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setActionBar();
        if(savedInstanceState==null){
            setUI(getIntent().getExtras());
        }
    }

    private void setUI(Bundle args){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe, DetailFragment.newInstance(args), RecipeConstants.DETAIL_TAG )
                .commit();
    }

    private void setActionBar(){
        setSupportActionBar(actionBar);
        if(getSupportActionBar()!=null){
            actionBar.setTitleTextColor( ContextCompat.getColor(this,R.color.textColor));
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable drawable=actionBar.getNavigationIcon();
            actionBar.setNavigationOnClickListener(v->onBackPressed());
            if(drawable!=null){
                drawable.setColorFilter( ContextCompat.getColor(this,R.color.textColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    void handleEvent(@NonNull Object event) {
        if(event instanceof StepsOnClick){
            stepView(StepsOnClick.class.cast(event));
        }else if(event instanceof ToolbarChangeEvent){
            changeToolbar(ToolbarChangeEvent.class.cast(event));
        }else if(event instanceof Visibility){
            changeVisibility(Visibility.class.cast(event));
        }else if(event instanceof StepNavigation){
            stepMovement(StepNavigation.class.cast(event));
        }
    }

    private void stepMovement(StepNavigation event){
        DetailFragment fragment=DetailFragment.class.cast
                (getSupportFragmentManager().findFragmentByTag( RecipeConstants.DETAIL_TAG ));
        if(fragment!=null){
            fragment.highlightStep(event.step);
        }
    }

    private void changeToolbar(ToolbarChangeEvent event){
        actionBar.setTitle(event.text);
    }

    private void changeVisibility(Visibility event){
        if(!event.visible){
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(0);
        }
    }

    private void stepView(StepsOnClick clickEvent){
        if(!isTablet){
            StepHierachy(clickEvent);
        }else{
            stepUpdate(clickEvent);
        }
    }

    private void actionBarVisibility(boolean isVisible){
        if(getSupportActionBar()!=null){
            if(!isVisible) {
                getSupportActionBar().hide();
            }else{
                getSupportActionBar().show();
            }
        }
    }
    private void StepHierachy(StepsOnClick clickEvent){
        actionBarVisibility(false);
        FragmentManager manager=getSupportFragmentManager();
        if(manager.findFragmentByTag( RecipeConstants.STEPS_TAG)!=null){
            manager.beginTransaction()
                    .remove(manager.findFragmentByTag( RecipeConstants.DETAIL_TAG ))
                    .show(manager.findFragmentByTag( RecipeConstants.STEPS_TAG))
                    .commit();
        }else{
            StepsPresenter.StepsWrapper wrapper= StepsPresenter.StepsWrapper.wrap(clickEvent.steps,clickEvent.currentStep);
            stepsPresenter=new StepsPresenter(wrapper,new MessageNotifications(this));
            StepsFragment fragment=new StepsFragment();
            fragment.attachPresenter(stepsPresenter);
            manager.beginTransaction()
                    .addToBackStack( RecipeConstants.DETAIL_TAG )
                    .replace(R.id.recipe,fragment, RecipeConstants.STEPS_TAG)
                    .commit();
        }
    }

    private void stepUpdate(StepsOnClick clickEvent){
        if(stepsPresenter==null){
            StepsPresenter.StepsWrapper wrapper= StepsPresenter.StepsWrapper.wrap(clickEvent.steps,clickEvent.currentStep);
            stepsPresenter=new StepsPresenter(wrapper,new MessageNotifications(this));
            StepsFragment fragment=StepsFragment.class.cast(
                    getSupportFragmentManager().findFragmentById(R.id.recipe_steps));
            fragment.attachPresenter(stepsPresenter);
            stepsPresenter.showCurrent();
        }else{
            stepsPresenter.requestStep(clickEvent.currentStep);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isTablet) {
            actionBarVisibility(true);
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                manager.popBackStack();
                manager.beginTransaction()
                        .show(manager.findFragmentByTag( RecipeConstants.DETAIL_TAG ))
                        .commit();
                if(getSupportActionBar()!=null){
                    if(!getSupportActionBar().isShowing()){
                        changeVisibility( Visibility.change(true));
                    }
                }
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    void initializeDependencies() {
        App.appInstance()
                .appComponent()
                .inject(this);
    }


}
