package com.example.demoaccessories.api;

import androidx.annotation.NonNull;

import com.example.demoaccessories.model.AccountInfo;
import com.example.demoaccessories.model.Chat;
import com.example.demoaccessories.model.Comment;
import com.example.demoaccessories.model.CommentList;
import com.example.demoaccessories.model.Product;
import com.example.demoaccessories.model.ProductList;
import com.example.demoaccessories.model.User;
import com.example.demoaccessories.model.UserInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @NonNull
    @FormUrlEncoded
    @POST("api/register-account")
    Call<User> registerUser(@Field("fullName") @NonNull String fullName, @Field("email") @NonNull String email, @Field("password") @NonNull String password);

    @FormUrlEncoded
    @POST("api/verify_email")
    Call<User> verifyEmail(@Field("email") @NonNull String email);

    @FormUrlEncoded
    @POST("api/update_isActive")
    Call<User> setIsActive(@Field("email") @NonNull String email);

    @NonNull
    @FormUrlEncoded
    @POST("api/login-account")
    Call<User> loginApp(@Field("email") @NonNull String email, @Field("password") @NonNull String password);

    @FormUrlEncoded
    @POST("api/send_email")
    Call<User> sendMailResetPass(@Field("email") @NonNull String email);

    @FormUrlEncoded
    @POST("api/reset_password")
    Call<User> resetPassword(@Field("email") @NonNull String email, @Field("password") @NonNull String password);

    @FormUrlEncoded
    @POST("api/change_password")
    Call<User> changePassword(@Field("email") @NonNull String email, @Field("password") @NonNull String new_password);

    @NonNull
    @POST("api/get-token-account")
    @FormUrlEncoded
    Call<UserInfo> getTokenAPI(@Field("email") @NonNull String email);

    @NonNull
    @POST("api/get_account_user")
    @FormUrlEncoded
    Call<AccountInfo> get_account_user(@Field("email") @NonNull String email);

    @Multipart
    @POST("api/update_user")
    Call<User> updateUser(@Part("email") @NonNull RequestBody email,
                          @Part("fullName") @NonNull RequestBody fullName,
                          @Part("phone") @NonNull RequestBody phone,
                          @Part("address") @NonNull RequestBody address,
                          @Part MultipartBody.Part image,
                          @Part("image") @NonNull RequestBody name);

    @FormUrlEncoded
    @POST("api/delete_user")
    Call<User> deleteUser(@Field("email") @NonNull String email);

    //Product
    @Multipart
    @POST("api/add_product")
    Call<ProductList> addProduct(@Part("nameSeller") @NonNull RequestBody nameSeller,
                                 @Part("nameProduct") @NonNull RequestBody nameProduct,
                                 @Part("price") @NonNull RequestBody price,
                                 @Part("address") @NonNull RequestBody address,
                                 @Part("category") @NonNull RequestBody category,
                                 @Part("description") @NonNull RequestBody description,
                                 @Part MultipartBody.Part image,
                                 @Part("upload") @NonNull RequestBody name,
                                 @Part("imageSeller") @NonNull RequestBody imageSeller,
                                 @Part("createAt") @NonNull RequestBody createAt);

    @GET("api/get_all_product")
    Call<ProductList> getAllProduct();

    @NonNull
    @POST("api/get_product_details")
    @FormUrlEncoded
    Call<Product> getProductById(@Field("_id") @NonNull String _id);

    @Multipart
    @POST("api/update_product/")
    Call<ProductList> updateProduct(@Part("_id") @NonNull RequestBody _id,
                                @Part("nameSeller") @NonNull RequestBody nameSeller,
                                @Part("nameProduct") @NonNull RequestBody nameProduct,
                                @Part("price") @NonNull RequestBody price,
                                @Part("address") @NonNull RequestBody address,
                                @Part("category") @NonNull RequestBody category,
                                @Part("description") @NonNull RequestBody description,
                                @Part MultipartBody.Part image,
                                @Part("image") @NonNull RequestBody name,
                                @Part("imageSeller") @NonNull RequestBody imageSeller,
                                @Part("createAt") @NonNull RequestBody createAt);

    @FormUrlEncoded
    @POST("api/delete_product")
    Call<ProductList> deleteProduct(@Field("_id") @NonNull String _id);

    @FormUrlEncoded
    @POST("api/add_comment")
    Call<CommentList> add_comment(@Field("id_product") @NonNull String id_product,
                                  @Field("comment") @NonNull String comment,
                                  @Field("user") @NonNull String user,
                                  @Field("image_user") @NonNull String image_user,
                                  @Field("create_at") @NonNull String create_at);

    @FormUrlEncoded
    @POST("api/get_comment_byID")
    Call<CommentList> get_commentByID(@Field("id_product") @NonNull String id_product);

    @GET("api/get_all_comment")
    Call<CommentList> get_all_comment();

    @FormUrlEncoded
    @POST("api/delete_comment_byID")
    Call<CommentList> delete_commentByID(@Field("id_product") @NonNull String id_product);

    @FormUrlEncoded
    @POST("api/send_messenger")
    Call<Chat> send_messenger(@Field("id_product") @NonNull String id_product,
                              @Field("admin") @NonNull String admin,
                              @Field("user") @NonNull String user,
                              @Field("image_admin") @NonNull String image_admin,
                              @Field("image_user") @NonNull String image_user,
                              @Field("messenger") @NonNull String messenger,
                              @Field("create_at") @NonNull String create_at);

    @GET("api/get_all_messenger")
    Call<Chat> get_all_messenger();
}
