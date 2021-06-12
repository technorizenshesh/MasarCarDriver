package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.masarcardriver.R;
import com.masarcardriver.activity.ManageDocumentAct;
import com.masarcardriver.databinding.ItemEmergencyBinding;
import com.masarcardriver.databinding.ItemVehicleBinding;
import com.masarcardriver.model.ModelVehicle;

import java.util.ArrayList;

public class AdapterVehicle extends RecyclerView.Adapter<AdapterVehicle.MyViewHolder> {
    Context context;
    ArrayList<ModelVehicle>data;

    public AdapterVehicle(Context context, ArrayList<ModelVehicle> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVehicleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_vehicle,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setVehicle(data.get(position));
        holder.binding.executePendingBindings();
        Glide.with(context)
                .load(data.get(position).getVehicleImage())
                .centerCrop()
                .into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemVehicleBinding binding;
        public MyViewHolder(@NonNull ItemVehicleBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}