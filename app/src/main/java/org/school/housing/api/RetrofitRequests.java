package org.school.housing.api;

import org.school.housing.models.BaseResponse;
import org.school.housing.models.Category;
import org.school.housing.models.Operation;
import org.school.housing.models.admin.Admin;
import org.school.housing.models.admin.Advertisement;
import org.school.housing.models.admin.Employee;
import org.school.housing.models.admin.User;
import org.school.housing.models.special_respond.AuthResponse;
import org.school.housing.models.special_respond.PasswordChangeResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RetrofitRequests {

    //THE GET REQUEST -------------------------------------
    @GET("users")
    Call<BaseResponse<User>> GET_USERS_CALL();

    @GET("categories")
    Call<BaseResponse<Category>> GET_CATEGORIES_CALL();

    @GET("employees")
    Call<BaseResponse<Employee>> GET_EMPLOYEES_CALL();

    @GET("operations")
    Call<BaseResponse<Operation>> GET_OPERATION_CALL();

    @GET("advertisements")
    Call<BaseResponse<Advertisement>> GET_Advertisements_CALL();
    //--------------------------------------------------------------------------

    //**################################THIS FOR USER
    //CREATE REQUEST

    /*-
    @Multipart
    @POST("users")
    Call<BaseResponse<User>> create_user(@Part("name") RequestBody name, @Part("email") RequestBody email ,@Part("mobile") RequestBody mobile, @Part("national_number") RequestBody national_number, @Part("family_members") RequestBody family_members, @Part("gender") RequestBody gender, @Part MultipartBody.Part image);
     */

    @Multipart
    @POST("users")
    Call<BaseResponse<User>> create_user_map(@PartMap Map<String, RequestBody> parameters, @Part MultipartBody.Part image);


    //UPDATE REQUEST -- !Path('id')! as parameter
    /*0
    @Multipart
    @FormUrlEncoded
    @POST("users/{id}")
    Call<BaseResponse<User>> UPDATE_USERS_FULL_CALL(@Path("id") int id, @Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("national_number") String national_number, @Field("family_members") int family_members, @Field("gender") char gender, @Part("image") MultipartBody.Part imagePart);
    */

    @Multipart
    @POST("users/{id}")
    Call<BaseResponse<User>> update_user_Map(@Path("id") int id, @PartMap Map<String, RequestBody> parameters, @Part MultipartBody.Part imagePart);

    //here without editing the pic
    /*-
    @FormUrlEncoded
    @POST("users/{id}")
    Call<BaseResponse<User>> UPDATE_USERS_NO_PIC_CALL(@Path("id") int id, @Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("national_number") String national_number, @Field("family_members") int family_members, @Field("gender") char gender);
    */

    @FormUrlEncoded
    @POST("users/{id}")
    Call<BaseResponse<User>> update_user_no_pic_Map(@Path("id") int id, @PartMap Map<String, RequestBody> parameters);

    //DELETE REQUEST this is to delete a user by it id
    @DELETE("users/{id}")
    Call<BaseResponse<User>> DELETE_USER_CALL(@Path("id") int id);

    //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


    //LOGIN REQUEST FOR ADMIN========================
    @FormUrlEncoded
    @POST("auth/login")
    Call<AuthResponse<Admin>> POST_Login_Call(@Field("email") String email, @Field("password") String password);

    //Logout REQUEST===========WORKS
    @GET("auth/logout")
    Call<AuthResponse<Admin>> GET_Logout();


    //***PASSWORD THINGS
    @FormUrlEncoded
    @POST("auth/forget-password")
    Call<AuthResponse<Admin>> Post_Forget_password(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("auth/reset-password")
    Call<AuthResponse<Admin>> Post_Reset_password(@Field("mobile") String mobile, @Field("code") String code, @Field("password") String password, @Field("password_confirmation") String password_confirmation);


    //Now Change Password
    @FormUrlEncoded
    @POST("auth/change-password")
    Call<PasswordChangeResponse> POSTChangePassword(@Field("current_password") String current_password, @Field("new_password") String new_password, @Field("new_password_confirmation") String new_password_confirmation);
    //**********************************************************************************************************************************
    /*-
    @POST("employees")
    @Multipart
    Call<BaseResponse<Employee>> store_emp(@Part("name") RequestBody name, @Part("mobile") RequestBody mobile, @Part("national_number") RequestBody national_number, @Part MultipartBody.Part imagePart);
  */

    //Employee-------------
    @POST("employees")
    @Multipart
    Call<BaseResponse<Employee>> store_emp_map(@PartMap Map<String, RequestBody> parameters, @Part MultipartBody.Part imagePart);

    /*-
    @PUT("employees")//i changed post to put
    @Multipart
    Call<BaseResponse<Employee>> update_emp(@Part("name") RequestBody name, @Part("mobile") RequestBody mobile, @Part("national_number") RequestBody national_number, @Part MultipartBody.Part imagePart);
*///i changed post to put XXXIX
    @POST("employees/{id}")
    @Multipart
    Call<BaseResponse<Employee>> update_emp_map(@Path("id") int id, @PartMap Map<String, RequestBody> parameters, @Part MultipartBody.Part imagePart);

    @DELETE("employees/{id}")
    Call<BaseResponse<Employee>> delete_emp(@Path("id") int id);
    //**********************************************************************************************************************************

    @POST("advertisements")
    @Multipart
    Call<BaseResponse<Advertisement>> store_advertisements(@Part("title") String title, @Part("info") String info, @Part MultipartBody.Part image);

    @POST("advertisements")
    @Multipart
    Call<BaseResponse<Advertisement>> store_advertisements_noImage(@Part("title") String title, @Part("info") String info);
    /*-1
    @POST("advertisements/{id}")
    @Multipart
    Call<BaseResponse<Advertisement>> update_advertisements(@Path("id") int id, @Part("_method") String _method, @Part("title") String title, @Part("info") String info, @Part MultipartBody.Part image);
*/
    @POST("advertisements/{id}")
    @Multipart
    Call<BaseResponse<Advertisement>> update_advertisements_map(@Path("id") int id, @PartMap Map<String, RequestBody> parameters, @Part MultipartBody.Part image);
    /*-1
    @POST("advertisements/{id}")
    @Multipart
    Call<BaseResponse<Advertisement>> update_advertisements_noImage(@Path("id") int id, @Part("title") String title, @Part("info") String info);
*/
    @POST("advertisements/{id}")
    @Multipart
    Call<BaseResponse<Advertisement>> update_advertisements_noImage_map(@Path("id") int id,@PartMap Map<String , RequestBody> parameters);

    @DELETE("advertisements/{id}")
    Call<BaseResponse<Advertisement>> delete_advertisements(@Path("id") int id);

}
