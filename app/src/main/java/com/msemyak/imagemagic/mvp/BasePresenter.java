package com.msemyak.imagemagic.mvp;

public interface BasePresenter {

    void unsubscribeObservers();

    interface MainActivityPresenter extends BasePresenter{

        void processLogin(String avatarPath, String username, String email, String password);

    }

    interface ImagesListPresenter extends BasePresenter {

        void loadAndShowGif(String token);

        void loadAndShowUserImages(String token);

    }

    interface AddImagePresenter extends BasePresenter {

        void uploadImage(String token, String imagePath, String description, String hashtag, String latitude, String longtitude);

    }
}