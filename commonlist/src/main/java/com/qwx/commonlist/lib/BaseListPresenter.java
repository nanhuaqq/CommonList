package com.qwx.commonlist.lib;


import com.qwx.basemvp.BasePresenter;

import java.util.Map;

/**
 * Created by qqin on 2018/12/20
 * <p>
 * email qqin@finbtc.net
 */
public interface BaseListPresenter extends BasePresenter {

    default void subscribe(){}

    default void unsubscribe(){}

    default void refreshRequest(){};

    default void loadMore(){};

    /**
     * 设置额外的参数
     */
    default void setExtraParams(Map extraParams){}
}