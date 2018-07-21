package com.elmoneyman.yummythreats.Exoplayer;


public interface ExoPlayback<T> {
    void play(String source);
    void seekTo(long position);
    void setCallback(Callback callback);
    void pause();
    void stop();
    boolean isPlaying();

    void onPositionDiscontinuity();

    long getStreamingPosition();
    String getResource();
    ExoMediaSessionCallback getExoMediaSessionCallback();
    T getPlayer();

    interface Callback {
        void onMediaPlay();
        void onMediaStop();
    }


}
