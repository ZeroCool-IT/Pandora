package it.zerocool.pandora;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.zerocool.pandora.model.Cardable;
import it.zerocool.pandora.model.Eat;


public class ContentFragment extends Fragment {

    public static String FRAG_SECTION_ID = "frag_section_id";

    private RecyclerView rvContent;
    private ContentAdapter adapter;

//    private ImageView ivContent;

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
        rvContent = (RecyclerView) layout.findViewById(R.id.content_recycler_view);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ContentAdapter(getActivity(), getData(getActivity()));
        rvContent.setAdapter(adapter);


//        ivContent = (ImageView) layout.findViewById(R.id.ivContent);


        int id = getArguments().getInt(FRAG_SECTION_ID);


//        setUp(id);
        return layout;


    }

    private List<Cardable> getData(Context context) {
        List<Cardable> data = new ArrayList<Cardable>();
        int[] imagery = {R.drawable.im_restaurant, R.drawable.im_pizza, R.drawable.im_pub, R.drawable.im_pizza_grill, R.drawable.im_fastfood, R.drawable.im_holycow, R.drawable.im_bakery, R.drawable.im_brunch};
        String[] places = context.getResources().getStringArray(R.array.content_test_array);
        for (int i = 0; i < places.length; i++) {
            Eat current = new Eat(i);
            current.setName(places[i]);
            current.setImage(Integer.valueOf(imagery[i]).toString());
            data.add(current);
        }
        return data;
    }

/*    private void setUp(int id) {

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

    }*/


}
