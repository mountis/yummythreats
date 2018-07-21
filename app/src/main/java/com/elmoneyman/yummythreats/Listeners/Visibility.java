package com.elmoneyman.yummythreats.Listeners;


public class Visibility {

    public final boolean visible;

    private Visibility(boolean visible){
        this.visible=visible;
    }

    public static Visibility change(boolean visible){
        return new Visibility(visible);
    }
}
