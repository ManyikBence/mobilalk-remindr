package com.example.remindr;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

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

        String name = item.getName();
        holder.titleTextView.setText(name);

        if (name != null && !name.isEmpty()) {
            holder.initialTextView.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }

        holder.titleTextView.setText(item.getName());

        long millisUntilExpiry = item.getExpirationDate() - System.currentTimeMillis();

        if (millisUntilExpiry > 0) {
            long days = millisUntilExpiry / (24 * 60 * 60 * 1000);
            holder.timerTextView.setText(days + " nap van hátra");
        } else {
            holder.timerTextView.setText("Lejárt");
        }
        holder.buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditSubscriptionActivity.class);
            intent.putExtra("subscriptionId", item.getId()); // Add hozzá az ID-t
            v.getContext().startActivity(intent);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Törlés megerősítése")
                    .setMessage("Biztosan törölni szeretnéd ezt az előfizetést?")
                    .setPositiveButton("Igen", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("subscriptions")
                                .document(item.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    subscriptionList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, subscriptionList.size());
                                    Toast.makeText(v.getContext(), "Előfizetés törölve", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(v.getContext(), "Törlés sikertelen", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Mégse", null)
                    .show();
        });


    }


    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }

    static class SubscriptionViewHolder extends RecyclerView.ViewHolder {
        TextView timerTextView, titleTextView, initialTextView;
        Button buttonEdit, buttonDelete;

        public SubscriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            timerTextView = itemView.findViewById(R.id.timerTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            initialTextView = itemView.findViewById(R.id.initialTextView);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

