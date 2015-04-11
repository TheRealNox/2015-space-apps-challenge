package nz.co.spaceapp.stellarviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Roberta on 10/04/2015.
 */
public class Discovery implements Parcelable {

    protected static Type mArrayListType = new TypeToken<ArrayList<Discovery>>() {
    }.getType();


    @SerializedName("id")
    private int mId;

    @SerializedName("image_collection_id")
    private int mCollectionId;

    @SerializedName("uuid")
    private String mUuid;

    @SerializedName("unique_key")
    private String mUniqueKey;

    @SerializedName("date_taken")
    private String mDateTaken;

    @SerializedName("modified")
    private String mDateModified;

    @SerializedName("url")
    private String mUrl;

    public Discovery() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mCollectionId);
        dest.writeString(mUniqueKey);
        dest.writeString(mDateModified);
        dest.writeString(mDateTaken);
        dest.writeString(mUrl);
        dest.writeString(mUuid);
    }

    public static final Parcelable.Creator<Discovery> CREATOR = new Parcelable.Creator<Discovery>() {
        public Discovery createFromParcel(Parcel in) {
            return new Discovery(in);
        }

        public Discovery[] newArray(int size) {
            return new Discovery[size];
        }
    };

    private Discovery(Parcel in) {
        mId = in.readInt();
        mCollectionId = in.readInt();
        mUniqueKey = in.readString();
        mDateModified = in.readString();
        mDateTaken = in.readString();
        mUrl = in.readString();
        mUuid = in.readString();
    }

    public static Type getArrayType() {
        return mArrayListType;
    }

    public String getUrl() {
        return mUrl;
    }
}
