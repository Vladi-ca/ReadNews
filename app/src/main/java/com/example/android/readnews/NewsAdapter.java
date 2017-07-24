package com.example.android.readnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Vladi on 24.7.17.
 */

public class NewsAdapter extends ArrayAdapter<Article> {

    public NewsAdapter(Context context, List<Article> news) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        NewsAdapter.ViewHolder holder;

        if (listItemView != null) {

            holder = (ViewHolder) listItemView.getTag();
        } else {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_layout, parent, false);
            holder = new ViewHolder(listItemView);
            listItemView.setTag(holder);
        }

        Article currentArticle = getItem(position);

        // Find the TextView with view ID section_name
        if (currentArticle.getSection().isEmpty())
            holder.sectionTextView.setVisibility(View.GONE);
        else {
            holder.sectionTextView.setText(currentArticle.getSection());
            holder.sectionTextView.setVisibility(View.VISIBLE);
        }

        // Find the TextView with view ID title and hide it, if it is empty
        if (currentArticle.getTitle().isEmpty()) holder.titleTextView.setVisibility(View.GONE);
        else {
            holder.titleTextView.setText(currentArticle.getTitle());
            holder.titleTextView.setVisibility(View.VISIBLE);
        }

        return listItemView;
    }

    // Added dependency for ButterKnife InjectView
    static class ViewHolder {
        @InjectView(R.id.section)
        TextView sectionTextView;
        @InjectView(R.id.title)
        TextView titleTextView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
