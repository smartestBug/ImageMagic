package com.msemyak.dev.imagemagic.activities.images_list;

import com.msemyak.dev.imagemagic.data.model.Image;

import java.util.List;

public interface ImagesListContract {

    // view contract
    interface View {

        void showMessage(String message);

        void showImages(List<Image> imageList);

        void showGif(String gifURL);
    }

    //  presenter contract
    interface Presenter {

        void loadAndShowGif(String token);

        void loadAndShowUserImages(String token);

    }

}

