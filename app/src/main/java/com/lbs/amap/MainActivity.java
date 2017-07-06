package com.lbs.amap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.lbs.amap.marker.LocalMarkerInfoWindowAdapter;
import com.lbs.amap.popwindow.PoiSearchPopWindow;

import static android.R.attr.mode;

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, PoiSearchPopWindow.OnPoiSearchResultListener, RouteSearch.OnRouteSearchListener {


    private MapView mapView = null;
    private AMap aMap;

    private boolean isFirst = false;

    private AMapLocation amapLocation;
    private Marker mLocMarker;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocalMarkerInfoWindowAdapter localMarkerInfoWindowAdapter;
    public static final String LOCATION_MARKER_FLAG = "我的位置：%s";


    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

        }

        SHAUtil.getSha(this);

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        //开启以中心点进行手势操作
        aMap.getUiSettings().setGestureScaleByMapCenter(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false


//        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
//        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
//        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
//        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
//                mLocMarker.hideInfoWindow();
                //地图中心点转换
//                AMap.getProjection().fromScreenLocation(ScreenUtil.getScreenCenterPoint(MainActivity.this))
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                mLocMarker.showInfoWindow();
            }
        });

        localMarkerInfoWindowAdapter = new LocalMarkerInfoWindowAdapter(this);
        aMap.setInfoWindowAdapter(localMarkerInfoWindowAdapter);// 设置自定义InfoWindow样式


        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(false);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);

        //开启以中心点进行手势操作的方法
        //  aMap.getUiSettings().setGestureScaleByMapCenter(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.e("XLog", "==============amapLocation=======================" + amapLocation.toString());
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {

                this.amapLocation = amapLocation;
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                addMarker(location);//添加定位图标


                //   mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                if (!isFirst) {
                    //参数依次是：视角调整区域的中心点坐标
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(location);
                    aMap.moveCamera(mCameraUpdate);
                    isFirst = true;
                }


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /*
     * 添加标签
     */
    private void addMarker(LatLng latlng) {
        if (mLocMarker != null) {
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.poi_marker_pressed);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

//		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        options.title(String.format(LOCATION_MARKER_FLAG, amapLocation.getStreet()));
        options.snippet("DefaultMarker");
        mLocMarker = aMap.addMarker(options);
        mLocMarker.showInfoWindow();

    }


    /*
     * 兴趣点查询及POI搜索
     */
    public void poiSearch(View v) {
        PoiSearchPopWindow poiSearchPopWindow = new PoiSearchPopWindow(MainActivity.this);
        poiSearchPopWindow.setAMapLocation(amapLocation);
        poiSearchPopWindow.setOnPoiSearchResultListener(this);
        poiSearchPopWindow.showAsDropDown(findViewById(R.id.toolbar));
    }


    /*
     * 根据查询结果进行规划线路
     */
    public void onPoiItemResult(PoiItem poiItem) {
        Log.e("XLog", "========LatLonPoint==============" + poiItem.getLatLonPoint());
        Log.e("XLog", "========title==============" + poiItem.getTitle());

        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        LatLonPoint myLocation = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(myLocation, poiItem.getLatLonPoint());

        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                null, "");
        routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);

                    DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(
                            MainActivity.this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                } else {

                }
            }

        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    public void OnUpdateCameraToLocalClick(View view) {


        if (aMap != null){
            aMap.clear();
            mLocMarker = null;
            LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
            addMarker(location);//添加定位图标
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLng(location);
            aMap.moveCamera(mCameraUpdate);
        }

    }
}
