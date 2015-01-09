package it.zerocool.pandora;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import it.zerocool.pandora.utilities.Constraints;


public class ContentFragment extends Fragment {

    public static String FRAG_SECTION_ID = "frag_section_id";

    private ImageView ivContent;

    public ContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_content, container, false);
        ivContent = (ImageView) layout.findViewById(R.id.ivContent);


        int id = getArguments().getInt(FRAG_SECTION_ID);

//        ivContent.setImageResource(getResources().getStringArray(R.array.drawer_list)[id]);
        Log.i("ID SELECTION", Integer.valueOf(id).toString());

        setUp(id);
        return layout;
//        return inflater.inflate(R.layout.fragment_content, container, false);


    }

    private void setUp(int id) {

        switch (id) {

            case Constraints.TOSEE:
                ivContent.setImageResource(R.drawable.ic_tosee);
                ivContent.invalidate();
                Log.i("Selection", "TO SEE");
                break;
            case Constraints.EVENT:
                ivContent.setImageResource(R.drawable.ic_event);
                break;
            case Constraints.EAT:
                ivContent.setImageResource(R.drawable.ic_eat);
                break;
            case Constraints.SLEEP:
                ivContent.setImageResource((R.drawable.ic_sleep));
                break;
            case Constraints.SERVICES:
                ivContent.setImageResource(R.drawable.ic_services);
                break;
            case Constraints.SHOP:
                ivContent.setImageResource(R.drawable.ic_shop);
                break;
            case Constraints.ABOUT:
                ivContent.setImageResource(R.drawable.ic_about);
                break;
            case Constraints.NEWS:
                ivContent.setImageResource(R.drawable.ic_news);
                break;
            default:
                break;


        }

    }


}
