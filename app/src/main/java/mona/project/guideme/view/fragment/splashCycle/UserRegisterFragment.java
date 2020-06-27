package mona.project.guideme.view.fragment.splashCycle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import mona.project.guideme.BaseFragment;
import mona.project.guideme.R;
import mona.project.guideme.data.UserModel;
import mona.project.guideme.view.activity.UserStartActivity;

public class UserRegisterFragment extends BaseFragment {

    @BindView(R.id.profile)
    CircleImageView profile;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ProgressDialog progressDialog;
    Uri photoPath;
    private String gender, email, password, name, phone, confirm_password, imageurl;

    public UserRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_user_register, container, false);
        ButterKnife.bind(this, view);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        return view;
    }

    @OnClick({R.id.profile, R.id.male_rb, R.id.female_rb, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                openGallery();
                break;
            case R.id.male_rb:
                gender = "male";
                break;
            case R.id.female_rb:
                gender = "female";
                break;
            case R.id.btn_register:
                check();
                break;
        }
    }

    private void check() {
        email = edtEmail.getText().toString();
        name = edtName.getText().toString();
        password = edtPassword.getText().toString();
        confirm_password = edtConfirmPassword.getText().toString();
        phone = edtPhone.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "please enter  name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getContext(), "password is too short", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirm_password.equals(password)) {
            Toast.makeText(getContext(), "password is not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(getContext(), "please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "please enter number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoPath == null) {
            Toast.makeText(getContext(), "please add your picture", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait Until Creating Account ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        createUser(name, email, gender, imageurl, phone);
    }


    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .start(getActivity(), UserRegisterFragment.this);

    }

    private void createUser(final String name, final String email, final String gender, final String imageurl, final String mobile) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadImage(name, email, mobile, gender, imageurl);
                        } else {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void uploadImage(final String name, final String email, final String gender, final String id, final String mobile) {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri = task.getResult();

                imageurl = downloadUri.toString();
                addUser(name, email, gender, imageurl, phone);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void addUser(String name, String email, String gender, String imageurl, String mobile) {
        UserModel userModel = new UserModel(getUID(), name, email, mobile, gender, imageurl);
        databaseReference.child("Users").child(getUID()).setValue(userModel);

        Intent intent = new Intent(getContext(), UserStartActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    photoPath = result.getUri();

                    Glide.with(getActivity())
                            .load(photoPath)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(profile);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private String getUID() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
