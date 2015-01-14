package it.zerocool.pandora;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import it.zerocool.pandora.dialog.WarningDialog;
import it.zerocool.pandora.model.Cardable;
import it.zerocool.pandora.model.Eat;
import it.zerocool.pandora.utilities.Constraints;
import it.zerocool.pandora.utilities.ParsingUtilities;
import it.zerocool.pandora.utilities.RequestUtilities;


public class ContentFragment extends Fragment {

    public static String FRAG_SECTION_ID = "frag_section_id";

    private RecyclerView rvContent;
    private ContentAdapter adapter;
    private RetrieveDataAsyncTask task;
    private List<Cardable> searchResults;
    private View layout;
    private LayoutInflater inflater;
    private ViewGroup container;

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
        this.inflater = inflater;
        this.container = container;
        layout = inflater.inflate(R.layout.fragment_content, container, false);
        rvContent = (RecyclerView) layout.findViewById(R.id.content_recycler_view);
        rvContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        int id = getArguments().getInt(FRAG_SECTION_ID);
        searchResults = getData(getActivity(), id);


//        rvContent.setAdapter(adapter);


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

    private List<Cardable> getData(Context context, int typeID) {
        String uri = null;
        int type = Constraints.PLACE;
        switch (typeID) {
            case Constraints.TOSEE:
                uri = Constraints.URI_TOSEE;
                break;
            case Constraints.EVENT:
                uri = Constraints.URI_EVENT;
                type = Constraints.EVENT;
                break;
            case Constraints.EAT:
                uri = Constraints.URI_EAT;
                break;
            case Constraints.SLEEP:
                uri = Constraints.URI_SLEEP;
                break;
            case Constraints.SERVICES:
                uri = Constraints.URI_SERVICES;
                break;
            case Constraints.SHOP:
                uri = Constraints.URI_SHOP;
                break;
            case Constraints.NEWS:
                uri = Constraints.URI_NEWS;
                type = Constraints.NEWS;
                break;
            default:
                break;
        }
        uri += Integer.valueOf(Constraints.USER_ID).toString();

        if (RequestUtilities.isOnline(getActivity())) {
            task = new RetrieveDataAsyncTask();
            task.execute(uri, Integer.valueOf(type).toString());
        } else {
            String message = getResources().getString(
                    R.string.dialog_message_no_connection);
            String title = getResources().getString(
                    R.string.dialog_title_warning);

            WarningDialog dialog = new WarningDialog();
            Bundle arguments = new Bundle();
            arguments.putString(WarningDialog.TITLE, title);
            arguments.putString(WarningDialog.MESSAGE, message);
            arguments.putInt(WarningDialog.ICON, R.drawable.ic_connection_failure);
            dialog.setArguments(arguments);
            dialog.show(getFragmentManager(), "No Connection warning");
            ContentFallbackFragment f = new ContentFallbackFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            Log.i("TASK ERROR", "No connection");
        }
        return searchResults;
    }

    private class RetrieveDataAsyncTask extends AsyncTask<String, Void, List<Cardable>> {

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
        protected List<Cardable> doInBackground(String... params) {
            String uri = params[0];
            List<Cardable> res = null;
            int type = Integer.parseInt(params[1]);
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                switch (type) {
                    case Constraints.PLACE:
                        res = ParsingUtilities.parsePlaceFromJSON(json);
                        break;
                    case Constraints.NEWS:
                        res = ParsingUtilities.parseNewsFromJSON(json);
                        break;
                    case Constraints.EVENT:
                        res = ParsingUtilities.parseEventFromJSON(json);
                }
                return res;

            } catch (IOException e) {
                Log.e("TASK ERROR", e.getMessage());
                e.printStackTrace();
            }
            return res;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param cardables The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(List<Cardable> cardables) {
            if (cardables.isEmpty()) {
                ContentFallbackFragment f = new ContentFallbackFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.content_frame, f)
                        .commit();
                Log.i("TASK", "No results!");
            } else {
                super.onPostExecute(cardables);
                adapter = new ContentAdapter(getActivity(), cardables);
                rvContent.setAdapter(adapter);
            }
        }
    }

}
