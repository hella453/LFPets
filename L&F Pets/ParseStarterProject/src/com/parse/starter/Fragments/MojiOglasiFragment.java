package com.parse.starter.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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
public class MojiOglasiFragment extends ListFragment implements AdapterView.OnItemClickListener {


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
    public CustomListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = container.getContext();
        View view = inflater.inflate(R.layout.fragment_lost_found_pets, container, false);

        //Bundle
        Bundle b = getArguments();
        String vrsta = b.getString("Vrsta");
        String tipOglasa = b.getString("tipOglasa");


        //get currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

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
            query.whereEqualTo("addedByUser", currentUser.getUsername().toString());
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

        listView = (ListView) view.findViewById(android.R.id.list);
        adapter = new CustomListViewAdapter(getActivity(), R.id.list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        //On long click delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int row, long arg3) {
            // your code

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle("Izbriši iz mojih oglasa");
            dialogBuilder.setMessage("Da li sigurno želite obrisati svoj oglas?");
            dialogBuilder.setPositiveButton("Obriši", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Pets");
                    query.getInBackground(ljubimci.get(row).getObjectId().toString(), new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                try {
                                    object.delete();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }

                                rowItems.remove(row);
                                adapter.notifyDataSetChanged();

                            } else {
                                // something went wrong
                            }
                        }
                    });
                    //Edit
                }
            });
            dialogBuilder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Delete
                }
            });
            dialogBuilder.create().show();
            return true;
        }
    });
    return view;
            }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


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