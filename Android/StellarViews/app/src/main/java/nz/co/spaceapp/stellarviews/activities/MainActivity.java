package nz.co.spaceapp.stellarviews.activities;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import nz.co.spaceapp.stellarviews.Authentication;
import nz.co.spaceapp.stellarviews.Discovery;
import nz.co.spaceapp.stellarviews.R;
import nz.co.spaceapp.stellarviews.fragments.ExploreFragment;
import nz.co.spaceapp.stellarviews.fragments.MyDiscoveriesFragment;


public class MainActivity extends ActionBarActivity {

    public static final String DISCOVERIES = "discoveries";
    public static final String AUTHENTICATION = "auth";

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] mTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentPosition = -1;
    private Fragment[] mFragments;
    private ArrayList<Discovery> mDiscoveries;
    private Authentication mAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mDiscoveries = getIntent().getExtras().getParcelableArrayList(DISCOVERIES);
        mAuthentication = getIntent().getExtras().getParcelable(AUTHENTICATION);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTitles = getResources().getStringArray(R.array.drawer_menu);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupHeader();
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mFragments = new Fragment[mTitles.length];
        mFragments[0] = new ExploreFragment();
        mFragments[1] = new MyDiscoveriesFragment();

        if (savedInstanceState == null)
            selectItem(1);
    }

    private void setupHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.header_login, null);

        TextView emailAddress = (TextView) view.findViewById(R.id.email);
        emailAddress.setText(mAuthentication.getEmailAddress());
        mDrawerList.addHeaderView(view);
    }

    public ArrayList<Discovery> getDiscoveries() {
        return mDiscoveries;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public Authentication getAuthentication() {
        return mAuthentication;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        if (position != 0 && position != mCurrentPosition) {
            mCurrentPosition = position;

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_fragment, mFragments[position  - 1])
                    .commit();

            mDrawerList.setItemChecked(position, true);
            setTitle(mTitles[position - 1]);
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
