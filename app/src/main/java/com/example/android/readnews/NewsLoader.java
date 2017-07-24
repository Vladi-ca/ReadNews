package com.example.android.readnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Vladi on 22.7.17.
 */

public class NewsLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST, onStarLoading() called ...");
        forceLoad();
    }

    // Background thread
    @Override
    public List<Article> loadInBackground() {

        Log.i(LOG_TAG, "TEST: loadInBackground() called ...");

        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Article> news = QueryUtils.fetchNewsData(mUrl);
        return news;
    }

}
