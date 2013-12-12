
package com.example.testdemo;

import com.nineoldandroids.view.ViewHelper;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

    public MyRelativeLayout(Context context) {

        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {

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
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = false;
        boolean updateCanvas = child == mFitScaleXView;
        Log.d("MyRelativeLayout", "===drawing child");
        if (updateCanvas) {
            canvas.save();
            final float scaleX = ViewHelper.getScaleX(child);
            final float scaleY = ViewHelper.getScaleY(child);
            final float scaleDelta = scaleX / scaleY;
            Log.d("MyRelativeLayout", "===drawing child fitscaleXView scaleX: " + scaleX + " scaleY: " + scaleY + " scaleDelta: " + scaleDelta);
            canvas.scale(1.0f, scaleDelta, child.getWidth(), child.getHeight());
        }
        result = super.drawChild(canvas, child, drawingTime);
        if (updateCanvas) {
            canvas.restore();
        }
        return result;
    }


    private View mFitScaleXView;
    public void setFitScaleXChild(ImageView currrentTabView) {
        mFitScaleXView = currrentTabView;
    }
}
