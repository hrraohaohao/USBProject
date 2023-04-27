package com.hao.usbproject.adapter;

/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataBindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnItemLongClickListener<M> mOnItemLongClickListener;
    private OnTouchListener<M> mOnTouchListener;

    private List<M> mData = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<M> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setmOnTouchListener(OnTouchListener<M> mOnTouchListener) {
        this.mOnTouchListener = mOnTouchListener;
    }

    public List<M> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public BaseDataBindingAdapter(Context context) {
        this.mContext = context;
    }

    public void notifyData(List<M> mData) {
        if (mData == null) {
            this.mData = new ArrayList<>();
        } else {
            this.mData = mData;
        }
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(this.mContext), this.getLayoutResId(viewType), parent, false);
        BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = holder.getLayoutPosition();
                M item = getItem(position);
                if (item == null) {
                    return;
                }
                mOnItemClickListener.onItemClick(item, position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                int position = holder.getLayoutPosition();
                M item = getItem(position);
                if (item == null) {
                    return true;
                }
                mOnItemLongClickListener.onItemLongClick(item, position);
            }
            return false;
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mOnTouchListener != null) {
                    int position = holder.getLayoutPosition();
                    M item = getItem(position);
                    if (item == null) {
                        return true;
                    }
                    mOnTouchListener.onTouch(v, item, position, event);
                }
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        M item = getItem(position);
        if (item == null) {
            return;
        }
        this.onBindItem(binding, item, holder);
        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    private M getItem(int position) {
        if (mData == null || mData.size() <= position) {
            return null;
        }
        return mData.get(position);
    }

    protected abstract @LayoutRes
    int getLayoutResId(int viewType);

    /**
     * 注意：
     * RecyclerView 中的数据有位置改变（比如删除）时一般不会重新调用 onBindViewHolder() 方法，除非这个元素不可用。
     * 为了实时获取元素的位置，我们通过 ViewHolder.getBindingAdapterPosition() 方法。
     *
     * @param binding .
     * @param item    .
     * @param holder  .
     */
    protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }

    public interface OnTouchListener<M> {
        boolean onTouch(View v, M item, int position, MotionEvent event);
    }

}
