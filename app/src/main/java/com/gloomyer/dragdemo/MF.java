package com.gloomyer.dragdemo;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 *
 */
public class MF extends FrameLayout {
    private RecyclerView rv;
    SwipeRefreshLayout swr;
    private TextView tv;
    private boolean topIsShow;

    public MF(Context context) {
        this(context, null);
    }

    public MF(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        downY = -1;
        topIsShow = false;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        rv = (RecyclerView) findViewById(R.id.rv);
        swr = (SwipeRefreshLayout) findViewById(R.id.mswr);
        tv = (TextView) findViewById(R.id.tv);
    }

    float downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE: {

                if (downY == -1) {
                    //第一次开始收到事件
                    downY = ev.getY();
                    return true;
                }

                float moveY = ev.getY();
                int dY = (int) (moveY - downY);


                FrameLayout.LayoutParams layoutParams = (LayoutParams) swr.getLayoutParams();

                int top = dY;

                if (!topIsShow) {
                    if (top <= tv.getHeight() && top > 0) {
                        layoutParams.topMargin = top;
                        swr.setLayoutParams(layoutParams);
                        l("top:" + top);
                    } else {
                        swr.onTouchEvent(ev);
                    }
                } else {
                    l("top:" + top + ",topMargin:" + layoutParams.topMargin);
                    top /= 3;
                    top += layoutParams.topMargin;
                    if (top > 0 && top <= tv.getHeight()) {
                        layoutParams.topMargin = top;
                        swr.setLayoutParams(layoutParams);
                    }
                }


                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                setTopViewStatus();
                swr.onTouchEvent(ev);
                downY = -1;
                break;
            }
        }
        return true;
    }

    private void setTopViewStatus() {
        FrameLayout.LayoutParams layoutParams = (LayoutParams) swr.getLayoutParams();
        if (layoutParams.topMargin >= tv.getHeight() / 2) {
            layoutParams.topMargin = tv.getHeight();
            topIsShow = true;
        } else {
            layoutParams.topMargin = 0;
            topIsShow = false;
        }
        swr.setLayoutParams(layoutParams);
    }


    float inDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                inDownY = ev.getY();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float inMoveY = ev.getY();
                int dY = (int) (inMoveY - inDownY);
                if (dY > 0 && isTop() && !topIsShow) {
                    return true;
                }

                if (dY < 0 && isTop() && topIsShow) {
                    return true;
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    private boolean isTop() {
        View child = rv.getChildAt(0);
        int pos = rv.getChildPosition(child);
        if (pos == 0) {
            return true;
        }
        return false;
    }

    private void l(String text) {
        Log.i("MF", text);
    }
}
