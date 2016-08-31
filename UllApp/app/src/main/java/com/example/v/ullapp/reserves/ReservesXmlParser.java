package com.example.v.ullapp.reserves;

import com.example.v.ullapp.service.ServiceItem;
import com.example.v.ullapp.utils.XmlParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 13/08/2016.
 */
public class ReservesXmlParser extends XmlParser {
    @Override
    protected List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    @Override
    protected ReserveItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "object");
        int id = Integer.parseInt(parser.getAttributeValue(null, "pk"));
        String date = null;
        ArrayList<String> court = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getAttributeValue(null, "name");
            if (name.equals("pista")) {
                 court = readCourt(parser);
            } else if (name.equals("fecha")) {
                date = readDate(parser);
            }else {
                skip(parser);
            }
        }
        return new ReserveItem(id,court.get(1),court.get(0),date);
    }

    private ArrayList<String> readCourt(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<String> courtData = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "field");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("natural")) {
                courtData.add(readText(parser));
            }else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "field");
        return courtData;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "field");
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "field");
        return removeTime(date);
    }
    public String removeTime(String text){
        String replaced = text.replaceAll("T.*", "");
        return replaced;
    }
}
