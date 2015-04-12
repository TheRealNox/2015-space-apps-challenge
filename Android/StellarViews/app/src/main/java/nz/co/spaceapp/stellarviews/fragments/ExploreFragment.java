package nz.co.spaceapp.stellarviews.fragments;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nz.co.spaceapp.library.network.GetRequest;
import nz.co.spaceapp.library.network.HttpPostParams;
import nz.co.spaceapp.library.network.PostRequest;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.library.view.SwipeTouchListenerRecyclerView;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.activities.DiscoveryActivity;
import nz.co.spaceapp.stellarviews.activities.MainActivity;
import nz.co.spaceapp.stellarviews.activities.RequestConstants;
import nz.co.spaceapp.stellarviews.adapters.DiscoveryAdapter;

/**
 * Created by Roberta on 10/04/2015.
 */
public class ExploreFragment extends Fragment implements SwipeTouchListenerRecyclerView.DismissCallback, SwipeTouchListenerRecyclerView.SwipeListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private RecyclerView mRecycler;
    private ArrayList<Discovery> mDiscoveries;
    private DiscoveryAdapter mAdapter;
    private View mLike;
    private View mDislike;
    private boolean mIsSwiping = false;
    private boolean mDidLike = false;
    private HashMap<Discovery, Boolean> mLikedDiscovery = new HashMap<>();
    private View mComeAgain;
    private View mLoading;
    private View mLoader;
    private int mPendingRequestDone = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

        mDiscoveries = ((MainActivity) getActivity()).getDiscoveries();

        mLoading = rootView.findViewById(R.id.loading);
        mLoader = rootView.findViewById(R.id.loader_ring);

        mComeAgain = rootView.findViewById(R.id.come_again);
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
                        i.putExtra(DiscoveryActivity.AUTHENTICATION, ((MainActivity)getActivity()).getAuthentication());

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                            getActivity().startActivity(i);
                        else
                            getActivity().startActivity(i, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                    }
                }));

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new DiscoveryAdapter(getActivity(), mRecycler, mDiscoveries);
        mRecycler.setAdapter(mAdapter);

        return rootView;

    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] positions) {
        for (int position : positions) {

            if (mDidLike)
                mLikedDiscovery.put(mDiscoveries.get(position), true);
            else
                mLikedDiscovery.put(mDiscoveries.get(position), false);
            mDiscoveries.remove(position);
        }

        mAdapter.notifyDataSetChanged();
        mLike.setVisibility(View.INVISIBLE);
        mDislike.setVisibility(View.INVISIBLE);
        mIsSwiping = false;

        if (mDiscoveries.isEmpty())
            processEmptyList();
    }

    private void processEmptyList() {
        sendDislikedDiscoveries();
        sendLikedDiscoveries();
        mLoading.setVisibility(View.VISIBLE);
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_ring);
        mLoader.startAnimation(rotation);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestDiscoveries();
            }
        }, 3000);
    }

    @Override
    public void willBeDismiss() {
        mLike.animate().alpha(255).setDuration(200).start();
        mDislike.animate().alpha(255).setDuration(200).start();
    }

    @Override
    public void onSwipe(View view, boolean swipeRight, float progress) {

        mLike = view.findViewById(R.id.like);
        mDislike = view.findViewById(R.id.dislike);

        if (swipeRight) {
            mLike.setVisibility(View.VISIBLE);
            mLike.setAlpha(1 - progress);
            mDidLike = true;
        } else {
            mDislike.setVisibility(View.VISIBLE);
            mDislike.setAlpha(1 - progress);
            mDidLike = false;
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

    @Override
    public void onStop() {
        super.onStop();

        sendLikedDiscoveries();
        sendDislikedDiscoveries();
    }

    private void sendDislikedDiscoveries() {
        if (mLikedDiscovery.isEmpty())
            return;

        HttpPostParams params = new HttpPostParams();

        ArrayList<InterestingDiscovery> discoveries = new ArrayList<>();

        for (Map.Entry pair : mLikedDiscovery.entrySet()) {
            if (!((Boolean) pair.getValue()))
                discoveries.add(new InterestingDiscovery(((Discovery) pair.getKey()).getId(), 0));
        }

        Gson gson = new Gson();
        params.addParameters(RequestConstants.AUTH_TOKEN, ((MainActivity) getActivity()).getAuthentication().getAccessToken());
        params.addParameters(RequestConstants.DISCOVERY_ID, gson.toJson(discoveries));
        PostRequest request = new PostRequest(RequestConstants.BASE_URL + RequestConstants.RATE_URL, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error liking");
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Iterator it = mLikedDiscovery.entrySet().iterator();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();

                            if (!((Boolean) pair.getValue()))
                                it.remove();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }

    private void sendLikedDiscoveries() {
        if (mLikedDiscovery.isEmpty())
            return;

        HttpPostParams params = new HttpPostParams();

        ArrayList<InterestingDiscovery> discoveries = new ArrayList<>();

        for (Map.Entry pair : mLikedDiscovery.entrySet()) {
            if (((Boolean) pair.getValue()))
                discoveries.add(new InterestingDiscovery(((Discovery) pair.getKey()).getId(), 1));
        }

        Gson gson = new Gson();
        params.addParameters(RequestConstants.AUTH_TOKEN, ((MainActivity) getActivity()).getAuthentication().getAccessToken());
        params.addParameters(RequestConstants.DISCOVERY_ID, gson.toJson(discoveries));
        PostRequest request = new PostRequest(RequestConstants.BASE_URL + RequestConstants.RATE_URL, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error disliking");
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Iterator it = mLikedDiscovery.entrySet().iterator();
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();

                            if (((Boolean) pair.getValue()))
                                it.remove();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }

    private void requestDiscoveries() {
        GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.DISCOVERIES_URL + ((MainActivity) getActivity()).getAuthentication().getAccessToken(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error while fetching discoveries");
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    final JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        Gson gson = new Gson();

                        mDiscoveries.clear();
                        mDiscoveries.addAll(gson.<Collection<? extends Discovery>>fromJson(object.getJSONArray(RequestConstants.IMAGES).toString(), Discovery.getArrayType()));
                        mAdapter.notifyDataSetChanged();

                        mLoading.setVisibility(View.GONE);
                        if (mDiscoveries.isEmpty())
                            mComeAgain.setVisibility(View.VISIBLE);

                    } else
                        Log.e(TAG, "error while fetching discoveries");
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }

    private class InterestingDiscovery {
        @SerializedName("image_id")
        protected int mId;

        @SerializedName("is_interesting")
        protected int mInteresting = 0;

        public InterestingDiscovery(int id, int interesting) {
            mId = id;
            mInteresting = interesting;
        }
    }
}
