package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityAddBankBinding;
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

public class AddBankAct extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener{
    public static String TAG = "AddBankAct";
    ActivityAddBankBinding binding;
    DriverInterface apiInterface;
    NetworkReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_bank);
        initView();
    }

    private void initView() {
        receiver = new NetworkReceiver();
        App.getInstance().setConnectivityListener(AddBankAct.this);

        binding.header.tvTitle.setText(getString(R.string.add_bank_account));
        binding.header.ivBack.setVisibility(View.GONE);

        binding.btnAddAccount.setOnClickListener(v -> {validation(); });
    }

    private void validation() {
        if(binding.etHolderName.getText().toString().equals("")){
           binding.etHolderName.setError(getString(R.string.enter_account_holder_name));
           binding.etHolderName.setFocusable(true);
        }
       else if(binding.etBankName.getText().toString().equals("")){
            binding.etBankName.setError(getString(R.string.enter_account_bank_name));
            binding.etBankName.setFocusable(true);
        }
        else if(binding.etBIC.getText().toString().equals("")){
            binding.etBIC.setError(getString(R.string.enter_bic));
            binding.etBIC.setFocusable(true);
        }

        else if(binding.etIBAN.getText().toString().equals("")){
            binding.etIBAN.setError(getString(R.string.enter_iban));
            binding.etIBAN.setFocusable(true);
        }

        else if(binding.etAccountNumber.getText().toString().equals("")){
            binding.etAccountNumber.setError(getString(R.string.enter_account_number));
            binding.etAccountNumber.setFocusable(true);
        }
        else {
            if (NetworkReceiver.isConnected())
                AddBank();
            else
                App.showToast(AddBankAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
        }
    }

    public void AddBank(){
        DataManager.getInstance().showProgressMessage(AddBankAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("acc_holder_name",binding.etAccountNumber.getText().toString());
        map.put("bank_name",binding.etBankName.getText().toString());
        map.put("code_bic",binding.etBIC.getText().toString());
        map.put("iban",binding.etIBAN.getText().toString());
        map.put("account_number",binding.etAccountNumber.getText().toString());
        map.put("user_id", SessionManager.get(this).getUserID());
        Log.e(TAG,"Add Bank Request ;"+map);
        Call<SignupModel> addBankCall = apiInterface.addBank(map);
        addBankCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String dataString = new Gson().toJson(response.body());
                    Log.e(TAG, "Add Bank RESPONSE" + dataString);
                    if(data.status.equals("1")){
                        App.showToast(AddBankAct.this, data.message, Toast.LENGTH_SHORT);
                        startActivity(new Intent(AddBankAct.this,LoginAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                    else if(data.status.equals("0")){
                        App.showToast(AddBankAct.this, data.message, Toast.LENGTH_SHORT);
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
            App.showSnack(this, findViewById(R.id.addBank), true);
        } else {
            App.showSnack(this, findViewById(R.id.addBank), false);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.addBank), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.addBank), true);
    }

}
