package com.lbs.amap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.lbs.amap.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 类描述：
 * 创建人：alan
 * 创建时间：2017-07-07 16:35
 * 修改备注：
 */

public class PoiResultAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<PoiItem> poiItems;

    public PoiResultAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        poiItems = new ArrayList<PoiItem>();
    }

    public void addDataToList(List<PoiItem> poiItems) {
        this.poiItems.addAll(poiItems);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return poiItems.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return poiItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PosItemViewHold posItemViewHold = null;
        if (convertView == null) {
            posItemViewHold = new PosItemViewHold();
            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.pos_search_item, null);


            posItemViewHold.title = (TextView) convertView.findViewById(R.id.title);
            posItemViewHold.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(posItemViewHold);
        } else {
            posItemViewHold = (PosItemViewHold) convertView.getTag();
        }

        PoiItem poiItem = poiItems.get(position);
        posItemViewHold.title.setText(poiItem.getTitle());
        posItemViewHold.distance.setText(poiItem.getBusinessArea());
        return convertView;
    }


    public final class PosItemViewHold {
        TextView title;
        TextView distance;

    }

}
