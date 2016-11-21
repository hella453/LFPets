package com.parse.starter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.Parse.CheckRegisterInputs;
import com.parse.starter.R;
import com.parse.starter.Spinners;


public class RegisterActivity extends ActionBarActivity {

    TextView imeTxt, kontaktTxt,emailTxt, passTxt, errorMessage;
    ImageView emailIcon, personIcon, callIcon, keyIcon;
    String ime, kontakt, email, pass, confPass, result;
    ScrollView scroll;
    Toolbar toolbar;
    Spinner brojevi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Set toolbar
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Registracija");

        //Resizes ScrollView's height when keyboard is on
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        //Enable home bttn
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize widgets
        imeTxt = (TextView)findViewById(R.id.imeTxt);
        kontaktTxt = (TextView)findViewById(R.id.telTxt);
        emailTxt = (TextView)findViewById(R.id.registerEmail);
        passTxt = (TextView)findViewById(R.id.registerPassTxt);
        personIcon=(ImageView)findViewById(R.id.iconPerson);
        emailIcon=(ImageView)findViewById(R.id.iconEmail);
        callIcon=(ImageView)findViewById(R.id.iconCall);
        keyIcon=(ImageView)findViewById(R.id.iconKey);
        errorMessage = (TextView)findViewById(R.id.errorTxt);

        //Spinner
        brojevi =(Spinner) findViewById(R.id.registerSpinner);
        Spinners.setSpinner(getApplication(), brojevi, "pozivniBroj_array");

        //checkFocus
        imeTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    personIcon.setImageResource(R.drawable.ic_person_blue);
                } else {
                    personIcon.setImageResource(R.drawable.ic_person_grey);
                }
            }
        });

        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailIcon.setImageResource(R.drawable.ic_mail_blue);
                } else {
                   emailIcon.setImageResource(R.drawable.ic_mail_grey);
                }
            }
        });

        kontaktTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    callIcon.setImageResource(R.drawable.ic_call_blue);
                } else {
                    callIcon.setImageResource(R.drawable.ic_call_grey);
                }
            }
        });

        passTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyIcon.setImageResource(R.drawable.ic_vpn_key_blue);
                } else {
                    keyIcon.setImageResource(R.drawable.ic_vpn_key_grey);
                }
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.registerIcon:
                registerUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void registerUser() {
        ime = imeTxt.getText().toString();
        kontakt = kontaktTxt.getText().toString();
        email = emailTxt.getText().toString();
        pass = passTxt.getText().toString();



        //Check inputs
        CheckRegisterInputs check = new CheckRegisterInputs();
        result = check.checkInputs(ime, kontakt, email, pass);

        if (result.equals("true"))
        {
            //Store user
            ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(pass);
            user.setEmail(email);
            user.put("ime", ime);
            user.put("tel", brojevi.getSelectedItem().toString() + kontakt);
            ParseUser currentUser = ParseUser.getCurrentUser();
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("username", email);
            installation.saveInBackground();


            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null)
                    {
                        // Open Pets Category
                        Intent i = new Intent(RegisterActivity.this,CategoriesActivity.class);
                        //i.putExtra(KEY_USER, json_user.getString(KEY_NAME));
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        errorMessage.setText("Korisnik sa tim email-om već postoji.");
                    }
                }
            });
        }else{
            errorMessage.setText(result);
        }

    }
}
