package com.msemyak.dev.imagemagic.activities.add_image;

import com.msemyak.dev.imagemagic.R;
import com.msemyak.dev.imagemagic.data.NetworkEngine;
import com.msemyak.dev.imagemagic.utils.RxUtil;

public class AddImagePresenter implements AddImageContract.Presenter {

    AddImageContract.View myView;

    public AddImagePresenter(AddImageContract.View view) {
        myView = view;
    }

    @Override
    public void uploadImage(String token, String imagePath, String description, String hashtag, String latitude, String longtitude) {

        myView.showWaitDialog();

        NetworkEngine.uploadImage(token, imagePath, description, hashtag, latitude, longtitude)
                .compose(RxUtil.applySingleSchedulers())
                .subscribe(
                        response -> {
                            if (response.code() == 201) {
                                myView.dismissWaitDialog();
                                myView.doneUploading();
                            }
                            else {
                                myView.dismissWaitDialog();
                                myView.showMessage(R.string.error_bad_image);
                            }
                        },
                        error -> {
                            myView.dismissWaitDialog();
                            myView.showMessage(R.string.error_uploading_image);
                        }
                );


    }

}
