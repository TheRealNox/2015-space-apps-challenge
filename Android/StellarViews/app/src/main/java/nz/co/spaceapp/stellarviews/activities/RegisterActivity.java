package nz.co.spaceapp.stellarviews.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button mRegister;
    private EditText mEmailAddress;
    private EditText mPassword;
    private EditText mPasswordConf;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailAddress = (EditText)findViewById(R.id.email_field);
        mPassword = (EditText)findViewById(R.id.password_field);
        mPasswordConf = (EditText)findViewById(R.id.password_conf_field);

        mRegister = (Button)findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields()) {
                    mProgressDialog = ProgressDialog.show(RegisterActivity.this, "", getResources().getString(R.string.register), true);

                    mProgressDialog.show();
                    HttpPostParams params = new HttpPostParams();

                    params.addParameters(RequestConstants.EMAIL_ADDRESS, mEmailAddress.getText().toString());
                    params.addParameters(RequestConstants.PASSWORD, mPassword.getText().toString());
                    PostRequest request = new PostRequest(RequestConstants.BASE_URL + RequestConstants.REGISTER_URL, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleError(error);
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

                                    final Authentication authentication = new Authentication(token, mEmailAddress.getText().toString());
                                    SharedPreferencesManager.getInstance().setAuthenticationInfo(authentication);
                                    GetRequest request = new GetRequest(RequestConstants.BASE_URL + RequestConstants.DISCOVERIES_URL + authentication.getAccessToken(), new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            handleError(error);
                                        }
                                    }, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d(TAG, response);
                                            try {
                                                JSONObject object = new JSONObject(response);
                                                boolean success = (Boolean) object.get(RequestConstants.SUCCESS);

                                                if (success) {
                                                    mProgressDialog.dismiss();
                                                    Log.d(TAG, "Discoveries=" + object.getJSONArray(RequestConstants.IMAGES).toString());

                                                    Gson gson = new Gson();
                                                    ArrayList<Discovery> discoveries = gson.fromJson(object.getJSONArray(RequestConstants.IMAGES).toString(), Discovery.getArrayType());

                                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);

                                                    i.putExtra(MainActivity.DISCOVERIES, discoveries);
                                                    i.putExtra(MainActivity.AUTHENTICATION, authentication);

                                                    SharedPreferencesManager.getInstance().setAuthenticationInfo(authentication);

                                                    mProgressDialog.dismiss();
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Log.e(TAG, "error while fetching discoveries");
                                                    handleError(null);
                                                }
                                            } catch (JSONException e) {
                                                handleError(null);
                                                Log.e(TAG, e.getMessage());
                                            }
                                        }
                                    });
                                    RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
                                } else
                                    handleRegisterError(object.getJSONObject(RequestConstants.ERRORS).toString());
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                                handleError(null);
                            }
                        }
                    }, params);
                    RequestManager.getInstance().addToRequestQueue(request, TAG, new DefaultRetryPolicy(2000, 1, 5));
                }
            }
        });
    }

    private void handleRegisterError(String errors) {
        mProgressDialog.dismiss();
        if (errors.contains("email_address")) {
            mProgressDialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("Oops")
                    .setMessage(R.string.invalid_email_format)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

    private void handleError(VolleyError error) {
        mProgressDialog.dismiss();
        Log.e(TAG, "error while registering");
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(R.string.something_went_wrong)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private boolean checkFields() {
        String error = "";
        String email = mEmailAddress.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConf = mPasswordConf.getText().toString();

        if (email.equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            error = "Please verify your email address.";
        else if (password.equals("") || passwordConf.equals("") || !password.equals(passwordConf))
            error = "Password do not match.";
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
