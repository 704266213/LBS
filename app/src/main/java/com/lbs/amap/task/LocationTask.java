package com.lbs.amap.task;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;


/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 10:46
 * 修改备注：高德地图定位
 */

public class LocationTask implements AMapLocationListener, LocationSource {


    private static LocationTask mLocationTask;

    private OnLocationChangedListener onLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationSuccessListener onLocationSuccessListener;


    private Context mContext;

    public void setOnLocationSuccessListener(OnLocationSuccessListener onLocationSuccessListener) {
        this.onLocationSuccessListener = onLocationSuccessListener;
    }


    private LocationTask(Context context) {
        mContext = context;
    }


    public static LocationTask getInstance(Context context) {
        if (mLocationTask == null) {
            mLocationTask = new LocationTask(context);
        }
        return mLocationTask;
    }

    /**
     * 单次定位
     */
    public void startLocation() {
        Log.e("XLog", "============mlocationClient====================" + mlocationClient);
        if (mlocationClient != null){
            mlocationClient.startLocation();
        } else if(onLocationChangedListener != null) {
            activate(onLocationChangedListener);
        }
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (onLocationChangedListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                if (onLocationSuccessListener != null) {
                    onLocationSuccessListener.onLocationSuccess(aMapLocation);
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
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
        onLocationChangedListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    public void onDestroy() {
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }


}