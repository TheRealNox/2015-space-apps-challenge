package nz.co.spaceapp.library.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Marc Giovannoni on 11/07/14.
 */
public class RequestManager {

    private static final String TAG = RequestManager.class.getSimpleName();

    private static RequestManager mInstance;
    private RequestQueue mRequestQueue;
    private DefaultRetryPolicy mDefaultRetryPolicy;
    private boolean mInitialized = false;

    public static RequestManager getInstance() {
        if (mInstance == null) {
            synchronized (RequestManager.class) {
                if (mInstance == null) {
                    mInstance = new RequestManager();
                }
            }
        }
        return mInstance;
    }

    public void initialize(Context context) {
        if (mInitialized)
            return;
        mRequestQueue = Volley.newRequestQueue(context);
        mDefaultRetryPolicy = new DefaultRetryPolicy(20000, 0, 1.0f);
        mInitialized = true;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, DefaultRetryPolicy defaultRetryPolicy) {

        req.setTag(tag);
        if (defaultRetryPolicy == null)
            req.setRetryPolicy(mDefaultRetryPolicy);
        mRequestQueue.add(req);
    }

    public void cancelPendingRequests(String tag) {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }
}
