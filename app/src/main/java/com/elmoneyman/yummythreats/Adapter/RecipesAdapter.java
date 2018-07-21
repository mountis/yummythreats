package com.elmoneyman.yummythreats.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elmoneyman.yummythreats.Listeners.RecipeOnClick;
import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Model.Recipe;
import com.elmoneyman.yummythreats.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.elmoneyman.yummythreats.Utils.RecipeStringUtils.mergeColoredText;

public class RecipesAdapter extends AbstractAdapter<Recipe>{

    public RecipesAdapter(@NonNull Context context,
                          @NonNull RxBus rxBus){
        super(context,rxBus);
    }

    public class RecipeViewHolder extends AbstractAdapter<Recipe>.AbstractViewHolder
            implements View.OnClickListener{

        @BindView(R.id.recipe_title)
        TextView recipeTitle;

        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        @BindView(R.id.step_label)
        TextView steps;

        @BindView(R.id.servings_label)
        TextView servings;

        RecipeViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(!isLocked()){
                lock();
                Recipe recipe=at(getAdapterPosition());
                RecipeOnClick click= RecipeOnClick.click(recipe.getId());
                rxBus.send(click);
            }
        }

        void onBind(){
            Recipe recipe=at(getAdapterPosition());
            recipeTitle.setText(recipe.getName());
            if(!TextUtils.isEmpty(recipe.getImageUrl())) {
                Picasso.get()
                        .load(recipe.getImageUrl())
                        .into(recipeImage);
            }
            Context context=inflater.getContext();
            String numberText=Integer.toString(recipe.getSteps().size());
            steps.setText(mergeColoredText(context.getString(R.string.steps_label),numberText,
                        ContextCompat.getColor(context,R.color.colorPrimary),
                        ContextCompat.getColor(context,R.color.yellow_color)));
            numberText=Integer.toString(recipe.getServings());
            servings.setText(mergeColoredText(context.getString(R.string.servings_label),numberText,
                    ContextCompat.getColor(context,R.color.colorPrimary),
                    ContextCompat.getColor(context,R.color.yellow_color)));
        }

    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(inflater.inflate(R.layout.recipe_adapter,parent,false));
    }

}
