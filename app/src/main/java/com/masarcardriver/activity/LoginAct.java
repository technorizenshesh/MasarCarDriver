package com.masarcardriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.Dialogs.DialogMessage;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityLoginBinding;
import com.masarcardriver.helper.GPSTracker;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import www.develpoeramit.mapicall.ApiCallBuilder;
public class LoginAct extends AppCompatActivity {
    ActivityLoginBinding binding;
    private GPSTracker gps;
    private String login_sts="yes";
    private SessionManager session;
    private String FireBaseID="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        gps= new GPSTracker(this);
        session= SessionManager.get(this);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FireBaseID=s;
                Log.e("FireBaseID",""+FireBaseID);
            }
        });
        binding.btnLogin.setOnClickListener(v -> {
          if (Validation()){
              Continue();
          }
        });
        binding.rlLinkSignup.setOnClickListener(v -> {startActivity(new Intent(this,RegisterAct.class));});

        binding.rlForgotPass.setOnClickListener(v -> {startActivity(new Intent(this,ForgotPasswordAct.class));});
    }
    private HashMap<String,String>getParam(){
        HashMap<String,String>param=new HashMap<>();
        param.put("email",binding.etEmail.getText().toString());
        param.put("password",binding.etPass.getText().toString());
        param.put("lat",""+gps.getLatitude());
        param.put("lon",""+gps.getLongitude());
        param.put("register_id",""+FireBaseID);
        param.put("ios_register_id","");
        param.put("type","DRIVER");
        param.put("continue",login_sts);
        return param;
    }
    private void Continue(){
        ApiCallBuilder.build(this)
                .setUrl(BaseClass.get().Login())
                .isShowProgressBar(true)
                .setParam(getParam())
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            if (object.getString("status").equals("1")){
                                session.CreateSession(object.getString("result"));
                                startActivity(new Intent(LoginAct.this,HomeAct.class));
                                finish();
                            } else if (object.getString("result").equalsIgnoreCase("user already logged in")) {
                                login_sts = "yes";
                                DialogMessage.get(LoginAct.this).setMessage(getResources().getString(R.string.alreadylog)).Callback(()->Continue()).show();
                            } else {
                                login_sts = "no";
                                Toast.makeText(LoginAct.this, getResources().getString(R.string.invalidcredential), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failed(String error) {
                        login_sts = "no";
                    }
                });
    }
    private boolean Validation(){
        if (binding.etEmail.getText().toString().isEmpty()){
            binding.etEmail.setError("Required");
            binding.etEmail.requestFocus();
            return false;
        }  if (!Tools.isValidEmail(binding.etEmail.getText().toString())){
            binding.etEmail.setError("Email not valid");
            binding.etEmail.requestFocus();
            return false;
        }if (binding.etPass.getText().toString().isEmpty()){
            binding.etPass.setError("Required");
            binding.etPass.requestFocus();
            return false;
        }
        return true;
    }

}
