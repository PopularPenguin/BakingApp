package com.popularpenguin.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.popularpenguin.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/** The fragment holding the individual instructions for a step */
public class InstructionsFragment extends Fragment implements View.OnClickListener,
        Player.EventListener {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    private static final String POSITION_EXTRA = "position";
    private static final String PLAYING_EXTRA = "playing";

    @BindView(R.id.exo_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_instructions) TextView mInstructions;
    @BindView(R.id.iv_thumbnail) ImageView mThumbnailImage;

    private Button mPrevious;
    private Button mNext;

    private Recipe mRecipe;
    private int mIndex;
    private boolean isPhone;
    private boolean isPortrait;

    // ExoPlayer members
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private long mPosition; // the position in ms of the video
    private boolean mPlayWhenReady; // track the play/pause state on rotation/home pressed

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instructions, container, false);

        isPhone = getResources().getBoolean(R.bool.isPhone);

        int orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        isPortrait = orientation == Surface.ROTATION_0 ||
                orientation == Surface.ROTATION_180;

        // show buttons unless it is on phone in landscape mode
        if (!isPhone || isPortrait) {
            mPrevious = view.findViewById(R.id.btn_previous);
            mPrevious.setOnClickListener(this);

            mNext = view.findViewById(R.id.btn_next);
            mNext.setOnClickListener(this);
        }

        ButterKnife.bind(this, view);

        Bundle args;

        if (savedInstanceState != null) {
            args = savedInstanceState;
            setData(args, true);
        }
        else {
            args = getActivity().getIntent().getExtras();
            setData(args, false);
        }

        setViews();

        return view;
    }

    private void setData(@NonNull Bundle args, boolean hasSavedState) {
        mRecipe = args.getParcelable(MainActivity.RECIPE_EXTRA);

        SharedPreferences prefs = getActivity()
                .getSharedPreferences(RecipeActivity.PREFS_KEY, Context.MODE_PRIVATE);
        mIndex = prefs.getInt(RecipeActivity.INDEX_KEY, 0);

        mPosition = args.getLong(POSITION_EXTRA);

        if (hasSavedState) {
            mPlayWhenReady = args.getBoolean(PLAYING_EXTRA);
        }
        else {
            mPlayWhenReady = true; // autoplay video if this is the first time the fragment is initialized
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MainActivity.RECIPE_EXTRA, mRecipe);

        SharedPreferences prefs = getActivity()
                .getSharedPreferences(RecipeActivity.PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(RecipeActivity.INDEX_KEY, mIndex);
        editor.apply();

        // save the video seek position
        if (mExoPlayer != null) {
            outState.putLong(POSITION_EXTRA, mExoPlayer.getCurrentPosition());
        }
        else {
            outState.putLong(POSITION_EXTRA, 0L);
        }

        outState.putBoolean(PLAYING_EXTRA, mPlayWhenReady);

        super.onSaveInstanceState(outState);
    }

    // https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // hide the navigation bar and status bar on phones while in landscape mode (full screen video)
        // https://developer.android.com/training/system-ui/status.html
        if (isPhone && !isPortrait) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initPlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
    }

    /** set the text for the instructions and if it is a phone, enable/disable buttons based
     * on index */
    private void setViews() {
        String instructionsText = mRecipe.getSteps().get(mIndex).getDescription();
        mInstructions.setText(instructionsText);

        // disable buttons if they are at first/last index
        if (!isPhone || isPortrait) {
            mPrevious.setEnabled(mIndex > 0);
            mNext.setEnabled(mIndex < mRecipe.getSteps().size() - 1);
        }

        // Hide ExoPlayer if there is no video to display (uri is empty)
        if (mRecipe.getVideoUri(mIndex).toString().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
            mThumbnailImage.setVisibility(View.VISIBLE);

            // load the thumbnail image, if no image available load the novideo placeholder image
            Uri thumbnailUri = mRecipe.getThumbnailUri(mIndex);
            Picasso.with(getContext())
                    .load(thumbnailUri)
                    .placeholder(R.drawable.novideo)
                    .error(R.drawable.novideo)
                    .into(mThumbnailImage);
        }
        else {
            mPlayerView.setVisibility(View.VISIBLE);
            mThumbnailImage.setVisibility(View.GONE);
        }
    }

    /** Release ExoPlayer */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /** Initialize ExoPlayer */
    // Logcat error - java.lang.IllegalStateException: Handler (com.google.android.exoplayer2.upstream.Loader$ReleaseTask)
    // https://github.com/google/ExoPlayer/issues/426 - safe to ignore
    private void initPlayer() {
        // get the video uri from the recipe object
        Uri uri = mRecipe.getVideoUri(mIndex);

        // if the uri is empty, no need to initialize the player now
        if (uri.toString().isEmpty()) {
            return;
        }

        initMediaSession();

        if (mExoPlayer == null) {
            DefaultRenderersFactory renderer = new DefaultRenderersFactory(getContext());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderer, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            // prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mPosition);
        }
    }

    private void initMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // MediaButtons's can't restart the player when app isn't visible
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new SessionCallback());

        mMediaSession.setActive(true);
    }

    /** Switch the video to proper step when navigating back/forward */
    private void switchVideo() {
        releasePlayer();

        mPosition = 0; // reset the seek position
        mPlayWhenReady = true;

        initPlayer();
    }

    /** Handle previous and next button clicks on mobile */
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btn_previous:
                if (mIndex > 0) {
                    mIndex--;
                    setViews();
                    switchVideo();
                }

                break;

            case R.id.btn_next:
                if (mIndex < mRecipe.getSteps().size() - 1) {
                    mIndex++;
                    setViews();
                    switchVideo();
                }

                break;

            default:
                throw new UnsupportedOperationException("Invalid view id");
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(),
                    1f);
        }
        else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(),
                    1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    /** Media Session callbacks */
    private class SessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    /** Broadcast receiver */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() { }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
