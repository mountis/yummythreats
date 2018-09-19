package com.elmoneyman.yummythreats.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elmoneyman.yummythreats.App;
import com.elmoneyman.yummythreats.Contract.StepsContract;
import com.elmoneyman.yummythreats.Dagger.DaggerViewComponent;
import com.elmoneyman.yummythreats.Dagger.PresenterModule;
import com.elmoneyman.yummythreats.Exoplayer.ExoPlayback;
import com.elmoneyman.yummythreats.Listeners.RxBus;
import com.elmoneyman.yummythreats.Listeners.StepNavigation;
import com.elmoneyman.yummythreats.Listeners.Visibility;
import com.elmoneyman.yummythreats.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

import static android.transition.TransitionManager.beginDelayedTransition;
import static com.elmoneyman.yummythreats.Utils.RecipeConstants.SESSION_TAG;
import static com.google.android.exoplayer2.ExoPlayerLibraryInfo.TAG;

public class StepsFragment extends BaseFragment
        implements StepsContract.View, ExoPlayback.Callback {

    @BindView(R.id.player)
    protected SimpleExoPlayerView playerView;

    @BindView(R.id.step_short_description)
    protected TextView shortDescription;

    @BindView(R.id.step_description)
    protected TextView description;

    @Nullable
    @BindView(R.id.pages)
    protected TextView pageNumberTracker;

    @Nullable
    @BindView(R.id.step_next)
    protected View next;

    @Nullable
    @BindView(R.id.step_prev)
    protected View previous;

    @Inject
    protected ExoPlayback<?> playback;

    @Inject
    protected RxBus rxBus;

    @Nullable
    @BindView(R.id.footer)
    protected View footer;

    @BindView(R.id.cardView)
    protected CardView cardView;

    private boolean isRotated;
    private StepsContract.Presenter presenter;
    private MediaSessionCompat mediaSession;
    long playbackPosition;
    private ExoPlayer player;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        playback.setCallback( this );

        if (savedInstanceState != null) {
            Log.e( TAG, "(saveInstanceState)get current position : " + playbackPosition );
            playbackPosition = savedInstanceState.getLong( "playback_position" );
        }

        mediaSession = new MediaSessionCompat( getContext(), SESSION_TAG );
        mediaSession.setFlags( MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS );
        mediaSession.setMediaButtonReceiver( null );
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder()
                .setActions( PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE );
        mediaSession.setPlaybackState( stateBuilder.build() );
        mediaSession.setCallback( playback.getExoMediaSessionCallback() );

        if (player != null && playbackPosition != 0) {
            player.seekTo( playbackPosition );
            player.setPlayWhenReady( true );
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup parentContainer,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate( R.layout.fragment_steps, parentContainer, false );
        bind( root );
        return root;
    }

    private void updateSystemUI() {
        rxBus.send( Visibility.change( isPortrait() ) );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.showCurrent();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        if (view != null) {
            if (presenter != null) presenter.showCurrent();
        }
    }

    private boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {

        if (player != null) {
            currentState.putLong( "playback_position", player.getCurrentPosition() );
        }
    }


    @Optional
    @OnClick(R.id.step_next)
    public void clickNext() {
        presenter.showNext();
    }

    @Optional
    @OnClick(R.id.step_prev)
    public void clickPrev() {
        presenter.showPrev();
    }

    @Override
    public void hidePlayer() {
        playback.stop();
        playerView.setPlayer( null );
        playerView.setVisibility( View.GONE );
    }

    @Override
    public void onMediaPlay() {
        mediaSession.setActive( true );
        playerView.setVisibility( View.VISIBLE );
        if (playerView.getPlayer() == null) {
            playerView.setPlayer( SimpleExoPlayer.class.cast( playback.getPlayer() ) );
        }
    }

    @Override
    public void onMediaStop() {
        mediaSession.setActive( false );
    }

    @Override
    public void showMessage(@NonNull String message) {
    }

    @Override
    public void showPageNumber(int currentPage, int total) {
        if (pageNumberTracker != null) {
            String result = Integer.toString( currentPage + 1 ) + '/' + Integer.toString( total );
            pageNumberTracker.setText( result );
        }
        if (isTablet) {
            rxBus.send( StepNavigation.move( currentPage ) );
        }
    }

    @Override
    public void showDescription(String shortDescription, String description) {
        setText( this.shortDescription, shortDescription );
        setText( this.description, description );
    }

    private void setText(TextView textView, String text) {
        if (text == null) {
            textView.setVisibility( View.GONE );
        } else {
            if (textView.getVisibility() != View.VISIBLE) {
                textView.setVisibility( View.VISIBLE );
            }
            textView.setText( text );
        }
    }

    @Override
    public void playVideo(String videoUrl) {
        playback.play( videoUrl );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            makeRotationIfNeeded();
        }
    }

    @Override
    public void pauseVideo() {
        playback.pause();
    }

    @Override
    public void attachPresenter(@NonNull StepsContract.Presenter presenter) {
        this.presenter = presenter;
        this.presenter.attachView( this );
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public void hidePrevButton() {
        if (previous != null) hideButton( previous );
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    private void hideButton(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.animate()
                    .alpha( 0f )
                    .setDuration( 150 )
                    .setListener( new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd( animation );
                            view.setVisibility( View.GONE );
                        }
                    } )
                    .start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    private void showButton(View view) {
        view.setVisibility( View.VISIBLE );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            view.animate()
                    .alpha( 1f )
                    .setDuration( 150 )
                    .setListener( null )
                    .start();
        }
    }

    @Override
    public void hideNextButton() {
        if (next != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            hideButton( next );
        }
    }

    @Override
    public void showNextButton() {
        if (next != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            showButton( next );
        }
    }

    @Override
    public void showPrevButton() {
        if (previous != null) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            showButton( previous );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void makeRotationIfNeeded() {
        if (!isRotated) {
            if (!isPortrait()) {
                rotate();
            }
        }
    }

    private ViewGroup getRoot() {
        return ViewGroup.class.cast( getView() );
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged( newConfig );
        rotate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void rotate() {
        if (!isTablet) {
            handleScreenRotation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleScreenRotation() {
        boolean isVisible = playerView.getVisibility() == View.VISIBLE;
        beginDelayedTransition( getRoot() );

        if (!isPortrait()) {
            if (isVisible) {
                isRotated = true;
                if (footer != null) footer.setVisibility( View.GONE );
                cardView.setVisibility( View.GONE );
                updateSystemUI();
                playerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        } else {
            if (isVisible) {
                isRotated = false;
                playerView.post( () -> playerView.getLayoutParams().height = (int) getResources().getDimension( R.dimen.player_height ) );
                if (footer != null) footer.setVisibility( View.VISIBLE );
                cardView.setVisibility( View.VISIBLE );
                updateSystemUI();
            }
        }
    }

    @Override
    void initializeDependencies() {
        DaggerViewComponent.builder()
                .appComponent( App.appInstance().appComponent() )
                .presenterModule( new PresenterModule() )
                .build().inject( this );
    }
}
