package mona.project.guideme.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import mona.project.guideme.BaseActivity;
import mona.project.guideme.R;
import mona.project.guideme.data.UserModel;

public class UserRegisterActivity extends BaseActivity {

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
    @BindView(R.id.profile)
    CircleImageView profile;
    private String gender, email, password, name, phone, confirm_password, imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

    }

    @Override
    public void onBackPressed() {
        super.superBackPressed();
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
            Toast.makeText(getApplicationContext(), "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "please enter  name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "password is too short", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirm_password.equals(password)) {
            Toast.makeText(getApplicationContext(), "password is not matching", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(getApplicationContext(), "please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "please enter number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoPath == null) {
            Toast.makeText(getApplicationContext(), "please add your picture", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(UserRegisterActivity.this);
        progressDialog.setMessage("Please Wait Until Creating Account ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        createUser(name, email, phone, gender, imageurl);
    }


    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .start(UserRegisterActivity.this);

    }

    private void createUser(String name, String email ,String phone, String gender, String imageurl) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uploadImage(name,email,phone,gender);
                    } else {
                        String error_message = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void uploadImage( String name, String email ,String phone, String gender) {
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
                addUser(name,email, phone,gender,imageurl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    private void addUser( String name, String email ,String mobile, String gender, String imageurl) {
        UserModel userModel = new UserModel(getUID(), name, email, mobile, gender, imageurl);
        databaseReference.child("Users").child(getUID()).setValue(userModel);
        Intent intent = new Intent(getApplicationContext(), UserStartActivity.class);
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

                    Glide.with(getApplicationContext())
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
}