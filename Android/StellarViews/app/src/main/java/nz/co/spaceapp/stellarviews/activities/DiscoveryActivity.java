package nz.co.spaceapp.stellarviews.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import nz.co.spaceapp.library.view.DepthPageTransformer;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.adapters.DiscoverPagerAdapter;

/**
 * Created by Roberta on 11/04/2015.
 */
public class DiscoveryActivity extends ActionBarActivity {
    public static final String DISCOVERY = "discovery";

    private ViewPager mPager;
    private DiscoverPagerAdapter mPagerAdapter;
    private Discovery mDiscovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        mDiscovery = getIntent().getExtras().getParcelable(DISCOVERY);

        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new DiscoverPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());
    }
}
