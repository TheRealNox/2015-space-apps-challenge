package nz.co.spaceapp.stellarviews.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.fragments.DiscoveryBrowseFragment;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoverPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Discovery> mDiscoveries;

    public DiscoverPagerAdapter(FragmentManager supportFragmentManager, List<Discovery> discoveries) {
        super(supportFragmentManager);

        mDiscoveries = discoveries;
    }

    @Override
    public int getCount() {
        return mDiscoveries.size();
    }

    @Override
    public Fragment getItem(int position) {
        DiscoveryBrowseFragment fragment = new DiscoveryBrowseFragment();

        Bundle b = new Bundle();

        b.putParcelable(DiscoveryBrowseFragment.DISCOVERY, mDiscoveries.get(position));
        fragment.setArguments(b);
        return fragment;
    }

}
