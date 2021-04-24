package com.masarcardriver.model;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.masarcardriver.R;

public class SignupModel {

    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;


    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("phone_code")
        @Expose
        public String phoneCode;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("cust_id")
        @Expose
        public Object custId;
        @SerializedName("card_id")
        @Expose
        public Object cardId;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("social_id")
        @Expose
        public Object socialId;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("car_id")
        @Expose
        public Object carId;
        @SerializedName("car_type_id")
        @Expose
        public Object carTypeId;
        @SerializedName("online_status")
        @Expose
        public String onlineStatus;
        @SerializedName("wallet")
        @Expose
        public String wallet;
        @SerializedName("verify_code")
        @Expose
        public Object verifyCode;
        @SerializedName("lang")
        @Expose
        public Object lang;
        @SerializedName("currency")
        @Expose
        public Object currency;
        @SerializedName("place")
        @Expose
        public Object place;
        @SerializedName("promo_code")
        @Expose
        public String promoCode;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("nxn_point")
        @Expose
        public String nxnPoint;
        @SerializedName("license")
        @Expose
        public Object license;
        @SerializedName("car_model")
        @Expose
        public Object carModel;
        @SerializedName("year_of_manufacture")
        @Expose
        public Object yearOfManufacture;
        @SerializedName("car_color")
        @Expose
        public Object carColor;
        @SerializedName("car_number")
        @Expose
        public String carNumber;
        @SerializedName("car_image")
        @Expose
        public Object carImage;
        @SerializedName("car_document")
        @Expose
        public Object carDocument;
        @SerializedName("insurance")
        @Expose
        public Object insurance;
        @SerializedName("document")
        @Expose
        public Object document;
        @SerializedName("identity")
        @Expose
        public String identity;
        @SerializedName("lang_id")
        @Expose
        public Object langId;
        @SerializedName("activity")
        @Expose
        public Object activity;
        @SerializedName("badge")
        @Expose
        public String badge;
        @SerializedName("bearing")
        @Expose
        public Object bearing;
        @SerializedName("screen_count")
        @Expose
        public String screenCount;
        @SerializedName("brand")
        @Expose
        public String brand;
        @SerializedName("acc_holder_name")
        @Expose
        public String accHolderName;
        @SerializedName("bank_name")
        @Expose
        public String bankName;
        @SerializedName("code_bic")
        @Expose
        public String codeBic;
        @SerializedName("iban")
        @Expose
        public String iban;
        @SerializedName("account_number")
        @Expose
        public String accountNumber;
        @SerializedName("address")
        @Expose
        public String address;



    }

    @BindingAdapter({"android:image"})
    public static void loadImage(ImageView view, String imageUrl){
        Glide.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.user_default))
                .override(300,300)
                .into(view);
    }

}
