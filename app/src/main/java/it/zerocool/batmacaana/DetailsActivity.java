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

    private String header;
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
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

/*    *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {

        private TextView tvTest;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            tvTest = (TextView) rootView.findViewById(R.id.detailsTv);
            String object = getArguments().getString(Constraints.JSON_ARG);
            int type = getArguments().getInt(Constraints.TYPE_ARG);
            Cardable test = null;
            switch (type) {
                case Constraints.TYPE_PLACE:
                    Place p = ParsingUtilities.parseSinglePlace(object);
                    test = p;
                    break;
                case Constraints.TYPE_NEWS:
                    News n = ParsingUtilities.parseSingleNews(object);
                    test = n;
                    break;
                case  Constraints.TYPE_EVENT:
                    Event e = ParsingUtilities.parseSingleEvent(object);
                    test = e;
            }
            tvTest.setText(test.getHeader());
            return rootView;
        }
    }*/
}
