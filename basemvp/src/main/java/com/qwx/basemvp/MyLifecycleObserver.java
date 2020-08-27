package com.qwx.basemvp;


import androidx.lifecycle.DefaultLifecycleObserver;

/**
 * Created by qqin on 2019/9/10
 * <p>
 * email qqin@finbtc.net
 */
public interface MyLifecycleObserver extends DefaultLifecycleObserver {

    default void onCreateView(){}

    default void onDestroyView(){}

}
