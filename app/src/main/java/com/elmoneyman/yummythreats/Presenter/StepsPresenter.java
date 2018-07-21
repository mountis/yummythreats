package com.elmoneyman.yummythreats.Presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.elmoneyman.yummythreats.Contract.StepsContract;
import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Utils.MessageNotificationProvider;

import java.util.List;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

public class StepsPresenter implements StepsContract.Presenter{

    private StepsContract.View view;
    private MessageNotificationProvider messageNotificationProvider;
    private Step currentStep;
    private StepsWrapper stepsWrapper;

    public StepsPresenter(@NonNull StepsWrapper stepsWrapper,
                          @NonNull MessageNotificationProvider messageNotificationProvider){
        this.stepsWrapper = stepsWrapper;
        this.messageNotificationProvider = messageNotificationProvider;
    }

    @Override
    public void attachView(@NonNull StepsContract.View view) {
        checkNotNull(view);
        this.view=view;
    }

    @Override
    public void stop() {
        view.pauseVideo();
    }

    @Override
    public void showCurrent() {
        Step step=currentStep;
        if(stepsWrapper.isInBounds()){
            step= stepsWrapper.current();
        }
        currentStep=step;
        if(currentStep!=null){
            view.showPageNumber( stepsWrapper.currentIndex, stepsWrapper.count());
            String shortDescription=currentStep.getShortDescription();
            String description=currentStep.getDescription();
            if(TextUtils.equals(shortDescription,description)){
                description=null;
            }
            view.showDescription(shortDescription,description);
            buttonManager();
            if(playVideo(currentStep.getVideoUrl())||playVideo(currentStep.getImageUrl())){
                return;
            }
            view.hidePlayer();
        }else{
            view.showMessage( messageNotificationProvider.emptyMessage());
        }
    }

    private boolean playVideo(String url){
        if(!TextUtils.isEmpty(url)){
            view.playVideo(url);
            return true;
        }
        return false;
    }

    private void buttonManager(){
        if(stepsWrapper.currentIndex==0){
            view.hidePrevButton();
        }else{
            view.showPrevButton();
        }

        if(stepsWrapper.currentIndex>= stepsWrapper.count()-1){
            view.hideNextButton();
        }else{
            view.showNextButton();
        }
    }

    @Override
    public void requestStep(int step) {
        if(step>=0 && step< stepsWrapper.count()){
            stepsWrapper.currentIndex=step;
            showCurrent();
        }
    }

    @Override
    public void showNext() {
        if((stepsWrapper.currentIndex+1)< stepsWrapper.count()) {
            stepsWrapper.currentIndex++;
            showCurrent();
        }
    }

    @Override
    public void showPrev() {
        if((stepsWrapper.currentIndex-1)>=0) {
            stepsWrapper.currentIndex--;
            showCurrent();
        }
    }

    public static class StepsWrapper {
        private int currentIndex;
        private List<Step> steps;

        public int count(){
            return steps.size();
        }

        public Step current(){
            return steps.get(currentIndex);
        }

        public boolean isInBounds(){
            return currentIndex <=count();
        }

        private StepsWrapper(List<Step> steps,int currentStep){
            this.steps=steps;
            this.currentIndex =currentStep;
        }

        public static StepsWrapper wrap(List<Step> steps, int currentStep){
            return new StepsWrapper(steps,currentStep);
        }
    }
}
