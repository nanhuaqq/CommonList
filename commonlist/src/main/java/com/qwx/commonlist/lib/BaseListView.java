package com.qwx.commonlist.lib;

import com.qwx.basemvp.BaseView;

import java.util.List;

/**
 * Created by qqin on 2018/12/20
 * <p>
 * email qqin@finbtc.net
 */
public interface BaseListView extends BaseView {
    void renderListData(List<? extends ViewBean> viewBeans, boolean isRefresh, boolean hasMore);

    default void renderHeaderView(ViewBean viewBean){}

    default void showEmptyView(){};

    default void clearList(){};

    default void finishRefresh(){};
}
