package dev.eduardoleal.meecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import dev.eduardoleal.meecommerce.auth.Activate;
import dev.eduardoleal.meecommerce.auth.Recovery;
import dev.eduardoleal.meecommerce.auth.SignUp;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtSubtitle, txtInstructions, txtHelp, txtRegister, txtRegisterAction;
    EditText edtEmail, edtPassword;
    Button btnSignin;
    LinearLayoutCompat layout;
    ProgressBar progress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Reference UI
        txtTitle = findViewById(R.id.txt_title_signin);
        txtSubtitle = findViewById(R.id.txt_subtitle_signin);
        txtInstructions = findViewById(R.id.txt_instructions_signin);
        txtHelp = findViewById(R.id.txt_help_signin);
        txtRegister = findViewById(R.id.txt_register_signin);
        txtRegisterAction = findViewById(R.id.txt_register_signin_action);
        edtEmail = findViewById(R.id.edt_email_signin);
        edtPassword = findViewById(R.id.edt_password_signin);
        btnSignin = findViewById(R.id.btn_signin);
        layout = findViewById(R.id.layout_signin);
        progress = findViewById(R.id.progress_signin);

        // TODO: Initialize context app
        mAuth = FirebaseAuth.getInstance();

        // TODO: Debug Config
        if (BuildConfig.DEBUG) {
            // Warning: This user was registered by me, please do not use it because I will find you and cut off your fingers.
            edtEmail.setText("xidoyi2507@chinamkm.com");
            edtPassword.setText("123456");
        }

        // TODO: Actions
        txtHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHelp();
            }
        });

        txtRegisterAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignIn();
            }
        });
    }

    private void onHelp() {
        Intent i = new Intent(getApplicationContext(), Recovery.class);
        startActivity(i);
    }

    private void onRegister() {
        Intent i = new Intent(getApplicationContext(), SignUp.class);
        startActivity(i);
    }

    private void onSignIn() {
        String email, password;
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            onShowSnack("Please enter email...");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            onShowSnack("Please enter password...");
            return;
        }

        progress.setVisibility(View.VISIBLE);
        btnSignin.setVisibility(View.GONE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        onUserRedirectToHome();
                    } else {
                        onUserVerifyEmail(email);
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        onShowSnack("Passowrd invalid length");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        onShowSnack("Password or User incorrect...");
                    } catch (FirebaseAuthUserCollisionException e) {
                        onShowSnack("Error with user");
                    } catch (Exception e) {
                        onShowSnack("Exists un error, try again later...");
                    }
                }
                progress.setVisibility(View.GONE);
                btnSignin.setVisibility(View.VISIBLE);
                onClearInputs();
            }
        });
    }

    private void onUserRedirectToHome() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
    }

    private void onUserVerifyEmail(String email) {
        Intent i = new Intent(getApplicationContext(), Activate.class);
        i.putExtra("email", email);
        startActivity(i);
    }

    private void onClearInputs() {
        edtEmail.setText("");
        edtPassword.setText("");
    }

    private void onShowSnack(String text) {
        Snackbar snackbar = Snackbar.make(layout, text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Intent i = new Intent(getApplicationContext(), Home.class);
            startActivity(i);
        }
    }
}