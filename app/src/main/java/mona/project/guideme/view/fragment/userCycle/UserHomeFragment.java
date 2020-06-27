package mona.project.guideme.view.fragment.userCycle;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.view.fragment.splashCycle.LoginFragment;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class UserHomeFragment extends BaseFragment {

    @BindView(R.id.edt_where_to_go)
    EditText edtWhereToGo;
    public String destination_txt;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    private void check() {
        destination_txt = edtWhereToGo.getText().toString();

        if (TextUtils.isEmpty(destination_txt)) {
            Toast.makeText(getActivity(), "Please enter your destination", Toast.LENGTH_SHORT).show();
            return;
        }

        TripsFragment tripsFragment = new TripsFragment();
        tripsFragment.destinationTxt = destination_txt;

        replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_container, tripsFragment);
    }

    @OnClick({R.id.user_logout, R.id.btn_seach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_logout:
                new AlertDialog.Builder(getContext())
                        .setTitle("Sign Out !!")
                        .setMessage("Are You Sure To Sign Out ?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            replaceFragment(getActivity().getSupportFragmentManager(), R.id.user_container, new LoginFragment());
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.btn_seach:
                check();
                break;
        }
    }

    @Override
    public void onBack() {
        getActivity().finishAffinity();
    }
}