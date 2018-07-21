package com.elmoneyman.yummythreats.Widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elmoneyman.yummythreats.Model.Ingredient;
import com.elmoneyman.yummythreats.R;
import com.elmoneyman.yummythreats.Utils.RecipeConstants;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.elmoneyman.yummythreats.Utils.RecipeWidgetUtils.convertFromJsonString;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class Service extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String jsonString=intent.getStringExtra( RecipeConstants.EXTRA_WIDGET_DATA);
        Type type = new TypeToken<ArrayList<com.elmoneyman.yummythreats.Model.Recipe>>() {}.getType();
        return new RecipeRemoteFactory(getApplicationContext(),convertFromJsonString(jsonString,type));
    }

    private class RecipeRemoteFactory implements RemoteViewsFactory {

        private Context context;
        private List<com.elmoneyman.yummythreats.Model.Recipe> recipes;

        public RecipeRemoteFactory(@NonNull Context context,
                                   @Nullable List<com.elmoneyman.yummythreats.Model.Recipe> recipes){
            this.context=context;
            this.recipes=recipes;
        }

        @Override
        public void onCreate() {}

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            com.elmoneyman.yummythreats.Model.Recipe recipe=at(position);
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.widget_recipe_title,recipe.getName());
            List<Ingredient> ingredients=recipe.getIngredients();
            if(ingredients!=null){
                StringBuilder builder=new StringBuilder();
                final String BLANK=" ";
                final String bullet="\u25CF";
                for(Ingredient ingredient:ingredients){
                    builder.append(bullet);
                    builder.append(BLANK);
                    builder.append(ingredient.getQuantity());
                    builder.append(BLANK);
                    builder.append(ingredient.getMeasure());
                    builder.append(BLANK);
                    builder.append(ingredient.getIngredient());
                    builder.append('\n');
                    builder.append('\n');
                }
                remoteViews.setTextViewText(R.id.widget_recipe_ingredients,builder.toString());
            }
            return remoteViews;
        }

        private com.elmoneyman.yummythreats.Model.Recipe at(int index){
            return recipes.get(index);
        }

        @Override
        public int getCount() {
            return recipes!=null?recipes.size():0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
