package com.msemyak.dev.imagemagic.activities.main;

import com.msemyak.dev.imagemagic.R;
import com.msemyak.dev.imagemagic.data.NetworkEngine;
import com.msemyak.dev.imagemagic.utils.RxUtil;

import timber.log.Timber;


public class MainActivityPresenter implements MainActivityContract.Presenter {

    MainActivityContract.View myView;

    public MainActivityPresenter(MainActivityContract.View view) {
        myView = view;
    }

    @Override
    public void processLogin(String avatarPath, String username, String email, String password) {

        if (avatarPath == null) {

            NetworkEngine.userLogin(email, password)
                    .compose(RxUtil.applySingleSchedulers())
                    .subscribe(
                            user -> myView.navigateToImageList(user.getToken()),
                            error -> myView.showMessage(R.string.error_bad_login)
                    );
        }
        else {
            NetworkEngine.createUser(avatarPath, username, email, password)
                    .compose(RxUtil.applySingleSchedulers())
                    .subscribe(
                            user -> myView.navigateToImageList(user.getToken()),
                            error -> myView.showMessage(R.string.error_creating_new_user)
                    );
        }

    }

}
