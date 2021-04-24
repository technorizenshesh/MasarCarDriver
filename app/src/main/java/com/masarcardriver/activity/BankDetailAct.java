package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityBankDetailBinding;


public class BankDetailAct extends AppCompatActivity {
    ActivityBankDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bank_detail);
        initView();
    }

    private void initView() {
        if(getIntent().getStringExtra("type").equals("1"))
        binding.header.tvTitle.setText(getString(R.string.bank_details1));
        else if(getIntent().getStringExtra("type").equals("2"))
            binding.header.tvTitle.setText(getString(R.string.payment1));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

    }
}
