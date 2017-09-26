package com.msemyak.imagemagic.mvp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import com.msemyak.imagemagic.R;
import com.msemyak.imagemagic.mvp.BasePresenter;
import com.msemyak.imagemagic.mvp.BaseView;
import com.msemyak.imagemagic.mvp.model.local.Image;
import com.msemyak.imagemagic.mvp.presenter.ImagesListPresenter;
import com.msemyak.imagemagic.utils.GlideApp;
import com.msemyak.imagemagic.utils.IMUtil;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ImagesListActivity extends IMAppCompatActivity implements BaseView.ImagesListView {

    final private int GALLERY_REQUEST_CODE = 333;
    final private int READ_REQUEST_CODE = 222;
    final private int ADD_IMAGE_ACTIVITY_CODE = 444;

    String[] permissionsNeeded = {Manifest.permission.READ_EXTERNAL_STORAGE};
    boolean isAvatarLoaded = false;
    Uri avatarUri;

    @BindView(R.id.tbMain) Toolbar toolbarMain;
    @BindView(R.id.rvMain) RecyclerView recyclerViewMain;

    private RVAdapterImages listAdapter = null;

    BasePresenter.ImagesListPresenter myPresenter;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        token = getIntent().getStringExtra("token");
        //token = "26f062965ee2097d375fe8bd2cb86a6a"; //dummy
        //token = "8695058e1e1ad453bc3b56569e7e6af1"; //cowboy

        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        myPresenter = new ImagesListPresenter(this, token);

        myPresenter.loadAndShowUserImages(token);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_images_list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            avatarUri = data.getData();
            isAvatarLoaded = true;

            getImageAndProceedToUpload();
        }

        if (requestCode == ADD_IMAGE_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {

            myPresenter.loadAndShowUserImages(token);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(READ_REQUEST_CODE)
    private void getImageAndProceedToUpload() {

        if (EasyPermissions.hasPermissions(this, permissionsNeeded)) {

            String imageFilePath = IMUtil.getRealPathFromURIPath(avatarUri, this);

            Intent addImageIntent = new Intent(getApplicationContext(), AddImageActivity.class);
            addImageIntent.putExtra("token", token);
            addImageIntent.putExtra("image", imageFilePath);
            startActivityForResult(addImageIntent, ADD_IMAGE_ACTIVITY_CODE);

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void showImages(List<Image> imageList) {

        if (listAdapter == null) {

            recyclerViewMain.setLayoutManager(new GridLayoutManager(this, 2));
            listAdapter = new RVAdapterImages(imageList, this);
            recyclerViewMain.setAdapter(listAdapter);
        }
        else {
            listAdapter.setNewData(imageList);
            listAdapter.notifyDataSetChanged();
            recyclerViewMain.smoothScrollToPosition(imageList.size()-1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.miAddIMage:
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                pickImageIntent.setType("image/*");
                startActivityForResult(pickImageIntent, GALLERY_REQUEST_CODE);
                break;

            case R.id.miMakeGif:
                myPresenter.loadAndShowGif(token);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void showGif(String gifURL) {

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.show_gif_placeholder, null, true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        ImageView ivGifPreview = (ImageView) dialoglayout.findViewById(R.id.ivGifPreview);
        builder.show();
        GlideApp.with(this)
                .load(gifURL)
                .centerCrop()
                .into(ivGifPreview);
    }

}
