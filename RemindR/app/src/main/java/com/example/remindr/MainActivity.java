package com.example.remindr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseAuth auth;
    private static final int SECRET_KEY = 99;

    EditText userNameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userNameET = findViewById(R.id.EditTextUserName);
        passwordET = findViewById(R.id.EditTextPassword);
        auth = FirebaseAuth.getInstance();

    }

    public void login(View view) {


        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();

        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Sikertelen belépés: Nincs kitöltve minden adat", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startHome();
                } else{
                    Toast.makeText(MainActivity.this, "Sikertelen belépés: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(LOG_TAG, "Nev: " + userName + ",  Jelszo: " + password);
    }

    public void regist(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(registerIntent);
    }

    private void startHome(){
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(homeIntent);
    }

    public void loginAsGuest(View view) {
        auth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startHome();
                } else{
                    Toast.makeText(MainActivity.this, "Sikertelen belépés: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}