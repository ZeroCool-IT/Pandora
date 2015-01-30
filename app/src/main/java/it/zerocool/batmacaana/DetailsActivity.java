/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import it.zerocool.batmacaana.utilities.Constraints;


public class DetailsActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Fragment frag = chooseFragment(intent.getIntExtra(Constraints.TYPE_ARG, 0));
        Bundle args = new Bundle();
        args.putString(Constraints.JSON_ARG, intent.getStringExtra(Constraints.JSON_ARG));
        frag.setArguments(args);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }

    private Fragment chooseFragment(int type) {
        Fragment f = null;
        switch (type) {
            case Constraints.TYPE_PLACE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_TOSEE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SLEEP:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_EAT:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SERVICE:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_SHOP:
                f = new PlaceFragment();
                break;
            case Constraints.TYPE_NEWS:
                f = new NewsFragment();
                break;
            case Constraints.TYPE_EVENT:
                f = new EventFragment();
        }
        return f;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_details, menu);
//
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_item_share) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_SEND);
//            String message = getResources().getString(R.string.share_message) +
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
