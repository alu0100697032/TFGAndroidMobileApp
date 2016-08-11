package com.example.v.ullapp.service;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 11/08/2016.
 */
public class ServiceXmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List courts = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "django-objects");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("object")) {
                courts.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return courts;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private ServiceItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "object");
        int id = Integer.parseInt(parser.getAttributeValue(null, "pk"));
        String courtName = null;
        String courtType = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getAttributeValue(null, "name");
            if (name.equals("nombre")) {
                courtName = readCourtName(parser);
            } else if (name.equals("tipoPista")) {
                courtType = readCourtType(parser);
            }else {
                skip(parser);
            }
        }
        return new ServiceItem(id,courtType,courtName);
    }

    // Processes title tags in the feed.
    private String readCourtName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "field");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "field");
        return name;
    }

    // Processes link tags in the feed.
    private String readCourtType(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "field");
        String type = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "field");
        return parseType(type);
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    public String parseType(String type){
        String parsed = "";
        switch (type){
            case "BAL":
                parsed = "Baloncesto";
                break;
            case "FUT":
                parsed = "Futbol";
                break;
            case "PAD":
                parsed = "Padel";
                break;
            case "TEN":
                parsed = "Tenis";
                break;
            case "ATL":
                parsed = "Atletismo";
                break;
            default:
                parsed = "Desconocido";
                break;
        }
        return parsed;
    }
}
