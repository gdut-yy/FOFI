package net.fofi.app.improve.base.activities.swipe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import net.fofi.app.R;

/**
 * Created by ZYY on 2018/2/26.
 */

public class SwipeBackActivityHelper {
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;

    SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(
                R.layout.swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {

            }
        });
    }

    void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
