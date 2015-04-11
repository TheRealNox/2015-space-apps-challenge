package nz.co.spaceapp.stellarviews;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Timestamp;
import java.util.Set;

public class SharedPreferencesManager {

    private static final String PREFS_NAME = "stellar_views";
    private static final String ACCESS_TOKEN = "access_";
    private static final String EMAIL_ADDRESS = "email_address";

    private static volatile SharedPreferencesManager instance = null;

    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    public SharedPreferencesManager() {

    }

    public static SharedPreferencesManager getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager();
                }
            }
        }
        return instance;
    }

    public void initialize(Context context) {
        if (context != null)
            settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings != null)
            editor = settings.edit();
    }

    public boolean commit() {
        return editor.commit();
    }

    public void setAuthenticationInfo(Authentication auth) {
        editor.putString(ACCESS_TOKEN, auth.getAccessToken());
        editor.putString(EMAIL_ADDRESS, auth.getEmailAddress());

        editor.commit();
    }

    public Authentication getAuthenticationInfo() {
        String accessToken = settings.getString(ACCESS_TOKEN, "");
        String emailAddress = settings.getString(EMAIL_ADDRESS, "");

        return new Authentication(accessToken, emailAddress);
    }
}
