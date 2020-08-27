package com.qwx.basemvp;

import io.reactivex.disposables.Disposable;

/**
 * Created by qqin on 2018/3/5 0005.
 * email qqin@finbtc.net <br/>
 *
 * mvp中 presenter基本接口
 */
public interface BasePresenter extends MyLifecycleObserver {

    default void disPose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    void subscribe();
    void unsubscribe();
}
