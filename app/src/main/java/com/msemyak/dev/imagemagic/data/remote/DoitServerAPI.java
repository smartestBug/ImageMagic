package com.msemyak.dev.imagemagic.data.remote;

import com.msemyak.dev.imagemagic.data.model.GetAllImagesResponse;
import com.msemyak.dev.imagemagic.data.model.GifUrl;
import com.msemyak.dev.imagemagic.data.model.Image;
import com.msemyak.dev.imagemagic.data.model.UserData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DoitServerAPI {

    @Multipart
    @POST("create")
    Single<UserData> createUser(@Part MultipartBody.Part filePart,
                                   @Part("username") RequestBody username,
                                   @Part("email") RequestBody email,
                                   @Part("password") RequestBody password);

    @Multipart
    @POST("login")
    Single<UserData> userLogin (@Part("email") RequestBody email, @Part("password") RequestBody password);

    @Multipart
    @POST("image")
    Single<Response<ResponseBody>> uploadImage(@Header("token") String token,
                                   @Part MultipartBody.Part filePart,
                                   @Part("description") RequestBody description,
                                   @Part("hashtag") RequestBody hashtag,
                                   @Part("latitude") RequestBody latitude,
                                   @Part("longitude") RequestBody longitude);

    @GET("all")
    Single<GetAllImagesResponse> getAllImages(@Header("token") String token);

    @GET("gif")
    Single<GifUrl> getGif(@Header("token") String token);

}
