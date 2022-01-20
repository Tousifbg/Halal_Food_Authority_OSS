package com.example.halalfoodauthorityoss;

import com.example.halalfoodauthorityoss.model.LoginResponse;
import com.example.halalfoodauthorityoss.model.Model;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Interface {

    @FormUrlEncoded
    @POST("sign_up")
    Call<Model> Sign_Up(
            @Field("name") String name,
            @Field("cnic") String cnic,
            @Field("c_mobile") String c_mobile,
            @Field("password") String password,
            @Field("district_id") String district_id
    );

    @Multipart
    @POST("add_owner_reg")
    Call<Model> Add_Owner(
            @Part("name") String name,
            @Part("fname") String fname,
            @Part("cnic_no") String cnic_no,
            @Part("contact_no") String contact_no,
            @Part MultipartBody.Part profileimage,
            @Part("bussiness_address") String bussiness_address,
            @Part("bussiness_name") String bussiness_name,
            @Part("bussiness_category") int bussiness_category,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part  MultipartBody.Part cnicimage,
            @Part("DistrictId") int DistrictId
    );
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> Login(
            @Field("cnic") String cnic,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("forgot_password")
    Call<Model> Forgot( @Field("cnic") String cnic
    );

    @GET("bussiness_type_record")
    Call<List<Model>> getCategory();
    @GET("distric_record")
    Call<List<Model>> getDistrict();

    @FormUrlEncoded
    @POST("find_business_name")
    Call<Model> GetName( @Field("name") String name
    );

    @FormUrlEncoded
    @POST("complain_record")
    Call<Model> Complaint(
            @Field("comp_title") String title,
            @Field("comp_desc") String desc,
            @Field("cust_id") String id,
            @Field("address") String address,
            @Field("distric_id") String district_id
    );
}
