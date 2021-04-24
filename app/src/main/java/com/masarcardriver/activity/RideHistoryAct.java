package com.masarcardriver.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterRideHistory;
import com.masarcardriver.databinding.ActivityRideHistoryBinding;
import com.masarcardriver.model.BookingDetailModel;
import com.masarcardriver.model.ModelPayment;
import com.masarcardriver.model.ModelRideHistory;
import com.utils.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class RideHistoryAct extends AppCompatActivity {
    ActivityRideHistoryBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ride_history);
        session= SessionManager.get(this);
        initView();
    }

    private void initView() {
        binding.header.tvTitle.setText(getText(R.string.ride_history));
        binding.header.ivBack.setOnClickListener(v -> {finish();});
        GetHistory();
    }
    private void GetHistory(){
        HashMap<String,String> param=new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("type", "DRIVER");
        ApiCallBuilder.build(this).setUrl(BaseClass.get().getUserHistory())
                .setParam(param).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                Type listType = new TypeToken<ModelRideHistory>(){}.getType();
                ModelRideHistory data = new GsonBuilder().create().fromJson(response, listType);
                if (data.getStatus().equals("1")){
                    binding.rvHistory.setAdapter(new AdapterRideHistory(RideHistoryAct.this,data.getResult()));
                }
            }
            @Override
            public void Failed(String error) {
            }
        });
    }
}
