package com.example.remindr;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubscriptionItemAdapter extends RecyclerView.Adapter<SubscriptionItemAdapter.SubscriptionViewHolder> {

    private List<SubscriptionItem> subscriptionList;

    public SubscriptionItemAdapter(List<SubscriptionItem> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    @NonNull
    @Override
    public SubscriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        SubscriptionItem item = subscriptionList.get(position);

        holder.titleTextView.setText(item.getName());
        holder.imageView.setImageResource(item.getImageResId());

        long millisUntilExpiry = item.getExpiryTimeMillis() - System.currentTimeMillis();

        if (millisUntilExpiry > 0) {
            new CountDownTimer(millisUntilExpiry, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long totalSecs = millisUntilFinished / 1000;
                    long days = totalSecs / (24 * 3600);
                    long hours = (totalSecs % (24 * 3600)) / 3600;
                    long minutes = (totalSecs % 3600) / 60;

                    String timeString = days + " nap " + String.format("%02d:%02d", hours, minutes);
                    holder.timerTextView.setText(timeString);
                }

                @Override
                public void onFinish() {
                    holder.timerTextView.setText("Lejárt");
                }
            }.start();
        } else {
            holder.timerTextView.setText("Lejárt");
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView timerTextView, titleTextView;
        ImageView imageView;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            timerTextView = itemView.findViewById(R.id.timerTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }
    }
}

