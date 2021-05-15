package com.masarcardriver.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityPaymentSummaryBinding;
import com.masarcardriver.dialog.NewRequestDialog;
import com.masarcardriver.model.ModelCurrentBooking;
import com.masarcardriver.model.ModelCurrentBookingResult;
import com.masarcardriver.model.ModelPayment;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;

public class PaymentSummary extends AppCompatActivity {
    ActivityPaymentSummaryBinding binding;
    private ModelCurrentBooking data;
    private ModelCurrentBookingResult result;
    private SessionManager session;
    private ModelPayment payment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_summary);
        if(getIntent()!=null){
            data=(ModelCurrentBooking)getIntent().getSerializableExtra("data");
            result=data.getResult().get(0);
        }
        initView();
    }

    private void initView() {
        session= SessionManager.get(this);
        binding.tvTitle.setText(getString(R.string.payment_summary));
       binding.btnCollect.setOnClickListener(v -> {
           binding.rlFeedback.setVisibility(View.VISIBLE);
       });
        binding.btnRate.setOnClickListener(v -> {
            FinishRide();
        });
        GetFare();
    }



    private void GetFare(){
        HashMap<String,String> param=new HashMap<>();
        param.put("request_id",result.getId());
        ApiCallBuilder.build(this).setUrl(BaseClass.get().getPayment())
                .setParam(param).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                Log.e("Payment",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("status").equals("1")){
                        Type listType = new TypeToken<ModelPayment>(){}.getType();
                         payment = new GsonBuilder().create().fromJson(object.getJSONArray("result").getJSONObject(0).toString(), listType);
                        binding.setPayment(payment);
                        binding.executePendingBindings();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void Failed(String error) {

            }
        });
    }
    private void FinishRide(){
        HashMap<String,String> params=new HashMap<>();
        params.put("request_id",result.getId());
        params.put("amount", payment.getTotal());
        params.put("car_charge", payment.getCarCharge());
        params.put("rating", ""+binding.ratingBar.getRating());
        params.put("discount", ""+payment.getDiscount());
        params.put("tip", "0");
        params.put("timezone", Tools.get().getTimeZone());
        params.put("review", ""+binding.editText.getText().toString());
        ApiCallBuilder.build(this).setUrl(BaseClass.get().add_payment())
                .isShowProgressBar(true)
                .setParam(params).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                Log.e("Payment",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("status").equals("1")){
                        startActivity(new Intent(PaymentSummary.this,HomeAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void Failed(String error) {

            }
        });
    }
}
