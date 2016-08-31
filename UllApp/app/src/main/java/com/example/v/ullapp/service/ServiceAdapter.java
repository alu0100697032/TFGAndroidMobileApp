package com.example.v.ullapp.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v.ullapp.R;
import com.example.v.ullapp.news.NewsAdapter;
import com.example.v.ullapp.news.NewsItem;

import java.util.List;

/**
 * Created by Usuario on 11/08/2016.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<ServiceItem> courtList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView courtType, courtName;
        public ViewHolder(View view) {
            super(view);
            courtType = (TextView) view.findViewById(R.id.type);
            courtName = (TextView) view.findViewById(R.id.name);
        }
    }
    //Constructor
    public ServiceAdapter(List<ServiceItem> myDataset) {
        this.courtList = myDataset;
    }
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ServiceAdapter.ViewHolder holder, int position) {
        ServiceItem n = courtList.get(position);
        holder.courtType.setText("Tipo: " + n.getCourtType());
        holder.courtName.setText("Nombre: " + n.getCourtName());
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }
}
