package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterWallet;
import com.masarcardriver.databinding.ActivityWalletBinding;

public class WalletAct extends AppCompatActivity {
    ActivityWalletBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_wallet);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getText(R.string.wallet));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.rvWallet.setAdapter(new AdapterWallet(WalletAct.this));
    }
}
