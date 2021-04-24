package com.masarcardriver.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityForgotPassBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.NetworkReceiver;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.masarcardriver.retrofit.Constant.emailPattern;


public class ForgotPasswordAct extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener {
    public static String TAG = "ForgotPasswordAct";
    ActivityForgotPassBinding binding;
    DriverInterface apiInterface;
    NetworkReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_pass);
        initView();
    }

    private void initView() {
        receiver = new NetworkReceiver();
        App.getInstance().setConnectivityListener(ForgotPasswordAct.this);

        binding.header.tvTitle.setText(getString(R.string.forgot_password));
        binding.header.ivBack.setOnClickListener(v -> {validation();});


        binding.btnSend.setOnClickListener(v -> {finish();});
    }


    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else {
            if(NetworkReceiver.isConnected()) forGotPass(); else App.showToast(ForgotPasswordAct.this,getString(R.string.network_failure), Toast.LENGTH_SHORT);
        }
    }


    private void forGotPass() {
        DataManager.getInstance().showProgressMessage(ForgotPasswordAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",binding.etEmail.getText().toString());
        Log.e(TAG,"ForgotPass Request :"+map.toString());
        Call<Map<String,String>> signupCall = apiInterface.forgotPass(map);
        signupCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"ForgotPass Response :"+responseString);
                    if(data.get("status").equals("1")){
                        App.showToast(ForgotPasswordAct.this,"",Toast.LENGTH_SHORT);
                        finish(); }
                    else if(data.get("status").equals("0")){
                        App.showToast(ForgotPasswordAct.this,data.get("result"),Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, App.intentFilter);
        if (NetworkReceiver.isConnected()) {
            App.showSnack(this, findViewById(R.id.forgotPass), true);
        } else {
            App.showSnack(this, findViewById(R.id.forgotPass), false);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.forgotPass), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.forgotPass), true);
    }

}
