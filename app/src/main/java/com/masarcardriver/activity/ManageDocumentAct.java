package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityManageDocumentBinding;

public class ManageDocumentAct extends AppCompatActivity {
    ActivityManageDocumentBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_document);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.manage_docu));
        binding.header.ivBack.setOnClickListener(v -> {finish();});
    }
}
