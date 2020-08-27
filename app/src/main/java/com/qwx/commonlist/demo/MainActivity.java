package com.qwx.commonlist.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;

import android.os.Bundle;

import com.qwx.commonlist.demo.list.ListDemoActivity;
import com.qwx.commonlist.demo.list.mvp.DemoEmptyPresenter;
import com.qwx.commonlist.demo.list.mvp.DemoListPresenter;
import com.qwx.commonlist.lib.DecorationConfig;
import com.qwx.commonlist.lib.ListFragmentConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_list)
                .setOnClickListener(v -> {
                    ListFragmentConfig config = ListFragmentConfig.newBuilder()
                            .aClass(DemoListPresenter.class)
                            .isSupportRefresh(false)
                            .isSupportLoadMore(true)
                            .extraParams(null)
                            .isSupportEmpty(false)
                            .isAutoSubsribe(false)
                            .orientation(OrientationHelper.VERTICAL)
                            .viewBeanRes(R.layout.app_itemview_person)
                            .build();
                    ListDemoActivity.start(MainActivity.this, config);
                });


        findViewById(R.id.btn_empty)
                .setOnClickListener(v -> {
                    ListFragmentConfig config = ListFragmentConfig.newBuilder()
                            .aClass(DemoEmptyPresenter.class)
                            .isSupportEmpty(true)
                            .listEmptyRes(R.layout.layout_empty)
                            .build();
                    ListDemoActivity.start(MainActivity.this, config);
                });

        findViewById(R.id.btn_divider)
                .setOnClickListener(v -> {
                    DecorationConfig decorationConfig = new DecorationConfig.Builder()
                            .dividerColorRes(R.color.decoration_color)
                            .dividerHeight(2)
                            .leftOffset(16)
                            .rightOffset(16)
                            .build();
                    ListFragmentConfig config = ListFragmentConfig.newBuilder()
                            .aClass(DemoListPresenter.class)
                            .isSupportRefresh(false)
                            .isSupportLoadMore(true)
                            .extraParams(null)
                            .isSupportEmpty(false)
                            .isAutoSubsribe(false)
                            .decorationConfig(decorationConfig)
                            .orientation(OrientationHelper.VERTICAL)
                            .viewBeanRes(R.layout.app_itemview_person)
                            .build();
                    ListDemoActivity.start(MainActivity.this, config);
                });
    }
}