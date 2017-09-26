package com.msemyak.imagemagic.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class IMAppCompatActivity extends AppCompatActivity {

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

}
