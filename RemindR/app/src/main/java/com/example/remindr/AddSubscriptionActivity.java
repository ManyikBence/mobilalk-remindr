package com.example.remindr;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddSubscriptionActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice;
    private Button buttonPickDate, buttonSave;
    private TextView textViewDate;
    private long selectedTimestamp = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonPickDate = findViewById(R.id.buttonPickDate);
        buttonSave = findViewById(R.id.buttonSave);
        textViewDate = findViewById(R.id.textViewDate);

        buttonPickDate.setOnClickListener(v -> showDatePicker());
        buttonSave.setOnClickListener(v -> saveSubscription());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth, 0, 0);
                    selectedTimestamp = calendar.getTimeInMillis();
                    textViewDate.setText(dayOfMonth + "." + (month + 1) + "." + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveSubscription() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || selectedTimestamp == -1) {
            Toast.makeText(this, "Tölts ki minden mezőt!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("subscriptions").document().getId();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("userId", userId);
        data.put("name", name);
        data.put("price", price);
        data.put("expirationDate", selectedTimestamp);

        db.collection("subscriptions").document(id)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Előfizetés mentve!", Toast.LENGTH_SHORT).show();
                    finish(); // visszatérés a HomeActivity-be
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba történt!", Toast.LENGTH_SHORT).show();
                });
    }
}
