package com.kunfei.basemvplib.base;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecycleView的统一定制Adapter（方便统一添加header View和foot View）
 *
 * @author XHui.sun
 *         created at 2017/10/26 0026  18:05
 */
public class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecViewHolder> {
    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public BaseRecycleAdapter(Context context) {
        mContext = context;
    }

    public void addDatas(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public BaseRecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //此处要被覆盖的 lxl
        //View v = LayoutInflater.from(mContext).inflate(R.layout.layout_recyclerview_item_view, parent, false);
        return new BaseRecViewHolder(null);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecViewHolder holder, int position) {
        holder.setData(getDatas().get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


}
