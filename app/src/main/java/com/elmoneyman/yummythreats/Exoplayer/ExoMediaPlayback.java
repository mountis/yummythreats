package com.elmoneyman.yummythreats.Exoplayer;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.EventListener;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class ExoMediaPlayback implements ExoPlayback<ExoPlayer>, EventListener {

    private Context context;
    private Callback callback;
    private ExoPlayer player;
    private String currentSource;
    private ExoMediaSessionCallback exoMediaSessionCallback;

    public ExoMediaPlayback(@NonNull Context context){
        this.context=context;
        this.exoMediaSessionCallback =new ExoMediaSessionCallback(this);
    }

    @Override
    public void play(String source) {
        if(source!=null) {
            createPlayerIfNeeded();
            boolean hasChanged= TextUtils.equals(source,currentSource);
            if(!hasChanged) {
                this.currentSource=source;
                String userAgent = Util.getUserAgent(context, context.getApplicationInfo().name);
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(source), new DefaultDataSourceFactory(context,
                        userAgent), new DefaultExtractorsFactory(), null, null);
                player.prepare(mediaSource);
            }
            player.setPlayWhenReady(true);
            if (callback != null) {
                callback.onMediaPlay();
            }
        }
    }

    @Override
    public void pause() {
        if(player!=null){
            player.setPlayWhenReady(false);
            if(callback!=null){
                callback.onMediaStop();
            }
        }
    }

    @Override
    public void stop() {
        currentSource=null;
        releasePlayer();
        if(callback!=null){
            callback.onMediaStop();
        }
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback=callback;
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onPositionDiscontinuity() {}

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {}

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    private int convertState(int playbackState, boolean playWhenReady){
        switch (playbackState){
            case Player.STATE_BUFFERING:
                return PlaybackStateCompat.STATE_BUFFERING;
            case Player.STATE_READY:
                return playWhenReady?PlaybackStateCompat.STATE_PLAYING:PlaybackStateCompat.STATE_PAUSED;
            case Player.STATE_ENDED:
                return PlaybackStateCompat.STATE_STOPPED;
            default:
                return PlaybackStateCompat.STATE_NONE;
        }
    }

    @Override
    public long getStreamingPosition() {
        if(player!=null){
            return player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public boolean isPlaying() {
        return player!=null && player.getPlayWhenReady();
    }

    @Override
    public void seekTo(long position) {
        if(player!=null){
            player.seekTo(position);
        }
    }

    private void createPlayerIfNeeded(){
        if(player==null){
            TransferListener<? super DataSource> bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory((BandwidthMeter) bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            player= ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            player.addListener(this);

        }
    }

    private void releasePlayer() {
        if(player!=null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public ExoPlayer getPlayer() {
        return player;
    }

    @Override
    public ExoMediaSessionCallback getExoMediaSessionCallback() {
        return exoMediaSessionCallback;
    }

    @Override
    public String getResource() {
        return currentSource;
    }
}
