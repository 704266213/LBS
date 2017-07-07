package com.lbs.amap.task;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 15:25
 * 修改备注：
 */

public class RouteTask implements RouteSearch.OnRouteSearchListener {





    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int resultCode) {
        if (resultCode == 0 && driveRouteResult != null) {
//            synchronized (this) {
//                for (OnRouteCalculateListener listener : mListeners) {
//                    List<DrivePath> drivepaths = driveRouteResult.getPaths();
//                    float distance = 0;
//                    int duration = 0;
//                    if (drivepaths.size() > 0) {
//                        DrivePath drivepath = drivepaths.get(0);
//
//                        distance = drivepath.getDistance() / 1000;
//
//                        duration = (int) (drivepath.getDuration() / 60);
//                    }
//
//                    float cost = driveRouteResult.getTaxiCost();
//
//                    listener.onRouteCalculate(cost, distance, duration);
//                }
//
//            }
        }
// TODO 可以根据app自身需求对查询错误情况进行相应的提示或者逻辑处理
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
