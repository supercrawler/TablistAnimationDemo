
package com.example.testdemo;

import com.nineoldandroids.animation.Animator;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private ImageView currrentTabView;
    private View mlistView;
    private RelativeLayout mMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainContent = (RelativeLayout) findViewById(R.id.main_content);
        currrentTabView = (ImageView) findViewById(R.id.img);
        mlistView = findViewById(R.id.img_cut);


    }


    public void runAnimation(View view) {

        boolean flag = false;
        if (flag) {

            ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f);
            scaleAnimation.setDuration(1000);
            currrentTabView.startAnimation(scaleAnimation);
        } else {

            final int tabScreenShotWidth = getResources().getDimensionPixelSize(
                    R.dimen.tablist_item_img_width);
            final int tabScreenShotHeight = getResources().getDimensionPixelSize(
                    R.dimen.tablist_item_img_height);
            final int imageWidth = currrentTabView.getWidth();
            final int imageHeight = currrentTabView.getHeight();
            final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

            final float scaleX = tabScreenShotWidth / (imageWidth * 1.0f);
            final float scaleY = tabScreenShotHeight / (imageHeight * 1.0f);
            final float posX = ViewHelper.getX(currrentTabView);
            final float poxY = ViewHelper.getY(currrentTabView);
            final int tablistHeight = mlistView.getHeight();

            ViewHelper.setPivotX(currrentTabView, imageWidth);
            ViewHelper.setPivotY(currrentTabView, imageHeight);

            // ViewHelper.setScaleX(currrentTabView, scaleX);
            // ViewHelper.setScaleY(currrentTabView, scaleX);
            // ViewHelper.setX(currrentTabView, 0);
            // ViewHelper.setY(currrentTabView, tabScreenShotHeight);

            // PropertyValuesHolder tabTranslationY =
            // PropertyValuesHolder.ofFloat("translationY",
            // screenHeight, screenHeight - tabScreenShotHeight);

            PropertyValuesHolder tabTranslationY = PropertyValuesHolder.ofFloat("translationY",
                    tabScreenShotHeight * 2, tabScreenShotHeight);
            ObjectAnimator tabAnimator1 = ObjectAnimator.ofPropertyValuesHolder(currrentTabView,
                    tabTranslationY);
            tabAnimator1.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {

                    ViewHelper.setScaleX(currrentTabView, scaleX);
                    ViewHelper.setScaleY(currrentTabView, scaleX);
                    // ViewHelper.setX(currrentTabView, 0);
                    ViewHelper.setY(currrentTabView, 0);

                }

                @Override
                public void onAnimationEnd(Animator arg0) {

                    int[] location = new int[2];
                    ViewHelper.getLocationInWindow(currrentTabView, location);
                    Log.d("onAnimationEnd:", "location X:" + location[0] + ";location Y:"
                            + location[1]);

                }
            });
            tabAnimator1.addUpdateListener(new AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    int[] location = new int[2];
                    ViewHelper.getLocationInWindow(currrentTabView, location);
                    Log.d("onAnimationUpdate:", "currrentTabView location X:" + location[0]
                            + ";location Y:" + location[1]);

                    Float translationY = (Float) valueAnimator.getAnimatedValue();
                    ViewHelper.getLocationInWindow(mlistView, location);
                    Log.d("onAnimationUpdate:", "mlistView location X:" + location[0]
                            + ";location Y:" + location[1] + ", translationY:" + translationY);

                }
            });

            // tabAnimator1.setDuration(1000);
            // tabAnimator1.start();
            // mlistView.setVisibility(View.INVISIBLE);

            tabTranslationY = PropertyValuesHolder.ofFloat("translationY", 0, -tabScreenShotHeight);
            ObjectAnimator tabAnimator2 = ObjectAnimator.ofPropertyValuesHolder(mlistView,
                    tabTranslationY);
            // tabAnimator2.setDuration(1000);
            // tabAnimator2.start();

            AnimatorSet animatorSet1 = new AnimatorSet();
            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet1.setInterpolator(new LinearInterpolator());
            animatorSet1.playTogether(tabAnimator1, tabAnimator2);
            animatorSet1.setDuration(500);
            animatorSet1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator arg0) {

                    animatorSet.start();
                }
            });
            animatorSet1.start();

            PropertyValuesHolder tabScaleX = PropertyValuesHolder.ofFloat("scaleX", scaleX, 1.0f);
            PropertyValuesHolder tabScaleY = PropertyValuesHolder.ofFloat("scaleY", scaleX, 1.0f);
            final ObjectAnimator tabAnimator3 = ObjectAnimator.ofPropertyValuesHolder(
                    currrentTabView, tabScaleX, tabScaleY);
            tabTranslationY = PropertyValuesHolder.ofFloat("translationY", -tabScreenShotHeight,
                    -tablistHeight);
            ObjectAnimator tabAnimator4 = ObjectAnimator.ofPropertyValuesHolder(mlistView,
                    tabTranslationY);

            animatorSet.setDuration(500);
            animatorSet.playTogether(tabAnimator3, tabAnimator4);
            animatorSet.setInterpolator(new LinearInterpolator());
            // animatorSet.play(tabAnimator3).after(10).with(tabAnimator4);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {

                    // ViewHelper.setX(currrentTabView, posX);
                    // ViewHelper.setY(currrentTabView, poxY);
                    // ViewHelper.setPivotX(currrentTabView, imageWidth);
                    // ViewHelper.setPivotY(currrentTabView, imageHeight );
                }
            });

            // animatorSet.start();

        }

    }

}
