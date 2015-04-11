package nz.co.spaceapp.stellarviews.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import nz.co.spaceapp.library.network.HttpPostParams;
import nz.co.spaceapp.library.network.PostRequest;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.stellarviews.Authentication;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.SharedPreferencesManager;

/**
 * Created by Roberta on 11/04/2015.
 */
public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button mRegister;
    private EditText mEmailAddress;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailAddress = (EditText)findViewById(R.id.email_field);
        mPassword = (EditText)findViewById(R.id.password_field);

        mRegister = (Button)findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkFields();
                HttpPostParams params = new HttpPostParams();

                params.addParameters(RequestConstants.EMAIL_ADDRESS, mEmailAddress.getText().toString());
                params.addParameters(RequestConstants.PASSWORD, mPassword.getText().toString());
                PostRequest request = new PostRequest(RequestConstants.BASE_URL + RequestConstants.REGISTER_URL, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error while registering");
                    }
                }, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                            if (success) {
                                String token = (String) object.get(RequestConstants.AUTH_TOKEN);

                                SharedPreferencesManager.getInstance().setAuthenticationInfo(new Authentication(token, mEmailAddress.getText().toString()));
                            }
                            else
                                Log.e(TAG, "error while registering");
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }, params);
                RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
            }
        });
    }

    private void checkFields() {
        if (mEmailAddress.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setMessage("Please check your Email.")
                    .show();
        }
    }
}
