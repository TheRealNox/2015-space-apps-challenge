package nz.co.spaceapp.stellarviews;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Roberta on 11/04/2015.
 */
public class Authentication implements Parcelable {

    private String mAccessToken;
    private String mEmailAddress;

    public Authentication(String token, String emailAddress) {
        mAccessToken = token;
        mEmailAddress = emailAddress;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public boolean isValid() {
        return !mAccessToken.equals("") || !mEmailAddress.equals("");
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAccessToken);
        dest.writeString(mEmailAddress);
    }

    public static final Parcelable.Creator<Authentication> CREATOR = new Parcelable.Creator<Authentication>() {
        public Authentication createFromParcel(Parcel in) {
            return new Authentication(in);
        }

        public Authentication[] newArray(int size) {
            return new Authentication[size];
        }
    };

    private Authentication(Parcel in) {
        mAccessToken = in.readString();
        mEmailAddress = in.readString();
    }
}
