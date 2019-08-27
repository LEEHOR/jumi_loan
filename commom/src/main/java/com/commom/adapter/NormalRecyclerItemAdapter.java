package com.commom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commom.R;

import java.util.List;

/**
 * 作者： zxb
 * 时间： 2018/4/11
 * 描述：
 */

public class NormalRecyclerItemAdapter extends RecyclerView.Adapter<NormalRecyclerItemAdapter.MyViewHolder> {

    private List<String> list;//存放数据
    private Context context;

    public NormalRecyclerItemAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_normal_rc, parent, false));
        return holder;
    }


    /**
     * 在这里可以获得每个子项里面的控件的实例，比如TextView,子项本身的实例是itemView，
     * holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
     *
     * @param holder
     * @param position 是点击位置
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        //设置textView显示内容为list里的对应项


//        holder.textView.setText(list.get(position));

        int color = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);

        holder.itemView.setBackgroundColor(color);

    }

    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_normal_rc);
        }
    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
    public boolean addItem(int position, String msg) {
        if (position < list.size() && position >= 0) {
            list.add(position, msg);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }
}
