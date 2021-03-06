/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.util.List;

import it.zerocool.batmacaana.dialog.LocationWarningDialog;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;


public class HomeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private LocationWarningDialog dialog;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestLocationServices();


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);
        editor.apply();

    }


    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.dismiss();
        }
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        Log.i("ZCLOG", "onResume() called");
        requestLocationServices();

    }

    /**
     * Dispatch onPause() to fragments.
     * Stops location update
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZCLOG", "onPause() called");
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Stops location update
     */
    @Override
    protected void onStop() {
        Log.i("ZCLOG", "onStop() called");
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Stops location update on activity destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ZCLOG", "onStop() called");
        locationManager.removeUpdates(locationListener);
    }

    /**
     * Create the search option menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        //Get the ID for the search bar LinearLayout
        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        //Get the search bar Linearlayout
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(searchBarId);
        //Give the Linearlayout a transition animation.
        searchBar.setLayoutTransition(new LayoutTransition());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save current location on Shared Preferences
     * @param location is the location to save
     */
    private void saveLocationToPreferences(Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constraints.PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        editor.putString(Constraints.LATITUDE, Double.valueOf(latitude).toString());
        editor.putString(Constraints.LONGITUDE, Double.valueOf(longitude).toString());
        editor.apply();
    }

    /**
     * Request the location services
     */
    private void requestLocationServices() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        List<String> availableProvider = locationManager.getProviders(criteria, true);
        if (!availableProvider.contains(LocationManager.GPS_PROVIDER) && !availableProvider.contains(LocationManager.NETWORK_PROVIDER)) {
            dialog = new LocationWarningDialog();
            dialog.show(getSupportFragmentManager(), "Location warning");
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                saveLocationToPreferences(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.w("ZCLOG", "The provider " + provider + " is enabled!");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.w("ZCLOG", "The provider " + provider + " is disabled!");
            }
        };
        if (provider != null && locationManager.getAllProviders().contains(provider)) {
            Log.i("ZCLOG", "Using " + provider + " provider");
            locationManager.requestLocationUpdates(provider, Constraints.LOCATION_UPDATE_TIME,
                    Constraints.LOCATION_MIN_DISTANCE_UPDATE, locationListener);
        } else
            Log.e("ZCLOG", "The provider " + provider + " is not available!");
    }

}
