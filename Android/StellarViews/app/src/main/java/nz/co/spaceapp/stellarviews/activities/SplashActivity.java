package nz.co.spaceapp.stellarviews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nz.co.spaceapp.library.image.ImageManager;
import nz.co.spaceapp.library.network.GetRequest;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.stellarviews.Authentication;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.SharedPreferencesManager;

/**
 * Created by Roberta on 11/04/2015.
 */
public class SplashActivity extends ActionBarActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Authentication mAuthentication;
    private View mErrorMessage;
    private View mLoader;
    private View mLogo;
    private boolean mCanRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        mLoader = findViewById(R.id.loader_ring);
        mLogo = findViewById(R.id.space_app_logo);

        SharedPreferencesManager.getInstance().initialize(SplashActivity.this);
        RequestManager.getInstance().initialize(SplashActivity.this);
        ImageManager.getInstance().initialize(SplashActivity.this, getSupportFragmentManager(), 0.5f);

        mAuthentication = SharedPreferencesManager.getInstance().getAuthenticationInfo();
        mErrorMessage = findViewById(R.id.something_wrong);

        if (!mAuthentication.isValid()) {
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.loading_ring);
            mLoader.startAnimation(rotation);

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                    startActivity(i);
                    finish();
                }
            }, 2000);
            return;
        }

        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanRequest)
                    requestDiscoveries();
            }
        });
        requestDiscoveries();
    }

    private void requestDiscoveries() {
        mLoader.animate().rotation(30).start();
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.loading_ring);
        mLoader.startAnimation(rotation);
        mErrorMessage.setVisibility(View.GONE);

        GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.DISCOVERIES_URL + mAuthentication.getAccessToken(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error while fetching discoveries");
                handleError(error);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    final JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        mCanRequest = false;
                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(TAG, "Discoveries=" + object.getJSONArray(RequestConstants.IMAGES).toString());

                                    Gson gson = new Gson();
                                    ArrayList<Discovery> discoveries = gson.fromJson(object.getJSONArray(RequestConstants.IMAGES).toString(), Discovery.getArrayType());

                                    Intent i = new Intent(SplashActivity.this, MainActivity.class);

                                    i.putExtra(MainActivity.DISCOVERIES, discoveries);
                                    i.putExtra(MainActivity.AUTHENTICATION, mAuthentication);

                                    startActivity(i);
                                    finish();
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

    private void handleError(VolleyError error) {
        mErrorMessage.setVisibility(View.VISIBLE);
        mLoader.getAnimation().cancel();
        mCanRequest = true;
    }
}
