package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityManageVehicleBinding;

public class ManageVehicleAct extends AppCompatActivity {
    ActivityManageVehicleBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_manage_vehicle);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.manage_vehicle));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.ivDocument.setOnClickListener(v -> {finish();});

        binding.ivEdit.setOnClickListener(v -> {finish();});

        binding.ivDelete.setOnClickListener(v -> {finish();});
    }
}
