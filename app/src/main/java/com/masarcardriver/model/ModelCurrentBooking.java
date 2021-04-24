package com.masarcardriver.model;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCurrentBooking implements Serializable {

    @SerializedName("estimated_time")
    @Expose
    private String estimatedTime;
    @SerializedName("estimated_dis")
    @Expose
    private String estimatedDis;
    @SerializedName("fare")
    @Expose
    private String fare;
    @SerializedName("time_taken")
    @Expose
    private String timeTaken;
    @SerializedName("wait_time")
    @Expose
    private Object waitTime;
    @SerializedName("base_fare")
    @Expose
    private String baseFare;
    @SerializedName("trip_fare")
    @Expose
    private String tripFare;
    @SerializedName("tax_amt")
    @Expose
    private String taxAmt;
    @SerializedName("service_tax")
    @Expose
    private String serviceTax;
    @SerializedName("service_amt")
    @Expose
    private String serviceAmt;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;
    @SerializedName("discount_percent")
    @Expose
    private String discountPercent;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("route_img")
    @Expose
    private String routeImg;
    @SerializedName("fav_status")
    @Expose
    private String favStatus;
    @SerializedName("result")
    @Expose
    private List<ModelCurrentBookingResult> result = null;
    @SerializedName("map")
    @Expose
    private String map;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("diff_second")
    @Expose
    private String diffSecond;
    @SerializedName("booking_dropoff")
    @Expose
    private List<Object> bookingDropoff = null;
    @SerializedName("status")
    @Expose
    private String status;

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getEstimatedDis() {
        return estimatedDis;
    }

    public void setEstimatedDis(String estimatedDis) {
        this.estimatedDis = estimatedDis;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Object getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Object waitTime) {
        this.waitTime = waitTime;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getTripFare() {
        return tripFare;
    }

    public void setTripFare(String tripFare) {
        this.tripFare = tripFare;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getServiceAmt() {
        return serviceAmt;
    }

    public void setServiceAmt(String serviceAmt) {
        this.serviceAmt = serviceAmt;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getRouteImg() {
        return routeImg;
    }

    public void setRouteImg(String routeImg) {
        this.routeImg = routeImg;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public List<ModelCurrentBookingResult> getResult() {
        return result;
    }

    public void setResult(List<ModelCurrentBookingResult> result) {
        this.result = result;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDiffSecond() {
        return diffSecond;
    }

    public void setDiffSecond(String diffSecond) {
        this.diffSecond = diffSecond;
    }

    public List<Object> getBookingDropoff() {
        return bookingDropoff;
    }

    public void setBookingDropoff(List<Object> bookingDropoff) {
        this.bookingDropoff = bookingDropoff;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
