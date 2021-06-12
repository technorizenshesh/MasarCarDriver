package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterVehicle;
import com.masarcardriver.databinding.ActivityManageVehicleBinding;
import com.masarcardriver.model.ModelCurrentBooking;
import com.masarcardriver.model.ModelCurrentBookingResult;
import com.masarcardriver.model.ModelVehicle;
import com.utils.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class ManageVehicleAct extends AppCompatActivity {
    ActivityManageVehicleBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_manage_vehicle);
        initView();
    }

    private void initView() {
        session= SessionManager.get(this);
        binding.header.tvTitle.setText(getString(R.string.manage_vehicle));
        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.ivDocument.setOnClickListener(v -> {finish();});

        binding.ivEdit.setOnClickListener(v -> {finish();});

        binding.ivDelete.setOnClickListener(v -> {finish();});
        binding.imgAddVehicle.setOnClickListener(v->{
            startActivity(new Intent(this,AddVehicleAct.class).putExtra("type","1"));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetVehicle();
    }

    private void GetVehicle(){
        HashMap<String,String> param=new HashMap<>();
        param.put("driver_id", session.getUserID());
        ApiCallBuilder.build(this).setUrl(BaseClass.get().vehicle_list())
                .isShowProgressBar(true)
                .setParam(param).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                Log.e("BookingResponse",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("status").equals("1")){
                        Type listType = new TypeToken<ArrayList<ModelVehicle>>(){}.getType();
                        ArrayList<ModelVehicle> data = new GsonBuilder().create().fromJson(object.getString("result"), listType);
                        binding.recycleVehicle.setAdapter(new AdapterVehicle(ManageVehicleAct.this,data));
                    }
                } catch (JSONException e) {
                    Log.e("Error",e.getLocalizedMessage());
                    e.printStackTrace();
                }
                Log.e("End","======================>");
            }

            @Override
            public void Failed(String error) {

            }
        });
    }
}
