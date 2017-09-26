package com.msemyak.imagemagic.mvp;

import com.msemyak.imagemagic.mvp.model.local.Image;

import java.util.List;

public interface BaseView {

    void showMessage(String message);
    void showMessage(int stringId);

    void showWaitDialog(String message);
    void showWaitDialog(int stringId);
    void dismissWaitDialog();

    interface MainView extends BaseView{

        void navigateToImageList(String token);

    }

    interface ImagesListView extends BaseView {

        void showImages(List<Image> imageList);

        void showGif(String gifURL);
    }

    interface AddImageView extends BaseView {

        void doneUploading();

    }

}
