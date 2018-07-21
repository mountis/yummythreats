package com.elmoneyman.yummythreats.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.Network.RecipeService;
import com.elmoneyman.yummythreats.R;
import com.elmoneyman.yummythreats.Utils.BaseSchedulerProvider;
import com.elmoneyman.yummythreats.Utils.RecipeConstants;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.elmoneyman.yummythreats.Utils.RecipeWidgetUtils.convertToJsonString;

public class Widget extends AppWidgetProvider {

    @Inject
    protected RecipeService<com.elmoneyman.yummythreats.Model.Recipe> recipeService;{
        App.appInstance()
                .appComponent()
                .inject(this);
    }

    @Inject
    protected BaseSchedulerProvider schedulerProvider;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, List<Recipe> recipes) {
        Intent intent=new Intent(context,Service.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        Type type = new TypeToken<ArrayList<Recipe>>() {}.getType();
        intent.putExtra( RecipeConstants.EXTRA_WIDGET_DATA,convertToJsonString(recipes,type));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            views.setRemoteAdapter(R.id.widget_recipe_list,intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            views.setEmptyView(R.id.widget_recipe_list,R.id.empty_view);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if(recipeService !=null){
            recipeService.getRecipes()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(list->{
                        for(int appWidgetId:appWidgetIds){
                            updateAppWidget(context,appWidgetManager,appWidgetId,list);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.widget_recipe_list);
                        }
                    });
        }
    }
}

