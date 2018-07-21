package com.elmoneyman.yummythreats.Exoplayer;

import android.support.v4.media.session.MediaSessionCompat;

public class ExoMediaSessionCallback extends MediaSessionCompat.Callback {

    private final ExoPlayback<?> playback;

    public ExoMediaSessionCallback(ExoPlayback<?> playback){
        this.playback=playback;

    }

    @Override
    public void onPlay() {
        super.onPlay();
        playback.play(playback.getResource());
    }

    @Override
    public void onPause() {
        super.onPause();
        playback.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        playback.stop();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
        playback.seekTo(pos);
    }


}
