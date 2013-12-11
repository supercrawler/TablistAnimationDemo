package com.example.testdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MyView extends ImageView {

    public MyView(Context context) {

        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    private float mScaleX;
    private float mScaleY;
    private boolean isScaleCanvas = false;

    public void setScaleCanvasEnable(boolean enable) {

        isScaleCanvas = enable;
    }

    public void setScale(float sx, float sy) {

        mScaleX = sx;
        mScaleY = sy;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (isScaleCanvas) {
            canvas.scale(mScaleX, mScaleY);
            Log.d("MyView", "dispatchDraw:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY);
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isScaleCanvas) {
            canvas.scale(mScaleX, mScaleY);
            Log.d("MyView", "onDraw:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY);
        }
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {

        if (isScaleCanvas) {
            canvas.scale(mScaleX, mScaleY);
            Log.d("MyView", "draw:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY);
        }
        super.draw(canvas);
    }
}
