package jp.smartlinks.beacon_tutorial;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, View.OnClickListener {

    public static final String TAG = HomeActivity.class.getSimpleName();


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private AppController mAppCon;
    private FragmentManager mFragmentManager ;
    private Context mContext ;

    private int mCurrentFragmentTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mFragmentManager = getSupportFragmentManager();
        mAppCon = (AppController)getApplication();
        mContext = this;

        // UUID/Major/Minor値の読み込み
        mAppCon.Load( mContext );

        // AltBeacon
        mAppCon.mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mAppCon.mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(mAppCon.IBEACON_FORMAT));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        boolean back = false;
        mFragmentManager = getSupportFragmentManager();

        Log.d(TAG, " onNavigationDrawerItemSelected( " + position + ")");
        Log.d(TAG, " mCurrentFragmentTag = " + mCurrentFragmentTag );
        if( mFragmentManager != null ) {
            Log.d(TAG, "  getBackStackEntryCount()" + mFragmentManager.getBackStackEntryCount() );
            if( mFragmentManager.getBackStackEntryCount() == 0) {
                back = true;
            }
        }

        // Home
        if (position == 0) {
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                    .commit();

        }

        mCurrentFragmentTag = position;


        if (position == 1) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, SettingDescFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();

        }

        if (position == 2) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, Receive1DescFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
        if (position == 3) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, Receive2DescFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
        if (position == 4) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, Receive3DescFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();

        }
        if (position == 5) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, CheckLogDescFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();

        }
        if (position == 6) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, AboutAppsFragment.newInstance());

            if( back == true ) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingDescFragment.newInstance() )
//                    .addToBackStack(null)
                    .commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, " onBackPressed()");
        Log.d(TAG, " mCurrentFragmentTag = " + mCurrentFragmentTag );
        if( mFragmentManager != null ) {
            Log.d(TAG, "  getBackStackEntryCount()" + mFragmentManager.getBackStackEntryCount() );
        }

        super.onBackPressed();
    }

    @Override
    public void onClick( View v ) {
        int btnId = v.getId();

        Log.d(TAG, " tap " + btnId);

//        Toast.makeText(this, "tap " + btnId , Toast.LENGTH_SHORT).show();

        if (btnId == R.id.btnSetting) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingDescFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnRecv1) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, Receive1DescFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnRecv2) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, Receive2DescFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnRecv3) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, Receive3DescFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnLog) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, CheckLogDescFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnAbout) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, AboutAppsFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }
        if (btnId == R.id.btnSendMail) {
            sendMail();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((HomeActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void sendMail() {

        String csv = mAppCon.LogData.CreateCSV();
        Log.d(TAG, " sendMail() CSV Data " + csv);


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Beacon Log");
        intent.putExtra(Intent.EXTRA_TEXT, csv);

        mContext.startActivity(intent);

    }
}
