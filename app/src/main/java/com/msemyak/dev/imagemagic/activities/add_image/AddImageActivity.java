package com.msemyak.dev.imagemagic.activities.add_image;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.msemyak.dev.imagemagic.R;

import com.msemyak.dev.imagemagic.utils.GlideApp;
import com.msemyak.dev.imagemagic.utils.IMUtil;
import com.msemyak.dev.imagemagic.utils.LocationLatLong;


import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class AddImageActivity extends AppCompatActivity implements AddImageContract.View {

    @BindView(R.id.tbAddImage) Toolbar toolbarMain;
    @BindView(R.id.ivAddImage) ImageView ivAddImage;
    @BindView(R.id.etImageDescription) EditText etImageDescription;
    @BindView(R.id.etImageHashtag) EditText etImageHashtag;

    final private int LOCATION_REQUEST_CODE = 555;
    String[] permissionsNeeded = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private String token;
    private String imagePath;

    //Fallback coordinates (Amsterdam). Used if no other coordinates were obtained
    String fallbackLatitude = "52.37308";
    String fallbackLongitude = "4.892404";

    String fallbackLatitude2 = "41.386735";
    String fallbackLongitude2 = "9.157973";

    private ProgressDialog progressDialog;

    AddImageContract.Presenter myPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        ButterKnife.bind(this);

        token = getIntent().getStringExtra("token");
        imagePath = getIntent().getStringExtra("image");

        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Image");

        GlideApp.with(this)
                .load(imagePath)
                .centerCrop()
                .into(ivAddImage);

        myPresenter = new AddImagePresenter(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.miAddImage:

                LocationLatLong location = IMUtil.getLocationFromImage(imagePath);
                if (location.hasCoordinates) {
                    uploadImage(location);
                }
                else {
                    if (EasyPermissions.hasPermissions(this, permissionsNeeded)) {
                        uploadImage(IMUtil.getCurrentGeoLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE)));
                    } else {
                        EasyPermissions.requestPermissions(this, getString(R.string.read_file), LOCATION_REQUEST_CODE, permissionsNeeded);
                    }
                }

                break;

            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

            default:
                break;
        }

        return true;
    }

    private void uploadImage(LocationLatLong location) {

        if (location.hasCoordinates) {
            //TODO - uncomment in production
            //myPresenter.uploadImage(token, imagePath, etImageDescription.getText().toString(), etImageHashtag.getText().toString(), location.getLatitudeString(), location.getLongtitudeString());
            myPresenter.uploadImage(token, imagePath, etImageDescription.getText().toString(), etImageHashtag.getText().toString(), fallbackLatitude2, fallbackLongitude2);
        }
        else {
            showMessage(getString(R.string.error_accessing_coordinates));
            myPresenter.uploadImage(token, imagePath, etImageDescription.getText().toString(), etImageHashtag.getText().toString(), fallbackLatitude, fallbackLongitude);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        if (EasyPermissions.hasPermissions(this, permissionsNeeded)) {
            uploadImage(IMUtil.getCurrentGeoLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE))); //permission to access location services granted - get current coordinates and upload
        } else {
            uploadImage(new LocationLatLong()); //permission to access location services denied - using default coordinates
        }
    }

    @Override
    public void showWaitDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.uploading_image));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void dismissWaitDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void doneUploading() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showMessage(String message) {
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(int stringId) {
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), getString(stringId), Toast.LENGTH_LONG).show();
    }

}
