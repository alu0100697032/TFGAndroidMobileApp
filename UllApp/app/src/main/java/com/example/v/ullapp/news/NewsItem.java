package com.example.v.ullapp.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 05/08/2016.
 */
public class NewsItem implements Parcelable{
    private String title, link,pubDate, description, content;

    public NewsItem() {

    }
    public NewsItem(String title, String pubDate, String link, String description, String content) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.description = description;
        this.content = content;
    }
    public String getTitle(){
        return title;
    }
    public String getPubDate(){
        return pubDate;
    }
    public String getLink(){
        return link;
    }
    public String getDescription(){
        return description;
    }
    public String getContent(){
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(pubDate);
        dest.writeString(link);
        dest.writeString(content);
        dest.writeString(description);
    }

    public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {

        @Override
        public NewsItem createFromParcel(Parcel source) {
            return new NewsItem(source);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }

    };

    private NewsItem(Parcel source) {
        this.title = source.readString();
        this.pubDate = source.readString();
        this.link = source.readString();
        this.content = source.readString();
        this.description = source.readString();
    }
}
