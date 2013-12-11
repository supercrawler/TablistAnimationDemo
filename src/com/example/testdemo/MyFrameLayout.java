
package com.example.testdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

    public MyFrameLayout(Context context) {

        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyle) {

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
    public void draw(Canvas canvas) {

        Log.d("MyFrameLayout", "draw");
        Log.d("MyFrameLayout", "drawChild:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY
                + ",isScaleCanvas:" + isScaleCanvas);

        super.draw(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        Log.d("MyFrameLayout", "drawChild");
        Log.d("MyFrameLayout", "drawChild:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY
                + ",isScaleCanvas:" + isScaleCanvas);

        if (isScaleCanvas) {
            canvas.scale(mScaleX, mScaleY);
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d("MyFrameLayout", "dispatchDraw");
        Log.d("MyFrameLayout", "drawChild:mScaleX:" + mScaleX + ",mScaleY:" + mScaleY
                + ",isScaleCanvas:" + isScaleCanvas);

        super.dispatchDraw(canvas);
    }
}
