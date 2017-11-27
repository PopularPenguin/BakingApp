package com.popularpenguin.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
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
import com.popularpenguin.bakingapp.Data.Recipe;
import com.popularpenguin.bakingapp.Data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/** The fragment holding the individual instructions for a step */
public class InstructionsFragment extends Fragment implements View.OnClickListener,
        Player.EventListener {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    @BindView(R.id.exo_view) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_instructions) TextView mInstructions;
    Button mPrevious;
    Button mNext;

    private Recipe mRecipe;
    private int mIndex;
    private String mInstructionsText;
    private boolean isPhone;
    private boolean isPortrait;

    // ExoPlayer members
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public static InstructionsFragment newInstance(@NonNull Bundle args) {
        InstructionsFragment fragment = new InstructionsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instructions, container, false);

        isPhone = getResources().getBoolean(R.bool.isPhone);

        int orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        isPortrait = orientation == Surface.ROTATION_0 ||
                orientation == Surface.ROTATION_180;

        if (isPhone && isPortrait) {
            // these views don't exist in the tablet layout
            mPrevious = view.findViewById(R.id.btn_previous);
            mPrevious.setOnClickListener(this);

            mNext = view.findViewById(R.id.btn_next);
            mNext.setOnClickListener(this);
        }

        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        if (args != null) {
            setData(args);
        }
        else {
            throw new RuntimeException("args is null");
        }

        return view;
    }

    /** Get the recipe step and pass the uri to initialize ExoPlayer */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable("recipe");
            mIndex = savedInstanceState.getInt("index");
        }

        Step step = mRecipe.getSteps().get(mIndex);
        Uri uri = getVideoUri(step);

        initMediaSession();
        initPlayer(uri);
    }

    public void setData(@NonNull Bundle args) {
        mRecipe = args.getParcelable(MainActivity.RECIPE_EXTRA);
        mIndex = args.getInt(RecipeActivity.INDEX_EXTRA);

        setViews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("recipe", mRecipe);
        outState.putInt("index", mIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releasePlayer();
        mMediaSession.setActive(false);
    }

    /** set the text for the instructions and if it is a phone, enable/disable buttons based
     * on index */
    private void setViews() {
        mInstructionsText = mRecipe.getSteps().get(mIndex).getDescription();
        mInstructions.setText(mInstructionsText);

        // disable buttons if they are at first/last index
        if (isPhone && isPortrait) {
            mPrevious.setEnabled(mIndex > 0);
            mNext.setEnabled(mIndex < mRecipe.getSteps().size() -1);
        }
    }

    /** Get the video's Uri from whatever JSON text field has it */
    private Uri getVideoUri(Step step) {
        if (!step.getVideoURL().isEmpty()) {
            return Uri.parse(step.getVideoURL());
        }
        else {
            // if the thumbnail is empty too just return an empty Uri
            return Uri.parse(step.getThumbnailURL());
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

    /** Release ExoPlayer */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /** Initialize ExoPlayer */
    private void initPlayer(Uri uri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            // prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /** Switch the video to proper step when navigating back/forward */
    private void switchVideo() {
        releasePlayer();

        Step step = mRecipe.getSteps().get(mIndex);
        Uri videoUri = getVideoUri(step);

        initPlayer(videoUri);
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
        //showNotification(mStateBuilder.build());
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
