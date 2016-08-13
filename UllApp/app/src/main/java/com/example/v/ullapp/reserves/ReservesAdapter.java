package com.example.v.ullapp.reserves;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v.ullapp.R;
import com.example.v.ullapp.service.ServiceItem;

import java.util.List;

/**
 * Created by Usuario on 13/08/2016.
 */
public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {
    private List<ReserveItem> courtList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView courtType, courtName, date;
        public ViewHolder(View view) {
            super(view);
            courtType = (TextView) view.findViewById(R.id.type);
            courtName = (TextView) view.findViewById(R.id.name);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
    //Constructor
    public ReservesAdapter(List<ReserveItem> myDataset) {
        this.courtList = myDataset;
    }
    @Override
    public ReservesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserve_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReservesAdapter.ViewHolder holder, int position) {
        ReserveItem n = courtList.get(position);
        holder.courtType.setText(n.getCourtType());
        holder.courtName.setText(n.getCourtName());
        holder.date.setText(n.getDate());
    }

    @Override
    public int getItemCount() {
        return courtList.size();
    }
}
