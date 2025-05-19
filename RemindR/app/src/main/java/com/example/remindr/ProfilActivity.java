package com.example.remindr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilActivity extends AppCompatActivity {

    private TextView emailTextView, usernameTextView, accountTypeTextView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        emailTextView = findViewById(R.id.emailTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        accountTypeTextView = findViewById(R.id.accountTypeTextView);
        logoutButton = findViewById(R.id.logoutButton);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getEmail() != null) {
            emailTextView.setText("E-mail: " + user.getEmail());

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            String accountType = documentSnapshot.getString("accountType");

                            usernameTextView.setText("Felhasználónév: " + username);
                            accountTypeTextView.setText("Fiók típusa: " + accountType);
                        } else {
                            Toast.makeText(this, "Nem található felhasználói adat.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba történt az adatok lekérdezésekor.", Toast.LENGTH_SHORT).show();
                    });
        }else {
            emailTextView.setText("Vendég profil.");
            usernameTextView.setText(" ");
            accountTypeTextView.setText(" ");
        }

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Kijelentkeztél", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
