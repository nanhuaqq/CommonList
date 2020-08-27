package com.qwx.commonlist.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorRes;
import androidx.annotation.IntDef;
import androidx.recyclerview.widget.RecyclerView;

public class RVItemSplitDecoration extends RecyclerView.ItemDecoration {

    public static final int STYLE_COMMON = 0x01;
    public static final int STYLE_NO_FIRST_DIVIDER = 0x11;
    public static final int STYLE_NO_SECOND_DIVIDER = 0x111;

    @IntDef({STYLE_COMMON,STYLE_NO_FIRST_DIVIDER,STYLE_NO_SECOND_DIVIDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleType{}

    @StyleType
    private int mStyle;

    public Context context;
    private int normalDividerHeight;
    public Paint normalDividerPaint;
    float mLeftOffet;
    float mRightOffset;
    int mLastItemBottomOffset = 24;

    public RVItemSplitDecoration(Context context) {
        this(context, 0, 0, STYLE_COMMON);
    }

    public float getmLeftOffet() {
        return mLeftOffet;
    }

    public float getmRightOffset() {
        return mRightOffset;
    }

    public int getmLastItemBottomOffset() {
        return mLastItemBottomOffset;
    }

    public void setmLastItemBottomOffset(int mLastItemBottomOffset) {
        this.mLastItemBottomOffset = mLastItemBottomOffset;
    }

    public RVItemSplitDecoration(Context context, float leftOffset, float rightOffset, @StyleType int style) {
        this(context,leftOffset,rightOffset,1, R.color.decoration_color,style);
    }

    public RVItemSplitDecoration(Context context, float leftOffset, float rightOffset, int dividerHeight, @ColorRes int dividerColorRes, @StyleType int style) {
        this.context = context;
        //绘制分割线
        normalDividerPaint = new Paint(Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        normalDividerPaint.setColor(context.getResources().getColor(dividerColorRes));
        //定义分割线的高度
        normalDividerHeight = dividerHeight;
        this.mLeftOffet = leftOffset;
        this.mRightOffset = rightOffset;
        mStyle =style;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int childCount = 0;
        int bottom = 0;
        if (parent.getAdapter() != null && parent.getAdapter().getItemCount() > 0) {
            childCount = parent.getAdapter().getItemCount();
        }
        if (childCount - 1 == position) {
            bottom = mLastItemBottomOffset;
        }
        if (position == 0) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.set(0, normalDividerHeight, 0, bottom);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            /*分割线绘制在该条目位置的顶部，所以该条目的底部top+normalDividerHeight位置为分割线的顶部top，向下偏移normalDividerHeight
            的高度作为分割线的高度*/
            int top = view.getTop();
            //绘制分割线
            drawLine(c, i, left, top, right);
        }
    }

    private void drawLine(Canvas canvas, int position, int left, int top, int right) {
        if (position == 0) {
            return;
        }
        if (position == 1) {
            if ((mStyle & STYLE_NO_FIRST_DIVIDER) == STYLE_NO_FIRST_DIVIDER) {
                return;
            }
        }
        if (position == 2) {
            if ((mStyle & STYLE_NO_SECOND_DIVIDER) == STYLE_NO_SECOND_DIVIDER) {
                return;
            }
        }
        canvas.drawRect(left + mLeftOffet, top - normalDividerHeight, right - mRightOffset, top, normalDividerPaint);
    }
}
