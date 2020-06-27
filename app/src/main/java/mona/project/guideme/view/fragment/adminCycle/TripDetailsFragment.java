package mona.project.guideme.view.fragment.adminCycle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class TripDetailsFragment extends BaseFragment {

    @BindView(R.id.trip_details_fragment_bus_iv)
    CircleImageView tripDetailsFragmentBusIv;
    @BindView(R.id.add_trip_fragment_tv_driver_name)
    TextView addTripFragmentTvDriverName;
    @BindView(R.id.add_trip_fragment_tv_phone)
    TextView addTripFragmentTvPhone;
    @BindView(R.id.add_trip_fragment_tv_bus_id)
    TextView addTripFragmentTvBusId;
    @BindView(R.id.add_trip_fragment_tv_no_seats)
    TextView addTripFragmentTvNoSeats;
    @BindView(R.id.add_trip_fragment_tv_destination)
    TextView addTripFragmentTvDestination;
    @BindView(R.id.add_trip_fragment_tv_assembly_point)
    TextView addTripFragmentTvAssemblyPoint;
    @BindView(R.id.add_trip_fragment_tv_destination_point)
    TextView addTripFragmentTvDestinationPoint;
    @BindView(R.id.add_trip_fragment_tv_no_passengers)
    TextView addTripFragmentTvNoPassengers;
    @BindView(R.id.add_trip_fragment_tv_trip_day)
    TextView addTripFragmentTvTripDay;
    @BindView(R.id.add_trip_fragment_tv_no_available_seats)
    TextView addTripFragmentTvNoAvailableSeats;
    private String tripIdstring;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String tripDestination;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            intiFragment();
        } catch (Exception e) {
            Toast.makeText(getContext(), "samar " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        ButterKnife.bind(this, view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle bundle = getArguments();
        tripIdstring = bundle.getString("tripId");
        //int tripId = Integer.parseInt(tripIdstring);
        String busId = bundle.getString("busId");
        tripDestination = bundle.getString("tripDestination");
        String tripAssemblyPoint = bundle.getString("tripAssemblyPoint");
        String tripNoSeats = bundle.getString("tripNoSeats");
        String availableSeats = bundle.getString("availableSeats");
        String tripNoPassengers = bundle.getString("tripNoPassengers");
        String tripDay = bundle.getString("tripDay");
        String tripImageUrltripImageUrl = bundle.getString("tripImageUrl");
        String tripDestinationPoint = bundle.getString("tripDestinationPoint");
        String driverName = bundle.getString("driverName");
        String driverPhone = bundle.getString("driverPhone");

        addTripFragmentTvDriverName.setText(driverName);
        addTripFragmentTvBusId.setText(busId);
        addTripFragmentTvDestination.setText(tripDestination);
        addTripFragmentTvAssemblyPoint.setText(tripAssemblyPoint);
        addTripFragmentTvDestinationPoint.setText(tripDestinationPoint);
        addTripFragmentTvPhone.setText(driverPhone);
        addTripFragmentTvNoSeats.setText(tripNoSeats);
        addTripFragmentTvNoAvailableSeats.setText(availableSeats);
        addTripFragmentTvNoPassengers.setText(tripNoPassengers);
        addTripFragmentTvTripDay.setText(tripDay);

        Glide.with(this).load(tripImageUrltripImageUrl).into(tripDetailsFragmentBusIv);

        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.btn_delete_trip)
    public void onViewClicked() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete trip")
                .setMessage("Are you want to delete this trip?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("AllTrips").child(tripIdstring).removeValue();
                        databaseReference.child("CategorisedTrips").child(tripDestination).child(tripIdstring).removeValue();
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.admin_container, new AdminHomeFragment());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}