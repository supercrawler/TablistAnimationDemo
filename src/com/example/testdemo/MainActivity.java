
package com.example.testdemo;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private ImageView currrentTabView;
    private View mlistView;
    private MyRelativeLayout mMainContent;

    private static final long PUSH_UP_TIME = 3000;
    private static final long ENLARGE_TIME = 500;
    private static final long ENLARGE_DELAY = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainContent = (MyRelativeLayout) findViewById(R.id.main_content);
        currrentTabView = (ImageView) findViewById(R.id.img);
        mlistView = findViewById(R.id.img_cut);

        mMainContent.setFitScaleXChild(currrentTabView);

    }

    private void resetViewProperties() {
        ViewHelper.setAlpha(mlistView, 1.0f);
        ViewHelper.setTranslationY(mlistView, 0);
        ViewHelper.setScaleX(currrentTabView, 1.0f);
        ViewHelper.setScaleY(currrentTabView, 1.0f);
        ViewHelper.setTranslationY(currrentTabView, 0);
        ViewHelper.removeViewAnimatorProxy(mlistView);
        ViewHelper.removeViewAnimatorProxy(currrentTabView);
    }


    private AnimatorSet set;

    public void runAnimation1(View view) {
        if (set != null && set.isRunning()) {
            set.cancel();
        } else {
            resetViewProperties();
        }
        final Resources res = getResources();
        final int tabScreenShotWidth = res.getDimensionPixelSize(R.dimen.tablist_item_img_width);
        final int tabScreenShotHeight = res.getDimensionPixelSize(R.dimen.tablist_item_img_height);
        final int imageWidth = currrentTabView.getWidth();
        final int imageHeight = currrentTabView.getHeight();

        final float scaleX = tabScreenShotWidth / (imageWidth * 1.0f);
        final float scaleY = tabScreenShotHeight / (imageHeight * 1.0f);

        ViewHelper.setPivotX(currrentTabView, imageWidth);
        ViewHelper.setPivotY(currrentTabView, imageHeight);

        ViewHelper.setScaleX(currrentTabView, scaleX);
        ViewHelper.setScaleY(currrentTabView, scaleX);

        long pushUp = PUSH_UP_TIME;
        long enlarge = pushUp * (imageHeight - tabScreenShotHeight) / tabScreenShotHeight;
        long delay = pushUp;

        final float p = ((float) pushUp) / (pushUp + enlarge);
        final float pd = (imageHeight * scaleY) / (imageHeight * scaleX);

        ObjectAnimator tabTransY = ObjectAnimator.ofFloat(currrentTabView, "translationY", imageHeight * scaleX, 0);
        tabTransY.setInterpolator(new Interpolator() {

            @Override
            public float getInterpolation(float input) {
                if (input < p) {
                    return pd / p * input;
                } else {
                    return (1 - pd) / (1 - p) * input + (pd - p) / (1 - p);
                }

            }
        });
        tabTransY.setDuration(pushUp + enlarge);

        ObjectAnimator tabEnlargeX = ObjectAnimator.ofFloat(currrentTabView, "scaleX", scaleX, 1.0f);
        tabEnlargeX.setDuration(enlarge);
        tabEnlargeX.setStartDelay(delay);

        ObjectAnimator tabEnlargeY = ObjectAnimator.ofFloat(currrentTabView, "scaleY", scaleX, 1.0f);
        tabEnlargeY.setDuration(enlarge);
        tabEnlargeY.setStartDelay(delay);

        ObjectAnimator listPushUpY = ObjectAnimator.ofFloat(mlistView, "translationY", 0, - imageHeight * scaleY);
        listPushUpY.setDuration(pushUp);

        ObjectAnimator listFadeOut = ObjectAnimator.ofFloat(mlistView, "alpha", 1.0f, 0f);
        listFadeOut.setDuration(enlarge);
        listFadeOut.setStartDelay(delay);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(tabTransY, listPushUpY, tabEnlargeX, tabEnlargeY, listFadeOut);
        set.start();

        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                resetViewProperties();
            }
        });
        this.set = set;

    }


    public void runAnimation(View view) {
        if (set != null && set.isRunning()) {
            set.cancel();
        } else {
            resetViewProperties();
        }
        final Resources res = getResources();
        final int tabScreenShotWidth = res.getDimensionPixelSize(R.dimen.tablist_item_img_width);
        final int tabScreenShotHeight = res.getDimensionPixelSize(R.dimen.tablist_item_img_height);
        final int imageWidth = currrentTabView.getWidth();
        final int imageHeight = currrentTabView.getHeight();

        final float scaleX = tabScreenShotWidth / (imageWidth * 1.0f);
        final float scaleY = tabScreenShotHeight / (imageHeight * 1.0f);

        ViewHelper.setPivotX(currrentTabView, imageWidth);
        ViewHelper.setPivotY(currrentTabView, imageHeight);

        ViewHelper.setScaleX(currrentTabView, scaleX);
        ViewHelper.setScaleY(currrentTabView, scaleX);

        long pushUp = PUSH_UP_TIME;
        long enlarge = pushUp * (imageHeight - tabScreenShotHeight) / tabScreenShotHeight;
        long delay = pushUp -50;

        ObjectAnimator tabTransY = ObjectAnimator.ofFloat(currrentTabView, "translationY", imageHeight * scaleX, imageHeight * scaleX - imageHeight * scaleY);
        tabTransY.setDuration(pushUp);

        ObjectAnimator listPushUpY = ObjectAnimator.ofFloat(mlistView, "translationY", 0, - imageHeight * scaleY);
        listPushUpY.setDuration(pushUp);

        ObjectAnimator tabEnlargeX = ObjectAnimator.ofFloat(currrentTabView, "scaleX", scaleX, 1.0f);
        tabEnlargeX.setDuration(enlarge);
        tabEnlargeX.setStartDelay(delay);

        ObjectAnimator tabEnlargeY = ObjectAnimator.ofFloat(currrentTabView, "scaleY", scaleX, 1.0f);
        tabEnlargeY.setDuration(enlarge);
        tabEnlargeY.setStartDelay(delay);

        ObjectAnimator tabTransY2 = ObjectAnimator.ofFloat(currrentTabView, "translationY", imageHeight * scaleX - imageHeight * scaleY, 0);
        tabTransY2.setDuration(enlarge);
        tabTransY2.setStartDelay(delay);

        ObjectAnimator listFadeOut = ObjectAnimator.ofFloat(mlistView, "alpha", 1.0f, 0f);
        listFadeOut.setDuration(enlarge);
        listFadeOut.setStartDelay(delay);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(tabTransY, listPushUpY, tabEnlargeX, tabEnlargeY, tabTransY2, listFadeOut);
//        set.playTogether(tabTransY, listPushUpY, tabEnlargeX, tabEnlargeY, listFadeOut);
        set.start();

        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                resetViewProperties();
            }
        });
        this.set = set;

    }

}
