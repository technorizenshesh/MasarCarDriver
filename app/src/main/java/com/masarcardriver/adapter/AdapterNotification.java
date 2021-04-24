package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ItemNotificationBinding;
import com.masarcardriver.databinding.ItemWalletBinding;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {
    Context context;

    public AdapterNotification(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notification,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;
        public MyViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
