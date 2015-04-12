package nz.co.spaceapp.stellarviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Roberta on 12/04/2015.
 */
public class ImageDetail implements Parcelable {
    @SerializedName("location_name")
    private String mLocationName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLocationName);
    }

    public static final Parcelable.Creator<ImageDetail> CREATOR = new Parcelable.Creator<ImageDetail>() {
        public ImageDetail createFromParcel(Parcel in) {
            return new ImageDetail(in);
        }

        public ImageDetail[] newArray(int size) {
            return new ImageDetail[size];
        }
    };

    private ImageDetail(Parcel in) {
        mLocationName = in.readString();
    }

    public String getLocationName() {
        return mLocationName;
    }
}
