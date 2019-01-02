package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.util.List;

public class HistoryRideAdapter  extends RecyclerView.Adapter<HistoryRideAdapter.ViewHolder> implements Filterable {

    private List<Ride> mRides;
    private List<Ride> orgiRides;
    private Filter rideFilterByDis;

    public HistoryRideAdapter(List<Ride> rides) {
        mRides = rides;
        orgiRides=rides;
    }
    @NonNull
    @Override
    public HistoryRideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rideView = inflater.inflate(R.layout.item_ride_history, parent, false);

        HistoryRideAdapter.ViewHolder viewHolder = new HistoryRideAdapter.ViewHolder(rideView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRideAdapter.ViewHolder holder, int position) {
        Ride ride = mRides.get(position);

        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(ride.getClientFirstName()+" "+ride.getClientLastName());
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    // Define listener member variable
    private HistoryRideAdapter.OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(HistoryRideAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button addToContacts;

        public ViewHolder(final View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textview);
            addToContacts = (Button) itemView.findViewById(R.id.add_to_contacts);

            addToContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
