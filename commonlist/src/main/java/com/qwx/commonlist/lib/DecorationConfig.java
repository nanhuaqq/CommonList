package com.qwx.commonlist.lib;

import android.content.Context;

import java.io.Serializable;

import androidx.annotation.ColorRes;

/**
 * Created by ceeto on 19-1-19.
 * 为RecycleView设置分割线的参数类
 */

public class DecorationConfig implements Serializable {

    int dividerHeight;
    float leftOffset;
    float rightOffset;
    @ColorRes
    int dividerColorRes;

    @RVItemSplitDecoration.StyleType
    int style;

    private DecorationConfig() {
    }

    public RVItemSplitDecoration createLinearItemDecoration(Context context){
        return new RVItemSplitDecoration(context,leftOffset,rightOffset,dividerHeight,dividerColorRes,style);
    }

    public static class Builder {

        private int dividerHeight = 1;
        private float leftOffset = 0;
        private float rightOffset = 0;

        private @ColorRes
        int dividerColorRes = R.color.decoration_color;

        @RVItemSplitDecoration.StyleType
        private int style = RVItemSplitDecoration.STYLE_COMMON;

        public Builder() {
        }

        public Builder leftOffset(float offset) {
            this.leftOffset = offset;
            return this;
        }

        public Builder rightOffset(float offset) {
            this.rightOffset = offset;
            return this;
        }

        public Builder dividerHeight(int height) {
            this.dividerHeight = height;
            return this;
        }

        public Builder dividerColorRes(@ColorRes int colorRes){
            dividerColorRes=colorRes;
            return this;
        }

        public Builder style(@RVItemSplitDecoration.StyleType int style) {
            this.style = style;
            return this;
        }

        public DecorationConfig build() {
            DecorationConfig config = new DecorationConfig();
            config.leftOffset = leftOffset;
            config.rightOffset = rightOffset;
            config.dividerHeight = dividerHeight;
            config.dividerColorRes = dividerColorRes;
            return config;
        }
    }
}
