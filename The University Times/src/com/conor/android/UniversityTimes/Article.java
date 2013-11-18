package com.conor.android.UniversityTimes;

public class Article {
    //member fields
    private int id;
    private String heading;
    private String body;
    private String url;
    private String tag;
    private String imageurl;

    public Article() {
    }

    public Article(int id, String heading, String body, String imageurl) {
        this.id = id;
        this.heading = heading;
        this.body = body;
        this.imageurl = imageurl;
    }

    public String getHeading() {
        return heading;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}