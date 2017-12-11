package com.hdarby.saspodcastplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.hdarby.saspodcastplayer.R;
import com.hdarby.saspodcastplayer.data.PodcastFeedLoader;
import com.hdarby.saspodcastplayer.model.FeedItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hdarby on 11/16/2017.
 */

public class MainActivity extends AppCompatActivity implements PodcastFeedLoader.FeedLoadListener {

    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private ImageButton stopPlaybackButton;
    private PodcastFeedLoader mPodcastFeedLoader;
    private ArrayList<FeedItem> feedList;

    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedList = new ArrayList<>();

        if (savedInstanceState != null) {
            isPlaying = savedInstanceState.getBoolean("isPlaying", false);
        }
        setContentView(R.layout.activity_main);

        mPodcastFeedLoader = new PodcastFeedLoader(getApplicationContext(), this);

        mRecylerView = findViewById(R.id.recycler_view);
        mRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecylerView.setLayoutManager(mLayoutManager);

        stopPlaybackButton = findViewById(R.id.stop_podcast);
        stopPlaybackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                isPlaying = !isPlaying;
                updateStopButton();
            }
        });

        updateStopButton();


        mRecylerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecylerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                FeedItem feedItem = feedList.get(position);

                //Check for Google Play Music exist
                if (!isPackageInstalled("com.google.android.music", getPackageManager())) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
                    startActivity(LaunchIntent);
                } else {
                    Intent intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC);

                    startActivity(intent);
                    Uri myUri = Uri.parse(feedItem.getLink());
                    try {
                        if (!isPlaying) {
                            mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource(MainActivity.this, myUri);
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } else {
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                        isPlaying = !isPlaying;
                        updateStopButton();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isPlaying", isPlaying);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPodcastFeedLoader.loadFeedItems();
    }

    private void updateStopButton() {
        stopPlaybackButton.setVisibility(isPlaying ? View.VISIBLE : View.GONE);
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void setItemData(List<FeedItem> feedItems) {
        for (FeedItem feedItem : feedItems) {
            feedList.add(feedItem);
        }

        mAdapter = new FeedAdapter(feedList);
        mRecylerView.setAdapter(mAdapter);
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            // Noop
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // Noop
        }
    }
}




