package mona.project.guideme.view.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.view.activity.AdminStartActivity;
import mona.project.guideme.view.activity.UserStartActivity;
import mona.project.guideme.view.fragment.adminCycle.AdminHomeFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class SplashFragment extends BaseFragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getUid().equals("BpdzmZD1ICeOQHrxedGkt4oTYJG2")) {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        // go to the main activity
                        Intent i = new Intent(getActivity(), AdminStartActivity.class);
                        startActivity(i);
                    }
                };
                // Show splash screen for 3 seconds
                new Timer().schedule(task, 3000);
            } else {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user.getUid())) {
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    // go to the main activity
                                    Intent i = new Intent(getActivity(), UserStartActivity.class);
                                    startActivity(i);
                                    // kill current activity
                                }
                            };
                            // Show splash screen for 3 seconds
                            new Timer().schedule(task, 3000);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // go to the main activity
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.container, new SliderFragment());
                }
            };
            // Show splash screen for 3 seconds
            new Timer().schedule(task, 3000);
        }
        return view;
    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }
}