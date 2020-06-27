package mona.project.guideme.view.fragment.userCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.adapter.UserTripAdapter;
import mona.project.guideme.data.TripModel;

public class TripsFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @BindView(R.id.rotateloading)
    RotateLoading rotateloading;
    @BindView(R.id.ch_show_all_trips)
    CheckBox chShowAllTrips;
    private UserTripAdapter tripAdapter;
    private List<TripModel> tripModelArrayList = new ArrayList<>();
    public String destinationTxt;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_trips, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        rotateloading.start();

        getCategorisedTrips();

        return view;
    }

    private void getCategorisedTrips() {
        databaseReference.child("CategorisedTrips").child(destinationTxt).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tripModelArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TripModel tripModel = snapshot.getValue(TripModel.class);
                    tripModelArrayList.add(tripModel);
                }

                tripAdapter = new UserTripAdapter(getActivity(), tripModelArrayList);
                recyclerView.setAdapter(tripAdapter);
                rotateloading.stop();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.ch_show_all_trips)
    public void onViewClicked() {
        if (chShowAllTrips.isChecked()) {
            rotateloading.stop();
            getAllTrips();
        }else {
            rotateloading.stop();
            getCategorisedTrips();
        }
    }

    private void getAllTrips() {
        databaseReference.child("AllTrips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tripModelArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TripModel tripModel = snapshot.getValue(TripModel.class);
                    tripModelArrayList.add(tripModel);
                }

                tripAdapter = new UserTripAdapter(getActivity(), tripModelArrayList);
                recyclerView.setAdapter(tripAdapter);
                rotateloading.stop();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}