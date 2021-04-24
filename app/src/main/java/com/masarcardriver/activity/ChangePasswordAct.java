package com.masarcardriver.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityChangePassBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.NetworkReceiver;
import com.masarcardriver.model.SignupModel;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.Session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordAct extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener {
    ActivityChangePassBinding binding;
    DriverInterface apiInterface;
    NetworkReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);
        initView();
    }

    private void initView() {
        receiver = new NetworkReceiver();
        App.getInstance().setConnectivityListener(ChangePasswordAct.this);

        binding.header.tvTitle.setText(getString(R.string.change_password));

        binding.header.ivBack.setOnClickListener(v -> {finish();});

        binding.btnSubmit.setOnClickListener(v -> {
            validation();
        });
    }


    private void validation() {
        if (binding.edOldPassword.getText().toString().equals("")) {
            binding.edOldPassword.setError(getString(R.string.please_enter_pass));
            binding.edOldPassword.setFocusable(true);

        } else if (binding.edNewPassword.getText().toString().equals("")) {
            binding.edNewPassword.setError(getString(R.string.please_enter_pass));
            binding.edNewPassword.setFocusable(true);

        } else if (!binding.edConfirmPass.getText().toString().equals(binding.edNewPassword.getText().toString())) {
            binding.edConfirmPass.setError(getString(R.string.password_cant_matched));
            binding.edConfirmPass.setFocusable(true);
        } else {
            if (NetworkReceiver.isConnected()) ChangePassword();
            else
                App.showToast(ChangePasswordAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);

        }

    }

    private void ChangePassword() {
        DataManager.getInstance().showProgressMessage(ChangePasswordAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SessionManager.get(this).getUserID());
        map.put("old_password",binding.edOldPassword.getText().toString());
        map.put("password", binding.edNewPassword.getText().toString());
        Log.e("MapMap", "CHANGE PASS REQUEST" + map);
        Call<SignupModel> loginCall = apiInterface.changePassword( map);
        loginCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "CHANGE PASS RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        App.showToast(ChangePasswordAct.this,data.message,Toast.LENGTH_SHORT);
                        finish();
                    } else if (data.status.equals("0")) {
                        App.showToast(ChangePasswordAct.this,data.message,Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, App.intentFilter);
        if (NetworkReceiver.isConnected()) {
            App.showSnack(this, findViewById(R.id.changePass), true);
        } else {
            App.showSnack(this, findViewById(R.id.changePass), false);
        }
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.changePass), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.changePass), true);
    }

}
