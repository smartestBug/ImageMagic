package com.msemyak.imagemagic.utils;

public class LatLong {
    private float Latitude;
    private float Longitude;
    public boolean hasCoordinates;

    public LatLong() {
        hasCoordinates = false;
    }

    public LatLong(float latitude, float longitude) {
        Latitude = latitude;
        Longitude = longitude;
        hasCoordinates = true;
    }

    public void setCoordinates(float latitude, float longitude) {
        Latitude = latitude;
        Longitude = longitude;
        hasCoordinates = true;
    }

    public float getLatitude() {
        return Latitude;
    }

    public String getLatitudeString() {
        return Float.toString(Latitude);
    }

    public float getLongitude() {
        return Longitude;
    }

    public String getLongtitudeString() {
        return Float.toString(Longitude);
    }

}
