package com.masarcardriver.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.masarcardriver.BuildConfig;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.Dialogs.DialogMessage;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityRegisterBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.GPSTracker;
import com.masarcardriver.helper.NetworkReceiver;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import www.develpoeramit.mapicall.ApiCallBuilder;
import www.develpoeramit.mapicall.Method;

import static com.masarcardriver.retrofit.Constant.emailPattern;

public class RegisterAct extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener {
    ActivityRegisterBinding binding;
    NetworkReceiver receiver;
    String str_image_path="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    DriverInterface apiInterface;
    private GPSTracker gps;
    private String FireBaseID="";
    private SessionManager session;
    private String login_sts="yes";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        receiver = new NetworkReceiver();
        session= SessionManager.get(this);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                FireBaseID=s;
            }
        });
        gps=new GPSTracker(this);
        App.getInstance().setConnectivityListener(RegisterAct.this);
        binding.header.tvTitle.setText(getString(R.string.register));
        binding.header.ivBack.setOnClickListener(v -> {finish();});
        binding.btnRegister.setOnClickListener(v -> { validation(); });
        binding.ivUser.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });
    }


    public void showImageSelection() {
        final Dialog dialog = new Dialog(RegisterAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera() {
        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/TropikVTCDriver/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/TropikVTCDriver/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(RegisterAct.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(RegisterAct.this, data.getData());
                Glide.with(RegisterAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivUser);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(RegisterAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivUser);


            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(RegisterAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(RegisterAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(RegisterAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(RegisterAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(RegisterAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(RegisterAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(RegisterAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(RegisterAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }

    private void validation() {
        if (binding.etSurName.getText().toString().equals("")) {
            binding.etSurName.setError(getString(R.string.please_enter_surname));
            binding.etSurName.setFocusable(true);
        } else if (binding.etName.getText().toString().equals("")) {
            binding.etName.setError(getString(R.string.please_enter_name));
            binding.etName.setFocusable(true);
        } else if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        } else if (binding.etMobile.getText().toString().equals("")) {
            binding.etMobile.setError(getString(R.string.enter_phone_number1));
            binding.etMobile.setFocusable(true);
        } else if (binding.etMobile.getText().toString().length() < 10) {
            binding.etMobile.setError(getString(R.string.enter_valid_number));
            binding.etMobile.setFocusable(true);
        } else if (binding.etPass.getText().toString().equals("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            binding.etPass.setFocusable(true);
        }
        else if(!binding.chkPolicy.isChecked()) {
            App.showToast(RegisterAct.this,"Please accept Terms & Conditions and Privacy Policys",Toast.LENGTH_SHORT);
        }
        else {
            if (NetworkReceiver.isConnected()) {
              Continue();
            } else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void Continue(){
        ApiCallBuilder.build(this)
                .setMethod(Method.POST)
                .setUrl(BaseClass.get().SignUp())
                .isShowProgressBar(true)
                .setParam(getParam())
                .setFile("image",str_image_path)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            if (object.getString("status").equals("1")){
                                session.CreateSession(object.getString("result"));
                                startActivity(new Intent(RegisterAct.this,AddVehicleAct.class));
                                finish();
                            } else if (object.getString("result").equalsIgnoreCase("user already logged in")) {
                                login_sts = "yes";
                                DialogMessage.get(RegisterAct.this).setMessage(getResources().getString(R.string.alreadylog)).Callback(()->Continue()).show();
                            } else {
                                login_sts = "no";
                                Toast.makeText(RegisterAct.this, getResources().getString(R.string.invalidcredential), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, App.intentFilter);
        if (NetworkReceiver.isConnected()) {
            App.showSnack(this, findViewById(R.id.register), true);
        } else {
            App.showSnack(this, findViewById(R.id.register), false);
        }
    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.register), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.register), true);
    }
    private HashMap<String,String>getParam(){
        HashMap<String,String>param=new HashMap<>();
        param.put("first_name",binding.etName.getText().toString());
        param.put("last_name",binding.etSurName.getText().toString());
        param.put("email",binding.etEmail.getText().toString());
        param.put("mobile",binding.etMobile.getText().toString());
        param.put("phone_code",binding.ccp.getSelectedCountryCode());
        param.put("password",binding.etPass.getText().toString());
        param.put("lat",""+gps.getLatitude());
        param.put("lon",""+gps.getLongitude());
        param.put("address",""+gps.getAddressLine(this));
        param.put("type","DRIVER");
        param.put("register_id",FireBaseID);
        param.put("continue",login_sts);
        return param;
    }

}
