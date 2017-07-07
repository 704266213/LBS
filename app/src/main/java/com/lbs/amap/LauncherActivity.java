package com.lbs.amap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lbs.amap.activity.MainActivity;
import com.lbs.amap.activity.NavigationActivity;
import com.lbs.amap.activity.PoiSearchActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }


    public void onMapClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void OnPoiSearchClick(View view) {
        Intent intent = new Intent(this, PoiSearchActivity.class);
        startActivity(intent);
    }


    public void OnNavigationClick(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

}
