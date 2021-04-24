package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityTripEndBinding;


public class TripEndAct extends AppCompatActivity {
    ActivityTripEndBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_trip_end);
        initView();
    }

    private void initView() {
        binding.btnOk.setOnClickListener(v -> {
          //  startActivity(new Intent(TripEndAct.this,HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
          //  finish();

           Intent intent = new Intent();
           intent.putExtra("editTextValue", "value_here");
           setResult(RESULT_OK, intent);
           finish();
        });
    }

    @Override
    public void onBackPressed() {
     //   super.onBackPressed();
        startActivity(new Intent(TripEndAct.this,HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
