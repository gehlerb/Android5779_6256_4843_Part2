package com.example.baruch.android5779_6256_4843_part2.controller;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baruch.android5779_6256_4843_part2.R;
import com.example.baruch.android5779_6256_4843_part2.model.entities.CurrentLocation;
import com.example.baruch.android5779_6256_4843_part2.model.entities.Ride;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;

public class WaitingRideAdapter extends RecyclerView.Adapter<WaitingRideAdapter.ViewHolder> implements Filterable{

    private List<Ride> mRides;
    private List<Ride> orgiRides;
    private Filter rideFilterByDis;
    private OnItemClickListener listener;
    private CurrentLocation mCurrentLocation;

    public WaitingRideAdapter(List<Ride> rides) {
        mRides = rides;
        orgiRides=rides;
        mCurrentLocation=new CurrentLocation();
        }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void setIsEmptyListListener(WaitingRideAdapter.isEmptyListListener isEmptyListListener) {
        this.isEmptyListListener = isEmptyListListener;
    }

    private isEmptyListListener isEmptyListListener;

    public interface isEmptyListListener{
        void onEmptyList();
        void onNonEmptyList();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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

        Location pickup=ride.getPickupAddress().getmLatitudeAndLongitudeLocation().location();
        Location dest=ride.getDestinationAddress().getmLatitudeAndLongitudeLocation().location();
        double dis=pickup.distanceTo(CurrentLocation.getCurrentLocation().getmLatitudeAndLongitudeLocation().location())/1000;
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



    public interface OnItemClickListener {
        void onItemClick(View itemView, Ride ride);
    }

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
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, mRides.get(position));
                        }
                    }
                }
            });
        }
    }

    private class RideFilterByDis extends Filter {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Ride> nRideList = new ArrayList<Ride>();
            int dis = Integer.parseInt(constraint.toString());

            for (Ride p : orgiRides) {
                if (filterByDis(CurrentLocation.getCurrentLocation().getmLatitudeAndLongitudeLocation().location(), p.getPickupAddress().getmLatitudeAndLongitudeLocation().location(), dis))
                    nRideList.add(p);
            }

            nRideList.sort(new Comparator<Ride>() {
                @Override
                public int compare(Ride o1, Ride o2) {
                    Location pickup1=o1.getPickupAddress().getmLatitudeAndLongitudeLocation().location();
                    Location pickup2=o2.getPickupAddress().getmLatitudeAndLongitudeLocation().location();
                    return (int)(pickup1.distanceTo(CurrentLocation.getCurrentLocation().getmLatitudeAndLongitudeLocation().location())-pickup2.distanceTo(CurrentLocation.getCurrentLocation().getmLatitudeAndLongitudeLocation().location()));
                }
            });

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
                mRides = (List<Ride>) results.values;
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                if(results.count==0){
                    isEmptyListListener.onEmptyList();
                }
                else {
                    isEmptyListListener.onNonEmptyList();
                }
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                notifyDataSetChanged();
        }

    }

    @Override
    public Filter getFilter() {
        if (rideFilterByDis == null)
            rideFilterByDis = new RideFilterByDis();

        return rideFilterByDis;
    }

}
