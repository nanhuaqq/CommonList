package com.qwx.commonlist.demo.list.mvp;

import com.qwx.commonlist.demo.list.repository.PersonRepository;
import com.qwx.commonlist.lib.BaseListPresenter;
import com.qwx.commonlist.lib.BaseListView;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import io.reactivex.disposables.Disposable;

/**
 * Created by qqin on 2020/8/27
 * <p>
 * email qqin@finbtc.net
 */
public class DemoListPresenter implements BaseListPresenter {

    private BaseListView mView;
    private Disposable mDis;

    public DemoListPresenter(BaseListView mView) {
        this.mView = mView;
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        subscribe();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        unsubscribe();
    }

    @Override
    public void subscribe() {
        refreshRequest();
    }

    @Override
    public void unsubscribe() {
        disPose(mDis);
    }

    @Override
    public void refreshRequest() {
        mDis = PersonRepository.INSTANCE.mockRequestFromServer(true)
                .subscribe(personBeans -> {
                        mView.finishRefresh();
                        mView.renderListData(personBeans, true, true);
                }, throwable -> {});
    }

    @Override
    public void loadMore() {
        mDis = PersonRepository.INSTANCE.mockLoadMore()
                .subscribe(personBeans -> {
                    mView.finishRefresh();
                    mView.renderListData(personBeans, false, false);
                }, throwable -> {
                    mView.finishRefresh();
                });
    }

    @Override
    public void setExtraParams(Map extraParams) {

    }
}
