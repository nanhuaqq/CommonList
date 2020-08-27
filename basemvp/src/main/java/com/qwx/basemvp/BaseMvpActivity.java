package com.qwx.basemvp;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

/**
 * Created by qqin on 2019/9/10
 * <p>
 * email qqin@finbtc.net
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends AppCompatActivity implements LifecycleOwner {
    protected LifecycleRegistry mLifecycleRegistry;
    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLifecycleRegistry = new LifecycleRegistry(this);
        mPresenter= initPresenter();
        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);
        }
        setLayout();

        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        //公共逻辑初始化完成后，一些跟业务相关的初始化放在这里
        initWork();

    }

    protected  void initWork(){};



    protected abstract void setLayout();



    protected abstract T initPresenter();


    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

}
