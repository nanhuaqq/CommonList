package com.qwx.commonlist.demo.list.mvp;

import android.app.Person;

import com.qwx.commonlist.demo.list.repository.PersonBean;
import com.qwx.commonlist.lib.BaseListPresenter;
import com.qwx.commonlist.lib.BaseListView;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.Observable;

/**
 * Created by qqin on 2020/8/27
 * <p>
 * email qqin@finbtc.net
 */
public class DemoEmptyPresenter implements BaseListPresenter {

    private BaseListView mView;

    public DemoEmptyPresenter(BaseListView mView) {
        this.mView = mView;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        subscribe();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void subscribe() {
        refreshRequest();
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void refreshRequest() {
        Observable.just(new ArrayList<PersonBean>())
                .subscribe(personBeans -> {
                    mView.finishRefresh();
                    mView.renderListData(personBeans, true, false);
                }, throwable -> {
                    mView.finishRefresh();
                });
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void setExtraParams(Map extraParams) {

    }
}
