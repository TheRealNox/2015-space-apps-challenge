package nz.co.spaceapp.stellarviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

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

    public SwipeTouchListenerRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        mSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {

                mDownX = motionEvent.getRawX();
                mDownY = motionEvent.getRawY();


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
                        mItemView = child; // This is your down view
                        break;
                    }
                }

                if (mItemView != null) {
                    mDownX = motionEvent.getRawX();
                    mItemPosition = mRecyclerView.getChildPosition(mItemView);
                }
                view.onTouchEvent(motionEvent);
                return false;
            }

            case MotionEvent.ACTION_CANCEL: {
                break;
            }

            case MotionEvent.ACTION_UP: {
                float deltaX = motionEvent.getRawX() - mDownX;

                if (mIsSwiping) {
                    if (Math.abs(deltaX) > mItemView.getWidth() / 4) {
                        mItemView.animate().translationX(mItemView.getWidth());

                        boolean swipeRight = false;

                        if (deltaX > 0)
                            swipeRight = true;

                        mItemView.animate()
                                .translationX(swipeRight ? mItemView.getWidth() : -mItemView.getWidth())
                                .alpha(0)
                                .setDuration(100)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                    }
                                });
                    } else
                        mItemView.animate().translationX(0);
                    mIsSwiping = false;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float deltaX = motionEvent.getRawX() - mDownX;
                float deltaY = motionEvent.getRawY() - mDownY;

                Log.d(TAG, "deltaX=" + (Math.abs(motionEvent.getRawX() - mDownX)));
                Log.d(TAG, "deltaY=" + (Math.abs(motionEvent.getRawX() - mDownY)));
                Log.d(TAG, "Slop=" + mSlop);

                if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    mRecyclerView.requestDisallowInterceptTouchEvent(true);
                    mItemView.setTranslationX(motionEvent.getRawX() - mDownX);
                    mIsSwiping = true;
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
