package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityProfileBinding;
import com.masarcardriver.model.SignupModel;
import com.utils.ModelUser;
import com.utils.Session.SessionManager;

public class ProfileAct extends AppCompatActivity {
    ActivityProfileBinding binding;
    SignupModel loginModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getText(R.string.my_profile1));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.btnEditProfile.setOnClickListener(v -> {startActivity(new Intent(ProfileAct.this,EditProfileAct.class));});

        binding.btnChangePass.setOnClickListener(v -> {startActivity(new Intent(ProfileAct.this,ChangePasswordAct.class));});
    }

    private void setUserInfo() {
        ModelUser user= SessionManager.get(this).getValue();
        binding.setDriver(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserInfo();
    }
}
