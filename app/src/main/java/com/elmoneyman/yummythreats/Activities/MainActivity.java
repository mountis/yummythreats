package com.elmoneyman.yummythreats.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Fragment.RecipesFragment;
import com.elmoneyman.yummythreats.Listeners.RecipeOnClick;
import com.elmoneyman.yummythreats.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.action_bar)
    protected Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(savedInstanceState==null) {
            setUI();
        }
    }

    private void setUI(){
        setSupportActionBar(actionBar);
        if(getSupportActionBar()!=null){
            actionBar.setTitleTextColor( ContextCompat.getColor(this,R.color.textColor));
            getSupportActionBar().setTitle(R.string.recipes_label);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame,new RecipesFragment())
                .commit();
    }

    @Override
    void handleEvent(@NonNull Object event) {
        if(event instanceof RecipeOnClick){
            showRecipeDetails(RecipeOnClick.class.cast(event));
        }
    }

    private void showRecipeDetails(RecipeOnClick clickEvent){
        recipeItem.goToRecipeDetails(this,clickEvent);
    }

    @Override
    void initializeDependencies() {
        App.appInstance()
                .appComponent()
                .inject(this);
    }
}
