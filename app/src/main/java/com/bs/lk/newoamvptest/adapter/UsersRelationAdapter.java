package com.bs.lk.newoamvptest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.bs.lk.newoamvptest.CApplication;
import com.bs.lk.newoamvptest.R;
import com.bs.lk.newoamvptest.bean.UserNewBean;
import com.bs.lk.newoamvptest.util.BitmapUtil;

import java.util.List;

/**
 * 切换账号适配器
 * @author lk
 */
public class UsersRelationAdapter extends RecyclerView.Adapter<UsersRelationAdapter.ViewHolder> {
    private List<UserNewBean> mFruitList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout llItemWeidoor;
        ImageView fruitImage;
        CheckedTextView fruitName;
        public ViewHolder(View itemView) {
            super(itemView);
            llItemWeidoor = (ConstraintLayout) itemView.findViewById(R.id.view_item_weidoor);
            fruitImage = (ImageView) itemView.findViewById(R.id.item_weidoor_imageview);
            fruitName = (CheckedTextView) itemView.findViewById(R.id.item_weidoor_title);
        }
    }

    public UsersRelationAdapter(Context context, List<UserNewBean> mFruitList) {
        this.context = context;
        this.mFruitList = mFruitList;
    }
    /**
     * Item的回调接口
     *
     */
    public interface OnItemClickListener {
        /**
         * 单击某条数据的监听
         * @param view
         * @param position
         */
        void onItemClickListener(View view, int position);
    }

    /**
     * 点击Item的回调对象
     */
    private OnItemClickListener listener;

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     *创建ViewHolder的实例
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_relation,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    /**
     * 用于对RecyclerView子项的数据进行赋值
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
               UserNewBean userCurrent = mFruitList.get(position);

        Bitmap bp1 = BitmapUtil.GetUserImageByNickName(context, userCurrent.getUserName());
        holder.fruitImage.setImageBitmap(bp1);
        UserNewBean currentUser = CApplication.getInstance().getCurrentUser();
        holder.fruitName.setText(userCurrent.getDeptname());
        if (currentUser.getDeptname().equals(userCurrent.getDeptname())){
            holder.fruitName.setChecked(true);
        }else {
            holder.fruitName.setChecked(false);
        }

        if (listener != null) {
            holder.llItemWeidoor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v, position);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
