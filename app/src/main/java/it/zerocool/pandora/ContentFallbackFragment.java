package it.zerocool.pandora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentFallbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentFallbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFallbackFragment extends Fragment {


    public ContentFallbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content_fallback, container, false);
        return layout;
    }


}
