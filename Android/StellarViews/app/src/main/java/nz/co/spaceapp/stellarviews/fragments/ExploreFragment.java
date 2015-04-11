package nz.co.spaceapp.stellarviews.fragments;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nz.co.spaceapp.library.view.SwipeTouchListenerRecyclerView;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.activities.DiscoveryActivity;
import nz.co.spaceapp.stellarviews.activities.MainActivity;
import nz.co.spaceapp.stellarviews.adapters.DiscoveryAdapter;

/**
 * Created by Roberta on 10/04/2015.
 */
public class ExploreFragment extends Fragment implements SwipeTouchListenerRecyclerView.DismissCallback, SwipeTouchListenerRecyclerView.SwipeListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private RecyclerView mRecycler;
    private ArrayList<Discovery> mDiscoveries;
    private LinearLayoutManager mLayoutManager;
    private DiscoveryAdapter mAdapter;
    private View mLike;
    private View mDislike;
    private boolean mIsSwiping = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        mDiscoveries = ((MainActivity)getActivity()).getDiscoveries();

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(false);

        SwipeTouchListenerRecyclerView touchListener = new SwipeTouchListenerRecyclerView(mRecycler, this);
        touchListener.setOnSwipeListener(this);
        touchListener.setSwipeView(R.id.swipe_view);

        mRecycler.setOnTouchListener(touchListener);
        mRecycler.setOnScrollListener(touchListener.makeScrollListener());
        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(getActivity(), DiscoveryActivity.class);

                        i.putExtra(DiscoveryActivity.DISCOVERY, mDiscoveries.get(mRecycler.getChildPosition(view)));

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().startActivity(i);
                        else
                            getActivity().startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }
                }));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new DiscoveryAdapter(getActivity(), mRecycler, mDiscoveries);
        mRecycler.setAdapter(mAdapter);

        return rootView;

    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] positions) {
        for (int position : positions)
            mDiscoveries.remove(position);

        mAdapter.notifyDataSetChanged();
        mLike.setVisibility(View.INVISIBLE);
        mDislike.setVisibility(View.INVISIBLE);
        mIsSwiping = false;
    }

    @Override
    public void onSwipe(View view, boolean swipeRight, float progress) {

        mLike = view.findViewById(R.id.like);
        mDislike = view.findViewById(R.id.dislike);

        if (swipeRight) {
            mLike.setVisibility(View.VISIBLE);
            mLike.setAlpha(1 - progress);
        } else {
            mDislike.setVisibility(View.VISIBLE);
            mDislike.setAlpha(1 - progress);
        }
        mIsSwiping = true;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                if (!mIsSwiping)
                    mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }
}
