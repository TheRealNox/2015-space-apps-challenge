package nz.co.spaceapp.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Roberta on 10/04/2015.
 */
public class SwipeTouchListenerRecyclerView implements View.OnTouchListener {
    private static final String TAG = SwipeTouchListenerRecyclerView.class.getSimpleName();

    private final RecyclerView mRecyclerView;

    private float mDownX;
    private float mDownY;
    private View mItemView;
    private int mItemPosition;
    private boolean mIsSwiping = false;
    private float mSlop;
    private VelocityTracker mVelocityTracker;
    private DismissCallback mCallback;
    private boolean mEnabled;
    private int mSwipeViewId = -1;
    private View mSwipeView = null;
    private SwipeListener mSwipeListener;
    private int mDismissAnimationRefCount = 0;
    private List<PendingDismissData> mPendingDismisses = new ArrayList<>();

    public SwipeTouchListenerRecyclerView(RecyclerView recyclerView, DismissCallback callback) {
        mRecyclerView = recyclerView;
        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        mSlop = vc.getScaledTouchSlop();
        mCallback = callback;
    }

    /**
     *
     * @param resId
     */
    public void setSwipeView(int resId) {
        mSwipeViewId = resId;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mEnabled)
                    return false;


                Rect rect = new Rect();
                int childCount = mRecyclerView.getChildCount();
                int[] listViewCoords = new int[2];

                mRecyclerView.getLocationOnScreen(listViewCoords);

                int x = (int) motionEvent.getRawX() - listViewCoords[0];
                int y = (int) motionEvent.getRawY() - listViewCoords[1];

                View child;
                for (int i = 0; i < childCount; i++) {
                    child = mRecyclerView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mItemView = child;

                        if (mSwipeViewId != -1)
                            mSwipeView = child.findViewById(mSwipeViewId);
                        else
                            mSwipeView = mItemView;
                        break;
                    }
                }

                if (mItemView != null) {
                    mDownX = motionEvent.getRawX();
                    mDownY = motionEvent.getRawY();
                    mItemPosition = mRecyclerView.getChildPosition(mItemView);
                }
                view.onTouchEvent(motionEvent);
                mVelocityTracker = VelocityTracker.obtain();
                return false;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (mItemView != null && mIsSwiping) {
                    mSwipeView.animate()
                            .translationX(0)
                            .alpha(1)
                            .setDuration(200)
                            .setListener(null);
                    mItemView.animate()
                            .translationX(0)
                            .alpha(1)
                            .setDuration(200)
                            .setListener(null);
                }
                mDownX = 0;
                mDownY = 0;
                mItemView = null;
                mItemPosition = ListView.INVALID_POSITION;
                mIsSwiping = false;
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (mVelocityTracker == null)
                    break;

                float deltaX = motionEvent.getRawX() - mDownX;

                if (mIsSwiping) {
                    if (Math.abs(deltaX) > mItemView.getWidth() / 4 && mItemPosition != ListView.INVALID_POSITION) {
                        ++mDismissAnimationRefCount;
                        boolean swipeRight = false;

                        if (deltaX > 0)
                            swipeRight = true;

                        final View itemView = mItemView;
                        final View swipeView = mSwipeView;
                        final int originalHeight = itemView.getHeight();
                        final int itemPosition = mItemPosition;

                        mSwipeView.animate()
                                .translationX(swipeRight ? mItemView.getWidth() : -mItemView.getWidth())
                                .alpha(0)
                                .setDuration(200)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        performDismiss(itemView, swipeView, itemPosition);
                                    }
                                });
                    } else
                        mSwipeView.animate().translationX(0);
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mDownY = 0;
                mItemView = null;
                mSwipeView = null;
                mItemPosition = ListView.INVALID_POSITION;
                mIsSwiping = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (!mEnabled || mItemView == null)
                    break;

                float deltaX = motionEvent.getRawX() - mDownX;
                float deltaY = motionEvent.getRawY() - mDownY;

//                Log.d(TAG, "deltaX=" + (Math.abs(motionEvent.getRawX() - mDownX)));
//                Log.d(TAG, "deltaY=" + (Math.abs(motionEvent.getRawY() - mDownY)));

                if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    float swipingSlop = (deltaX > 0 ? mSlop : -mSlop);

                    if (mSwipeListener != null)
                        mSwipeListener.onSwipe(mItemView, deltaX > 0, Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX) / mItemView.getWidth())));

                    mRecyclerView.requestDisallowInterceptTouchEvent(true);
                    mSwipeView.setTranslationX(deltaX - swipingSlop);
                    mIsSwiping = true;
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public RecyclerView.OnScrollListener makeScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                setEnabled(newState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        };
    }

    private void performDismiss(final View dismissView, final View swipeView, final int dismissPosition) {

        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalHeight = dismissView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(200);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                --mDismissAnimationRefCount;
                if (mDismissAnimationRefCount == 0) {
                    // No active animations, process all pending dismisses.
                    // Sort by descending position
                    Collections.sort(mPendingDismisses);

                    int[] dismissPositions = new int[mPendingDismisses.size()];
                    for (int i = mPendingDismisses.size() - 1; i >= 0; i--) {
                        dismissPositions[i] = mPendingDismisses.get(i).position;
                    }

                    if (mCallback != null)
                        mCallback.onDismiss(mRecyclerView, dismissPositions);

                    // Reset mDownPosition to avoid MotionEvent.ACTION_UP trying to start a dismiss
                    // animation with a stale position
                    mItemPosition = ListView.INVALID_POSITION;

                    ViewGroup.LayoutParams lp;
                    for (PendingDismissData pendingDismiss : mPendingDismisses) {

                        // Reset view presentation
                        pendingDismiss.view.setAlpha(1f);
                        pendingDismiss.view.setTranslationX(0);
                        lp = pendingDismiss.view.getLayoutParams();
                        lp.height = originalHeight;
                        pendingDismiss.view.setLayoutParams(lp);

                        if (pendingDismiss.mSwipeView != null) {
                            pendingDismiss.mSwipeView.setAlpha(1f);
                            pendingDismiss.mSwipeView.setTranslationX(0);
                            lp = pendingDismiss.mSwipeView.getLayoutParams();
                            lp.height = originalHeight;
                            pendingDismiss.mSwipeView.setLayoutParams(lp);
                        }
                    }

                    // Send a cancel event
                    long time = SystemClock.uptimeMillis();
                    MotionEvent cancelEvent = MotionEvent.obtain(time, time,
                            MotionEvent.ACTION_CANCEL, 0, 0, 0);
                    mRecyclerView.dispatchTouchEvent(cancelEvent);
                    mPendingDismisses.clear();
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);

                if (swipeView != null) {
                    final ViewGroup.LayoutParams lpSwipe = swipeView.getLayoutParams();

                    lpSwipe.height = (Integer) valueAnimator.getAnimatedValue();
                    swipeView.setLayoutParams(lpSwipe);
                }
            }
        });

        mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView, swipeView));
        animator.start();
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;
        public View mSwipeView;

        public PendingDismissData(int position, View view, View swipeView) {
            this.position = position;
            this.view = view;
            mSwipeView = swipeView;
        }

        @Override
        public int compareTo(PendingDismissData other) {
            // Sort by descending position
            return other.position - position;
        }
    }

    public void setOnSwipeListener(SwipeListener listener) {
        mSwipeListener = listener;
    }

    public interface SwipeListener {
        void onSwipe(View view, boolean swipeRight, float progress);
    }

    public interface DismissCallback {
        void onDismiss(RecyclerView recyclerView, int[] position);
    }
}
