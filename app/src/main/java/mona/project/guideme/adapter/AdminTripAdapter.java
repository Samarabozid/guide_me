package mona.project.guideme.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import mona.project.guideme.BaseActivity;
import mona.project.guideme.R;
import mona.project.guideme.data.TripModel;
import mona.project.guideme.view.activity.AdminStartActivity;
import mona.project.guideme.view.fragment.adminCycle.TripDetailsFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;


public class AdminTripAdapter extends RecyclerView.Adapter<AdminTripAdapter.ViewHolder> {
    Context context;
    private List<TripModel> tripList = new ArrayList<>();
    private BaseActivity activity;

    public AdminTripAdapter(Activity activity, List<TripModel> tripList) {
        this.context = activity;
        this.activity = (BaseActivity) activity;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, null);
        ViewHolder articleRowHolder = new ViewHolder(view);
        return articleRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context)
                .load(tripList.get(position)
                .getImageurl())
                .into(holder.busOrDriverImage);
        holder.area.setText(tripList.get(position).getDestination());
        holder.assemblyPoint.setText(tripList.get(position).getAssemblyPoint());
        holder.tripDay.setText(tripList.get(position).getDay());
        holder.tripTime.setText(tripList.get(position).getTime());
        holder.callDriver.setOnClickListener(view -> {
            String mobile = tripList.get(position).getDriverPhone();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            activity.startActivity(intent);
        });
        holder.tripLayout.setOnClickListener(view -> {
            AdminStartActivity adminStartActivity = (AdminStartActivity) context;
            TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
            String tripId = tripList.get(position).getId();
            String busId = tripList.get(position).getBusId();
            String tripDestination = tripList.get(position).getDestination();
            String noSeats = tripList.get(position).getNoSeats();
            String availableSeats = tripList.get(position).getAvailableSeats();
            String noPassengers = tripList.get(position).getNoPassengers();
            String day = tripList.get(position).getDay();
            String tripAssemblyPoint = tripList.get(position).getAssemblyPoint();
            String tripImageUrl = tripList.get(position).getImageurl();
            String tripDestinationPoint = tripList.get(position).getDestinationPoint();
            String driverName = tripList.get(position).getDriverName();
            String driverPhone = tripList.get(position).getDriverPhone();

            Bundle bundle = new Bundle();
            bundle.putString("tripId",tripId);
            bundle.putString("busId",busId);
            bundle.putString("tripDestination",tripDestination);
            bundle.putString("tripNoSeats",noSeats);
            bundle.putString("availableSeats",availableSeats);
            bundle.putString("tripNoPassengers",noPassengers);
            bundle.putString("tripDay",day);
            bundle.putString("tripAssemblyPoint",tripAssemblyPoint);
            bundle.putString("tripImageUrl",tripImageUrl);
            bundle.putString("tripDestinationPoint",tripDestinationPoint);
            bundle.putString("driverName",driverName);
            bundle.putString("driverPhone",driverPhone);

            tripDetailsFragment.setArguments(bundle);

            replaceFragment(adminStartActivity.getSupportFragmentManager(),R.id.admin_container, tripDetailsFragment);
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void filterList(ArrayList<TripModel> filteredList) {
        tripList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView busOrDriverImage,callDriver;
        TextView area,assemblyPoint,tripDay,tripTime;
        RelativeLayout tripLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busOrDriverImage = itemView.findViewById(R.id.bus_or_driver_image);
            callDriver = itemView.findViewById(R.id.call);
            area = itemView.findViewById(R.id.area);
            assemblyPoint = itemView.findViewById(R.id.assembly_point);
            tripLayout = itemView.findViewById(R.id.trip_item);
            tripDay = itemView.findViewById(R.id.trip_day);
            tripTime = itemView.findViewById(R.id.trip_time);
        }
    }
}