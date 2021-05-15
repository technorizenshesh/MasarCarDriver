package com.masarcardriver.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.Dialogs.DialogMessage;
import com.masarcardriver.R;
import com.masarcardriver.Utils.MyUtils;
import com.masarcardriver.adapter.AdapterDrawer;
import com.masarcardriver.databinding.ActivityHomeBinding;
import com.masarcardriver.dialog.NewRequestDialog;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.Config;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.GPSTracker;
import com.masarcardriver.helper.MyService;
import com.masarcardriver.helper.NetworkReceiver;
import com.masarcardriver.listener.menuListener;
import com.masarcardriver.listener.onRequestListener;
import com.masarcardriver.model.MenuModel;
import com.masarcardriver.model.ModelCurrentBooking;
import com.masarcardriver.model.ModelCurrentBookingResult;
import com.masarcardriver.model.SignupModel;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.ModelUser;
import com.utils.Session.SessionManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;
import www.develpoeramit.mapicall.ProgressStyle;

public class HomeAct extends AppCompatActivity implements OnMapReadyCallback, menuListener, NetworkReceiver.ConnectivityReceiverListener, onRequestListener {
    public static String TAG = "HomeAct";
    ActivityHomeBinding binding;
    ArrayList<MenuModel> arrayList;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    int PERMISSION_ID = 44;
    DriverInterface apiInterface;
    NetworkReceiver receiver;
    private Location mLocation;
    BroadcastReceiver JobStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("object") != null) {
            }else if (intent.getAction().equals("data_update_location")) {
                double lat=intent.getDoubleExtra("latitude",0);
                double lng=intent.getDoubleExtra("longitude",0);
                float bearing=intent.getFloatExtra("bearing",0);
                if (carMarker!=null){
                    Log.e("locationResult",""+bearing);
                    carMarker.position(new LatLng(lat,lng));
                    MyUtils.rotateMarker(carMarker,bearing);
                }
            }else {
                GetCurrentBooking();
            }
        }
    };
    private SessionManager session;
    private MarkerOptions carMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private NewRequestDialog requestDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        addDataList();
        initView();
    }

    private void initView() {
        setUserInfo();
        receiver = new NetworkReceiver();
       requestDialog= NewRequestDialog.get(this).Callback(this);
        App.getInstance().setConnectivityListener(HomeAct.this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLocation = location;
                        }
                    }
                });

        binding.chlidDashboard.navbar.setOnClickListener(v -> {
            navmenu();
        });
        binding.chlidDashboard.notification.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationAct.class));
        });
        binding.childNavDrawer.signout.setOnClickListener(v -> {
            DialogMessage.get(this)
                    .setMessage("Are you sure want to Logout?")
                    .Callback(() -> {
                        session.Logout();
                        startActivity(new Intent(this, LoginAct.class));
                        finish();
                    }).show();
        });

        binding.chlidDashboard.switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (NetworkReceiver.isConnected())
                        changeStatus("ONLINE");
                    else
                        App.showToast(HomeAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                } else {
                    if (NetworkReceiver.isConnected())
                        changeStatus("OFFLINE");
                    else
                        App.showToast(HomeAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                }
            }
        });
        createLocationRequest();
    }

        private void changeStatus(String status) {
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", session.getUserID());
            map.put("status", status);
            map.put("type", "DRIVER");
            ApiCallBuilder.build(this)
                    .setUrl(BaseClass.get().updateOnlineStatus())
                    .setParam(map).isShowProgressBar(true, ProgressStyle.STYLE_3)
                    .execute(new ApiCallBuilder.onResponse() {
                        @Override
                        public void Success(String response) {
                            Log.e("OnlineChangeresponse", response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getString("status").equals("1")) {
                                    session.setIsOnline(status.equals("ONLINE"));
                                    binding.chlidDashboard.switchOnOff.setText(session.IsOnline() ? "ONLINE" : "OFFLINE");
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

    public void setUserInfo() {
        session = SessionManager.get(this);
        ModelUser user = session.getValue();
        binding.childNavDrawer.setDriver(user);
        binding.chlidDashboard.setDriver(user);
        binding.chlidDashboard.switchOnOff.setChecked(session.IsOnline());
    }


    private void addDataList() {
        arrayList = new ArrayList<>();
        arrayList.add(new MenuModel(getString(R.string.home), 1));
        arrayList.add(new MenuModel(getString(R.string.my_profile), 2));
        arrayList.add(new MenuModel(getString(R.string.manage_document), 3));
        arrayList.add(new MenuModel(getString(R.string.manage_vehicles), 4));
        arrayList.add(new MenuModel(getString(R.string.your_trips), 5));
//        arrayList.add(new MenuModel(getString(R.string.bank_details), 6));
//        arrayList.add(new MenuModel(getString(R.string.payment), 7));
        arrayList.add(new MenuModel(getString(R.string.my_wallet), 8));
        arrayList.add(new MenuModel(getString(R.string.heat_view), 9));
        arrayList.add(new MenuModel(getString(R.string.emergency_contact), 10));
        arrayList.add(new MenuModel(getString(R.string.rider_feedback), 11));
//        arrayList.add(new MenuModel(getString(R.string.trip_statistics), 12));
        arrayList.add(new MenuModel(getString(R.string.need_help), 13));
        binding.childNavDrawer.rvDrawer.setAdapter(new AdapterDrawer(HomeAct.this, arrayList, this));

    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());

    }

    public void navmenu() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
         mMap.clear();
         carMarker= new MarkerOptions().position(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).title("My Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top));
        mMap.addMarker(carMarker);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))));
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(JobStatusReceiver, new IntentFilter("Job_Status_Action"));
        ContextCompat.startForegroundService(getApplicationContext(),new Intent(getApplicationContext(), MyService.class));
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                setCurrentLoc();
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPermissions();
        }

        registerReceiver(receiver, App.intentFilter);
        if (NetworkReceiver.isConnected()) {
            App.showSnack(this, findViewById(R.id.drawer_layout), true);
        } else {
            App.showSnack(this, findViewById(R.id.drawer_layout), false);
        }
        GetCurrentBooking();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        App.showSnack(this, findViewById(R.id.drawer_layout), isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        App.showSnack(this, findViewById(R.id.drawer_layout), true);
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLoc();
            }
        }
    }

    private void setCurrentLoc() {
        gpsTracker = new GPSTracker(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void OnMenuClick(int position, int id) {
        if (arrayList.get(position).getId() == 1) {
           navmenu();
        } else if (arrayList.get(position).getId() == 2) {
            startActivity(new Intent(this,ProfileAct.class));
        } else if (arrayList.get(position).getId() == 3) {
            startActivity(new Intent(this,ManageDocumentAct.class));
        } else if (arrayList.get(position).getId() == 4) {
            startActivity(new Intent(this,ManageVehicleAct.class));
        } else if (arrayList.get(position).getId() == 5) {
            startActivity(new Intent(this,TripAct.class));
        } else if (arrayList.get(position).getId() == 6) {
            startActivity(new Intent(this,BankDetailAct.class).putExtra("type","1"));
        } else if (arrayList.get(position).getId() == 7) {
            startActivity(new Intent(this,BankDetailAct.class).putExtra("type","2"));
        } else if (arrayList.get(position).getId() == 8) {
            startActivity(new Intent(this,WalletAct.class));
        } else if (arrayList.get(position).getId() == 9) {
            startActivity(new Intent(this,HeatViewAct.class));
        } else if (arrayList.get(position).getId() == 10) {
            startActivity(new Intent(this,EmergencyAct.class));
        } else if (arrayList.get(position).getId() == 11) {
            startActivity(new Intent(this,RiderFeedbackAct.class));
        } else if (arrayList.get(position).getId() == 12) {
            startActivity(new Intent(this,TripStatisticsAct.class));
        } else if (arrayList.get(position).getId() == 13) {
            startActivity(new Intent(this,NeedHelpAct.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(JobStatusReceiver);
        stopService(new Intent(this, MyService.class));
    }

  LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (carMarker!=null){
                    carMarker.position(new LatLng(location.getLatitude(),location.getLongitude()));
                    carMarker.rotation(location.getBearing());
                }
            }
        }
    };
    private void GetCurrentBooking(){
        Log.e("Start","======================>");
        HashMap<String,String>param=new HashMap<>();
        param.put("user_id", session.getUserID());
        param.put("type", "DRIVER");
        ApiCallBuilder.build(this).setUrl(BaseClass.get().getCurrentBooking())
                .setParam(param).execute(new ApiCallBuilder.onResponse() {
            @Override
            public void Success(String response) {
                Log.e("BookingResponse",response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("status").equals("1")){
                        Type listType = new TypeToken<ModelCurrentBooking>(){}.getType();
                        ModelCurrentBooking data = new GsonBuilder().create().fromJson(response, listType);
                        if (data.getStatus().equals("1")) {
                            Log.e("BookingStatus",""+data.getResult().size());
                            ModelCurrentBookingResult result=data.getResult().get(0);
                            if (!requestDialog.isShowing()&&result.getStatus().equalsIgnoreCase("Pending")) {
                                requestDialog.setData(data).show();
                            }else if (result.getStatus().equalsIgnoreCase("Accept")) {
                                Intent k = new Intent(HomeAct.this, TrackAct.class);
                                k.putExtra("data",data);
                                startActivity(k);
                            } else if (result.getStatus().equalsIgnoreCase("Arrived")) {
                                Intent j = new Intent(HomeAct.this, TrackAct.class);
                                j.putExtra("data",data);
                                startActivity(j);
                            } else if (result.getStatus().equalsIgnoreCase("Start")) {
                                Intent j = new Intent(HomeAct.this, TrackAct.class);
                                j.putExtra("data",data);
                                startActivity(j);
                            } else if (result.getStatus().equalsIgnoreCase("End")) {
                                Intent j = new Intent(HomeAct.this, PaymentSummary.class);
                                j.putExtra("data",data);
                                startActivity(j);
                            }
                            session.setLastRequestStatus(result.getStatus());
                        }
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

    @Override
    public void onRequestAccept() {
        GetCurrentBooking();
    }

    @Override
    public void onRequestCancel() {
        DialogMessage.get(this).setMessage("Request successfully canceled By You").show();
    }
}
