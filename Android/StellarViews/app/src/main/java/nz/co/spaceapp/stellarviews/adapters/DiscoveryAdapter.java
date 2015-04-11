package nz.co.spaceapp.stellarviews.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import nz.co.spaceapp.library.image.ImageManager;
import nz.co.spaceapp.library.view.SquareNetworkImageView;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;

/**
 * Created by Roberta on 10/04/2015.
 */
public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private final ArrayList<Discovery> mDiscoveries;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public SquareNetworkImageView discovery;

        public ViewHolder(View v) {
            super(v);

            discovery = (SquareNetworkImageView) v.findViewById(R.id.discovery);
        }
    }

    public DiscoveryAdapter(Context context, RecyclerView recycler, ArrayList<Discovery> beacons) {
        mDiscoveries = beacons;
        mContext = context;
        mRecyclerView = recycler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewStyle) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_discovery, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final Discovery discovery = mDiscoveries.get(position);

        viewHolder.discovery.setDefaultImageResId(R.mipmap.default_discovery);
        viewHolder.discovery.setImageUrl(discovery.getUrl(), ImageManager.getInstance().getImageLoader());
    }

    @Override
    public int getItemCount() {
        return mDiscoveries.size();
    }
}