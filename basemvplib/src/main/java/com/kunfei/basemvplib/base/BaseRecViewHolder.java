package com.kunfei.basemvplib.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseRecViewHolder extends RecyclerView.ViewHolder {
    private View itemView;

    public BaseRecViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public View getItemView() {
        return itemView;
    }

    public <T> void setData(T item) {
    }
}
