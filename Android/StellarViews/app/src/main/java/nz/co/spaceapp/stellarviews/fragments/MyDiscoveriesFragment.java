package nz.co.spaceapp.stellarviews.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nz.co.spaceapp.library.network.GetRequest;
import nz.co.spaceapp.library.network.HttpPostParams;
import nz.co.spaceapp.library.network.PostRequest;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.Ratings;
import nz.co.spaceapp.stellarviews.activities.MainActivity;
import nz.co.spaceapp.stellarviews.activities.RequestConstants;
import nz.co.spaceapp.stellarviews.activities.SplashActivity;
import nz.co.spaceapp.stellarviews.adapters.MyDiscoveryAdapter;

/**
 * Created by Roberta on 12/04/2015.
 */
public class MyDiscoveriesFragment extends Fragment {

    private static final String TAG = MyDiscoveriesFragment.class.getSimpleName();
    private GridView mGridView;
    private List<Ratings> mDiscoveries = new ArrayList<>();
    private MyDiscoveryAdapter mAdapter;
    private View mLoading;
    private View mLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_discoveries, container, false);

        mLoading = rootView.findViewById(R.id.loading);
        mLoader = rootView.findViewById(R.id.loader_ring);

        mGridView = (GridView) rootView.findViewById(R.id.grid_view);

        mAdapter = new MyDiscoveryAdapter(getActivity(), mDiscoveries);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });

        mLoading.setVisibility(View.VISIBLE);
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_ring);
        mLoader.startAnimation(rotation);

        requestMyDiscoveries();
        return rootView;
    }

    private void requestMyDiscoveries() {
        HttpPostParams params = new HttpPostParams();

        GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.RATINGS_URL + ((MainActivity)getActivity()).getAuthentication().getAccessToken(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoading.setVisibility(View.GONE);
                Log.e(TAG, "Error liking");
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    final JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(TAG, "Discoveries=" + object.getJSONArray(RequestConstants.RATINGS).toString());

                                    Gson gson = new Gson();
                                    mDiscoveries.clear();
                                    mDiscoveries.addAll(gson.<Collection<? extends Ratings>>fromJson(object.getJSONArray(RequestConstants.RATINGS).toString(), Ratings.getArrayType()));
                                    Collections.reverse(mDiscoveries);

                                    mAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }, 2000);
                    }
                    else
                        Log.e(TAG, "error while fetching discoveries");
                    mLoading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }
}
