
package com.example.testdemo;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ImageView currrentTabView;
    private View mTabList;
    private RelativeLayout mMainContent;

    private static final long PUSH_UP_TIME = 500;
    private static final long ENLARGE_TIME = 500;
    private static final long ENLARGE_DELAY = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainContent = (RelativeLayout) findViewById(R.id.main_content);
        currrentTabView = (ImageView) findViewById(R.id.img);
        mTabList = findViewById(R.id.img_cut);

        // mMainContent.setFitScaleXChild(currrentTabView);

    }

    private void resetViewProperties() {

        ViewHelper.setAlpha(mTabList, 1.0f);
        ViewHelper.setTranslationY(mTabList, 0);
        ViewHelper.setScaleX(currrentTabView, 1.0f);
        ViewHelper.setScaleY(currrentTabView, 1.0f);
        ViewHelper.setTranslationY(currrentTabView, 0);
        ViewHelper.removeViewAnimatorProxy(mTabList);
        ViewHelper.removeViewAnimatorProxy(currrentTabView);
    }

    private AnimatorSet mTablistAnimatorSet;
    private float mTranslationYFraction = 0;
    private float mCurrentTabTranslationYOffset;
    private float mTranslationYCount;
    private float mTranslationXFraction = 0;
    private float mCurrentTabTranslationXOffset;
    private float mTranslationXCount;
    public void runAnimation(View view) {

        if (mTablistAnimatorSet != null && mTablistAnimatorSet.isRunning()) {
            return;
        }

        final Resources res = getResources();
        final int tabScreenShotWidth = res.getDimensionPixelSize(R.dimen.tablist_item_img_width);
        final int tabScreenShotHeight = res.getDimensionPixelSize(R.dimen.tablist_item_img_height);
        final int screenWidth = mMainContent.getWidth();
        final int screenHeight = mMainContent.getHeight();
        Log.d(TAG, "ScreenWidth:" + screenWidth + ", ScreenHeight:" + screenHeight);

        final float scaleX = tabScreenShotWidth / (screenWidth * 1.0f);
        final float scaleY = tabScreenShotHeight / (screenHeight * 1.0f);

        ViewHelper.setPivotX(currrentTabView, screenWidth);
        ViewHelper.setPivotY(currrentTabView, screenHeight);

        if (isLandscape()) {
            mTablistAnimatorSet = createLandscapeAnimatorSet(scaleY, tabScreenShotWidth,
                    screenWidth, PUSH_UP_TIME);
        } else {
            mTablistAnimatorSet = createVerticalAnimatorSet(scaleX, tabScreenShotHeight,
                    screenHeight, PUSH_UP_TIME);
        }
        mTablistAnimatorSet.start();
    }

    private AnimatorSet createLandscapeAnimatorSet(final float scaleY, final int tablistItemWidth,
            final int screenWidth, long duration) {

        ViewHelper.setScaleX(currrentTabView, scaleY);
        ViewHelper.setScaleY(currrentTabView, scaleY);
        int imageHeight = currrentTabView.getHeight();

        mCurrentTabTranslationYOffset = imageHeight * scaleY;
        ViewHelper.setTranslationY(currrentTabView, -mCurrentTabTranslationYOffset);
        final float currentTabViewScaleWidth = screenWidth * scaleY;
        ObjectAnimator tablistTransXAnimator = ObjectAnimator.ofFloat(mTabList, "translationX", 0,
                -screenWidth);
        tablistTransXAnimator.setDuration(duration);
        tablistTransXAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                Float tablistTransX = (Float) valueAnimator.getAnimatedValue();

                if (Math.abs(tablistTransX.floatValue()) >= tablistItemWidth) {
                    if (mTranslationXFraction == 0) {
                        mTranslationXFraction = valueAnimator.getAnimatedFraction();
                        mTranslationXCount = 1 - mTranslationXFraction;
                    }

                    float mapFraction = (valueAnimator.getAnimatedFraction() - mTranslationXFraction)
                            / mTranslationXCount;

                    float currentTabTransX = (1 - mapFraction) * mCurrentTabTranslationXOffset;
                    float currentTabTransY = -(1 - mapFraction) * mCurrentTabTranslationYOffset;
                    float currentTabScaleTo = scaleY + mapFraction * (1 - scaleY);
                    Log.d(TAG,
                            "onAnimationUpdate: translationY:" + tablistTransX.floatValue()
                                    + ", scaleTo: " + currentTabScaleTo + ":Fraction:"
                                    + valueAnimator.getAnimatedFraction() + ",mapFraction:"
                                    + mapFraction);

                    float width = currentTabScaleTo * currrentTabView.getWidth();
                    float plus = Math.abs(tablistTransX) - width;

                    ViewHelper.setScaleX(currrentTabView, currentTabScaleTo);
                    ViewHelper.setScaleY(currrentTabView, currentTabScaleTo);
                    // ViewHelper.setTranslationX(currrentTabView,
                    // currentTabTransX);
                    ViewHelper.setTranslationX(currrentTabView, Math.abs(plus));
                    ViewHelper.setTranslationY(currrentTabView, currentTabTransY);
                } else {
                    mCurrentTabTranslationXOffset = currentTabViewScaleWidth + tablistTransX;
                    ViewHelper.setTranslationX(currrentTabView, currentTabViewScaleWidth
                            + tablistTransX);
                }
            }
        });

        ObjectAnimator tablistFadeOutAnimator = ObjectAnimator.ofFloat(mTabList, "alpha", 1.0f, 0f);
        tablistFadeOutAnimator.setDuration(duration);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(tablistTransXAnimator, tablistFadeOutAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {

                ViewHelper.setAlpha(mTabList, 1.0f);
                ViewHelper.setTranslationX(mTabList, 0);
                ViewHelper.setScaleX(currrentTabView, 1.0f);
                ViewHelper.setScaleY(currrentTabView, 1.0f);
                ViewHelper.setTranslationX(currrentTabView, 0);
                ViewHelper.removeViewAnimatorProxy(mTabList);
                ViewHelper.removeViewAnimatorProxy(currrentTabView);
            }
        });

        return set;

    }
    private AnimatorSet createVerticalAnimatorSet(final float scaleX, final int tablistItemHeight,
            final int screenHeight, long duration) {

        ViewHelper.setScaleX(currrentTabView, scaleX);
        ViewHelper.setScaleY(currrentTabView, scaleX);

        final float currentTabViewScaleHeight = screenHeight * scaleX;
        ObjectAnimator tablistTransYAnimator = ObjectAnimator.ofFloat(mTabList, "translationY", 0,
                -screenHeight);
        tablistTransYAnimator.setDuration(duration);
        tablistTransYAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                Float tablistTransY = (Float) valueAnimator.getAnimatedValue();

                if (Math.abs(tablistTransY.floatValue()) >= tablistItemHeight) {
                    if (mTranslationYFraction == 0) {
                        mTranslationYFraction = valueAnimator.getAnimatedFraction();
                        mTranslationYCount = 1 - mTranslationYFraction;
                    }

                    float mapFraction = (valueAnimator.getAnimatedFraction() - mTranslationYFraction)
                            / mTranslationYCount;

                    float mapRate = Math.abs(tablistTransY) / screenHeight;
                    float currentTabTransY = (1 - mapFraction) * mCurrentTabTranslationYOffset;
                    float currentTabScaleTo = scaleX + mapFraction * (1 - scaleX);
                    Log.d(TAG, "onAnimationUpdate: translationY:" + tablistTransY.floatValue()
                                    + ", scaleTo: " + currentTabScaleTo + ":Fraction:"
                                    + valueAnimator.getAnimatedFraction() + ",mapFraction:"
                                    + mapFraction + ",mapRate:" + mapRate);

                    ViewHelper.setScaleX(currrentTabView, currentTabScaleTo);
                    ViewHelper.setScaleY(currrentTabView, currentTabScaleTo);
                    float height = currentTabScaleTo * currrentTabView.getHeight();
                    float plus = Math.abs(tablistTransY) - height;
                    Log.d(TAG,
                            "translation:currrentTabView:"
                                    + ViewHelper.getTranslationY(currrentTabView)
                                    + ",translationY:" + tablistTransY + ",real Height:"
                                    + currrentTabView.getHeight() + ",height:" + height
 + ", plus:"
                                    + plus + ", currentTabTransY:" + currentTabTransY);
                    // ViewHelper.setTranslationY(currrentTabView,
                    // currentTabTransY);
                    ViewHelper.setTranslationY(currrentTabView, Math.abs(plus));
                } else {
                    mCurrentTabTranslationYOffset = currentTabViewScaleHeight + tablistTransY;
                    ViewHelper.setTranslationY(currrentTabView, currentTabViewScaleHeight
                            + tablistTransY);
                }
            }
        });

        ObjectAnimator tablistFadeOutAnimator = ObjectAnimator.ofFloat(mTabList, "alpha", 1.0f, 0f);
        tablistFadeOutAnimator.setDuration(duration);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(tablistTransYAnimator, tablistFadeOutAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator arg0) {

            }

            @Override
            public void onAnimationEnd(Animator arg0) {

            }

            @Override
            public void onAnimationCancel(Animator arg0) {

                ViewHelper.setAlpha(mTabList, 1.0f);
                ViewHelper.setTranslationY(mTabList, 0);
                ViewHelper.setScaleX(currrentTabView, 1.0f);
                ViewHelper.setScaleY(currrentTabView, 1.0f);
                ViewHelper.setTranslationY(currrentTabView, 0);
                ViewHelper.removeViewAnimatorProxy(mTabList);
                ViewHelper.removeViewAnimatorProxy(currrentTabView);
                // resetViewProperties();
            }
        });

        return set;
    }

    public void runAnimation1(View view) {

        if (mTablistAnimatorSet != null && mTablistAnimatorSet.isRunning()) {
            mTablistAnimatorSet.cancel();
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
        long enlarge = pushUp * (imageHeight - tabScreenShotHeight) / imageHeight;
        long delay = pushUp - 50;

        ObjectAnimator tabTransY = ObjectAnimator.ofFloat(currrentTabView, "translationY",
                imageHeight * scaleX, imageHeight * scaleX - imageHeight * scaleY);
        tabTransY.setDuration(pushUp);

        ObjectAnimator listPushUpY = ObjectAnimator.ofFloat(mTabList, "translationY", 0,
                -imageHeight * scaleY);
        listPushUpY.setDuration(pushUp);

        ObjectAnimator tabEnlargeX = ObjectAnimator
                .ofFloat(currrentTabView, "scaleX", scaleX, 1.0f);
        tabEnlargeX.setDuration(enlarge);
        tabEnlargeX.setStartDelay(delay);

        ObjectAnimator tabEnlargeY = ObjectAnimator
                .ofFloat(currrentTabView, "scaleY", scaleX, 1.0f);
        tabEnlargeY.setDuration(enlarge);
        tabEnlargeY.setStartDelay(delay);

        ObjectAnimator tabTransY2 = ObjectAnimator.ofFloat(currrentTabView, "translationY",
                imageHeight * scaleX - imageHeight * scaleY, 0);
        tabTransY2.setDuration(enlarge);
        tabTransY2.setStartDelay(delay);

        ObjectAnimator listFadeOut = ObjectAnimator.ofFloat(mTabList, "alpha", 1.0f, 0f);
        listFadeOut.setDuration(enlarge);
        listFadeOut.setStartDelay(delay);

        ObjectAnimator listTransY2 = ObjectAnimator.ofFloat(mTabList, "translationY", -imageHeight
                * scaleY, imageHeight * scaleY - imageHeight);
        listTransY2.setDuration(enlarge);
        listTransY2.setStartDelay(delay);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(tabTransY, listPushUpY, tabEnlargeX, tabEnlargeY, tabTransY2, listFadeOut,
                listTransY2);
        // set.playTogether(tabTransY, listPushUpY, tabEnlargeX, tabEnlargeY,
        // listFadeOut);
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
        this.mTablistAnimatorSet = set;

    }

    private boolean isLandscape() {

        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
