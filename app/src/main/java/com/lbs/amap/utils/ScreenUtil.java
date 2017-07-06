package com.lbs.amap.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 11:14
 * 修改备注：
 */

public class ScreenUtil {

    public static Point getScreenCenterPoint(Context context) {
        Point point = new Point();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        point.x = dm.widthPixels / 2;
        point.y = dm.heightPixels / 2;
        return point;
    }


}
