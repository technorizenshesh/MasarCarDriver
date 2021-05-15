package com.masarcardriver.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.masarcardriver.Constent.BaseClass;
import com.masarcardriver.R;
import com.masarcardriver.activity.TrackAct;
import com.masarcardriver.databinding.DialogNewRequestBinding;
import com.masarcardriver.helper.App;
import com.masarcardriver.helper.DataManager;
import com.masarcardriver.listener.onRequestListener;
import com.masarcardriver.model.BookingDetailModel;
import com.masarcardriver.model.ModelCurrentBooking;
import com.masarcardriver.model.ModelCurrentBookingResult;
import com.masarcardriver.retrofit.ApiClient;
import com.masarcardriver.retrofit.DriverInterface;
import com.utils.Session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.develpoeramit.mapicall.ApiCallBuilder;

public class NewRequestDialog extends Dialog {
    public static String TAG = "NewRequestDialog";
    private ModelCurrentBooking booking;
    private long timeCountInMilliSeconds = 2 * 60000;
    private CountDownTimer countDownTimer;
    DialogNewRequestBinding binding;
    private Ringtone ring;
    private onRequestListener listener;

    public NewRequestDialog(@NonNull Context context) {
        super(context);
    }

    public static NewRequestDialog get(Context context) {
        return new NewRequestDialog(context);
    }
    public NewRequestDialog setData(ModelCurrentBooking booking){
        this.booking=booking;
        return this;
    }
    public NewRequestDialog Callback(onRequestListener listener){
        this.listener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_new_request, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        BindData();
    }

    private void BindData() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ring = RingtoneManager.getRingtone(getContext(), notification);
        ring.setLooping(true);
        ring.play();
        ModelCurrentBookingResult result = booking.getResult().get(0);
        binding.tvPickupLoc.setText(result.getPicuplocation());
        binding.tvDestinationLoc.setText(result.getDropofflocation());
        binding.tvFare.setText("$"+booking.getFare());
        binding.tvMinuts.setText(booking.getEstimatedTime());
        binding.tvText.setText(result.getUserDetails().get(0).getFirstName());
        binding.tvAccept.setOnClickListener(v -> {
            AcceptCancel( "Accept");
        });
        binding.tvRefuse.setOnClickListener(v -> {
            AcceptCancel( "Cancel");
        });
        startTime();
    }

    private void startTime() {
        setProgressBarValues();
        startCountDownTimer();
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                binding.progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                stopCountDownTimer();
            }

        }.start();
    }


    private void stopCountDownTimer() {
        countDownTimer.cancel();
        ring.stop();
        dismiss();
    }

    private void setProgressBarValues() {
        binding.progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        binding.progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    private String hmsTimeFormatter(long milliSeconds) {
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;
    }

    public void AcceptCancel(String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("driver_id", SessionManager.get(getContext()).getUserID());
        map.put("request_id", booking.getResult().get(0).getId());
        map.put("status", status);
        ApiCallBuilder.build(getContext())
                .setUrl(BaseClass.get().AcceptCancelRequest())
                .setParam(map)
                .isShowProgressBar(true)
                .execute(new ApiCallBuilder.onResponse() {
                    @Override
                    public void Success(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            if (object.getString("status").equals("1")){
                                SessionManager.get(getContext()).setLastRequestStatus(status);
                                if (status.equals("Accept")){
                                    listener.onRequestAccept();
                                }else {
                                    listener.onRequestCancel();
                                }
                                stopCountDownTimer();
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

