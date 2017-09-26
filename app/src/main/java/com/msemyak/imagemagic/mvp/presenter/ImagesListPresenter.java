package com.msemyak.imagemagic.mvp.presenter;

import android.util.Log;

import com.msemyak.imagemagic.mvp.BasePresenter;
import com.msemyak.imagemagic.mvp.BaseView;
import com.msemyak.imagemagic.mvp.model.NetworkEngine;
import com.msemyak.imagemagic.mvp.model.local.Image;
import com.msemyak.imagemagic.utils.RxUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ImagesListPresenter implements BasePresenter.ImagesListPresenter {

    BaseView.ImagesListView myView;
    List<Image> imageList;
    String token;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public ImagesListPresenter(BaseView.ImagesListView view, String token) {
        myView = view;
        this.token = token;
    }

    public void loadAndShowUserImages(String token) {

        subscriptions.add(
                NetworkEngine.getAllImages(token)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                response -> {
                                    if (imageList != null) imageList.clear();
                                    imageList = response.getImages();
                                    myView.showImages(imageList);
                                },
                                error -> myView.showMessage("Error loading images")
                        ));

    }

    @Override
    public void loadAndShowGif(String token) {

        subscriptions.add(
                NetworkEngine.getGif(token)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                gif -> myView.showGif(gif.getGifUrl()),
                                error -> myView.showMessage("Error loading Gif image")
                        ));

    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
