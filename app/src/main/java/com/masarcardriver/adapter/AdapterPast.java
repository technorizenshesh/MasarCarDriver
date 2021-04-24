package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ItemPastBinding;
import com.masarcardriver.databinding.ItemUpcomingBinding;

public class AdapterPast extends RecyclerView.Adapter<AdapterPast.MyViewHolder> {
    Context context;

    public AdapterPast(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_past,parent,false);
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
        ItemPastBinding binding;
        public MyViewHolder(@NonNull ItemPastBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
