package com.nalazoocare.coordinatorlayout;

import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-08-21
 */
public class QuickReturnFooterBehavior extends CoordinatorLayout.Behavior<View> {


    private static final FastOutSlowInInterpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final long ANIMATION_DURATION = 200;
    private int dyDirectionSum;
    private boolean isShowing;
    private boolean isHiding;

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        //스크롤이 반대방향으로 전환
        if (dy > 0 && dyDirectionSum < 0 || dy < 0 && dyDirectionSum > 0) {
            child.animate().cancel();
            dyDirectionSum = 0;
        }

        dyDirectionSum += dy;

        if (dyDirectionSum > child.getHeight()) {
            hideView(child);
        } else if (dyDirectionSum < -child.getHeight()) {
            showView(child);
        }
    }

    private void showView(final View view) {

        if (isShowing || view.getVisibility() == View.VISIBLE) {
            return;
        }


        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_DURATION);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShowing = true;
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isShowing = false;
                hideView(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void hideView(final View view) {
        if (isHiding || view.getVisibility() != View.VISIBLE) {
            return;
        }


        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_DURATION);


        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isHiding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                isHiding = false;
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isHiding = false;
                showView(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }


}
