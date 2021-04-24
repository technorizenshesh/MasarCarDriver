package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterNotification;
import com.masarcardriver.databinding.ActivityNotificationBinding;

public class NotificationAct extends AppCompatActivity {
    ActivityNotificationBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.notifications));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.rvNoti.setAdapter(new AdapterNotification(NotificationAct.this));
    }
}
