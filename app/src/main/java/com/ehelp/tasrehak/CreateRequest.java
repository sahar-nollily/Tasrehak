package com.ehelp.tasrehak;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class CreateRequest extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button btn;
    Spinner TypeID;
    TextView fName, lName, ID, phone, dBirth, reason,timeFrom, timeTo, date;

    String key;
    String Request_ID;

    //String User_ID ;
    private DatabaseReference mDatabase;

    private String Id;
    private String FName;
    private String LName;
    private String Phone;
    private String DBirth;
    private String Date;
    private String TimeFrom;
    private String TimeTo;
    private String Reason;
    private String RequestState;
    private String Type_ID;

    Calendar calendar;
    int currentHour;
    int currentMinute;
    TimePickerDialog timePickerDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Id = getIntent().getExtras().getString("ID");
        btn = findViewById(R.id.btn);


        TypeID = findViewById(R.id.TypeID);
        fName = findViewById(R.id.FirstName);
        lName = findViewById(R.id.LastName);
        ID = findViewById(R.id.TxtID);
        phone = findViewById(R.id.PhoneNo);
        date = findViewById(R.id.TxtDate);
        timeFrom = findViewById(R.id.TxtTimeFrom);
        timeTo = findViewById(R.id.TxtTimeTo);
        dBirth = findViewById(R.id.DateBirth);
        reason = findViewById(R.id.TxtReason);

        date.setOnClickListener(this);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);



        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        load();
        openForm();
    }

    private void openForm() {

        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (date.getText().toString().isEmpty()) {
                    date.setError("Required");
                } else if (timeFrom.getText().toString().isEmpty()) {
                    timeFrom.setError("Required");
                } else if (timeTo.getText().toString().isEmpty()) {
                    timeTo.setError("Required");
                } else if (reason.getText().toString().isEmpty()) {
                    reason.setError("Required");
                } else {

                    insert();
                }

            }
        });

    }

    public  void insert(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Requests");
        key = new Random().nextInt(111111) + 888888+"";

        FName = fName.getText().toString();
        LName = lName.getText().toString();
        Phone = phone.getText().toString();
        DBirth = dBirth.getText().toString();
        Date = date.getText().toString();
        TimeFrom = timeFrom.getText().toString();
        TimeTo = timeTo.getText().toString();
        Reason = reason.getText().toString();
        RequestState = "In Progress";
        Id = ID.getText().toString();
        Type_ID = TypeID.getSelectedItem().toString();

        Request_ID = key + Id;

        Save_Request request = new Save_Request(Request_ID, RequestState,
                FName, LName, Id, Type_ID, Phone, DBirth, Date, TimeFrom, TimeTo, Reason, "0");

        mDatabase.child(Request_ID + "").setValue(request);

        //Toast.makeText(Create_Request.this, "added successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateRequest.this, PermissionDetail.class);
        intent.putExtra("Request_ID", Request_ID);
        finish();
        startActivity(intent);

        //create();
        // ضيفي اسم االباركود هنا oraginal_image
        //User_ID = ID.getText().toString();
        //barcodeScale = Bitmap.createScaledBitmap(original_image,600,250,false);
        //createPDF();
    }

    public void load() {
        final ProgressDialog loading = ProgressDialog.show(this, "", "Please wait", false, false);

        DatabaseReference Permission = FirebaseDatabase.getInstance().getReference("Users");
        Query query = Permission.orderByChild("id").equalTo(Id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    fName.setText(childSnapshot.child("first_Name").getValue(String.class));
                    lName.setText(childSnapshot.child("last_Name").getValue(String.class));
                    phone.setText(childSnapshot.child("phone_Number").getValue(String.class));
                    dBirth.setText(childSnapshot.child("date_of_Birth").getValue(String.class));
                    ID.setText(Id);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loading.dismiss();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,month);
        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        month =month+1 ;
        String setDate = year + "-" + month + "-" + dayOfMonth;
        date.setText(setDate);

    }


    @Override
    public void onClick(View v) {

        if (v == date){
            DialogFragment date = new DatePickerFregment();
            date.show(getSupportFragmentManager(),"date Picker");

        }
        if (v== timeFrom) {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(CreateRequest.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {


                    timeFrom.setText(String.format("%02d:%02d", hourOfDay, minutes));
                }
            }, currentHour, currentMinute, false);

            timePickerDialog.show();
        }


        if (v== timeTo) {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(CreateRequest.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                    timeTo.setText(String.format("%02d:%02d", hourOfDay, minutes)  );
                }
            }, currentHour, currentMinute, false);

            timePickerDialog.show();
        }


    }
}



