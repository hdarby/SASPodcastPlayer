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
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String PUBDATE = "pubDate";
    private static final String LINK = "media:content";
    private static final String URL_ATTRIBUTE = "url";

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

    // Parses the contents of an item. If it encounters a title, author, pubDate or media:content
    // (link) tag, hands them off to their respective "read" methods for processing. Otherwise,
    // skips the tag.

    static FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ITEM);
        String title = null;
        String author = null;
        String datePublished = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case TITLE:
                    title = readTitle(parser);
                    break;
                case AUTHOR:
                    author = readAuthor(parser);
                    break;
                case PUBDATE:
                    datePublished = readPublishedDate(parser);
                    break;
                case LINK:
                    link = readLink(parser);
                    break;
                default:
                    skip(parser);
            }
        }
        return new FeedItem(title, author, datePublished, link);
    }

    // Processes title tags in the feed.
    static private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TITLE);
        return title;
    }

    static private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, AUTHOR);
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, AUTHOR);
        return author;
    }

    static private String readPublishedDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, PUBDATE);
        String publishedDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, PUBDATE);
        return publishedDate;
    }

    // Processes link tags in the feed.
    static private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, LINK);
        String url = parser.getAttributeValue(null, URL_ATTRIBUTE);
        parser.nextTag();
        return url;
    }

    static private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
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
