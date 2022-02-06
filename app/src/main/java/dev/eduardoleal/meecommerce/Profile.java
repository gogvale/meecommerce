package dev.eduardoleal.meecommerce;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Calendar;

public class Profile extends AppCompatActivity {

    TextView txtCheers;
    EditText edtFullName, edtEmail, edtPhone;
    Button btnUpdate;
    ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edtFullName = findViewById(R.id.edt_fullname_profile);
        edtEmail = findViewById(R.id.edt_email_profile);
        edtPhone = findViewById(R.id.edt_phone_profile);
        btnUpdate = findViewById(R.id.btn_update_profile);
        imageProfile = findViewById(R.id.imageProfile);
        txtCheers = findViewById(R.id.txt_cheers);

        onUpdateUI();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateProfile();
            }
        });
    }

    private void onUpdateProfile() {
        String newFullName = edtFullName.getText().toString().trim();
        if (newFullName.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newFullName).build();

            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Email is not empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void onUpdateUI() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            Uri image = user.getPhotoUrl();

            edtFullName.setText(name);
            edtEmail.setText(email);
            edtPhone.setText(phone);
            Glide.with(getApplicationContext()).load(image).into(imageProfile);

            onSetCheers();
        }
    }

    private void onSetCheers(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            txtCheers.setText("Good Morning");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            txtCheers.setText("Good Afternoon");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            txtCheers.setText("Good Evening");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            txtCheers.setText("Good Night");
        }
    }
}