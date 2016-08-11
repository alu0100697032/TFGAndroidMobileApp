package com.example.v.ullapp.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v.ullapp.R;

import java.util.List;

/**
 * Created by Usuario on 06/08/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsItem> newsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, description, pubDate;
        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            pubDate = (TextView) view.findViewById(R.id.pubDate);
        }
    }
    //Constructor
    public NewsAdapter(List<NewsItem> myDataset) {
        this.newsList = myDataset;
    }
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        NewsItem n = newsList.get(position);
        holder.title.setText(n.getTitle());
        holder.description.setText(n.getDescription());
        holder.pubDate.setText(n.getPubDate());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
