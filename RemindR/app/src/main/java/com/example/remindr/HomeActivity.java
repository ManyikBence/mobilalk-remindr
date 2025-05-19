package com.example.remindr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 99;

    ViewPager2 viewPager;
    SubscriptionItemAdapter adapter;
    List<SubscriptionItem> subscriptionList;
    TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secret_key != SECRET_KEY){
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);

        emptyTextView = findViewById(R.id.emptyTextView);

        // Lista és adapter egyszeri létrehozása
        subscriptionList = new ArrayList<>();
        adapter = new SubscriptionItemAdapter(subscriptionList);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);

        viewPager.setPageTransformer((page, position) -> {
            float scale = 0.85f + (1 - Math.abs(position)) * 0.15f;
            page.setScaleY(scale);
            page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
        });

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddSubscriptionActivity.class);
            startActivity(intent);
        });

        // Első adatbetöltés
        loadSubscriptions();
    }

    private void loadSubscriptions() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }
        String userId = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("subscriptions")
                .whereEqualTo("userId", userId)
                .orderBy("expirationDate")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    subscriptionList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        SubscriptionItem item = doc.toObject(SubscriptionItem.class);
                        item.setId(doc.getId());
                        subscriptionList.add(item);
                    }

                    adapter.notifyDataSetChanged();

                    if (subscriptionList.isEmpty()) {
                        emptyTextView.setVisibility(View.VISIBLE);
                    } else {
                        emptyTextView.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Hiba a Firestore lekérdezés során", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubscriptions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
