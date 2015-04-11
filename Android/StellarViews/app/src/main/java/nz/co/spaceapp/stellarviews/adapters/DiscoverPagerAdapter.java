package nz.co.spaceapp.stellarviews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import nz.co.spaceapp.stellarviews.fragments.DiscoveryBrowseFragment;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoverPagerAdapter extends FragmentStatePagerAdapter {
    public DiscoverPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        return new DiscoveryBrowseFragment();
    }

}
