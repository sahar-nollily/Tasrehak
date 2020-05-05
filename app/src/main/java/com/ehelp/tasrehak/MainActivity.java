package com.ehelp.tasrehak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();


        Button Rrquest = findViewById(R.id.request_button);
        Button logout = findViewById(R.id.logout_button);
        Button Show_Permission = findViewById(R.id.Show_Permission);


        HashMap<String, String> user = sessionManager.getUserDetail();
        ID = user.get(sessionManager.USERNAME);

        Rrquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateRequest.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();

            }
        });


        Show_Permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Permission_List.class);
                intent.putExtra("ID",ID);
                startActivity(intent);

            }
        });

    }

}
