package com.ehelp.tasrehak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView id, password, ForgetPassword, Register;
    Button login ;
    String ID , Password ;
    String dbPassword ="0";

    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        id = findViewById(R.id.ID);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        Register = findViewById(R.id.Create);
        ForgetPassword = findViewById(R.id.forgetpass);


        login.setOnClickListener(this);
        Register.setOnClickListener(this);
        ForgetPassword.setOnClickListener(this);


    }

    private void check() {

        ID= id.getText().toString();
        Password = md5(password.getText().toString());

        if(ID.isEmpty()){
            id.setError("Required");
        }

        if(Password.isEmpty()){
            password.setError("Required");
        }
        else{
            final ProgressDialog loading = ProgressDialog.show(Login.this, "Loading...", "Please Wait", false, false);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            Query query = databaseReference.orderByChild("id").equalTo(ID);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                            dbPassword = childSnapshot.child("password").getValue(String.class);
                        }
                        if(dbPassword.equals(Password)) {
                            sessionManager.createSession(ID);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra("id",ID);
                            startActivity(intent);
                            finish();
                        }else{

                            Toast.makeText(Login.this, "Wrong Password ", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(Login.this, "ID does not exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

            loading.dismiss();


        }



    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

        if(v== login){

            check();
        }

        if(v == ForgetPassword){
            Intent intent = new Intent(Login.this, Forget_Password.class);
            startActivity(intent);
        }

        if(v == Register){
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        }

    }
}
