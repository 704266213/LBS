package com.lbs.amap.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.lbs.amap.R;
import com.lbs.amap.adapter.PoiResultAdapter;

import java.util.List;

/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 15:23
 * 修改备注：
 */

public class PoiSearchPopWindow extends PopupWindow implements View.OnClickListener, PoiSearch.OnPoiSearchListener, AdapterView.OnItemClickListener {

    private AMapLocation amapLocation;
    private View conentView;
    private EditText keyWord;
    private TextView searchByPoi;
    private ListView searchResultList;
    private Context context;
    private PoiResultAdapter poiResultAdapter;
    private OnPoiSearchResultListener onPoiSearchResultListener;


    public void setOnPoiSearchResultListener(OnPoiSearchResultListener onPoiSearchResultListener) {
        this.onPoiSearchResultListener = onPoiSearchResultListener;
    }

    public void setAMapLocation(AMapLocation amapLocation) {
        this.amapLocation = amapLocation;
    }

    public PoiSearchPopWindow(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.poi_search_popup_window, null);
        setContentView(conentView);


        keyWord = (EditText) conentView.findViewById(R.id.keyWord);
        searchByPoi = (TextView) conentView.findViewById(R.id.searchByPoi);
        searchByPoi.setOnClickListener(this);
        searchResultList = (ListView) conentView.findViewById(R.id.searchResultList);
        poiResultAdapter = new PoiResultAdapter(context);
        searchResultList.setAdapter(poiResultAdapter);
        searchResultList.setOnItemClickListener(this);


        // 设置弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置弹出窗体可点击
        this.setTouchable(true);
        this.setFocusable(true);
        // 设置点击是否消失
        this.setOutsideTouchable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable background = new ColorDrawable(0x4f000000);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(background);

    }

    @Override
    public void onClick(View v) {
        String keywords = keyWord.getText().toString();
        if (!TextUtils.isEmpty(keywords)) {

            PoiSearch.Query query = new PoiSearch.Query(keywords, "", amapLocation.getCityCode());
            //keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            query.setPageSize(30);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);//设置查询页码


            PoiSearch poiSearch = new PoiSearch(context, query);


            LatLonPoint myLocation = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
//            //设置周边搜索的中心点以及半径
            poiSearch.setBound(new PoiSearch.SearchBound(myLocation, 10000,true));


            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> poiItems = poiResult.getPois();
        poiResultAdapter.addDataToList(poiItems);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onPoiSearchResultListener != null) {
            onPoiSearchResultListener.onPoiItemResult(poiResultAdapter.getItem(position));
        }
        dismiss();
    }


    public interface OnPoiSearchResultListener {

        void onPoiItemResult(PoiItem poiItem);

    }


}