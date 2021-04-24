package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivitySelectLangBinding;

public class SelectLangAct extends AppCompatActivity {
    ActivitySelectLangBinding binding;
    String lang ="English";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_lang);
        initView();
    }

    private void initView() {
        selectlang(1);
        binding.btnEnglish.setOnClickListener(v -> {selectlang(1);});
        binding.btnFrance.setOnClickListener(v -> {selectlang(2);});
        binding.btnLogin.setOnClickListener(v -> {startActivity(new Intent(this,LoginAct.class));
        });
        binding.btnRegister.setOnClickListener(v -> {startActivity(new Intent(this,RegisterAct.class));});
    }

    private void selectlang(int i) {
        if(i==1){
            binding.rlEnglish.setBackgroundColor(getColor(R.color.select_lang_bg_color));
            binding.rlFrance.setBackgroundColor(getColor(R.color.white));
            binding.ivEnglishChk.setVisibility(View.VISIBLE);
            binding.ivFranceChk.setVisibility(View.GONE);
            lang ="English";
        }
        else if(i==2){
            binding.rlEnglish.setBackgroundColor(getColor(R.color.white));
            binding.rlFrance.setBackgroundColor(getColor(R.color.select_lang_bg_color));
            binding.ivEnglishChk.setVisibility(View.GONE);
            binding.ivFranceChk.setVisibility(View.VISIBLE);
            lang ="France";
        }
    }
}
