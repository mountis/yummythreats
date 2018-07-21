package com.elmoneyman.yummythreats.Listeners;


public class StepNavigation {

    public final int step;

    private StepNavigation(int step){
        this.step=step;
    }

    public static StepNavigation move(int step){
        return new StepNavigation(step);
    }
}
