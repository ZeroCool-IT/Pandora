package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.zerocool.batmacaana.utilities.Constraints;


/**
 *
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "preference";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    public static final String FRAG_SECTION_ID = "frag_section_id";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private RecyclerView recyclerView;
    private View previousSelected;

    private DrawerAdapter adapter;


    //Commit test
    //2nd commit test
    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);


        adapter = new DrawerAdapter(getActivity(), getData(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        selectItem(position);
//                        drawSelected(view);

                    }
                })
        );

        return layout;
    }

    public static List<DrawerItem> getData(Context context) {
        List<DrawerItem> data = new ArrayList<DrawerItem>();
        int[] icons = {R.drawable.ic_beenhere_grey600_24dp,
                R.drawable.ic_event_note_grey600_24dp,
                R.drawable.ic_local_restaurant_grey600_24dp,
                R.drawable.ic_local_hotel_grey600_24dp,
                R.drawable.ic_newspaper_grey600_24dp,
                R.drawable.ic_local_mall_grey600_24dp,
                R.drawable.ic_directions_train_grey600_24dp,
                R.drawable.ic_local_library_grey600_24dp};
        String[] titles = context.getResources().getStringArray(R.array.drawer_list);
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            DrawerItem current = new DrawerItem();
            current.iconID = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                    getActivity().invalidateOptionsMenu();

                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Set transparency of toolbar on drawer slide
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                    Log.d("TEST", "offset " + slideOffset);
                }
            }
        };
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        selectItem(Constraints.TOSEE);


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, preferenceValue);
    }


    private void selectItem(int position) {
        if (position != Constraints.ABOUT) {
            ContentFragment f = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(FRAG_SECTION_ID, position);
            f.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();
            getActivity().setTitle(getResources().getStringArray(R.array.drawer_list)[position]);
//        drawSelected(view);
            mDrawerLayout.closeDrawers();
        } else {
            //TODO About fragment
        }
    }

    private void drawSelected(View view) {
        TextView prevTv = (TextView) previousSelected.findViewById(R.id.listText);
        prevTv.setTextColor(getResources().getColor(R.color.text87));
        TextView tv = (TextView) view.findViewById(R.id.listText);
        tv.setTextColor(getResources().getColor(R.color.primaryColor));
        previousSelected = view;
    }


}
