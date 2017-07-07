package com.lbs.amap.task;

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 11:27
 * 修改备注：
 */

public class RegeocodeTask implements GeocodeSearch.OnGeocodeSearchListener {


    private static final float SEARCH_RADIUS = 50;
    private OnRegeocodeSearchResultListener onRegeocodeSearchResultListener;

    private GeocodeSearch mGeocodeSearch;

    public RegeocodeTask(Context context) {
        mGeocodeSearch = new GeocodeSearch(context);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
    }

    public void search(double latitude, double longitude) {
        RegeocodeQuery regecodeQuery = new RegeocodeQuery(new LatLonPoint(
                latitude, longitude), SEARCH_RADIUS, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(regecodeQuery);
    }

    public void setOnRegeocodeSearchResultListener(OnRegeocodeSearchResultListener onRegeocodeSearchResultListener) {
        this.onRegeocodeSearchResultListener = onRegeocodeSearchResultListener;
    }


    @Override
    public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeReult, int resultCode) {
        if (resultCode == 1000) {
            if (regeocodeReult != null ) {
                if (onRegeocodeSearchResultListener != null) {
                    onRegeocodeSearchResultListener.onRegeocodeSearchResult(regeocodeReult);
                }
            }
        }
    }

}
