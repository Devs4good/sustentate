package com.sustentate.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sustentate.app.R;
import com.sustentate.app.adapter.SlideAdapter;
import com.sustentate.app.utils.KeySaver;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by mzorilla on 11/5/17.
 */

public class SlideActivity extends AppCompatActivity {

    private Button finish;
    private Button skip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

        ViewPager viewPager = findViewById(R.id.view_slide);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SlideFragment.newInstance("SUSTENTATE", "Nuestra aplicación móvil esta pensada para ayudarte a saber si un producto es reciclable o no.", R.color.slide_one, R.drawable.slide_one));
        fragmentList.add(SlideFragment.newInstance("¿Cómo funciona?", "Sacale una foto al residuo que tengas duda a través de nuestra aplicación móvil.", R.color.slide_two, R.drawable.slide_two));
        fragmentList.add(SlideFragment.newInstance("¡Ya está!", "La aplicación analiza la imágen con inteligencia artificial de Watson IBM y te indicará en qué cesto depositar el residuo.", R.color.slide_three, R.drawable.slide_three));

        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        finish = findViewById(R.id.slide_finish);
        finish.setVisibility(View.GONE);

        skip = findViewById(R.id.slide_skip);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    finish.setVisibility(View.VISIBLE);
                    skip.setVisibility(View.GONE);
                } else {
                    finish.setVisibility(View.GONE);
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        LinearLayout slideIndicator = findViewById(R.id.slide_count);

        if (KeySaver.getBoolSavedShare(this, "sliding")) {
            startActivity(new Intent(SlideActivity.this, HomeActivity.class));
            finish();
        }

        finish.setOnClickListener(onFinishClickListener);
        skip.setOnClickListener(onFinishClickListener);
    }

    View.OnClickListener onFinishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KeySaver.saveShare(SlideActivity.this, "sliding", true);
            startActivity(new Intent(SlideActivity.this, HomeActivity.class));
            finish();
        }
    };
}
