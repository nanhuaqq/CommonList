package com.qwx.commonlist.lib;

/**
 * Created by qqin on 2018/4/20 0020.
 * email qqin@finbtc.net
 */
public interface ItemView<T extends ViewBean> {

    void bindData(T bean);
}
