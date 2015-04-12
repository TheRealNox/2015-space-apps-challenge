package nz.co.spaceapp.stellarviews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import nz.co.spaceapp.library.image.ImageManager;
import nz.co.spaceapp.library.view.SquareNetworkImageView;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.Ratings;

/**
 * Created by Roberta on 12/04/2015.
 */
public class MyDiscoveryAdapter extends BaseAdapter {

    private List<Ratings> mDiscoveries;
    private Context mContext;

    public MyDiscoveryAdapter(Context context, List<Ratings> discoveries) {
        mContext = context;
        mDiscoveries = discoveries;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Ratings discovery = mDiscoveries.get(position);
        RatingsHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_my_discovery, parent, false);

            holder = new RatingsHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (RatingsHolder) convertView.getTag();

        holder.discovery.setImageUrl(discovery.getDiscovery().getUrl(), ImageManager.getInstance().getImageLoader());
        return convertView;
    }

    @Override
    public int getCount() {
        return mDiscoveries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class RatingsHolder {
        public SquareNetworkImageView discovery;

        public RatingsHolder(View convertView) {
            discovery = (SquareNetworkImageView) convertView.findViewById(R.id.discovery);
        }
    }
}