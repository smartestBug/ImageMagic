package com.msemyak.imagemagic.mvp.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class IMAppCompatActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    public void showMessage(String message) {
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(int stringId) {
        //Snackbar.make(findViewById(R.id.constraintLayout), message, BaseTransientBottomBar.LENGTH_LONG).show();
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }

    public void showWaitDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void showWaitDialog(int stringId) {
        showWaitDialog(getString(stringId));
    }

    public void dismissWaitDialog() {
        progressDialog.dismiss();
    }
}
