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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.masarcardriver.BuildConfig;
import com.masarcardriver.R;
import com.masarcardriver.adapter.AdapterBrand;
import com.masarcardriver.adapter.AdapterModel;
import com.masarcardriver.adapter.AdapterType;
import com.masarcardriver.databinding.ActivityAddVehicleBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.NetworkReceiver;
import com.masarcardriver.model.BrandListModel;
import com.masarcardriver.model.CarListModel;
import com.masarcardriver.model.ModListModel;
import com.masarcardriver.model.SignupModel;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.Session.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleAct extends AppCompatActivity implements NetworkReceiver.ConnectivityReceiverListener {
    public static String TAG = "AddVehicleAct";
    ActivityAddVehicleBinding binding;
    DriverInterface apiInterface;
    NetworkReceiver receiver;
    String car_id = "", brand_id = "", model_id = "";
    ArrayList<CarListModel.Result> carArrayList;
    ArrayList<BrandListModel.Result> brandArrayList;
    ArrayList<ModListModel.Result> modArrayList;
    String str_image_path = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private Uri uriSavedImage;
    private String type="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehicle);
        if (getIntent().getExtras()!=null){
            type=getIntent().getExtras().getString("type");
        }
        initView();
        getCarList();
    }

    private void getCarList() {
        Call<CarListModel> signupCall = apiInterface.getCarList();
        signupCall.enqueue(new Callback<CarListModel>() {
            @Override
            public void onResponse(Call<CarListModel> call, Response<CarListModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    CarListModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Car Type Response :" + responseString);
                    if (data.status.equals("1")) {
                        carArrayList.clear();
                        carArrayList.addAll(data.result);
                        binding.spinnerServiceType.setAdapter(new AdapterType(AddVehicleAct.this, carArrayList));
                        getCardBand(carArrayList.get(0).id);
                    } else if (data.status.equals("0")) {
                        App.showToast(AddVehicleAct.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CarListModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });
    }

    private void getCardBand(String carBand) {
        Map<String, String> map = new HashMap<>();
        map.put("car_type_id", carBand);
        Log.e(TAG, "Car Brand Request :" + carBand);
        Call<BrandListModel> signupCall = apiInterface.cardBrandList(map);
        signupCall.enqueue(new Callback<BrandListModel>() {
            @Override
            public void onResponse(Call<BrandListModel> call, Response<BrandListModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    BrandListModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Car Brand Response :" + responseString);
                    if (data.status.equals("1")) {
                        brandArrayList.clear();
                        brandArrayList.addAll(data.result);
                        binding.spinnerBrand.setAdapter(new AdapterBrand(AddVehicleAct.this, brandArrayList));
                        getModel(brandArrayList.get(0).getId());
                    } else if (data.status.equals("0")) {
                        App.showToast(AddVehicleAct.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BrandListModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }

    private void getModel(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("brand_id", id);
        Call<ModListModel> signupCall = apiInterface.modelList(map);
        signupCall.enqueue(new Callback<ModListModel>() {
            @Override
            public void onResponse(Call<ModListModel> call, Response<ModListModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ModListModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Car Model Response :" + responseString);
                    if (data.status.equals("1")) {
                        modArrayList.clear();
                        modArrayList.addAll(data.result);
                        binding.spinnerModel.setAdapter(new AdapterModel(AddVehicleAct.this, modArrayList));
                    } else if (data.status.equals("0")) {
                        App.showToast(AddVehicleAct.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ModListModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }

    private void initView() {
        receiver = new NetworkReceiver();
        App.getInstance().setConnectivityListener(AddVehicleAct.this);
        carArrayList = new ArrayList<>();
        brandArrayList = new ArrayList<>();
        modArrayList = new ArrayList<>();
        binding.header.tvTitle.setText(getString(R.string.add_vehicle));
        binding.header.ivBack.setVisibility(View.GONE);

        binding.btnNext.setOnClickListener(v -> {validation(); });
        binding.btnNext.setText(type.equals("1")?"Add Vehicle":"Next");
        binding.rlImg.setOnClickListener(v -> {
            if (checkPermisssionForReadStorage())
                showImageSelection();
        });


        binding.spinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car_id = carArrayList.get(position).id;
                getCardBand(car_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brand_id = brandArrayList.get(position).getId();
                getModel(brand_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                model_id = modArrayList.get(position).id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void showImageSelection() {
        final Dialog dialog = new Dialog(AddVehicleAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
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

        uriSavedImage = FileProvider.getUriForFile(AddVehicleAct.this,
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
            binding.rlPickImg.setVisibility(View.GONE);
            binding.ivImg.setVisibility(View.VISIBLE);
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(AddVehicleAct.this, data.getData());
                Glide.with(AddVehicleAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivImg);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(AddVehicleAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivImg);


            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(AddVehicleAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(AddVehicleAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(AddVehicleAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddVehicleAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(AddVehicleAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(AddVehicleAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(AddVehicleAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(AddVehicleAct.this,
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
                        Toast.makeText(AddVehicleAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddVehicleAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }


    public void validation() {
        if (str_image_path.equals("")) {
            App.showToast(AddVehicleAct.this,getString(R.string.please_upload_vehicle_image),Toast.LENGTH_SHORT);
        }
        else if (car_id.equals(""))
            App.showToast(AddVehicleAct.this, getString(R.string.please_select_service_type), Toast.LENGTH_SHORT);

        else if (brand_id.equals(""))
            App.showToast(AddVehicleAct.this, getString(R.string.please_select_brand), Toast.LENGTH_SHORT);

        else if (model_id.equals(""))
            App.showToast(AddVehicleAct.this, getString(R.string.please_select_model), Toast.LENGTH_SHORT);

        else if (binding.etYear.getText().toString().equals("")) {
            binding.etYear.setError(getString(R.string.please_enter_year));
            binding.etYear.requestFocus();
        } else if (binding.etNumberPlate.getText().toString().equals("")) {
            binding.etNumberPlate.setError(getString(R.string.please_enter_vehicle_number));
            binding.etNumberPlate.requestFocus();
        } else if (binding.etColor.getText().toString().equals("")) {
            binding.etColor.setError(getString(R.string.please_enter_vehicle_color));
            binding.etColor.requestFocus();
        } else {
            if (NetworkReceiver.isConnected())
                AddVehicle();
            else
                App.showToast(AddVehicleAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
        }


    }


    public void AddVehicle() {
        DataManager.getInstance().showProgressMessage(AddVehicleAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("vehicle_image", file.getName(), RequestBody.create(MediaType.parse("car_document/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("vehicle_image", "", attachmentEmpty);
        }
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SessionManager.get(this).getUserID());
        RequestBody car_type_id = RequestBody.create(MediaType.parse("text/plain"), "0.0");
        RequestBody brand = RequestBody.create(MediaType.parse("text/plain"), "0.0");
        RequestBody car_model = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody year_of_manufacture = RequestBody.create(MediaType.parse("text/plain"), binding.etYear.getText().toString());
        RequestBody car_number = RequestBody.create(MediaType.parse("text/plain"), binding.etNumberPlate.getText().toString());
        RequestBody car_color = RequestBody.create(MediaType.parse("text/plain"), binding.etColor.getText().toString());

        Call<SignupModel> signupCall = apiInterface.addVehicle(user_id, car_type_id, brand, car_model, year_of_manufacture,
                car_number, car_color, filePart);
        signupCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                Log.e(TAG, "RESPONSE" + response);
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    if (data.status.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e(TAG, "ADD VEHICLE RESPONSE" + dataResponse);
                        App.showToast(AddVehicleAct.this, data.message, Toast.LENGTH_SHORT);
                        if (type.equals("0")) {
                            startActivity(new Intent(AddVehicleAct.this, AddBankAct.class));
                        }
                        finish();
                    } else if (data.status.equals("0")) {
                        App.showToast(AddVehicleAct.this, data.message, Toast.LENGTH_SHORT);
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
            App.showSnack(this, findViewById(R.id.addVehicle), true);
        } else {
            App.showSnack(this, findViewById(R.id.addVehicle), false);
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.v("isConnected", "isConnected=$isConnected");
        App.showSnack(this, findViewById(R.id.addVehicle), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.addVehicle), true);
    }
}
