package com.msemyak.imagemagic.mvp.model.local;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GifUrl {

    @SerializedName("gif")
    @Expose
    private String gifUrl;

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

}
