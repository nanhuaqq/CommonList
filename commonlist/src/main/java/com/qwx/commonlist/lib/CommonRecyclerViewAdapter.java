package com.qwx.commonlist.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.qwx.basemvp.UsePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qqin on 2018/4/20 0020.
 * email qqin@finbtc.net
 */
public class CommonRecyclerViewAdapter<VB extends ViewBean> extends RecyclerView.Adapter<CommonRecyclerViewAdapter.ViewHolder<? extends ItemView>>{

    protected List<VB> dataList;
    protected Integer layoutResId;
    protected LayoutInflater mInflater;
    protected int deltaY;
    protected Object mExtraData;
    protected boolean isRecyclable = true;

    private int currentPage = 0;

    protected OnRecyclerViewItemClickListner onRecyclerItemClickListner;

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView rv = getParentRecyclerView(v);
            RecyclerView.ViewHolder vh = getViewHolder(rv, v);
            int flatPosition = vh.getAdapterPosition();
            if (flatPosition == RecyclerView.NO_POSITION) {
                return;
            }
            VB bean = dataList.get(flatPosition - deltaY);
            if (onRecyclerItemClickListner == null) {
                return;
            }
            onRecyclerItemClickListner.onItemClick(flatPosition);
            onRecyclerItemClickListner.onItemClick(v, rv, flatPosition - deltaY);
            onRecyclerItemClickListner.onItemClick(v, rv, flatPosition - deltaY, bean);
        }
    };


    public void setmExtraData(Object mExtraData) {
        this.mExtraData = mExtraData;
    }

    public OnRecyclerViewItemClickListner getOnRecyclerItemClickListner() {
        return onRecyclerItemClickListner;
    }

    public void setOnRecyclerItemClickListner(OnRecyclerViewItemClickListner onRecyclerItemClickListner) {
        this.onRecyclerItemClickListner = onRecyclerItemClickListner;
    }

    @Nullable
    public RecyclerView.ViewHolder getViewHolder(@Nullable RecyclerView rv, View view) {
        if (rv == null) {
            return null;
        }
        return rv.findContainingViewHolder(view);
    }

    public static RecyclerView getParentRecyclerView(@Nullable View view) {
        if (view == null) {
            return null;
        }
        ViewParent parent = view.getParent();
        if (parent instanceof RecyclerView) {
            return (RecyclerView) parent;
        } else if (parent instanceof View) {
            return getParentRecyclerView((View) parent);
        } else {
            return null;
        }
    }

    public CommonRecyclerViewAdapter(Integer layoutResId, Context mContext) {
        this(layoutResId, mContext, true);
    }

    public CommonRecyclerViewAdapter(Integer layoutResId, Context mContext, boolean isRecyclable) {
        this.isRecyclable = isRecyclable;
        this.dataList = new ArrayList<>();
        this.layoutResId = layoutResId;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(layoutResId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setIsRecyclable(this.isRecyclable);
        view.setOnClickListener(onClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemView itemView = holder.view;
        itemView.bindData(dataList.get(position));

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public List<VB> getDataList(){
        return dataList;
    }

    public static class ViewHolder<T extends ItemView> extends RecyclerView.ViewHolder {
        private T view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = (T) itemView;
        }
    }

    public void updateList(List<VB> list) {
        if (list == null) {
            return;
        }
        this.dataList.clear();
        this.dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendList(List<VB> list) {
        this.dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void appendVB(VB bean, boolean notify) {
        this.dataList.add(bean);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public void poll() {
        if (this.dataList == null || this.dataList.isEmpty()) {
            return;
        }
        this.dataList.remove(0);
    }

    public void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public VB getItem(int index) {
        if (index < dataList.size()) {
            return this.dataList.get(index);
        }
        return null;
    }

    public interface OnRecyclerViewItemClickListner<VB> extends UsePresenter<BaseListPresenter> {

        default void onItemClick(int position) {
        }

        default void onItemClick(View view, RecyclerView rv, int position) {
        }

        default void onItemClick(View view, RecyclerView rv, int position, VB data) {
        }

        default void attachPresenter(BaseListPresenter presenter) {
        }
    }
}
