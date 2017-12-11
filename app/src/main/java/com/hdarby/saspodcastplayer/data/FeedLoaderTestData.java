package com.hdarby.saspodcastplayer.data;

import com.hdarby.saspodcastplayer.model.FeedItem;

import java.util.ArrayList;

/**
 * Created by hdarby on 12/8/2017.
 */

public class FeedLoaderTestData {

    public static ArrayList<FeedItem> loadTestData() {

        ArrayList<FeedItem> items = new ArrayList<>();

        items.add(new FeedItem("Title 1",
                "developers@android.com (Android Developers)",
                "Wed, 29 Nov 2017 16:14:59 PST",
                "https://storage.googleapis.com/androiddevelopers/android_developers_backstage/ADB%2084%20Instant%20Apps.mp3"));
        items.add(new FeedItem("Title 2",
                "developers@android.com (Android Developers)",
                "Wed, 15 Nov 2017 13:12:03 PST",
                "https://storage.googleapis.com/androiddevelopers/android_developers_backstage/ADB%2083%20ART%20Runtime.mp3"));
        items.add(new FeedItem("Title 3",
                "developers@android.com (Android Developers)",
                "Wed, 08 Nov 2017 14:33:04 PST",
                "https://storage.googleapis.com/androiddevelopers/android_developers_backstage/ADB%2082%20Tooling%20Around.mp3"));
        items.add(new FeedItem("Title 4",
                "developers@android.com (Android Developers)",
                "Mon, 06 Nov 2017 08:53:42 PST",
                "https://storage.googleapis.com/androiddevelopers/android_developers_backstage/ADB%2081%20Gradle%20Sync.mp3"));
        items.add(new FeedItem("Title 5",
                "developers@android.com (Android Developers)",
                "Tue, 31 Oct 2017 12:55:48 PDT",
                "https://storage.googleapis.com/androiddevelopers/android_developers_backstage/ADB%2080%20Crashlytics.mp3"));

        return items;
    }
}






