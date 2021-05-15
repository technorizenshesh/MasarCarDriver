package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterWallet;
import com.masarcardriver.databinding.ActivityWalletBinding;
import com.utils.Session.SessionManager;

public class WalletAct extends AppCompatActivity {
    ActivityWalletBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_wallet);
        initView();
    }

    private void initView() {
        session= SessionManager.get(this);
        binding.header.tvTitle.setText(getText(R.string.wallet));
        binding.header.ivBack.setOnClickListener(v -> {finish();});
        binding.rvWallet.setAdapter(new AdapterWallet(WalletAct.this));
        binding.tvMoney.setText("$"+session.getValue().getAmount());
    }
}
