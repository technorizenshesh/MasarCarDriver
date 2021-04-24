package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ItemDrawerBinding;
import com.masarcardriver.listener.menuListener;
import com.masarcardriver.model.MenuModel;

import java.util.ArrayList;

public class AdapterDrawer extends RecyclerView.Adapter<AdapterDrawer.MyViewHolder> {
    Context context;
    ArrayList<MenuModel>arrayList;
    menuListener listener;

    public AdapterDrawer(Context context, ArrayList<MenuModel> arrayList, menuListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDrawerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_drawer,parent,false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getItem());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDrawerBinding binding;
        public MyViewHolder(@NonNull ItemDrawerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.btnClick.setOnClickListener(v -> {
                listener.OnMenuClick(getAdapterPosition(),arrayList.get(getAdapterPosition()).getId());
            });
        }
    }
}
