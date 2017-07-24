package com.example.android.readnews;

/**
 * Created by Vladi on 22.7.17.
 */

public class Article {
    private String mTitle;
    private String mSection;
    private String mUrl;

    public Article(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }
}
