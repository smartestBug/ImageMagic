package com.msemyak.dev.imagemagic.utils;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import android.widget.Toast;

import java.io.IOException;


public class IMUtil {

    public static void showMessage(Context context, String message) {
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        if (contentURI == null) return null;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public static LocationLatLong getLocationFromImage(String imagePath) {

        ExifInterface exif;
        boolean imageHasLatLong = false;
        float[] latLongFloat = new float[2];
        LocationLatLong location = new LocationLatLong();

        try {
            exif = new ExifInterface(imagePath);
            imageHasLatLong = exif.getLatLong(latLongFloat);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageHasLatLong) {
            location.setCoordinates(latLongFloat[0], latLongFloat[1]);
        }

        return location;
    }

    public static LocationLatLong getCurrentGeoLocation(LocationManager locationManager) {

        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }

        LocationLatLong locationLatLong = new LocationLatLong();

        if (location != null) {
            locationLatLong.setCoordinates((float) location.getLatitude(), (float) location.getLongitude());
        }

        return locationLatLong;
    }

}
