package com.elmoneyman.yummythreats.Listeners;


import com.elmoneyman.yummythreats.Model.Step;

import java.util.List;

public class StepsOnClick {

    public final List<Step> steps;
    public final int currentStep;

    private StepsOnClick(List<Step> steps,
                         int currentStep){
        this.steps=steps;
        this.currentStep=currentStep;
    }

    public static StepsOnClick click(List<Step> steps,
                                     int currentStep){
        return new StepsOnClick(steps,currentStep);
    }
}
