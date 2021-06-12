package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityRideDetailBinding;
import com.masarcardriver.model.ModelRideHistory;


public class RideDetailActivity extends AppCompatActivity {
    ActivityRideDetailBinding binding;
    private ModelRideHistory.Result data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ride_detail);
        data = (ModelRideHistory.Result) getIntent().getSerializableExtra("data");
        binding.setData(data);
        binding.executePendingBindings();
        binding.ivBack.setOnClickListener(v->finish());
    }
}