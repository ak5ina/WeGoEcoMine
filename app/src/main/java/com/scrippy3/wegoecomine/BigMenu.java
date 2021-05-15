package com.scrippy3.wegoecomine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BigMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_menu);
        getSupportActionBar().hide();


    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}