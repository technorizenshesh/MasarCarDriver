package com.masarcardriver.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityHelpBinding;
import com.utils.Utils.Tools;

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

        binding.btnTermsCondition.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://masarcarjo.com/terms.html"));
            startActivity(browserIntent);
        });

        binding.btnPrivacyPolicy.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://masarcarjo.com/privacy.html"));
            startActivity(browserIntent);
        });

        binding.btnAbout.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://masarcarjo.com/about-us.html"));
            startActivity(browserIntent);
        });

        binding.btnContactUs.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://masarcarjo.com/faq.html"));
            startActivity(browserIntent);
        });
    }
}
