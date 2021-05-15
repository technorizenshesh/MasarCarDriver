package com.masarcardriver.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.Constent.DrawPollyLine;
import com.masarcardriver.Dialogs.DialogMessage;
import com.masarcardriver.R;
import com.masarcardriver.databinding.ActivityTrackBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.helper.GPSTracker;
import com.masarcardriver.maps.CallPolyLineMap;
import com.masarcardriver.maps.DownloadTask;
import com.masarcardriver.maps.LatLngInterpolator;
import com.masarcardriver.maps.MarkerAnimation;
import com.masarcardriver.model.BookingDetailModel;
import com.masarcardriver.model.ModelCurrentBooking;
import com.masarcardriver.model.ModelCurrentBookingResult;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.Session.SessionManager;
import com.utils.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class TrackAct extends AppCompatActivity implements OnMapReadyCallback {
    public static String TAG = "TrackAct";
    ActivityTrackBinding binding;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    int PERMISSION_ID = 44;
    Vibrator vibrator;
    LatLng location, dlocation, preLocation, preDlocation, bookingLocation;
    private Marker currentLocationMarker, dcurrentLocationMarker;
    ArrayList<Polyline> polylines;
    Runnable runnable;
    String url = null;
    DriverInterface apiInterface;
    private ModelCurrentBooking data;
    private ModelCurrentBookingResult result;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(DriverInterface.class);
        polylines = new ArrayList<>();
        session=SessionManager.get(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getIntent() != null) {
            data = (ModelCurrentBooking) getIntent().getSerializableExtra("data");
            result = data.getResult().get(0);
           BindData();
        }
        initView();
    }

    private void BindData() {
        location = new LatLng(Double.parseDouble(result.getPicuplat()), Double.parseDouble(result.getPickuplon()));
        dlocation = new LatLng(Double.parseDouble(result.getDroplat()), Double.parseDouble(result.getDroplon()));
        binding.setBooking(result);
        if (result.getStatus().equalsIgnoreCase("Arrived")) {
            binding.btnArrived.setVisibility(View.GONE);
            binding.btnBegin.setVisibility(View.VISIBLE);
        } else if (result.getStatus().equalsIgnoreCase("Start")) {
            binding.btnArrived.setVisibility(View.GONE);
            binding.btnBegin.setVisibility(View.GONE);
            binding.btnEnd.setVisibility(View.VISIBLE);
        } else if (result.getStatus().equalsIgnoreCase("End")) {
            startActivity(new Intent(TrackAct.this, PaymentSummary.class).putExtra("data", data));
            finish();
        } else if (result.getStatus().equalsIgnoreCase("Cancel")) {
            finish();
        }
    }

    private void initView() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        binding.header.tvTitle.setText(getString(R.string.pick_up_passenger));
        binding.header.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.ivNavigate.setOnClickListener(v -> {
        });
        binding.btnArrived.setOnClickListener(v -> {
            DialogMessage.get(this)
                    .setMessage(getResources().getString(R.string.are_you_sure_you_have_arrived_at_pickup_location_of_passenger))
                    .Callback(() -> {
                        DriverChangeStatus("Arrived");
                    }).show();
        });
        binding.slideViewBegin.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                vibrator.vibrate(100);
                DialogMessage.get(TrackAct.this)
                        .setMessage(getResources().getString(R.string.tostarttrip))
                        .Callback(() -> {
                            DriverChangeStatus("Start");
                        }).show();

            }
        });
        binding.slideViewEnd.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                vibrator.vibrate(100);
                DialogMessage.get(TrackAct.this)
                        .setMessage(getResources().getString(R.string.toendtrip))
                        .Callback(() -> {
                            DriverChangeStatus("End");
                        }).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpsTracker = new GPSTracker(TrackAct.this);
        drawRoute();
    }


    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }


    private void showMarker(@NonNull LatLng currentLocation) {
        if (currentLocation != null) {
            mMap.clear();
            if (currentLocationMarker == null) {
                currentLocationMarker = mMap.addMarker(new MarkerOptions().position(location).title("User")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_top)));
                animateCamera(currentLocation);
            } else {
                if (preLocation != location) {
                    MarkerAnimation.animateMarkerToGB(currentLocationMarker, currentLocation, new LatLngInterpolator.Spherical());
                }
            }
        }
    }

    private void showDestinationMarker(@NonNull LatLng dcurrentLocation) {
        Log.e("TAG", "showDestinationMarker: " + dcurrentLocation);
        if (dcurrentLocation != null) {
            if (dcurrentLocationMarker == null) {
                dcurrentLocationMarker = mMap.addMarker(new MarkerOptions().position(dlocation).title("Driver")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));
            } else {
                if (preDlocation != dlocation) {
                    MarkerAnimation.animateMarkerToGB(dcurrentLocationMarker, dcurrentLocation, new LatLngInterpolator.Spherical());
                }
            }
        }
    }


    private void animateCamera(@NonNull LatLng location) {
        //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(location)));
    }

    private void drawRoute() {
        showMarker(location);
        showDestinationMarker(dlocation);
        DrawPollyLine.get(this)
                .setOrigin(location)
                .setDestination(dlocation)
                .execute(new DrawPollyLine.onPolyLineResponse() {
                    @Override
                    public void Success(ArrayList<LatLng> latLngs) {
                        PolylineOptions options = new PolylineOptions();
                        options.addAll(latLngs);
                        options.color(Color.BLUE);
                        options.width(10);
                        options.startCap(new SquareCap());
                        options.endCap(new SquareCap());

                        Polyline line = mMap.addPolyline(options);


                    }
                });

    }

    protected void zoomMapInitial(LatLng finalPlace, LatLng currenLoc) {
        try {
            int padding = 200;
            LatLngBounds.Builder bc = new LatLngBounds.Builder();
            bc.include(finalPlace);
            bc.include(currenLoc);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), padding));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DriverChangeStatus(String status) {
        HashMap<String, String> params = new HashMap<>();
        params.put("request_id", result.getId());
        params.put("status", status);
        params.put("timezone", Tools.get().getTimeZone());
        params.put("driver_id", session.getUserID());
        params.put("droplat", "" + gpsTracker.getLatitude());
        params.put("droplon", "" + gpsTracker.getLongitude());
        ApiCallBuilder.build(this)
                .setUrl(BaseClass.get().AcceptCancelRequest())
                .setParam(params).isShowProgressBar(true)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("status").equals("1")) {
                                session.setLastRequestStatus(status);
                                if (status.equalsIgnoreCase("Arrived")) {
                                    binding.btnArrived.setVisibility(View.GONE);
                                    binding.btnBegin.setVisibility(View.VISIBLE);

                                } else if (status.equalsIgnoreCase("Start")) {
                                    binding.btnArrived.setVisibility(View.GONE);
                                    binding.btnBegin.setVisibility(View.GONE);
                                    binding.btnEnd.setVisibility(View.VISIBLE);
                                } else if (status.equalsIgnoreCase("End")) {
                                    startActivity(new Intent(TrackAct.this, PaymentSummary.class).putExtra("data", data));
                                    finish();
                                } else if (status.equalsIgnoreCase("Cancel")) {
                                    finish();
                                }
                            }else {
                                DialogMessage.get(TrackAct.this).setMessage("Something went wrong! try again").show();
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
