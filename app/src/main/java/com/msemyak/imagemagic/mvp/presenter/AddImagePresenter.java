package com.msemyak.imagemagic.mvp.presenter;

import com.msemyak.imagemagic.R;
import com.msemyak.imagemagic.mvp.BasePresenter;
import com.msemyak.imagemagic.mvp.BaseView;
import com.msemyak.imagemagic.mvp.model.NetworkEngine;
import com.msemyak.imagemagic.utils.RxUtil;

import io.reactivex.disposables.CompositeDisposable;

public class AddImagePresenter implements BasePresenter.AddImagePresenter {

    BaseView.AddImageView myView;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public AddImagePresenter(BaseView.AddImageView view) {
        myView = view;
    }

    @Override
    public void uploadImage(String token, String imagePath, String description, String hashtag, String latitude, String longtitude) {

        myView.showWaitDialog();

        subscriptions.add(
                NetworkEngine.uploadImage(token, imagePath, description, hashtag, latitude, longtitude)
                        .compose(RxUtil.applySingleSchedulers())
                        .subscribe(
                                response -> {
                                    if (response.code() == 201) {
                                        myView.dismissWaitDialog();
                                        myView.doneUploading();
                                    } else {
                                        myView.dismissWaitDialog();
                                        myView.showMessage(R.string.error_bad_image);
                                    }
                                },
                                error -> {
                                    myView.dismissWaitDialog();
                                    myView.showMessage(R.string.error_uploading_image);
                                }
                        ));

    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
