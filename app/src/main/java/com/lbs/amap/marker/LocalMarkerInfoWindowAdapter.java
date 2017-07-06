package com.lbs.amap.marker;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.lbs.amap.R;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 09:21
 * 修改备注：
 */

public class LocalMarkerInfoWindowAdapter implements AMap.InfoWindowAdapter {


    private Context context;
    private TextView title;

    public LocalMarkerInfoWindowAdapter(Context context) {
        this.context = context;
    }


    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = LayoutInflater.from(context).inflate(
                R.layout.local_info_window, null);
        initView(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = LayoutInflater.from(context).inflate(R.layout.local_info_window, null);
        initView(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void initView(Marker marker, View view) {
        title = (TextView) view.findViewById(R.id.title);

        SpannableString titleText = new SpannableString(marker.getTitle());
        titleText.setSpan(new ForegroundColorSpan(Color.RED), 5, titleText.length(), 0);
        title.setTextSize(15);
        title.setText(titleText);

    }

    public void setTitle(String msg) {
        if (title != null) {
            SpannableString titleText = new SpannableString(msg);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            title.setTextSize(15);
            title.setText(titleText);
        }
    }
}
