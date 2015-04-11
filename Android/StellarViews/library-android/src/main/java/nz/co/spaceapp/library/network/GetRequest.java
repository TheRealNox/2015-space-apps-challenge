package nz.co.spaceapp.library.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Giovannoni on 30/07/14.
 */
public class GetRequest extends Request<String> {

    private Response.Listener<String> mListener = null;

    public GetRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener) {
        super(Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String jsonString = new String(response.data);

        return Response.success(jsonString, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        if (mListener != null)
            mListener.onResponse(response);
    }
}
