package nz.co.spaceapp.stellarviews.explore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import nz.co.spaceapp.library.view.DropShadowImageView;
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

        public ImageView image;

        public ViewHolder(View v) {
            super(v);

            image = (ImageView) v.findViewById(R.id.image);
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

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Discovery discoveries = mDiscoveries.get(position);

//        viewHolder.vendor.setText(beacon.getVendor());
    }

    @Override
    public int getItemCount() {
        return mDiscoveries.size();
    }
}