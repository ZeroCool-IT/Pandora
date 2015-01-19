package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import it.zerocool.batmacaana.utilities.Constraints;


public class HomeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    public static final String PREF_LOCATION = "location";
    public static final int LOCATION_UPDATE_TIME = 120000;
    public static final int LOCATION_MIN_DISTANCE_UPDATE = 100;

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    private void saveLocationToPreferences(Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences(NavigationDrawerFragment.PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        editor.putString(Constraints.LATITUDE, Double.valueOf(latitude).toString());
        editor.putString(Constraints.LONGITUDE, Double.valueOf(longitude).toString());
                /*Gson gson = new Gson();
                String jLocation = gson.toJson(location);
                editor.putString(PREF_LOCATION, jLocation);*/
        editor.apply();
    }

    private void requestLocationServices() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                saveLocationToPreferences(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    saveLocationToPreferences(location);
                }
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Location location = locationManager.getLastKnownLocation(provider);
                saveLocationToPreferences(location);
            }


        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_TIME,
                LOCATION_MIN_DISTANCE_UPDATE, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        saveLocationToPreferences(location);
    }
}
