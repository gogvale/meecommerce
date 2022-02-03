package dev.eduardoleal.meecommerce.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dev.eduardoleal.meecommerce.Home;
import dev.eduardoleal.meecommerce.R;

public class SignIn extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnSignIn, btnRecoveryPassword;
    TextView txtSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // TODO: Reference UI
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_user_signin);
        btnRecoveryPassword = findViewById(R.id.btn_user_recovery_password);
        txtSignUp = findViewById(R.id.txt_signup);

        // TODO: Initialize context app
        mAuth = FirebaseAuth.getInstance();

        // TODO: Actions
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Recovery.class);
                startActivity(i);
            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });
    }

    private void loginUser() {
        String email, password;
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignIn.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}