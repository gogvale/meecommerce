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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.eduardoleal.meecommerce.Home;
import dev.eduardoleal.meecommerce.MainActivity;
import dev.eduardoleal.meecommerce.R;

public class SignUp extends AppCompatActivity {

    EditText edtName, edtLastName, edtEmail, edtPassword, edtRepeatPassword;
    Button btnSignUp;
    TextView txtSignIn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // TODO: Reference UI
        edtName = findViewById(R.id.edt_name);
        edtLastName = findViewById(R.id.edt_lastname);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtRepeatPassword = findViewById(R.id.edt_repeat_password);
        btnSignUp = findViewById(R.id.btn_signup_user);
        txtSignIn = findViewById(R.id.txt_signin);

        // TODO: Initialize context app
        mAuth = FirebaseAuth.getInstance();

        // TODO: Actions
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignIn.class);
                startActivity(i);
            }
        });
    }

    private void registerUser() {
        String name, lastName, email, password, repeatPassword;

        name = edtName.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();
        repeatPassword = edtRepeatPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(getApplicationContext(), "Please enter last name...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password) && password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Please enter a password, minimum long 6 characters...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(getApplicationContext(), "Password not match...", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    registerUserData(user);
                    Toast.makeText(getApplicationContext(), "User created!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUserData(FirebaseUser user) {
        String userId = user.getUid();
        String userName, userLastName, userEmail;

        userName = edtName.getText().toString().trim();
        userLastName = edtLastName.getText().toString().trim();
        userEmail = edtEmail.getText().toString().trim();
        Date currentTime = Calendar.getInstance().getTime();

        Map<String, Object> dataUser = new HashMap<>();
        dataUser.put("userId", userId);
        dataUser.put("userName", userName);
        dataUser.put("userLastName", userLastName);
        dataUser.put("userEmail", userEmail);
        dataUser.put("createAt", currentTime);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).set(dataUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.sendEmailVerification();
                onSuccessRegisterUser();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Oops there was error, try again later...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSuccessRegisterUser() {
        Toast.makeText(getApplicationContext(), "Please activated account with email.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}