package com.sustentate.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sustentate.app.R;
import com.sustentate.app.utils.KeySaver;

import jp.wasabeef.glide.transformations.BlurTransformation;

/*
 * Created by mzorilla on 11/4/17.
 */

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

        findViewById(R.id.button_cam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.button_how).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeySaver.removeKey(HomeActivity.this, "sliding");
                startActivity(new Intent(HomeActivity.this, SlideActivity.class));
                finish();
            }
        });

        ImageView homeBg = findViewById(R.id.home_bg);
        Glide.with(this).load(R.drawable.bg_main).apply(RequestOptions.bitmapTransform(new BlurTransformation(130))).into(homeBg);
    }
}
