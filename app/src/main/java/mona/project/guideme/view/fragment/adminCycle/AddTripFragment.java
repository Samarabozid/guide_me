package mona.project.guideme.view.fragment.adminCycle;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import mona.project.guideme.data.TripModel;

import static mona.project.guideme.helper.HelperMethod.replaceFragment;

public class AddTripFragment extends BaseFragment {

    @BindView(R.id.add_trip_fragment_bus_iv)
    CircleImageView addTripFragmentBusIv;
    @BindView(R.id.add_trip_fragment_edt_driver_name)
    EditText addTripFragmentEdtDriverName;
    @BindView(R.id.add_trip_fragment_edt_phone)
    EditText addTripFragmentEdtPhone;
    @BindView(R.id.add_trip_fragment_edt_bus_id)
    EditText addTripFragmentEdtBusId;
    @BindView(R.id.add_trip_fragment_edt_no_seats)
    EditText addTripFragmentEdtNoSeats;
    @BindView(R.id.add_trip_fragment_edt_assembly_point)
    EditText addTripFragmentEdtAssemblyPoint;
    @BindView(R.id.add_trip_fragment_edt_destination_point)
    EditText addTripFragmentEdtDestinationPoint;
    @BindView(R.id.add_trip_fragment_edt_no_passengers)
    EditText addTripFragmentEdtNoPassengers;
    @BindView(R.id.add_trip_fragment_spinner_trip_day)
    Spinner spinnerTripDay;
    @BindView(R.id.add_trip_fragment_ed_destination)
    EditText addTripFragmentEdDestination;
    @BindView(R.id.add_trip_fragment_edt_available_seats)
    EditText addTripFragmentEdtAvailableSeats;
    @BindView(R.id.btn_add_trip)
    Button btnAddTrip;
    @BindView(R.id.add_trip_fragment_ed_time)
    EditText addTripFragmentEdTime;
    private String driverName;
    private String busId;
    private String noSeats;
    private String destination;
    private String assemblyPoint;
    private String destinationPoint;
    private String driverPhone;
    private Uri photoPath;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ProgressDialog progressDialog;
    private String id;
    private String imageurl;
    private String noPassengers;
    private String trip_day_txt;
    private String availableSeats;
    private String time;

    public AddTripFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intiFragment();
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        ButterKnife.bind(this, view);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.days, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTripDay.setAdapter(adapter1);

        spinnerTripDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trip_day_txt = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.days, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTripDay.setAdapter(adapter2);

        spinnerTripDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trip_day_txt = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @OnClick({R.id.add_trip_fragment_bus_iv, R.id.btn_add_trip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_trip_fragment_bus_iv:
                openGallery();
                break;
            case R.id.btn_add_trip:
                check();
                break;
        }
    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setAspectRatio(1, 1)
                .start(getActivity(), AddTripFragment.this);

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
                            .into(addTripFragmentBusIv);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(String driverName, String driverPhone, String busId, String noSeats, String availableSeats, String noPassengers, String trip_day_txt,String trip_time_txt, String destination, String assemblyPoint, String destinationPoint) {
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
                createTrip(driverName, driverPhone, busId, noSeats, availableSeats, noPassengers, trip_day_txt,time, destination, assemblyPoint, destinationPoint, imageurl);
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

    private void check() {
        driverName = addTripFragmentEdtDriverName.getText().toString();
        driverPhone = addTripFragmentEdtPhone.getText().toString();
        busId = addTripFragmentEdtBusId.getText().toString();
        noSeats = addTripFragmentEdtNoSeats.getText().toString();
        availableSeats = addTripFragmentEdtAvailableSeats.getText().toString();
        destination = addTripFragmentEdDestination.getText().toString();
        time = addTripFragmentEdTime.getText().toString();
        noPassengers = addTripFragmentEdtNoPassengers.getText().toString();
        assemblyPoint = addTripFragmentEdtAssemblyPoint.getText().toString();
        destinationPoint = addTripFragmentEdtDestinationPoint.getText().toString();

        if (TextUtils.isEmpty(driverName)) {
            Toast.makeText(getContext(), "please enter name of driver", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(driverPhone)) {
            Toast.makeText(getContext(), "please enter phone of driver", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(busId)) {
            Toast.makeText(getContext(), "please enter bus ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(noSeats)) {
            Toast.makeText(getContext(), "please enter number of seats", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(availableSeats)) {
            Toast.makeText(getContext(), "please enter number of available seats", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(noPassengers)) {
            Toast.makeText(getContext(), "please enter number of passengers", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(time)) {
            Toast.makeText(getContext(), "please enter the time with am or pm", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(destination)) {
            Toast.makeText(getContext(), "please enter the destination", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(assemblyPoint)) {
            Toast.makeText(getContext(), "please enter the assembly point", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(destinationPoint)) {
            Toast.makeText(getContext(), "please enter the destination point", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoPath == null) {
            Toast.makeText(getContext(), "please add picture of bus or driver", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait Until Creating Trip ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        uploadImage(driverName, driverPhone, busId, noSeats, availableSeats, noPassengers, trip_day_txt, time,destination, assemblyPoint, destinationPoint);
    }

    private void createTrip(String driverName, String driverPhone, String busId, String noSeats, String availableSeats, String noPassengers, String trip_day_txt,String trip_time, String destination, String assemblyPoint, String destinationPoint, String imageurl) {
        id = databaseReference.child("Trips").push().getKey();
        TripModel tripModel = new TripModel(id, driverName, driverPhone, busId, noSeats, availableSeats, noPassengers, trip_day_txt,trip_time, destination, assemblyPoint, destinationPoint, imageurl);
        databaseReference.child("CategorisedTrips").child(tripModel.getDestination()).child(id).setValue(tripModel);
        databaseReference.child("AllTrips").child(id).setValue(tripModel);
        progressDialog.dismiss();
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.admin_container, new AdminHomeFragment());
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}
