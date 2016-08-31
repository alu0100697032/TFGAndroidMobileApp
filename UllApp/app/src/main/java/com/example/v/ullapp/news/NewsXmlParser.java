package com.example.v.ullapp.news;

import android.util.Xml;

import com.example.v.ullapp.utils.XmlParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 05/08/2016.
 */
public class NewsXmlParser extends XmlParser{
    @Override
    protected List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List news = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("channel")) {
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    if(name.equals("item"))
                        news.add(readEntry(parser));
                    else
                        skip(parser);
                }
            } else {
                skip(parser);
            }
        }
        return news;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    protected NewsItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String pubDate = null;
        String link = null;
        String description = null;
        String content = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            }  else if (name.equals("description")) {
                description = readDescription(parser);
            }else if (name.equals("content:encoded")) {
                content = readContent(parser);
            }else {
                skip(parser);
            }
        }
        return new NewsItem(title, pubDate, link, description, content);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    // Processes pubDate tags in the feed.
    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return removeTime(pubDate);
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    // Processes content tags in the feed.
    private String readContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "content:encoded");
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "content:encoded");
        return removeLinks(content);
    }

    public String removeLinks(String text){
        String replaced = text.replaceAll("<a.*?>|</a>", "");
        return replaced;
    }

    public String removeTime(String text){
        String replaced = text.replaceAll("\\d\\d:.*", "");
        return replaced;
    }
}
