package com.msemyak.dev.imagemagic.activities.main;

public interface MainActivityContract {

    // view contract
    interface View {

        void navigateToImageList(String token);

        void showMessage(String message);
        void showMessage(int stringId);

    }

    //  presenter contract
    interface Presenter {

        void processLogin(String avatarPath, String username, String email, String password);

    }

}

