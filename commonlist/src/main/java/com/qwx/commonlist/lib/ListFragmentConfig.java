package com.qwx.commonlist.lib;


import java.io.Serializable;
import java.util.Map;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;

public class ListFragmentConfig implements Serializable {

    private int orientation = OrientationHelper.VERTICAL;

    private boolean isSupportLoadMore = true;

    private boolean isSupportRefresh = true;

    private boolean isSupportEmpty = false;

    private boolean isAutoSubscribe = true;

    @NonNull
    @LayoutRes
    private int viewBeanRes;

    @NonNull
    @LayoutRes
    private int listHeaderRes;

    @NonNull
    @LayoutRes
    private int listEmptyRes;

    DecorationConfig decorationConfig;

    private Class<? extends BaseListPresenter> aClass;

    private Map extraParams;


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getViewBeanRes() {
        return viewBeanRes;
    }

    public void setViewBeanRes(int viewBeanRes) {
        this.viewBeanRes = viewBeanRes;
    }

    public int getListHeaderRes() {
        return listHeaderRes;
    }

    public void setListHeaderRes(int listHeaderRes) {
        this.listHeaderRes = listHeaderRes;
    }

    public Class<? extends BaseListPresenter> getaClass() {
        return aClass;
    }

    public void setaClass(Class<? extends BaseListPresenter> aClass) {
        this.aClass = aClass;
    }

    public boolean isSupportLoadMore() {
        return isSupportLoadMore;
    }

    public void setSupportLoadMore(boolean supportLoadMore) {
        isSupportLoadMore = supportLoadMore;
    }

    public boolean isSupportRefresh() {
        return isSupportRefresh;
    }

    public void setSupportRefresh(boolean supportRefresh) {
        isSupportRefresh = supportRefresh;
    }

    public boolean isSupportEmpty() {
        return isSupportEmpty;
    }

    public void setSupportEmpty(boolean supportEmpty) {
        isSupportEmpty = supportEmpty;
    }

    public Map getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map extraParams) {
        this.extraParams = extraParams;
    }

    public boolean isAutoSubscribe() {
        return isAutoSubscribe;
    }

    public void setAutoSubscribe(boolean autoSubscribe) {
        isAutoSubscribe = autoSubscribe;
    }

    @NonNull
    public int getListEmptyRes() {
        return listEmptyRes;
    }

    public void setListEmptyRes(@NonNull int listEmptyRes) {
        this.listEmptyRes = listEmptyRes;
    }

    public DecorationConfig getDecorationConfig() {
        return decorationConfig;
    }

    public void setDecorationConfig(DecorationConfig decorationConfig) {
        this.decorationConfig = decorationConfig;
    }

    private ListFragmentConfig(Builder builder) {
        setOrientation(builder.orientation);
        setSupportLoadMore(builder.isSupportLoadMore);
        setSupportRefresh(builder.isSupportRefresh);
        setViewBeanRes(builder.viewBeanRes);
        setListHeaderRes(builder.listHeaderRes);
        setaClass(builder.aClass);
        setSupportEmpty(builder.isSupportEmpty);
        setExtraParams(builder.extraParams);
        setAutoSubscribe(builder.isAutoSubscribe);
        setListEmptyRes(builder.listEmptyRes);
        setDecorationConfig(builder.decorationConfig);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private int orientation;
        private boolean isSupportLoadMore;
        private boolean isSupportRefresh;

        @LayoutRes
        private int viewBeanRes;

        @LayoutRes
        private int listHeaderRes;

        @LayoutRes
        private int listEmptyRes;

        private boolean isSupportEmpty;
        private Class<? extends BaseListPresenter> aClass;
        private Map extraParams;
        private boolean isAutoSubscribe = true;
        private DecorationConfig decorationConfig;

        private Builder() {
        }

        public Builder orientation(int val) {
            orientation = val;
            return this;
        }

        public Builder isSupportLoadMore(boolean val) {
            isSupportLoadMore = val;
            return this;
        }

        public Builder isSupportRefresh(boolean val) {
            isSupportRefresh = val;
            return this;
        }

        public Builder viewBeanRes(int val) {
            viewBeanRes = val;
            return this;
        }

        public Builder listHeaderRes(int val) {
            listHeaderRes = val;
            return this;
        }

        public Builder listEmptyRes(int val) {
            listEmptyRes = val;
            return this;
        }

        public Builder aClass(Class<? extends BaseListPresenter> val) {
            aClass = val;
            return this;
        }

        public Builder isSupportEmpty(boolean val) {
            isSupportEmpty = val;
            return this;
        }

        public Builder extraParams(Map val) {
            extraParams = val;
            return this;
        }


        public Builder isAutoSubsribe(boolean val) {
            isAutoSubscribe = val;
            return this;
        }

        public Builder decorationConfig(DecorationConfig config) {
            this.decorationConfig = config;
            return this;
        }

        public ListFragmentConfig build() {
            return new ListFragmentConfig(this);
        }
    }
}
