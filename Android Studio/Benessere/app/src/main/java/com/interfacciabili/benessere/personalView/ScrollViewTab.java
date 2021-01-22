package com.interfacciabili.benessere.personalView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewTab extends ScrollView {
    public ScrollViewTab(Context context) {
        super(context);
        listener = (ScrollViewTabCallback) context;
    }

    public ScrollViewTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        listener = (ScrollViewTabCallback) context;
    }

    public ScrollViewTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        listener = (ScrollViewTabCallback) context;
    }

    public ScrollViewTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        listener = (ScrollViewTabCallback) context;
    }

    public interface ScrollViewTabCallback {
        public void swipeRightScrollView();
        public void swipeLeftScrollView();
    }
    public ScrollViewTabCallback listener;

    private float swipePositionDownX, swipePositionUpX;
    private int swipeMinDistance = 200;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                swipePositionDownX = event.getX();
            case MotionEvent.ACTION_UP:
                if (swipePositionDownX > 1) {
                    swipePositionUpX = event.getX();
                    float deltaX = swipePositionUpX - swipePositionDownX;
                    if (Math.abs(deltaX) > swipeMinDistance) {
                        if (swipePositionUpX > swipePositionDownX) {
                            listener.swipeRightScrollView();
                        } else {
                            listener.swipeLeftScrollView();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}