package com.bs.lk.newoamvptest.util;

import com.bs.lk.newoamvptest.bean.AttendanceDataBean;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvertOrderListUtil {

    public static List<AttendanceDataBean> invertOrderList(List<AttendanceDataBean> L){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1;
        Date d2;
        AttendanceDataBean temp_r = new AttendanceDataBean();
        //这是一个冒泡排序，将大的放在数组前面
        for(int i=0; i<L.size()-1; i++){
            for(int j=i+1; j<L.size();j++){
                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                d1 = sdf.parse(L.get(i).getDate(), pos1);
                d2 = sdf.parse(L.get(j).getDate(), pos2);
                if(d1.before(d2)){//如果日期靠前，则换顺序
                    temp_r = L.get(i);
                    L.set(i, L.get(j));
                    L.set(j, temp_r);
                }
            }
        }
        return L;
    }
}
