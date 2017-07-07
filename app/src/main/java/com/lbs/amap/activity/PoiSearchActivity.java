package com.lbs.amap.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lbs.amap.R;
import com.lbs.amap.task.LocationTask;
import com.lbs.amap.task.OnLocationSuccessListener;
import com.lbs.amap.task.OnRegeocodeSearchResultListener;
import com.lbs.amap.task.RegeocodeTask;

import static com.lbs.amap.activity.MainActivity.LOCATION_MARKER_FLAG;

public class PoiSearchActivity extends AppCompatActivity implements OnLocationSuccessListener, AMap.OnCameraChangeListener, AMap.OnMapLoadedListener, OnRegeocodeSearchResultListener {

    private AMap map;
    private MapView mapView;
    private TextView start;
    private EditText end;
    private TextView describe;
    private boolean isFirst = false;

    private Marker positionMark;
    private LocationTask locationTask;
    private RegeocodeTask regeocodeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        initView(savedInstanceState);


    }


    private void initView(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mapView);
        start = (TextView) findViewById(R.id.start);
        end = (EditText) findViewById(R.id.end);
        describe = (TextView) findViewById(R.id.describe);

        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setOnMapLoadedListener(this);
        map.setOnCameraChangeListener(this);


        locationTask = LocationTask.getInstance(getApplicationContext());
        locationTask.setOnLocationSuccessListener(this);
        map.setLocationSource(locationTask);// 设置定位监听
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        map.setMyLocationEnabled(true);

        map.moveCamera(CameraUpdateFactory.zoomTo(18));


        regeocodeTask = new RegeocodeTask(getApplicationContext());
//        RouteTask.getInstance(getApplicationContext())
//                .addRouteCalculateListener(this);

    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        locationTask.deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != locationTask) {
            locationTask.onDestroy();
        }
    }


    /**
     * 定位成功
     */
    @Override
    public void onLocationSuccess(AMapLocation aMapLocation) {
        Log.e("XLog", "============onLocationSuccess====================");

        if (aMapLocation != null) {
            LatLng location = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            String street = aMapLocation.getStreet();
            if (!TextUtils.isEmpty(street)) {

                if (!isFirst) {
                    //参数依次是：视角调整区域的中心点坐标
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(location);
                    map.moveCamera(mCameraUpdate);
                    addMarker(location, street);
                    start.setText(street);
                    isFirst = true;
                }
            }


        }
    }

    /*****************地图加载成功的监听****************************/

    @Override
    public void onMapLoaded() {
        Log.e("XLog", "============onMapLoaded====================");
        locationTask.startLocation();
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        map.setMyLocationEnabled(true);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(0, 0));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.start)));
        positionMark = map.addMarker(markerOptions);
        positionMark.setPositionByPixels(mapView.getWidth() / 2, mapView.getHeight() / 2);
    }


    /*****************地图中心点变化的监听****************************/
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Log.e("XLog", "============onCameraChangeFinish====================" + regeocodeTask);
        if (regeocodeTask != null) {
            regeocodeTask.setOnRegeocodeSearchResultListener(this);
            Log.e("XLog", "============latitude====================" + cameraPosition.target.latitude);
            Log.e("XLog", "============longitude====================" + cameraPosition.target.longitude);
            regeocodeTask.search(cameraPosition.target.latitude, cameraPosition.target.longitude);
        }

    }


    /*
     * 根据经纬度搜索位置成功的回调
     * 及regeocodeTask search的回调
     */
    @Override
    public void onRegeocodeSearchResult(RegeocodeResult regeocodeReult) {
        Log.e("XLog", "============onRegeocodeSearchResult====================");
        if (regeocodeReult != null) {
            Log.e("XLog", "============onRegeocodeSearchResult====================" + regeocodeReult.getRegeocodeAddress().getCrossroads());
            start.setText(regeocodeReult.getRegeocodeAddress().getFormatAddress());
        }
    }


    /*
      * 添加标签
      */
    private void addMarker(LatLng latlng, String street) {

        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.poi_marker_pressed);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);


        options.title(String.format(LOCATION_MARKER_FLAG, street));
        options.snippet("DefaultMarker");
//        mLocMarker = map.addMarker(options);
//        mLocMarker.showInfoWindow();
    }


    public void onLocationClick(View view){
        Log.e("XLog", "============onLocationClick====================");
        isFirst = false;
        locationTask.startLocation();
    }
}
