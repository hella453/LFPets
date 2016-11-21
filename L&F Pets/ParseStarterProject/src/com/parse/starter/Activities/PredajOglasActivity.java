package com.parse.starter.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Fragments.NavigationDrawerFragment;
import com.parse.starter.R;
import com.parse.starter.Spinners;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import at.markushi.ui.CircleButton;


public class PredajOglasActivity extends ActionBarActivity {

    Spinner tipOglasa, kategorija, spol, starost, velicina;
    EditText imeZivotinje, pasmina, kvart, opis, grad;
    TextView tipOglasaTxt, kategorijaTxt, spolTxt, starostTxt, velicinaTxt, imeZivotinjeTxt, pasminaTxt, kvartTxt, opisTxt, gradTxt;
    Toolbar toolbar;
    CircleButton bttnPhoto;
    ImageView viewImage;
    Intent redirect;
    ProgressBar progres;
    ScrollView scroll;
    Bitmap bitmap;
    Bitmap small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predaj_oglas);

        //SetToolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Predaj oglas");
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Initialize
        tipOglasa = (Spinner) findViewById(R.id.spinnerTipOglasa);
        kategorija = (Spinner) findViewById(R.id.spinnerKategorija);
        spol = (Spinner) findViewById(R.id.spinnerSpol);
        starost = (Spinner) findViewById(R.id.spinnerStarost);
        velicina = (Spinner) findViewById(R.id.spinnerVelicina);
        grad = (EditText) findViewById(R.id.inputGrad);
        imeZivotinje = (EditText) findViewById(R.id.inputImeZivotinje);
        pasmina = (EditText) findViewById(R.id.inputPasmina);
        kvart = (EditText) findViewById(R.id.inputKvart);
        opis = (EditText) findViewById(R.id.inputOpis);
        bttnPhoto = (CircleButton) findViewById(R.id.bttnPhoto);
        viewImage = (ImageView) findViewById(R.id.imageViewZivotinje);
        progres = (ProgressBar) findViewById(R.id.progressBar);
        scroll = (ScrollView) findViewById(R.id.scrollView);

        tipOglasaTxt = (TextView) findViewById(R.id.texttipoglasa);
        kategorijaTxt = (TextView) findViewById(R.id.kategorijatext);
        spolTxt = (TextView) findViewById(R.id.spoltext);
        starostTxt = (TextView) findViewById(R.id.starosttext);
        velicinaTxt = (TextView) findViewById(R.id.velicinatext);
        gradTxt = (TextView) findViewById(R.id.gradtext);
        imeZivotinjeTxt = (TextView) findViewById(R.id.imetext);
        pasminaTxt = (TextView) findViewById(R.id.pasminatext);
        kvartTxt = (TextView) findViewById(R.id.kvarttext);
        opisTxt = (TextView) findViewById(R.id.opistext);

        progres.getIndeterminateDrawable().setColorFilter(0xFF4389FF, android.graphics.PorterDuff.Mode.MULTIPLY);

        //Set spinners
        Spinners.setSpinner(getApplicationContext(), tipOglasa, "tipOglasa_array");
        Spinners.setSpinner(getApplicationContext(), kategorija, "kategorija_array");
        Spinners.setSpinner(getApplicationContext(), spol, "spol_array");
        Spinners.setSpinner(getApplicationContext(), starost, "starost_array");
        Spinners.setSpinner(getApplicationContext(), velicina, "velicina_array");


        //Set Navigation Drawer
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_predaj_oglas, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.predajOglas:
                predajOglas();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickPhoto(View view) {
        final CharSequence[] options = {"Slikaj s kamerom", "Odaberi iz galerije", "Odustani"};


        AlertDialog.Builder builder = new AlertDialog.Builder(PredajOglasActivity.this);

        builder.setTitle("Dodajte fotografiju!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {


                if (options[item].equals("Slikaj s kamerom"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Odaberi iz galerije"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Odustani")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Ako je slikana
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    bitmap = scalePhoto(f, 570, 480);
                    small = scalePhoto(f, 80, 80);
                    viewImage.setImageBitmap(bitmap);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                //Ako je iz galerije
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                File fil = new File(picturePath);
                bitmap = scalePhoto(fil, 352, 240);
                small = scalePhoto(fil, 80, 80);
                viewImage.setImageBitmap(bitmap);
            }
        }
    }

    //Scale bitmap
    public static Bitmap scalePhoto(File f, int WIDTH, int HIGHT) {
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            //The new size we want to scale to
            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;
            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }


    public void predajOglas() {
        if (imeZivotinje.getText().toString() != null && !imeZivotinje.getText().toString().isEmpty()
                && pasmina.getText().toString() != null && !pasmina.getText().toString().isEmpty()
                && grad.getText().toString() != null && !grad.getText().toString().isEmpty()
                && kvart.getText().toString() != null && !kvart.getText().toString().isEmpty()
                && opis.getText().toString() != null && !opis.getText().toString().isEmpty()) {
            if (viewImage.getDrawable() != null) {

                progres.setVisibility(View.VISIBLE);
                scroll.scrollTo(0, 0);
                //small icon

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ByteArrayOutputStream stream1 = new ByteArrayOutputStream();

                small.compress(Bitmap.CompressFormat.JPEG, 90, stream1);
                byte[] dataSmall = stream1.toByteArray();
                ParseFile imageSmallParse = new ParseFile("slikaSmall.jpeg", dataSmall);
                imageSmallParse.saveInBackground();
                //large photo

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] data = stream.toByteArray();
                ParseFile tempy = new ParseFile("slika.jpeg", data);
                tempy.saveInBackground();

                ParseObject pet = new ParseObject("Pets");
                pet.put("addedByUser", ParseUser.getCurrentUser().getUsername().toString());
                pet.put("tipOglasa", tipOglasa.getSelectedItem().toString());
                pet.put("ime", imeZivotinje.getText().toString());
                pet.put("vrsta", kategorija.getSelectedItem().toString());
                pet.put("pasmina", pasmina.getText().toString());
                pet.put("spol", spol.getSelectedItem().toString());
                pet.put("starost", starost.getSelectedItem().toString());
                pet.put("velicina", velicina.getSelectedItem().toString());
                pet.put("grad", grad.getText().toString());
                pet.put("kvart", kvart.getText().toString());
                pet.put("opis", opis.getText().toString());
                pet.put("img", tempy);
                pet.put("imgSmall", imageSmallParse);
                pet.saveInBackground();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                        //Push notifikacija i redirect

                        ParsePush push = new ParsePush();
                        ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
                        pQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername().toString()); // <-- you'll probably want to target someone that's not the current user, so modify accordingly


                        if (tipOglasa.getSelectedItem().toString().contentEquals("Izgubljen sam")) {
                            if(spol.getSelectedItem().toString().contentEquals("Mužjak")) {
                                Intent i = new Intent(getApplicationContext(), LostPetsActivity.class);
                                redirect = i;
                                ParseAnalytics.trackAppOpenedInBackground(i);

                                push.sendMessageInBackground("Izgubio se " + imeZivotinje.getText().toString() + "! Jeste li ga vidjeli?", pQuery);
                                startActivity(redirect);
                                finish();
                            }
                            else
                            {
                                Intent i = new Intent(getApplicationContext(), LostPetsActivity.class);
                                redirect = i;
                                ParseAnalytics.trackAppOpenedInBackground(i);

                                push.sendMessageInBackground("Izgubila se " + imeZivotinje.getText().toString() + "! Jeste li ju vidjeli?", pQuery);
                                startActivity(redirect);
                                finish();
                            }
                        } else if (tipOglasa.getSelectedItem().toString().contentEquals("Nađen sam")) {
                            if(spol.getSelectedItem().toString().contentEquals("Mužjak")) {
                                Intent j = new Intent(getApplicationContext(), FoundPetsActivity.class);
                                redirect = j;
                                ParseAnalytics.trackAppOpenedInBackground(j);
                                push.sendMessageInBackground("Pronađen je " + imeZivotinje.getText().toString() + "! Poznaješ li vlasnika?", pQuery);
                                startActivity(redirect);
                                finish();
                            }
                            else
                            {
                                Intent j = new Intent(getApplicationContext(), FoundPetsActivity.class);
                                redirect = j;
                                ParseAnalytics.trackAppOpenedInBackground(j);
                                push.sendMessageInBackground("Pronađena je " + imeZivotinje.getText().toString() + "! Poznaješ li vlasnika?", pQuery);
                                startActivity(redirect);
                                finish();
                            }

                        }
                    }
                }, 1000);

            } else {
                Toast.makeText(this, "Molimo dodajte fotografiju", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Molimo ispunite sva polja", Toast.LENGTH_LONG).show();
        }

    }


}

