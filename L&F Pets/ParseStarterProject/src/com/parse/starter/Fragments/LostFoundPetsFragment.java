package com.parse.starter.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.Activities.PetProfileActivity;
import com.parse.starter.Adapters.CustomListViewAdapter;
import com.parse.starter.Parse.Ljubimac;
import com.parse.starter.R;
import com.parse.starter.RowItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Helena on 4/22/2015.
 */
public class LostFoundPetsFragment extends ListFragment implements AdapterView.OnItemClickListener {


    public String[] ime;
    public String[] pasmina;
    public String[] mjesto;
    public String[] vrijeme;
    public String[] images;

    ListView listView;
    List<RowItem> rowItems;
    Context thiscontext;
    List<ParseObject> ob;
    private List<Ljubimac> ljubimci = null;
    ProgressDialog mProgressDialog;
    View view;
    String vrsta;
    String tipOglasa;
    CustomListViewAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        view = inflater.inflate(R.layout.fragment_lost_found_pets, container, false);

        //Bundle
        Bundle b = getArguments();
        vrsta = b.getString("Vrsta");
        tipOglasa = b.getString("tipOglasa");

        //SwipeRefresh
      //  mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);


        //Dohvat iz baze
        dohvatiIzBaze();

        listView = (ListView) view.findViewById(android.R.id.list);
        adapter = new CustomListViewAdapter(getActivity(), R.id.list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;

                /**
                 * This enables us to force the layout to refresh only when the first item
                 * of the list is visible.
                 **/
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                //mSwipeRefreshLayout.setEnabled(enable);
            }
        });

   /*     mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dohvatiIzBaze();
                        adapter = new CustomListViewAdapter(getActivity(), R.id.list_item, rowItems);
                        listView.setAdapter(adapter);


                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);


            }
        });
*/

        return view;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }

    public void dohvatiIzBaze(){

        //Dohvat iz baze
        ljubimci = new ArrayList<Ljubimac>();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sati = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        String datum = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String jucer = sdf.format(cal.getTime());
        try {
            // Locate the class table named "Pets" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Pets");
            // Locate the column named "createdAt" in Parse.com and order list
            // by ascending
            query.whereEqualTo("vrsta", vrsta);
            query.whereEqualTo("tipOglasa", tipOglasa);
            query.orderByDescending("createdAt");
            ob = query.find();
            for (ParseObject oglas : ob) {
                // Locate images in img column
                ParseFile image = (ParseFile) oglas.get("imgSmall");

                Ljubimac pet = new Ljubimac();
                pet.setObjectId(oglas.getObjectId());
                pet.setAddedByUser((String) oglas.get("addedByUser"));
                pet.setTipOglasa((String) oglas.get("tipOglasa"));
                pet.setIme((String) oglas.get("ime"));
                pet.setVrsta((String) oglas.get("vrsta"));
                pet.setPasmina((String) oglas.get("pasmina"));
                pet.setSpol((String) oglas.get("spol"));
                pet.setStarost((String) oglas.get("starost"));
                pet.setVelicina((String) oglas.get("velicina"));
                pet.setGrad((String) oglas.get("grad"));
                pet.setKvart((String) oglas.get("kvart"));
                pet.setOpis((String) oglas.get("opis"));
                pet.setImg(image.getUrl());
                if (datum.contains(sdf.format(oglas.getCreatedAt())))
                    pet.setCreatedAt("Danas     " + sati.format(oglas.getCreatedAt()));
                else if (jucer.contains(sdf.format(oglas.getCreatedAt())))
                    pet.setCreatedAt("Jučer     " + sati.format(oglas.getCreatedAt()));
                else
                    pet.setCreatedAt(df.format(oglas.getCreatedAt()));
                ljubimci.add(pet);
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        ime = new String[ljubimci.size()];
        pasmina = new String[ljubimci.size()];
        mjesto = new String[ljubimci.size()];
        vrijeme = new String[ljubimci.size()];
        images = new String[ljubimci.size()];

        for (int br = 0; br < ljubimci.size(); br++) {
            ime[br] = ljubimci.get(br).getIme().toString();
            pasmina[br] = ljubimci.get(br).getPasmina().toString();
            mjesto[br] = ljubimci.get(br).getGrad().toString() + " - " + ljubimci.get(br).getKvart().toString();
            vrijeme[br] = ljubimci.get(br).getCreatedAt().toString();
            images[br] = ljubimci.get(br).getImg().toString();
        }

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < ime.length; i++) {
            RowItem item = new RowItem(images[i], ime[i], pasmina[i], mjesto[i], vrijeme[i]);
            rowItems.add(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity().getApplicationContext(), PetProfileActivity.class);
        i.putExtra("ID", ljubimci.get(position).getObjectId().toString());
        startActivity(i);
        //Toast toast = Toast.makeText(getActivity().getApplicationContext(),
        //       ljubimci.get(position).getObjectId().toString(),
        //         Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //  toast.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}