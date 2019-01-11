package com.am.hskt.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.am.hskt.R;

public class AddPopWindow implements AdapterView.OnItemClickListener {

    private PopupWindow popupWindow;
    private String[] values = new String[]{"pkgName", "model", "deviceId", "manufacture", "brand", "android_id", "sdk_int", "width", "height"};
    private OnItemSelect callBack;
    private Context context;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (callBack != null) {
            callBack.onSelect(values[position]);
            popupWindow.dismiss();
        }
    }

    public interface OnItemSelect {
        void onSelect(String title);
    }


    public AddPopWindow(Context context) {
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_pop, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        ListView lv = contentView.findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
        lv.setOnItemClickListener(this);
    }

    public void setSelectListener(OnItemSelect callBack) {
        this.callBack = callBack;
    }


    public void show(Activity activity) {
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    private class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return values.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_pop_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(values[position]);
            return convertView;
        }

        class ViewHolder {

            TextView tvTitle;

            public ViewHolder(View view) {
                tvTitle = view.findViewById(R.id.tv_title);
            }
        }

    }

}
