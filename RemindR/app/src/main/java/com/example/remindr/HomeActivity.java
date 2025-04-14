package com.example.remindr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 99;

    ViewPager2 viewPager;
    SubscriptionItemAdapter adapter;
    List<SubscriptionItem> subscriptionList;


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

        viewPager = findViewById(R.id.viewPager);

        subscriptionList = new ArrayList<>();
        subscriptionList.add(new SubscriptionItem("Netflix", R.drawable.ic_launcher_foreground, getFutureTime(2)));
        subscriptionList.add(new SubscriptionItem("Jogosítvány", R.drawable.ic_launcher_foreground, getFutureTime(30)));
        subscriptionList.add(new SubscriptionItem("Edzőterem bérlet", R.drawable.ic_launcher_foreground, getFutureTime(15)));

        adapter = new SubscriptionItemAdapter(subscriptionList);
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);

        viewPager.setPageTransformer((page, position) -> {
            float scale = 0.85f + (1 - Math.abs(position)) * 0.15f;
            page.setScaleY(scale);
            page.setAlpha(0.5f + (1 - Math.abs(position)) * 0.5f);
        });
    }

    private long getFutureTime(int daysFromNow) {
        return System.currentTimeMillis() + daysFromNow * 24L * 60 * 60 * 1000;
    }
}