package nz.co.spaceapp.stellarviews.explore;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.SwipeTouchListenerRecyclerView;

/**
 * Created by Roberta on 10/04/2015.
 */
public class ExploreFragment extends Fragment {

    private RecyclerView mRecycler;
    private ArrayList<Discovery> mDiscoveries;
    private LinearLayoutManager mLayoutManager;
    private DiscoveryAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        //todo fetch from api in main activity
        mDiscoveries = new ArrayList<>();

        for (int i = 0; i < 20; ++i)
            mDiscoveries.add(new Discovery());

        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(false);

        SwipeTouchListenerRecyclerView touchListener =
                new SwipeTouchListenerRecyclerView(mRecycler);
        mRecycler.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
//        mRecycler.setOnScrollListener(touchListener.makeScrollListener());
        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(), "Clicked " + mDiscoveries.get(position), Toast.LENGTH_SHORT).show();

                    }
                }));

        mRecycler.setOnScrollListener(new SwipeRecyclerViewListener());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new DiscoveryAdapter(getActivity(), mRecycler, mDiscoveries);
        mRecycler.setAdapter(mAdapter);

        return rootView;
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private AdapterView.OnItemClickListener mListener;

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Activity context, AdapterView.OnItemClickListener listener) {
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
//                mListener.onItemClick(childView, view.getChildPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }
    }
}
