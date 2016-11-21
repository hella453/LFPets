package com.parse.starter.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.starter.Adapters.RecyclerViewAdapter;
import com.parse.starter.NavigationDrawer.RecyclerViewRow;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private View containerView;
    private ActionBarDrawerToggle mdrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerViewAdapter adapter;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private boolean loggediIn;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";




    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "true"));
        //If nawDrawer is coming from rotation
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //Initialize widgets
        TextView user = (TextView) layout.findViewById(R.id.emailText);

        //get currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            loggediIn=true;
            user.setText(currentUser.getString("username"));
            recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
            adapter = new RecyclerViewAdapter(getActivity(), getData(), loggediIn);

        } else {
            loggediIn=false;
            user.setText("");
            recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
            adapter = new RecyclerViewAdapter(getActivity(), getDataNotSignedIn(), loggediIn);

        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager((new LinearLayoutManager(getActivity())));

        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
       getActivity().getFragmentManager().popBackStack();
    }

    //Gets data from array
    public static List<RecyclerViewRow> getData() {
        List<RecyclerViewRow> data = new ArrayList<>();
        int icons[] = {R.drawable.ic_home_grey, R.drawable.ic_add_box_grey, R.drawable.ic_perm_media_grey, R.drawable.ic_notifications_grey, R.drawable.ic_grade_grey, R.drawable.ic_person_grey, R.drawable.ic_odjava};
        String titles[] = {"Početna", "Predaj oglas", "Moji oglasi", "Notifikacije", "Favoriti", "Moj profil", "Odjava"};
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            RecyclerViewRow cur = new RecyclerViewRow();
            cur.iconId = icons[i];
            cur.title = titles[i];
            data.add(cur);
        }
        return data;

    }

    //Gets data from array
    public static List<RecyclerViewRow> getDataNotSignedIn() {
        List<RecyclerViewRow> data = new ArrayList<>();
        int icons[] = {R.drawable.ic_home_grey, R.drawable.ic_person_grey, R.drawable.ic_person_outline_grey};
        String titles[] = {"Početna", "Prijava", "Registracija", "Pratite nas", "Ocjenite nas"};
        for (int i = 0; i < titles.length && i < icons.length; i++) {
            RecyclerViewRow cur = new RecyclerViewRow();
            cur.iconId = icons[i];
            cur.title = titles[i];
            data.add(cur);
        }
        return data;

    }

        //Sets up the drawer nav

    public void setUp(int fragment_id, DrawerLayout drawerLayout, Toolbar toolbar) {

        containerView = getActivity().findViewById(fragment_id);
        mDrawerLayout = drawerLayout;
        mdrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //if user has never opened drawer before, open it for the first time
               if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mdrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mdrawerToggle.syncState();
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

}
