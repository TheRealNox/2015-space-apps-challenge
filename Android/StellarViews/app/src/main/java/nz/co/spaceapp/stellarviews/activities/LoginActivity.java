package nz.co.spaceapp.stellarviews.activities;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import nz.co.spaceapp.library.network.GetRequest;
import nz.co.spaceapp.library.network.HttpPostParams;
import nz.co.spaceapp.library.network.PostRequest;
import nz.co.spaceapp.library.network.RequestManager;
import nz.co.spaceapp.stellarviews.Authentication;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.SharedPreferencesManager;

/**
 * Created by Roberta on 11/04/2015.
 */
public class LoginActivity extends ActionBarActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button mRegister;
    private Button mLogin;
    private EditText mPassword;
    private EditText mEmailAddress;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailAddress = (EditText) findViewById(R.id.email_field);
        mPassword = (EditText) findViewById(R.id.password_field);
        mRegister = (Button) findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                    startActivity(i);
                else
                    startActivity(i, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
            }
        });

        mLogin = (Button) findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields())
                    requestLogin();
            }
        });
    }

    private void requestLogin() {
        HttpPostParams params = new HttpPostParams();
        mProgressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.login), true);

        mProgressDialog.show();
        params.addParameters(RequestConstants.EMAIL_ADDRESS, mEmailAddress.getText().toString());
        params.addParameters(RequestConstants.PASSWORD, mPassword.getText().toString());
        PostRequest request = new PostRequest(RequestConstants.BASE_URL + RequestConstants.LOGIN_URL, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleLoginError();
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                    if (success) {
                        final Authentication authentication = new Authentication((String) object.get(RequestConstants.AUTH_TOKEN), mEmailAddress.getText().toString());

                        GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.DISCOVERIES_URL + authentication.getAccessToken(), new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                try {
                                    JSONObject object = new JSONObject(response);
                                    boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                                    if (success) {
                                        Log.d(TAG, "Discoveries=" + object.getJSONArray(RequestConstants.IMAGES).toString());

                                        Gson gson = new Gson();
                                        ArrayList<Discovery> discoveries = gson.fromJson(object.getJSONArray(RequestConstants.IMAGES).toString(), Discovery.getArrayType());

                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                                        i.putExtra(MainActivity.DISCOVERIES, discoveries);
                                        i.putExtra(MainActivity.AUTHENTICATION, authentication);

                                        SharedPreferencesManager.getInstance().setAuthenticationInfo(authentication);

                                        mProgressDialog.dismiss();
                                        startActivity(i);
                                        finish();
                                    } else
                                        Log.e(TAG, "error while fetching discoveries");
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        });
                        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
                    } else {
                        handleLoginError();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, params);
        RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
    }

    private void handleLoginError() {
        Log.e(TAG, "error while login in");
        mProgressDialog.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(R.string.something_went_wrong)
                .show();
    }

    private boolean checkFields() {
        String error = "";
        String email = mEmailAddress.getText().toString();
        String password = mPassword.getText().toString();

        if (email.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            error = "Please verify your email address.";
        else if (password.equals(""))
            error = "Please enter your password";
        if (!error.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Oops")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setMessage(error)
                    .show();
            return false;
        } else
            return true;
    }
}
