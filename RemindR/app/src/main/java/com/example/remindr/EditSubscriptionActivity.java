package com.example.remindr;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class EditSubscriptionActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice;
    private TextView textViewDate;
    private Button buttonPickDate, buttonSave;
    private long selectedDate = -1;
    private String subscriptionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        textViewDate = findViewById(R.id.textViewDate);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonSave = findViewById(R.id.buttonSave);

        subscriptionId = getIntent().getStringExtra("subscriptionId");
        if (subscriptionId == null) {
            finish();
            return;
        }

        // Lekérdezzük a meglévő adatokat
        FirebaseFirestore.getInstance().collection("subscriptions")
                .document(subscriptionId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        SubscriptionItem item = documentSnapshot.toObject(SubscriptionItem.class);
                        editTextName.setText(item.getName());
                        editTextPrice.setText(String.valueOf(item.getPrice()));
                        selectedDate = item.getExpirationDate();
                        textViewDate.setText(android.text.format.DateFormat.format("yyyy.MM.dd", selectedDate));
                    }
                });

        buttonPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTimeInMillis();
                textViewDate.setText(android.text.format.DateFormat.format("yyyy.MM.dd", selectedDate));
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            double price = Double.parseDouble(editTextPrice.getText().toString().trim());

            FirebaseFirestore.getInstance().collection("subscriptions")
                    .document(subscriptionId)
                    .update("name", name,
                            "price", price,
                            "expirationDate", selectedDate)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Előfizetés frissítve", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Hiba a mentés során", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
