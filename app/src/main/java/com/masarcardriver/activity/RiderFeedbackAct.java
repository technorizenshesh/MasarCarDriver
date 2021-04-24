package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterRating;
import com.masarcardriver.databinding.ActivityRiderFeedbackBinding;

public class RiderFeedbackAct extends AppCompatActivity {
    ActivityRiderFeedbackBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rider_feedback);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.rider_feedback1));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.rvRating.setAdapter(new AdapterRating(RiderFeedbackAct.this));
    }
}
