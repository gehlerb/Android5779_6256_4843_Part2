package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Driver;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;

public class WaitingRideAdapter extends RecyclerView.Adapter<WaitingRideAdapter.ViewHolder> implements Filterable{

    private List<Ride> mRides;
    private List<Ride> orgiRides;
    private Filter rideFilterByDis;
    private Location driverLocation;

    // Pass in the contact array into the constructor
    public WaitingRideAdapter(List<Ride> rides, Location dl) {
        mRides = rides;
        orgiRides=rides;
        driverLocation=dl;
    }

    public void setDriverLocation(Location driverLocation) {
        this.driverLocation = driverLocation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View rideView = inflater.inflate(R.layout.item_ride, parent, false);

        ViewHolder viewHolder = new ViewHolder(rideView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Ride ride = mRides.get(position);

        TextView fromTextView = viewHolder.fromTextView;
        TextView toTextView = viewHolder.toTextView;
        TextView disTextView=viewHolder.disTextView;
        TextView disPickDestTextView=viewHolder.disPickDestTextView;

        Location pickup=ride.getPickupAddress().getmLatitudeAndLongitudeLocation().getLocation();
        Location dest=ride.getDestinationAddress().getmLatitudeAndLongitudeLocation().getLocation();
        double dis=pickup.distanceTo(driverLocation)/1000;
        disTextView.setText(new DecimalFormat("##.#").format(dis));
        dis=pickup.distanceTo(dest)/1000;
        disPickDestTextView.setText(new DecimalFormat("##.#").format(dis)+ " km");
        fromTextView.setText(ride.getPickupAddress().getAddress());
        toTextView.setText(ride.getDestinationAddress().getAddress());

    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void remove(int position){
        mRides.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fromTextView;
        public TextView toTextView;
        public TextView disTextView;
        public TextView disPickDestTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            toTextView = (TextView) itemView.findViewById(R.id.to_textview);
            fromTextView = (TextView) itemView.findViewById(R.id.from_textview);
            disTextView = (TextView) itemView.findViewById(R.id.dis_textview);
            disPickDestTextView = (TextView) itemView.findViewById(R.id.dis_pick_dest);
            itemView.setOnClickListener(new View.OnClickListener() {
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

    private class RideFilterByDis extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Ride> nRideList = new ArrayList<Ride>();
            int dis = Integer.parseInt(constraint.toString());

            for (Ride p : orgiRides) {
                if (filterByDis(driverLocation, p.getPickupAddress().getmLatitudeAndLongitudeLocation().getLocation(), dis))
                    nRideList.add(p);
            }

            results.values = nRideList;
            results.count = nRideList.size();

            return results;
        }

        private boolean filterByDis(Location driver, Location passenger, int dis) {
            if (driver.distanceTo(passenger) <= dis*1000) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0) {
                //notifyDataSetInvalidated();
                notifyDataSetChanged();
            } else {
                mRides = (List<Ride>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (rideFilterByDis == null)
            rideFilterByDis = new RideFilterByDis();

        return rideFilterByDis;
    }

}
