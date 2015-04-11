package nz.co.spaceapp.stellarviews.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nz.co.spaceapp.stellarviews.R;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoveryBrowseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_discovery_browse, container, false);

        return rootView;
    }
}
