package com.ehelp.tasrehak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    TextView fName, lName, ID, phone, dBirth, pass , pass2;
    Spinner TypeID ;
    Button Register ;

    private String Id;
    private String FName;
    private String LName;
    private String Phone;
    private String DBirth;
    private String Type_ID;
    private String Pass;
    private String Pass2;
    String EPass ;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fName = findViewById(R.id.FirstName);
        lName = findViewById(R.id.LastName);
        ID = findViewById(R.id.TxtID);
        phone = findViewById(R.id.PhoneNo);
        dBirth = findViewById(R.id.DateBirth);
        TypeID = findViewById(R.id.TypeID);
        pass = findViewById(R.id.pass);
        pass2 = findViewById(R.id.pass2);
        Register = findViewById(R.id.Register);



        Register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        final ProgressDialog loading = ProgressDialog.show(this, "Create Account", "Please wait", false, false);

        if (this.isValidData()) {


            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

            mDatabase.orderByChild("id").equalTo(Id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(Register.this, "User Exist", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Register.this, "Create successfully", Toast.LENGTH_SHORT).show();
                                EPass = md5(Pass);
                                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                                Save_User user = new Save_User(FName, LName, Id, Type_ID, Phone, DBirth, EPass);
                                String key = mDatabase.push().getKey();
                                mDatabase.child(key).setValue(user);
                                Intent intent = new Intent(Register.this,Login.class);
                                finish();
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

        loading.dismiss();
    }


    public boolean isValidData() {

        Id = ID.getText().toString();
        Type_ID = TypeID.getSelectedItem().toString();
        FName = fName.getText().toString();
        LName = lName.getText().toString();
        Phone = phone.getText().toString();
        DBirth = dBirth.getText().toString();
        Pass = pass.getText().toString();
        Pass2 = pass2.getText().toString();



        String  phonePattern = "\\d{8}|05\\d{8}|\\+1\\(\\d{3}\\)\\d{3}-\\d{4}|\\+1\\d{10}|\\d{3}-\\d{3}-\\d{4}";

        // Data Validation
        if (Id.isEmpty()|| Type_ID.isEmpty() || FName.isEmpty() || LName.isEmpty() || Phone.isEmpty() || DBirth.isEmpty() || Pass.isEmpty() || Pass2.isEmpty()) {
            Toast.makeText(Register.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (!isValidPassword(Pass)){
            pass.setError("Password must be more than 8 and contain (A,B,a,b,0,1,etc");
            return false;
        }
        else if (!Phone.matches(phonePattern)){
            phone.setError("Ex: 05xxxxxxxx");
            return false;

        }
        else if(!Pass2.equals(Pass)) {
            pass.setError("Password dose not match");
            pass2.setError("Password dose not match");
            return false;
        } else {

            return true ;
        }
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
}
