package com.msemyak.dev.imagemagic.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.msemyak.dev.imagemagic.R;
import com.msemyak.dev.imagemagic.activities.images_list.ImagesListActivity;

import com.msemyak.dev.imagemagic.utils.GlideApp;
import com.msemyak.dev.imagemagic.utils.IMUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    final private int GALLERY_REQUEST_CODE = 333;
    final private int READ_REQUEST_CODE = 222;
    String[] permissionsNeeded = {Manifest.permission.READ_EXTERNAL_STORAGE};
    boolean isAvatarLoaded = false;
    Uri avatarUri;

    @BindView(R.id.btnSendData) Button btnSendData;
    @BindView(R.id.ivAvatar) ImageView ivAvatar;
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etPassword) EditText etPassword;

    MainActivityContract.Presenter myPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        myPresenter = new MainActivityPresenter(this);

        GlideApp.with(this)
                .load(R.mipmap.add_avatar_icon)
                .circleCrop()
                .into(ivAvatar);
    }

    @OnClick (R.id.btnSendData)
    public void onSendDataButtonClick(View view) {

        if (validateInput()) {
            myPresenter.processLogin(IMUtil.getRealPathFromURIPath(avatarUri, this), etUsername.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());
        }

    }

    @OnClick (R.id.ivAvatar)
    public void onAvatarClick(View view) {

        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, GALLERY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            //if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(permissionsNeeded)))
            //{ new AppSettingsDialog.Builder(this).build().show(); }
            avatarUri = data.getData();
            isAvatarLoaded = true;

            getImageAndUpload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(READ_REQUEST_CODE)
    private void getImageAndUpload() {

        if (EasyPermissions.hasPermissions(this, permissionsNeeded)) {

            String imageFilePath = IMUtil.getRealPathFromURIPath(avatarUri, this);

            GlideApp.with(this)
                    .load(avatarUri)
                    .circleCrop()
                    .into(ivAvatar);

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void navigateToImageList(String token) {
        Intent editIntent = new Intent(getApplicationContext(), ImagesListActivity.class);
        editIntent.putExtra("token", token);
        startActivity(editIntent);
    }

    public boolean validateInput() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_enter_correct_email));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
            etPassword.setError(getString(R.string.error_input_correct_password));
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void showMessage(String message) {
        IMUtil.showMessage(this, message);
    }

    @Override
    public void showMessage(int stringId) {
        IMUtil.showMessage(this, getString(stringId));
    }
}
