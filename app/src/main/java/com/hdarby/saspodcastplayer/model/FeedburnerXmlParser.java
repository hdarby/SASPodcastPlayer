package com.hdarby.saspodcastplayer.model;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.hdarby.saspodcastplayer.model.FeedItem.readItem;
import static com.hdarby.saspodcastplayer.model.FeedItem.skip;

/**
 * Created by hdarby on 12/8/2017.
 */

// Each podcast appears in the feed as an item tag containing nested tags
// I want the title and the media:content (url)

public class FeedburnerXmlParser {

    private static final String ns = null;
    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readChannel(parser);
        } finally {
            in.close();
        }
    }

    private List readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the channel tag
            if (name.equals(CHANNEL)) {
                // Outer level of the item data, parse inside the channel data

                return readItems(parser);
            } else {
                skip(parser);
            }
        }
        return null;
    }

    private List readItems(XmlPullParser parser) throws XmlPullParserException, IOException {
        List items = new ArrayList();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals(ITEM)) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

}
