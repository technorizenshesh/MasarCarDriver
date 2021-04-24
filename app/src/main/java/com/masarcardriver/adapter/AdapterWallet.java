package com.masarcardriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ItemWalletBinding;

public class AdapterWallet extends RecyclerView.Adapter<AdapterWallet.MyViewHolder> {
    Context context;

    public AdapterWallet(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWalletBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_wallet,parent,false);
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
        ItemWalletBinding binding;
        public MyViewHolder(@NonNull ItemWalletBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
