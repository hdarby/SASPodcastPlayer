package com.hdarby.saspodcastplayer.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hdarby.saspodcastplayer.R;
import com.hdarby.saspodcastplayer.model.FeedItem;
import com.hdarby.saspodcastplayer.model.FeedburnerXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by hdarby on 12/9/2017.
 */

public class PodcastFeedLoader {
    public static final String URLString = "http://feeds.feedburner.com/blogspot/AndroidDevelopersBackstage?format=xml";
    public static final String DOWNLOAD_XML_TASK = "DownloadXmlTask";

    Context mContext;
    FeedLoadListener mFeedLoadListener;

    public PodcastFeedLoader(Context context, FeedLoadListener feedLoadListener) {
        mContext = context;
        mFeedLoadListener = feedLoadListener;
    }

    public void loadFeedItems() {
        new DownloadXmlTask().execute(URLString);
    }

    public interface FeedLoadListener {
        void setItemData(List<FeedItem> items);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                Log.e(DOWNLOAD_XML_TASK, mContext.getResources().getString(R.string.connection_error));
                return null;
            } catch (XmlPullParserException e) {
                Log.e(DOWNLOAD_XML_TASK, mContext.getResources().getString(R.string.xml_error));
                return null;
            }
        }

        @Override
        protected void onPostExecute(List items) {
            mFeedLoadListener.setItemData(items);
            Log.d(DOWNLOAD_XML_TASK, "Number of items downloaded:" + items.size());
        }

        private List loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            FeedburnerXmlParser feedburnerXmlParser = new FeedburnerXmlParser();
            List items;

            try {
                stream = downloadUrl(urlString);
                items = feedburnerXmlParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return items;
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}
