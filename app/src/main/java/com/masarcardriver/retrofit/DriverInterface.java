package com.masarcardriver.retrofit;




import com.masarcardriver.model.BookingDetailModel;
import com.masarcardriver.model.BrandListModel;
import com.masarcardriver.model.CarListModel;
import com.masarcardriver.model.ModListModel;
import com.masarcardriver.model.SignupModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DriverInterface {

    @Multipart
    @POST("signup")
    Call<SignupModel> signupDriver (@Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("email") RequestBody email,
                                    @Part("mobile") RequestBody mobile,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("password") RequestBody password,
                                    @Part("lat") RequestBody lat,
                                    @Part("lon") RequestBody lon,
                                    @Part("address") RequestBody address,
                                    @Part("type") RequestBody type,
                                        @Part("register_id") RequestBody register_id,
                                    @Part MultipartBody.Part file);


    @GET("car_list")
    Call<CarListModel> getCarList();

    @FormUrlEncoded
    @POST("brand_list")
    Call<BrandListModel> cardBrandList(@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("model_list")
    Call<ModListModel> modelList(@FieldMap Map<String,String> params);



    @Multipart
    @POST("add_vehicle")
    Call<SignupModel> addVehicle (@Part("driver_id") RequestBody user_id,
                                    @Part("vehicle_type_id") RequestBody car_type_id,
                                    @Part("brand") RequestBody brand,
                                    @Part("vehicle_model") RequestBody car_model,
                                    @Part("vehicle_model_year") RequestBody year_of_manufacture,
                                    @Part("vehicle_number") RequestBody car_number,
                                    @Part("vehicle_color") RequestBody car_color,
                                    @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("add_bank")
    Call<SignupModel> addBank(@FieldMap Map<String,String> params);



    @FormUrlEncoded
    @POST("login")
    Call<SignupModel> userLogin (@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("forgot_password")
    Call<Map<String,String>> forgotPass (@FieldMap Map<String,String> params);


    @Multipart
    @POST("update_profile")
    Call<SignupModel> editprofile(
            @Part("user_id") RequestBody id,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("phone_code") RequestBody phone_code,
            @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("change_password")
    Call<SignupModel> changePassword (@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("update_lat_lon")
    Call<Map<String,String>> updateLocation (@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("update_online_status")
    Call<Map<String,String>> updateStatus (@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("driver_accept_and_Cancel_request")
    Call<BookingDetailModel>  acceptCancelRequest (@FieldMap Map<String,String> params);


    @FormUrlEncoded
    @POST("get_booking_details")
    Call<BookingDetailModel>  bookingDetails(@FieldMap Map<String,String> params);





}
