package com.elmoneyman.yummythreats.Listeners;


public class ToolbarChangeEvent {

    public final String text;

    private ToolbarChangeEvent(String text){
        this.text=text;
    }


    public static ToolbarChangeEvent change(String text){
        return new ToolbarChangeEvent(text);
    }

}
