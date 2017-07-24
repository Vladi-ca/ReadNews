package com.example.android.readnews;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int NEWS_LOADER_ID = 1;

    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?api-key=test&show-tags=contributor&from-date=2017-01-01";

    ProgressDialog progDailog;

    private NewsAdapter mAdapter;

    private TextView mEmptyStateTextView;

    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isConnected(Context context) {

        // Creating ConnectivityManager and it gets network information
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // network is active ak nie je null or is connected or connecting
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // Now we need to hook up the TextView as the empty view of the ListView.
        // We can use the ListView setEmptyView() method. We can also make the
        // empty state TextView be a global variable, so we can refer to it in
        // a later method. The TextView class was also automatically imported
        // into the java file, as soon as we used that class.
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        Context context = getApplicationContext();

        //Check for internet connection
        if (isConnected(context)) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            //Provide feedback about no internet connection
            Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
        }

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the chosen article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert String URL into a URI object
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        progDailog = new ProgressDialog(MainActivity.this);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        Log.v(LOG_TAG, uriBuilder.toString());
        // Create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> news) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.empty_view);

        // Clear the adapter of previous news data
        mAdapter.clear();

        progDailog.dismiss();

        // If there is a valid list of {@link Article, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}
