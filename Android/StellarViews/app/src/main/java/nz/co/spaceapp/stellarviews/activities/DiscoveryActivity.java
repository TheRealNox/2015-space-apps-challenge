package nz.co.spaceapp.stellarviews.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import nz.co.spaceapp.library.network.GetRequest;
import nz.co.spaceapp.library.network.HttpPostParams;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.library.view.DepthPageTransformer;
import nz.co.spaceapp.stellarviews.Authentication;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.Ratings;
import nz.co.spaceapp.stellarviews.adapters.DiscoverPagerAdapter;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoveryActivity extends ActionBarActivity {
    public static final String DISCOVERY = "discovery";
    private static final String TAG = DiscoveryActivity.class.getSimpleName();
    public static final String AUTHENTICATION = "auth";

    private ViewPager mPager;
    private DiscoverPagerAdapter mPagerAdapter;
    private Discovery mDiscovery;
    private List<Discovery> mDiscoveries = new ArrayList<>();
    private Authentication mAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        mDiscovery = getIntent().getExtras().getParcelable(DISCOVERY);
        mAuthentication = getIntent().getExtras().getParcelable(AUTHENTICATION);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new DiscoverPagerAdapter(getSupportFragmentManager(), mDiscoveries);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        requestMyDiscoveries();
    }

    private void requestMyDiscoveries() {
        GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.PREVIOUS_URL + mAuthentication.getAccessToken() + "&limit=5&image_id=" + mDiscovery.getId(), new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching previous imagerie");
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
                                    Log.d(TAG, "Discoveries=" + object.getJSONArray(RequestConstants.IMAGES).toString());

                                    Gson gson = new Gson();
                                    mDiscoveries.clear();
                                    mDiscoveries.add(mDiscovery);
                                    mDiscoveries.addAll(gson.<Collection<? extends Discovery>>fromJson(object.getJSONArray(RequestConstants.IMAGES).toString(), Discovery.getArrayType()));

                                    mPagerAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }, 2000);
                    }
                    else
                        Log.e(TAG, "error while fetching discoveries");
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }
}
