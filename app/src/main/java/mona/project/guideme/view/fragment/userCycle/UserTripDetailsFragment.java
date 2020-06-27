package mona.project.guideme.view.fragment.userCycle;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.data.ReservationModel;

import static android.content.Context.WINDOW_SERVICE;

public class UserTripDetailsFragment extends BaseFragment {

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
    @BindView(R.id.add_trip_fragment_tv_no_passengers)
    TextView addTripFragmentTvNoPassengers;
    @BindView(R.id.add_trip_fragment_tv_trip_day)
    TextView addTripFragmentTvTripDay;
    @BindView(R.id.add_trip_fragment_tv_destination)
    TextView addTripFragmentTvDestination;
    @BindView(R.id.add_trip_fragment_tv_assembly_point)
    TextView addTripFragmentTvAssemblyPoint;
    @BindView(R.id.add_trip_fragment_tv_destination_point)
    TextView addTripFragmentTvDestinationPoint;
    @BindView(R.id.no_seats)
    TextView noSeats;
    @BindView(R.id.add_trip_fragment_tv_no_available_seats)
    TextView addTripFragmentTvNoAvailableSeats;
    @BindView(R.id.minus)
    Button minus;
    @BindView(R.id.plus)
    Button plus;
    @BindView(R.id.btn_reserve_trip)
    Button btnReserveTrip;
    @BindView(R.id.add_trip_fragment_tv_trip_time)
    TextView addTripFragmentTvTripTime;

    private String tripIdstring;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String tripDestination;
    private int numberOfSeats = 1;
    private String tripNoSeats;
    private String availableSeats;

    ProgressDialog progressDialog;

    private ImageView qrImage;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private UserTripDetailsFragment activity;

    public UserTripDetailsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_trip_details, container, false);
        ButterKnife.bind(this, view);

        qrImage = view.findViewById(R.id.qr_image);
        activity = this;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle bundle = getArguments();
        tripIdstring = bundle.getString("tripId");
        //int tripId = Integer.parseInt(tripIdstring);
        String busId = bundle.getString("busId");
        tripDestination = bundle.getString("tripDestination");
        String tripAssemblyPoint = bundle.getString("tripAssemblyPoint");
        tripNoSeats = bundle.getString("tripNoSeats");
        availableSeats = bundle.getString("availableSeats");
        String tripNoPassengers = bundle.getString("tripNoPassengers");
        String tripDay = bundle.getString("tripDay");
        String tripTime = bundle.getString("tripTime");
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
        addTripFragmentTvTripTime.setText(tripTime);

        Glide.with(this).load(tripImageUrltripImageUrl).into(tripDetailsFragmentBusIv);

        databaseReference.child("AllTrips").child(tripIdstring).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (Integer.parseInt(availableSeats) == 0) {
                        minus.setEnabled(false);
                        plus.setEnabled(false);
                        btnReserveTrip.setEnabled(false);
                        Toast.makeText(baseActivity, "No Available Seats To Reserve", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        databaseReference.child("Reservation").child(tripIdstring).child(getuId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    generateQRcode1(getuId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
    }


    @OnClick({R.id.minus, R.id.plus, R.id.btn_reserve_trip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.minus:
                if (numberOfSeats > 1) {
                    numberOfSeats--;
                    noSeats.setText(String.valueOf(numberOfSeats));
                    //numberOfSeats = 1;
                    //noSeats.setText(String.valueOf(numberOfSeats));
                } else {
                    Toast.makeText(baseActivity, "You can not reserve zero seat", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.plus:
                numberOfSeats++;
                noSeats.setText(String.valueOf(numberOfSeats));

                if (numberOfSeats > Integer.parseInt(availableSeats)) {
                    numberOfSeats = Integer.parseInt(availableSeats);
                    noSeats.setText(String.valueOf(availableSeats));
                    Toast.makeText(baseActivity, "You can not reserve more than " + availableSeats + " seats", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.btn_reserve_trip:

                if (Integer.parseInt(availableSeats) == 0) {
                    Toast.makeText(baseActivity, "No available seats to reserve", Toast.LENGTH_SHORT).show();
                }

                int i = Integer.parseInt(availableSeats) - numberOfSeats;
                availableSeats = String.valueOf(i);

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please Wait Until Submit Your Reservation ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                addReservation(tripIdstring, getuId(), Integer.parseInt(tripNoSeats), numberOfSeats, Integer.parseInt(availableSeats));
                break;
        }
    }

    private void addReservation(String tripID, String userID, int totalSeats, int reservedSeats, int availableSeats) {
        ReservationModel reservationModel = new ReservationModel(tripID, getuId(), totalSeats, reservedSeats, availableSeats);
        databaseReference.child("Reservation").child(tripIdstring).child(getuId()).setValue(reservationModel);
        databaseReference.child("AllTrips").child(tripIdstring).child("availableSeats").setValue(String.valueOf(availableSeats));
        databaseReference.child("CategorisedTrips").child(tripDestination).child(tripIdstring).child("availableSeats").setValue(String.valueOf(availableSeats));

        //TripsFragment tripsFragment = new TripsFragment();
        //tripsFragment.destinationTxt = tripDestination;
        //replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_container, tripsFragment);
        generateQRcode(getuId());
    }

    private void generateQRcode(String userID) {
        if (getuId().length() > 0) {
            WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    getuId(), null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            qrgEncoder.setColorBlack(Color.WHITE);
            qrgEncoder.setColorWhite(Color.BLACK);
            progressDialog.dismiss();
            try {
                bitmap = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generateQRcode1(String userID) {
        if (getuId().length() > 0) {
            WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    getuId(), null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            qrgEncoder.setColorBlack(Color.WHITE);
            qrgEncoder.setColorWhite(Color.BLACK);
            //progressDialog.dismiss();
            try {
                bitmap = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getuId() {
        return FirebaseAuth.getInstance().getUid();
    }
}
