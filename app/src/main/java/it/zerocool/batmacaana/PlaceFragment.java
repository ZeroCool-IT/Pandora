package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class PlaceFragment extends Fragment {


    private ExpandableTextView tvTest;
    private ImageView imTest;

    public PlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_place, container, false);
        tvTest = (ExpandableTextView) layout.findViewById(R.id.placeTVTest);
        Place p = ParsingUtilities.parseSinglePlace(getArguments().getString(Constraints.JSON_ARG));
        imTest = (ImageView) layout.findViewById(R.id.imageView);
        Picasso.with(getActivity()).
                load("http://www.ilmiositodemo.altervista.org/app/images/big/" + p.getImage()).
                error(R.drawable.im_noimage).
                placeholder(R.drawable.im_placeholeder).
                into(imTest);

        tvTest.setText(p.getDescription());
        return layout;
    }

}
