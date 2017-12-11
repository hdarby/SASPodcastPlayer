package com.hdarby.saspodcastplayer.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by hdarby on 12/8/2017.
 */

//    item -
//        title - the title of the podcast
//        media:content url - the url of the podcast mp3 file

public class FeedItem {

    private static final String ns = null;

    private String title;
    private String author;
    private String pubDate;
    private String link;

    public FeedItem(String title, String author, String pubDate, String link) {
        this.title = title;
        this.author = author;
        this.pubDate = pubDate;
        this.link = link;
    }

    // Parses the contents of an item. If it encounters a title or media:content (link) tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    static FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String author = null;
        String datePublished = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("author")) {
                author = readAuthor(parser);
            } else if (name.equals("pubDate")) {
                datePublished = readPublishedDate(parser);
            } else if (name.equals("media:content")) {
                link = readLink(parser);
            } else {
                skip(parser);
            }
        }
        return new FeedItem(title, author, datePublished, link);
    }

    // Processes title tags in the feed.
    static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    static String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "author");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "author");
        return author;
    }

    static String readPublishedDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String publishedDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return publishedDate;
    }

    // Processes link tags in the feed.
    static String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "media:content");
        String url = parser.getAttributeValue(null, "url");
        parser.nextTag();
        return url;
    }

    static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishedDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }
}
