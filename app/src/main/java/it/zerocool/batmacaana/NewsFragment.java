package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.zerocool.batmacaana.model.News;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class NewsFragment extends Fragment {

    private TextView tvTest;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_news, container, false);
        tvTest = (TextView) layout.findViewById(R.id.newsTVTest);
        News n = ParsingUtilities.parseSingleNews(getArguments().getString(Constraints.JSON_ARG));
        tvTest.setText(n.getBody());
        return layout;
    }


}
