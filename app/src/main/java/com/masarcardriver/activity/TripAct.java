package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.masarcardriver.R;
import com.masarcardriver.adapter.MyAdapter;
import com.masarcardriver.databinding.ActivityTripBinding;

public class TripAct extends AppCompatActivity {
    ActivityTripBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_trip);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getString(R.string.your_trip));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Past"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter(this,getSupportFragmentManager(), binding.tabLayout.getTabCount()));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
