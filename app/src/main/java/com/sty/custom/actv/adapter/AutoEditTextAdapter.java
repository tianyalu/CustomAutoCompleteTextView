package com.sty.custom.actv.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.sty.custom.actv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tian on 2018/7/21.
 */

public class AutoEditTextAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<String> dataList;
    private ArrayList<String> mUnfilteredData;
    private ArrayFilter mFilter;

    private MyOnItemClickListener listener;

    public interface MyOnItemClickListener{
        void onItemClicked(String str);
    }

    public MyOnItemClickListener getListener() {
        return listener;
    }

    public void setListener(MyOnItemClickListener listener) {
        this.listener = listener;
    }

    public AutoEditTextAdapter(Context context, List<String> dataList){
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList == null ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        MyViewHolder holder;
        if(convertView == null){
            view = View.inflate(mContext, R.layout.item_auto_complete_view, null);
            holder = new MyViewHolder();
            holder.tvItem = view.findViewById(R.id.tv_item);

            view.setTag(holder);
        }else{
            view = convertView;
            holder = (MyViewHolder) view.getTag();
        }

        holder.tvItem.setText(dataList.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClicked(dataList.get(position));
                }
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        if(mFilter == null){
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private static class MyViewHolder{
        private TextView tvItem;
    }

    private class ArrayFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if(mUnfilteredData == null){
                mUnfilteredData = new ArrayList<>(dataList);
            }

            if(prefix == null || prefix.length() == 0){
                ArrayList<String> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            }else{
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<String> unFilteredValues = mUnfilteredData;
                int count = unFilteredValues.size();

                ArrayList<String> newValues = new ArrayList<>(count);

                for(int i = 0; i < count; i++){
                    String pc = unFilteredValues.get(i);
                    if(!TextUtils.isEmpty(pc) && pc.startsWith(prefixString)){
                        newValues.add(pc);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList = (List<String>) results.values;
            if(results.count > 0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated(); //通知数据观察者当前所关联的数据源已经无效或者不能获得了，一旦触发了这个方法当前的adapter就变得无效了，也不应该报告自己的数据改变了
            }
        }
    }


}
