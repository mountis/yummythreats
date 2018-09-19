package com.elmoneyman.yummythreats.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Listeners.StepsOnClick;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends AbstractAdapter<Step>{

    private int highlightedPosition=-1;

    public StepsAdapter(@NonNull Context context,
                        @NonNull RxBus rxBus){
        super(context,rxBus);
    }

    public class StepViewHolder extends AbstractAdapter<Step>.AbstractViewHolder
            implements View.OnClickListener{

        @BindView(R.id.step_short_description)
        TextView shortDescription;


        @BindView(R.id.thumbnail_url)
        ImageView mStepThumbnail;

        public StepViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(!isLocked()){
                lock();
                rxBus.send( StepsOnClick.click(data, getAdapterPosition()));
            }
        }

        void onBind(){
            Step step=at(getAdapterPosition());
            shortDescription.setText(step.getShortDescription());
            String imageURL = step.getImageUrl();
            if (!(imageURL == null || imageURL.isEmpty())) {
                Picasso.get()
                        .load( imageURL )
                        .into( mStepThumbnail );
            }
            if(highlightedPosition==getAdapterPosition()){
                itemView.setBackgroundResource(R.drawable.background_selected);
            }else{
                itemView.setBackgroundResource(R.drawable.background_simple);
            }
        }
    }

    public void highlightPosition(int position){
        if(highlightedPosition!=position){
            int prev=highlightedPosition;
            this.highlightedPosition=position;
            notifyItemChanged(highlightedPosition);
            if(prev!=-1){
                notifyItemChanged(prev);
            }
        }
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=inflater.inflate(R.layout.steps_adapter,parent,false);
        return new StepViewHolder(root);
    }

}
