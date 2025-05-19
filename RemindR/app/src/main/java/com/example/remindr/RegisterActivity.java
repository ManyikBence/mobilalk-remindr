package com.example.remindr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 99;
    private FirebaseAuth auth;


    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;
    RadioGroup accountTypeRG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secret_key != SECRET_KEY){
            finish();
        }

        usernameET = findViewById(R.id.usernameEditText);
        emailET = findViewById(R.id.useremailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
        accountTypeRG = findViewById(R.id.accountRadioGroup);
        accountTypeRG.check(R.id.adultRadioButton);
        auth = FirebaseAuth.getInstance();

    }

    public void register(View view) {

        String userName = usernameET.getText().toString();
        String eMail = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        int checkedID = accountTypeRG.getCheckedRadioButtonId();
        RadioButton radioButton = accountTypeRG.findViewById(checkedID);
        String accountType = radioButton.getText().toString();

        if (eMail.isEmpty() || userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: Nincs kitöltve minden adat", Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(passwordAgain)){
            Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: Nem egyforma a két jelszó!", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i(LOG_TAG, "Nev: " + userName + ",  E-mail: " + eMail);

        auth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                String userId = auth.getCurrentUser().getUid();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Egyéni felhasználói adatok mentése külön gyűjteménybe
                UserProfile profile = new UserProfile(userName, accountType);
                db.collection("users").document(userId).set(profile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(RegisterActivity.this, "Sikeres regisztráció, de hiba történt az adatok mentésekor", Toast.LENGTH_LONG).show();
                            finish();
                        });

            } else if (password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: Túl rövid jelszó", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void cancel(View view) {
        finish();
    }
}