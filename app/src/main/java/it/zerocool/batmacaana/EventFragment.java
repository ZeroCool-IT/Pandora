package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.zerocool.batmacaana.model.Event;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;


public class EventFragment extends Fragment {
    private TextView tvTest;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_event, container, false);
        tvTest = (TextView) layout.findViewById(R.id.eventTVtest);
        Event e = ParsingUtilities.parseSingleEvent(getArguments().getString(Constraints.JSON_ARG));
        tvTest.setText(e.getDescription());
        return layout;
    }

}
