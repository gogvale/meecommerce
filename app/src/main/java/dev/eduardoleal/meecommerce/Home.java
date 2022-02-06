package dev.eduardoleal.meecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {

    Button btnLogout, btnProfile, btnMeet;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: Reference UI
        btnLogout = findViewById(R.id.btn_logout);
        btnProfile = findViewById(R.id.btn_profile);
        btnMeet = findViewById(R.id.btn_meet);

        // TODO: Initialize context app
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogout();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowProfile();
            }
        });

        btnMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowMeet();
            }
        });
    }

    private void onLogout(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private void onShowProfile(){
        Intent i = new Intent(getApplicationContext(), Profile.class);
        startActivity(i);
    }

    private void onShowMeet(){
        Intent i = new Intent(getApplicationContext(), Meet.class);
        startActivity(i);
    }
}