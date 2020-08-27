package com.qwx.basemvp;


/**
 * Created by qqin on 2018/12/22
 * <p>
 * email qqin@finbtc.net
 */
public interface UsePresenter<T extends BasePresenter> {
    default void attachPresenter(T presenter) {}
}
