package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.CustomDate;
import com.bs.lk.newoamvptest.bean.Schedual;
import java.util.ArrayList;
import java.util.List;



public class CalendarAdapter  extends BaseAdapter {
    private CustomDate mClickDate;
    private LayoutInflater mLayoutInflater;
    private String TABLE_NAME = "schedule.db";
    // 映射数据<泛型bean>
    List<Schedual> scheduals = new ArrayList<>();
    List<Schedual> schedualsSelect = new ArrayList<>();
    private Context mContext;
    public CalendarAdapter(Context context, CustomDate customDate,List<Schedual>scheduals) {
        mContext = context;
        mClickDate = customDate;
        this.scheduals = scheduals;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return scheduals.size();
    }

    @Override
    public Object getItem(int i) {
        return scheduals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //用viewholder 储存 findviewbyid的值，避免重复，提高了效率。
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            // 只将XML转化为View，不涉及具体布局，第二个参数设null
            view = mLayoutInflater.inflate(R.layout.list_item_text, null);
            holder.content = (TextView) view
                    .findViewById(R.id.tv_content);
            holder.startTime = (TextView) view
                    .findViewById(R.id.tv_start_time);
            holder.endTime = (TextView) view
                    .findViewById(R.id.tv_end_time);
            holder.isAllday = (TextView) view
                    .findViewById(R.id.tv_isAllday);
            //用tag储存viewholder,从而使convertView与holder关联
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        // 设置控件的数据
        holder.content.setText( scheduals.get(i).getContent());
        holder.startTime.setText( scheduals.get(i).getStartTime());
        holder.endTime.setText( scheduals.get(i).getEndTime());
        if (scheduals.get(i).getIsAllday()){
            holder.isAllday.setText( "全天");
        }else{
            holder.isAllday.setText( "不是全天");
        }

        return view;
    }
    // ViewHolder用于缓存控件
    class ViewHolder {
        public TextView content;
        public TextView startTime;
        public TextView endTime;
        public TextView isAllday;
    }
}
