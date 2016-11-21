package com.parse.starter.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PetProfileActivity extends ActionBarActivity {
    Toolbar toolbar;
    TextView imeTxt, pasminaTxt, spolTxt, starostTxt, velicinaTxt, gradTxt, kvartTxt, opisTxt, imeOglasivacaTxt, telefonOglasivacaTxt, emailOglasivacaTxt;
    ImageView petProfileImage;
    List<ParseObject> ob, obUser;
    String telefon, user,imeOglasivaca, emailOglasivaca, imageUrl;
    ParseFile image;
    private boolean zoomOut = false;
    String petId, id, favoritId;
    ParseUser currentUser;
    private Menu menu;
    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        //SetToolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("   Detalji");

        //Enable home bttn
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Floating button

        //Initialize widgets
        imeTxt = (TextView) findViewById(R.id.imePetTxt);
        pasminaTxt = (TextView) findViewById(R.id.pasminaTxt);
        spolTxt = (TextView) findViewById(R.id.spolTxt);
        starostTxt = (TextView) findViewById(R.id.starostTxt);
        velicinaTxt = (TextView) findViewById(R.id.velicinaTxt);
        gradTxt = (TextView) findViewById(R.id.gradTxt);
        kvartTxt = (TextView) findViewById(R.id.kvartTxt);
        opisTxt = (TextView) findViewById(R.id.opisTxt);
        petProfileImage = (ImageView) findViewById(R.id.imagePet);
        imeOglasivacaTxt = (TextView) findViewById(R.id.imeOglasivacaTxt);
        telefonOglasivacaTxt = (TextView) findViewById(R.id.telefonOglasivacaTxt);
        emailOglasivacaTxt = (TextView) findViewById(R.id.emailOglasivacaTxt);


        //Bundle
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        id = b.getString("ID");

        //get currentUser
        currentUser = ParseUser.getCurrentUser();

        //Dohvat iz baze
        dohvatiIzBaze();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pet_profile, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(), "Ova opcija još nije u funkciji", Toast.LENGTH_LONG).show();
                return true;
            case R.id.ic_favorit:
                if(currentUser == null){
                    Toast.makeText(getApplicationContext(), "Molimo ulogirajte se kako bi ste mogli dodavati oglase u favorite", Toast.LENGTH_LONG).show();
                }
                else{
                    //Dodaj u favorite
                    if (isFavorite){
                        Toast.makeText(this, "Ljubimac je već dodan u favorite!", Toast.LENGTH_SHORT).show();
                        //Brisi iz favorita

                       /* ParseQuery<ParseObject> daLiJeVecDodan = ParseQuery.getQuery("Favorit");
                        daLiJeVecDodan.whereEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
                        daLiJeVecDodan.whereEqualTo("petId",id);
                        daLiJeVecDodan.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> postojiFavoirit, ParseException e) {
                                if (e == null) {
                                    favoritId = postojiFavoirit.get(0).getObjectId().toString();
                                } else {
                                    Log.d("score", "Error: " + e.getMessage());
                                }
                            }
                        });


                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorit");
                        query.getInBackground(favoritId, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    try {
                                        object.delete();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                } else {
                                    // something went wrong
                                }
                            }
                        });
                        menu.getItem(0).setIcon(R.drawable.ic_star_outline_white);*/
                    }
                    else {
                        ParseObject favorit = new ParseObject("Favorit");
                        favorit.put("username", ParseUser.getCurrentUser().getUsername().toString());
                        favorit.put("petId", petId);
                        favorit.saveInBackground();
                        Toast.makeText(this, "Ljubimac je dodan u favorite!", Toast.LENGTH_SHORT).show();
                        menu.getItem(0).setIcon(R.drawable.ic_grade_white);
                        isFavorite = true;
                    }
                }


        }

        return super.onOptionsItemSelected(item);
    }

    //Poziv
    public void onClickCall(View view) {
        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + telefon));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
    public void dohvatiIzBaze(){

        //Dohvat iz baze
        try {
            // Locate the class table named "Pets" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Pets");
            ParseQuery<ParseObject> queryUser = new ParseQuery<ParseObject>(
                    "_User");
            // Locate the column named "createdAt" in Parse.com and order list
            // by ascending
            query.whereEqualTo("objectId", id);

            ob = query.find();
            for (ParseObject oglas : ob) {
                // Locate images in img column
                image = (ParseFile) oglas.get("img");

                imageUrl = image.getUrl().toString();
                imeTxt.setText((String) oglas.get("ime"));
                pasminaTxt.setText((String) oglas.get("pasmina"));
                spolTxt.setText((String) oglas.get("spol"));
                starostTxt.setText((String) oglas.get("starost"));
                velicinaTxt.setText((String) oglas.get("velicina"));
                gradTxt.setText((String) oglas.get("grad"));
                kvartTxt.setText((String) oglas.get("kvart"));
                opisTxt.setText((String) oglas.get("opis"));
                user = (String) oglas.get("addedByUser");
                petId = oglas.getObjectId().toString();
                Ion.with(this).load(imageUrl).intoImageView(petProfileImage);
                imeOglasivacaTxt.setText(user);
            }
            queryUser.whereEqualTo("username", user);
            obUser = queryUser.find();
            for (ParseObject oglasUser : obUser) {
                telefon = (String) oglasUser.get("tel");
                telefonOglasivacaTxt.setText(telefon);
                imeOglasivacaTxt.setText((String) oglasUser.get("ime"));
                emailOglasivacaTxt.setText((String) oglasUser.get("email"));

            }
            if (currentUser != null) {
            //Provjeri je li oglas u favoritima
            ParseQuery<ParseObject> fav = ParseQuery.getQuery("Favorit");
            fav.whereEqualTo("petId", petId);
            fav.whereEqualTo("username", currentUser.getUsername().toString());
            fav.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> unesen, ParseException e) {
                    if (e == null) {
                        if (unesen.size() > 0) {
                            menu.getItem(0).setIcon(R.drawable.ic_grade_white);
                            isFavorite = true;
                        } else {
                            menu.getItem(0).setIcon(R.drawable.ic_star_outline_white);
                            isFavorite = false;
                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });

        }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}
