package com.example.v.ullapp.news;

/**
 * Created by Usuario on 05/08/2016.
 */
public class New {
    private String title, link,pubDate, description, content;

    public New() {

    }
    public New(String title, String pubDate, String link, String description, String content) {
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
}
