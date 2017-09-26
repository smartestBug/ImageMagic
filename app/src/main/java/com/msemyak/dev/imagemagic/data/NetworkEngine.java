package com.msemyak.dev.imagemagic.data;

import android.util.Log;

import com.msemyak.dev.imagemagic.data.model.GetAllImagesResponse;
import com.msemyak.dev.imagemagic.data.model.GifUrl;

import com.msemyak.dev.imagemagic.data.model.UserData;
import com.msemyak.dev.imagemagic.data.remote.RetrofitClient;

import java.io.File;

import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class NetworkEngine {

    public static Single<GetAllImagesResponse> getAllImages(String token) {

        return RetrofitClient.getDoitServerAPI().getAllImages(token);
    }

    public static Single<GifUrl> getGif(String token) {

        return RetrofitClient.getDoitServerAPI().getGif(token);
    }


    public static Single<Response<ResponseBody>> uploadImage(String token, String imagePath, String descr, String hash, String lat, String lon) {

        File file = new File(imagePath);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descr);
        RequestBody hashtag = RequestBody.create(MediaType.parse("text/plain"), hash);
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody longtitude = RequestBody.create(MediaType.parse("text/plain"), lon);

       return RetrofitClient.getDoitServerAPI().uploadImage(token, fileToUpload, description, hashtag, latitude, longtitude);

    }

    public static Single<UserData> createUser(String imagePath, String user, String useremail, String userpass) {

        File file = new File(imagePath);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), user);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), useremail);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), userpass);

       return RetrofitClient.getDoitServerAPI().createUser(fileToUpload, username, email, password);

    }

    public static Single<UserData> userLogin(String email, String password) {

        RequestBody rbEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody rbPassword = RequestBody.create(MediaType.parse("text/plain"), password);

        return RetrofitClient.getDoitServerAPI().userLogin(rbEmail, rbPassword);

    }



}
