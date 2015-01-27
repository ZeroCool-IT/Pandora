package it.zerocool.batmacaana;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.zerocool.batmacaana.utilities.Constraints;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFallbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFallbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFallbackFragment extends Fragment {
    public static final String FALLBACK_TYPE_ARG = "fallback";
    public static final String FALLBACK_REFRESH_ARG = "refresh";
    private ImageButton btRefresh;

    public SearchFallbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout;

        int type = getArguments().getInt(FALLBACK_TYPE_ARG);

        if (type == Constraints.CONNECTION_ERROR) {
            layout = inflater.inflate(R.layout.fragment_content_fallback_error, container, false);
            btRefresh = (ImageButton) layout.findViewById(R.id.bt_refresh);
            btRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, getArguments().getString(FALLBACK_REFRESH_ARG));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    /*ContentFragment f = new ContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(NavigationDrawerFragment.FRAG_SECTION_ID, getArguments().getInt(FALLBACK_REFRESH_ARG)
                    );
                    f.setArguments(bundle);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.content_frame, f)
                            .commit();*/

                }
            });
        } else {
            layout = inflater.inflate(R.layout.fragment_content_fallback, container, false);
        }

        return layout;
    }

}