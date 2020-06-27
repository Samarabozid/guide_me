package mona.project.guideme.view.fragment.splashCycle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.view.activity.AdminStartActivity;
import mona.project.guideme.view.activity.UserRegisterActivity;
import mona.project.guideme.view.activity.UserStartActivity;
import mona.project.guideme.view.fragment.adminCycle.AdminHomeFragment;

import static android.content.Context.MODE_PRIVATE;
import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class LoginFragment extends BaseFragment {

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.ch_show_all_trips)
    CheckBox chRemmember;

    ProgressDialog progressDialog;

    String email_txt, password_txt;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SharedPreferences loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    Boolean saveLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_user_login, container, false);
        ButterKnife.bind(this, view);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            edtEmail.setText(loginPreferences.getString("username", ""));
            edtPassword.setText(loginPreferences.getString("password", ""));
            chRemmember.setChecked(true);
        }

        return view;
    }

    @OnClick({R.id.forgotpassword_tv, R.id.btn_login, R.id.sign_up_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgotpassword_tv:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = edtEmail.getText().toString();

                if (TextUtils.isEmpty(emailAddress)) {
                    Toast.makeText(getActivity(), "please enter your email firstly", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailAddress.equals("admin@admin.com")) {
                    Toast.makeText(getContext(), "you can't reset admin account password", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "password reset email has been sent to : " + emailAddress, Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
            case R.id.btn_login:
                check();
                break;
            case R.id.sign_up_tv:
                Intent i = new Intent(getActivity(), UserRegisterActivity.class);
                startActivity(i);
                //replaceFragment(getActivity().getSupportFragmentManager(), R.id.admin_container, new UserRegisterFragment());
                break;
        }
    }

    private void check() {
        email_txt = edtEmail.getText().toString();
        password_txt = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email_txt)) {
            Toast.makeText(getActivity(), "please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password_txt)) {
            Toast.makeText(getActivity(), "please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email_txt.equals("admin@admin.com") && password_txt.equals("admin1234")) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Admin Login");
            progressDialog.setMessage("Please Wait Until Admin Login ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            AdminLogin(email_txt, password_txt);

            loginPrefsEditor.putBoolean("savepassword", true);
            loginPrefsEditor.putString("pass", password_txt);
            loginPrefsEditor.apply();

            if (chRemmember.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", email_txt);
                loginPrefsEditor.putString("password", password_txt);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }
        } else {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("User Login");
            progressDialog.setMessage("Please Wait Until Login ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            UserLogin(email_txt, password_txt);

            loginPrefsEditor.putBoolean("savepassword", true);
            loginPrefsEditor.putString("pass", password_txt);
            loginPrefsEditor.apply();

            if (chRemmember.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", email_txt);
                loginPrefsEditor.putString("password", password_txt);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }
        }
    }

    private void UserLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getActivity(), UserStartActivity.class);
                            startActivity(i);
                            progressDialog.dismiss();
                        } else {
                            //String taskmessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "This email and password are not in database", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void AdminLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getUser().getUid().equals("BpdzmZD1ICeOQHrxedGkt4oTYJG2")) {
                                Intent i = new Intent(getActivity(), AdminStartActivity.class);
                                startActivity(i);
                                progressDialog.dismiss();
                            }
                        } else {
                            String taskmessage = task.getException().getMessage();
                            Toast.makeText(getContext(), taskmessage, Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onBack() {
        getActivity().finishAffinity();
    }
}