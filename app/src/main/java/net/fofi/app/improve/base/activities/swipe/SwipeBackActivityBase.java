package net.fofi.app.improve.base.activities.swipe;

/**
 * Created by ZYY on 2018/2/26.
 */

public interface SwipeBackActivityBase {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
