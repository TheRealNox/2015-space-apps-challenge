package nz.co.spaceapp.library.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marc Giovannoni on 18/12/14.
 */
public class LargeBitmapDownloadTask extends AsyncTask<String, Integer, Bitmap> {

    private static final String TAG = LargeBitmapDownloadTask.class.getSimpleName();

    private BitmapDownloadListener mListener;

    public LargeBitmapDownloadTask(BitmapDownloadListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream is = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 32 * 1024);

            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current;

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            byte[] imageData = baf.toByteArray();
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(Bitmap bitmap) {
        if (mListener != null) {
            if (bitmap != null)
                mListener.onResponse(bitmap);
            else
                mListener.onError();
        }
    }

    public interface BitmapDownloadListener {
        public void onResponse(Bitmap bitmap);
        public void onError();
    }
}