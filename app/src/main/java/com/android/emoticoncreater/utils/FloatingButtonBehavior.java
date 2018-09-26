package com.android.emoticoncreater.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * 浮动按钮 behavior
 */

public class FloatingButtonBehavior extends FloatingActionButton.Behavior {

    private final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;

    public FloatingButtonBehavior() {
        super();
    }

    public FloatingButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@androidx.annotation.NonNull androidx.coordinatorlayout.widget.CoordinatorLayout coordinatorLayout, @androidx.annotation.NonNull FloatingActionButton child, @androidx.annotation.NonNull View directTargetChild, @androidx.annotation.NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@androidx.annotation.NonNull androidx.coordinatorlayout.widget.CoordinatorLayout coordinatorLayout, @androidx.annotation.NonNull FloatingActionButton child, @androidx.annotation.NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && !mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            show(child);
        }
    }

    private void hide(FloatingActionButton button) {
        ViewCompat.animate(button).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        mIsAnimatingOut = true;
                    }

                    public void onAnimationCancel(View view) {
                        mIsAnimatingOut = false;
                    }

                    public void onAnimationEnd(View view) {
                        mIsAnimatingOut = false;
                        view.setVisibility(View.INVISIBLE);
                    }
                }).start();
    }

    private void show(FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        ViewCompat.animate(button).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start();
    }
}
