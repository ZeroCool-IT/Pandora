/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

import it.zerocool.batmacaana.dialog.LocationWarningDialog;
import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;


public class HomeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private LocationWarningDialog dialog;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        requestLocationServices();


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);
        editor.apply();
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            getData(segments.get(1));
        }
    }

    private void getData(String s) {
        StringTokenizer token = new StringTokenizer(s, "&");

        String[] args = new String[2];
        int cont = 0;
        while (token.hasMoreTokens()) {
            args[cont] = token.nextToken();
            cont++;
        }
        String query = Constraints.OBJECT_SEARCH1 +
                Constraints.USER_ID +
                Constraints.OBJECT_SEARCH2 +
                args[0] +
                Constraints.OBJECT_SEARCH3 +
                args[1];
        RequestObjectTask task = new RequestObjectTask();
        task.execute(query, args[1]);

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
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);
        editor.apply();
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
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ZCLOG", "onPause() called");
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onStop() {
        Log.i("ZCLOG", "onStop() called");
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ZCLOG", "onStop() called");
        locationManager.removeUpdates(locationListener);
    }

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

    private void saveLocationToPreferences(Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constraints.PREF_FILE_NAME,
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
                /*if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                    Location location = locationManager.getLastKnownLocation(provider);
                    saveLocationToPreferences(location);
                }*/
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
        /*Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        saveLocationToPreferences(location);*/
    }

    private class RequestObjectTask extends AsyncTask<String, Void, String> {

        private int type;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            String res = null;
            String uri = params[0];
            type = Integer.parseInt(params[1]);
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                res = ParsingUtilities.parseSingleResult(json);
            } catch (IOException e) {
                Log.e("ZCLOG", e.getMessage());
//                this.cancel(true);
                return null;
            }
            return res;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals(Constraints.EMPTY_VALUE)) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constraints.JSON_ARG, s);
                intent.putExtra(Constraints.TYPE_ARG, type);
                context.startActivity(intent);
            } else {
                String title, message;
                Bundle args = new Bundle();
                WarningDialog dialog = new WarningDialog();
                title = context.getResources().getString(R.string.dialog_title_uhoh);
                message = context.getResources().getString(R.string.dialog_message_error);
                args.putString(WarningDialog.TITLE, title);
                args.putString(WarningDialog.MESSAGE, message);
                args.putBoolean(WarningDialog.KILL, true);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "Error retrieving data");
            }
        }
    }

}
