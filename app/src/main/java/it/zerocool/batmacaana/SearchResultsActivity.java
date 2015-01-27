package it.zerocool.batmacaana;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.SearchResult;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;


public class SearchResultsActivity extends ActionBarActivity {

    private RecyclerView rvResults;
    private ProgressBarCircularIndeterminate progressBar;
    private Toolbar toolbar;
    private String query;
    private Context context;
    private SearchTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        context = this;
        rvResults = (RecyclerView) findViewById(R.id.search_results_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(layoutManager);
        progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.search_result_progressbar);
        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            //Object binding
            context = this;
            rvResults = (RecyclerView) findViewById(R.id.search_results_recycler_view);
            progressBar = (ProgressBarCircularIndeterminate) findViewById(R.id.search_result_progressbar);

            query = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(getResources().getString(R.string.results) + query);

            String uri = Constraints.URI_SEARCH1 + "594" + Constraints.URI_SEARCH2 + Uri.encode(query);
            if (RequestUtilities.isOnline(this)) {
                task = new SearchTask();
                task.execute(uri);
            } else {
                String message = getResources().getString(
                        R.string.dialog_message_no_connection);
                String title = getResources().getString(
                        R.string.dialog_title_warning);

                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                arguments.putInt(WarningDialog.ICON, R.drawable.ic_error_red_36dp);
                dialog.setArguments(arguments);
                dialog.show(getSupportFragmentManager(), "No Connection warning");
                finish();
                Log.i("TASK ERROR", "No connection");
            }

            Log.i("ZCLOG", query);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private class SearchTask extends AsyncTask<String, Void, List<SearchResult>> {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            rvResults.setVisibility(View.GONE);
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param searchResults The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<SearchResult> searchResults) {
            progressBar.setVisibility(View.GONE);
            rvResults.setVisibility(View.VISIBLE);
            Bundle args = new Bundle();
            args.putString(SearchFallbackFragment.FALLBACK_REFRESH_ARG, query);
            SearchFallbackFragment f = new SearchFallbackFragment();
            FragmentManager fm = getSupportFragmentManager();
            if (searchResults != null && searchResults.isEmpty()) {
//                args.putInt(SearchFallbackFragment.FALLBACK_TYPE_ARG, Constraints.NO_RESULTS);
//                f.setArguments(args);
//                fm.beginTransaction().add(f, "No result fragment").commit();
                Log.i("ZCLOG", "No results!");

            } else if (searchResults == null) {
                String title = getResources().getString(R.string.dialog_title_uhoh);
                String message = getResources().getString(R.string.dialog_message_error);
                WarningDialog dialog = new WarningDialog();
                Bundle arguments = new Bundle();
                arguments.putString(WarningDialog.TITLE, title);
                arguments.putString(WarningDialog.MESSAGE, message);
                arguments.putInt(WarningDialog.ICON, R.drawable.ic_error_red_36dp);
                dialog.setArguments(arguments);
                dialog.show(getSupportFragmentManager(), "Error retrieving data");
                args.putInt(ContentFallbackFragment.FALLBACK_TYPE_ARG, Constraints.CONNECTION_ERROR);
                f.setArguments(args);
                fm.beginTransaction().add(f, "Error retrieving data").commit();
                Log.e("ZCLOG TASK ERROR", "Failed to get results");
            } else {
                SearchAdapter adapter = new SearchAdapter(context, searchResults);
                rvResults.setAdapter(adapter);
            }
        }

        /**
         * <p>Applications should preferably override {@link #onCancelled(Object)}.
         * This method is invoked by the default implementation of
         * {@link #onCancelled(Object)}.</p>
         * <p/>
         * <p>Runs on the UI thread after {@link #cancel(boolean)} is invoked and
         * {@link #doInBackground(Object[])} has finished.</p>
         *
         * @see #onCancelled(Object)
         * @see #cancel(boolean)
         * @see #isCancelled()
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("ZCLOG", "onCancelled() called");
        }

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
        protected List<SearchResult> doInBackground(String... params) {
            String uri = params[0];
            List<SearchResult> res = null;
            if (isCancelled())
                return null;
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                if (isCancelled())
                    return null;
                res = ParsingUtilities.parseSearchResultsFromJSON(json);
                return res;
            } catch (IOException e) {
                Log.e("ZCLOG TASK", e.getMessage());
                e.printStackTrace();
            }
            return res;
        }
    }
}
