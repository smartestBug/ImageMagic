package com.msemyak.imagemagic.mvp.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.msemyak.imagemagic.R;
import com.msemyak.imagemagic.mvp.BasePresenter;
import com.msemyak.imagemagic.mvp.BaseView;
import com.msemyak.imagemagic.mvp.presenter.AddImagePresenter;
import com.msemyak.imagemagic.utils.GlideApp;
import com.msemyak.imagemagic.utils.IMUtil;
import com.msemyak.imagemagic.utils.LatLong;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class AddImageActivity extends IMAppCompatActivity implements BaseView.AddImageView {

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

    BasePresenter.AddImagePresenter myPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected int getLayoutId() {
        return R.layout.activity_add_image;
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

                LatLong location = IMUtil.getLocationFromImage(imagePath);
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

    private void uploadImage(LatLong location) {

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
            uploadImage(new LatLong()); //permission to access location services denied - using default coordinates
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

}
