package com.elmoneyman.yummythreats.Mapper;

import com.elmoneyman.yummythreats.Model.Step;
import com.elmoneyman.yummythreats.Model.StepSerialized;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Steps implements Mapper<Step,StepSerialized> {

    @Inject
    public Steps(){}

    @Override
    public Step map(StepSerialized stepSerialized) {
        if(stepSerialized ==null) return null;
        Step step=new Step();
        step.setImageUrl( stepSerialized.getImageUrl());
        step.setDescription( stepSerialized.getDescription());
        step.setShortDescription( stepSerialized.getShortDescription());
        step.setVideoUrl( stepSerialized.getVideoUrl());
        step.setStepId( stepSerialized.getId());
        return step;
    }

    @Override
    public StepSerialized reverseMap(Step step) {
        if(step==null) return null;
        StepSerialized stepSerialized =new StepSerialized();
        stepSerialized.setId(step.getStepId());
        stepSerialized.setVideoUrl(step.getVideoUrl());
        stepSerialized.setShortDescription(step.getShortDescription());
        stepSerialized.setImageUrl(step.getImageUrl());
        stepSerialized.setDescription(step.getDescription());
        return stepSerialized;
    }

    @Override
    public List<Step> map(List<StepSerialized> stepSerializedList) {
        if(stepSerializedList==null||stepSerializedList.isEmpty()) return null;
        List<Step> result=new ArrayList<>(stepSerializedList.size());
        for (StepSerialized stepEntity : stepSerializedList) {
            result.add( map( stepEntity ) );
        }
        return result;
    }
}
