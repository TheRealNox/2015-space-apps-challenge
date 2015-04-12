package nz.co.spaceapp.stellarviews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import nz.co.spaceapp.library.image.ImageManager;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoveryBrowseFragment extends Fragment {

    public static final String DISCOVERY = "discovery";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_discovery_browse, container, false);

        Discovery discovery = getArguments().getParcelable(DISCOVERY);
        NetworkImageView imageView = (NetworkImageView) rootView.findViewById(R.id.discovery_full);
        TextView dateView = (TextView) rootView.findViewById(R.id.discovery_date);

        dateView.setText(discovery.getCreatedTaken().substring(0, 10));
        imageView.setImageUrl(discovery.getUrl(), ImageManager.getInstance().getImageLoader());

        return rootView;
    }
}
