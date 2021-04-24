package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityHelpBinding;

public class NeedHelpAct extends AppCompatActivity {
    ActivityHelpBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_help);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.need_help1));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.btnTermsCondition.setOnClickListener(v -> {});

        binding.btnPrivacyPolicy.setOnClickListener(v -> {});

        binding.btnAbout.setOnClickListener(v -> {});

        binding.btnContactUs.setOnClickListener(v -> {});
    }
}
