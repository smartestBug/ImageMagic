package com.msemyak.dev.imagemagic.activities.images_list;

import android.util.Log;

import com.msemyak.dev.imagemagic.data.NetworkEngine;
import com.msemyak.dev.imagemagic.data.model.Image;
import com.msemyak.dev.imagemagic.utils.RxUtil;

import java.util.List;

public class ImagesListPresenter implements ImagesListContract.Presenter {

    ImagesListContract.View myView;
    List<Image> imageList;
    String token;

    public ImagesListPresenter(ImagesListContract.View view, String token) {
        myView = view;
        this.token = token;
    }

    public void loadAndShowUserImages(String token) {

        NetworkEngine.getAllImages(token)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        response -> {
                            if (imageList != null) imageList.clear();
                            imageList = response.getImages();
                            myView.showImages(imageList);
                        },
                        error -> myView.showMessage("Error loading images")
                );

    }

    @Override
    public void loadAndShowGif(String token) {

        NetworkEngine.getGif(token)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        gif -> {
                            Log.d("IM", "-----> Token: " + token);
                            Log.d("IM", "-----> Gif path: " + gif.getGifUrl());

                            myView.showGif(gif.getGifUrl());
                        },
                        error -> myView.showMessage("Error loading Gif image")
                );

    }

}
