package com.lbs.amap.task;

import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 11:29
 * 修改备注：
 */

public interface OnRegeocodeSearchResultListener {

    void onRegeocodeSearchResult(RegeocodeResult regeocodeReult);
}
