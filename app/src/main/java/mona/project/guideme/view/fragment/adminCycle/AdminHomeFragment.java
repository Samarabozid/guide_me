package mona.project.guideme.view.fragment.adminCycle;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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
import mona.project.guideme.adapter.AdminTripAdapter;
import mona.project.guideme.data.TripModel;
import mona.project.guideme.view.fragment.splashCycle.LoginFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class AdminHomeFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @BindView(R.id.rotateloading)
    RotateLoading rotateloading;
    private AdminTripAdapter tripAdapter;
    private List<TripModel> tripModelArrayList = new ArrayList<>();

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        rotateloading.start();
        getData();

        return view;
    }

    private void getData() {
        databaseReference.child("AllTrips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tripModelArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TripModel tripModel = snapshot.getValue(TripModel.class);
                    tripModelArrayList.add(tripModel);
                }

                tripAdapter = new AdminTripAdapter(getActivity(), tripModelArrayList);
                recyclerView.setAdapter(tripAdapter);
                rotateloading.stop();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.admin_logout, R.id.add_trip_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.admin_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle("Sign Out !!")
                        .setMessage("Are You Sure To Sign Out ?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            replaceFragment(getActivity().getSupportFragmentManager(), R.id.admin_container, new LoginFragment());
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.add_trip_fab:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.admin_container, new AddTripFragment());
                break;
        }
    }

    @Override
    public void onBack() {
        getActivity().finishAffinity();
    }
}