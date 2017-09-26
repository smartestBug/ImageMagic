package com.msemyak.dev.imagemagic.activities.add_image;

import android.content.Context;
import android.location.LocationManager;

public interface AddImageContract {

    // view contract
    interface View {

        void showMessage(String message);
        void showMessage(int stringId);

        void doneUploading();

        void showWaitDialog();

        void dismissWaitDialog();

    }

    //  presenter contract
    interface Presenter {

        void uploadImage(String token, String imagePath, String description, String hashtag, String latitude, String longtitude);

    }

}

