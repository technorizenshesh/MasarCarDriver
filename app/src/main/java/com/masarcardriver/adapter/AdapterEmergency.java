package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ItemEmergencyBinding;
import com.masarcardriver.databinding.ItemRatingBinding;

public class AdapterEmergency extends RecyclerView.Adapter<AdapterEmergency.MyViewHolder> {
    Context context;

    public AdapterEmergency(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEmergencyBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_emergency,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemEmergencyBinding binding;
        public MyViewHolder(@NonNull ItemEmergencyBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}