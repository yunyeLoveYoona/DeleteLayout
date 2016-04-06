package com.yun.deletelayout.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DeleteRelativelayout extends RelativeLayout {
    private int x;
    private View view;
    private TextView goneView;
    private ViewDragHelper mDragHelper;
    private OnClickListener mainViewOnClickListener;
    private boolean isMove = false;

    public DeleteRelativelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
        goneView = new TextView(context);
        goneView.setText("删除");
        goneView.setTextColor(Color.WHITE);
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        goneView.setLayoutParams(layoutParams);
        addView(goneView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        goneView.setTextSize(getMeasuredWidth() / 10 > 15 ? (getMeasuredWidth() / 10 > 30 ? 30
                : getMeasuredWidth() / 10)
                : 15);
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == getChildAt(1);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                if (left > 0 - goneView.getWidth()) {
                    return left;
                }
                int l = child.getLeft();
                return 0 - goneView.getWidth();
            }
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == getChildAt(1)) {
                if (getChildAt(1).getLeft() > (0 - goneView.getWidth()) / 2) {
                    close();
                } else if (getChildAt(1).getLeft() < (0 - goneView.getWidth()) / 2) {
                    open();
                }

            }
        }

    }

    private void close() {
        getChildAt(1).layout(0, 0, getChildAt(1).getWidth(), getChildAt(1).getHeight());
    }

    private void open() {
        getChildAt(1).layout(0 - goneView.getWidth(), 0, getChildAt(1).getWidth() - goneView.getWidth()
                , getChildAt(1).getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            isMove = true;
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (!isMove) {
                if (mainViewOnClickListener != null) {
                    mainViewOnClickListener.onClick(view);
                }
            }
            isMove = false;
        }

        return true;
    }

    public void setOnDelClickListener(OnClickListener onDelClickListener) {
        goneView.setOnClickListener(onDelClickListener);
    }

    public void setDelTag(Object tag) {
        goneView.setTag(tag);
    }

    public void setMainViewOnClickListener(OnClickListener onClickListener) {
        mainViewOnClickListener = onClickListener;
    }

    public void setMainViewCilckTag(Object tag){
        getChildAt(1).setTag(tag);
    }


}
