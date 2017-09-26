package com.msemyak.imagemagic.mvp.presenter;

import com.msemyak.imagemagic.R;
import com.msemyak.imagemagic.mvp.BasePresenter;
import com.msemyak.imagemagic.mvp.BaseView;
import com.msemyak.imagemagic.mvp.model.NetworkEngine;
import com.msemyak.imagemagic.utils.RxUtil;

import io.reactivex.disposables.CompositeDisposable;


public class MainActivityPresenter implements BasePresenter.MainActivityPresenter {

    BaseView.MainView myView;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    public MainActivityPresenter(BaseView.MainView view) {
        myView = view;
    }

    @Override
    public void processLogin(String avatarPath, String username, String email, String password) {

        if (avatarPath == null) {

            subscriptions.add(
                    NetworkEngine.userLogin(email, password)
                            .compose(RxUtil.applySingleSchedulers())
                            .subscribe(
                                    user -> myView.navigateToImageList(user.getToken()),
                                    error -> myView.showMessage(R.string.error_bad_login)
                            ));
        } else {
            subscriptions.add(
                    NetworkEngine.createUser(avatarPath, username, email, password)
                            .compose(RxUtil.applySingleSchedulers())
                            .subscribe(
                                    user -> myView.navigateToImageList(user.getToken()),
                                    error -> myView.showMessage(R.string.error_creating_new_user)
                            ));
        }

    }

    @Override
    public void unsubscribeObservers() {
        RxUtil.unsubscribe(subscriptions);
    }
}
