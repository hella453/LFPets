package com.parse.starter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.starter.ForgotPasswordActivity;
import com.parse.starter.R;


public class LoginActivity extends ActionBarActivity {
    TextView emailTxt, passTxt, errorMsgTxt;
    String email, pass;
    ScrollView scroll;
    Toolbar toolbar;
    ImageView emailIcon, keyIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Set toolbar
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("   Prijava");

        //Keyboard problems
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //Enable home bttn
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Intitialize widgets
        emailTxt = (EditText)findViewById(R.id.loginEmail);
        passTxt = (EditText)findViewById(R.id.loginPassTxt);
        errorMsgTxt = (TextView)findViewById(R.id.loginErrorMessage);
        emailIcon=(ImageView)findViewById(R.id.loginIconEmail);
        keyIcon=(ImageView)findViewById(R.id.loginIconKey);

        //Check Focus
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

        passTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    keyIcon.setImageResource(R.drawable.ic_vpn_key_blue);
                } else {
                    keyIcon.setImageResource(R.drawable.ic_call_grey);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case android.R.id.home:
               // app icon in action bar clicked; goto parent activity.
               this.finish();
               return true;
           case R.id.loginIcon:
               loginClick();
               return true;
           default:

               return super.onOptionsItemSelected(item);
       }
    }

    //User login

    public void loginClick() {
        email = emailTxt.getText().toString();
        pass = passTxt.getText().toString();

        ParseUser.logInInBackground(email, pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    Intent i = new Intent(LoginActivity.this, CategoriesActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {
                    errorMsgTxt.setText("Neispravan email i/ili lozinka! Pokušajte ponovo.");
                }
            }
        });
    }

    public void forgotClick(View view)
    {
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
    //Facebook login
    public void facebookLogin(View view) {

        Toast.makeText(getApplicationContext(),"Prijava preko Facebook-a još nije u funkciji.",Toast.LENGTH_LONG ).show();
    }
    //Google + login
    public void googleLogin(View view) {
        Toast.makeText(getApplicationContext(),"Prijava preko Google+ još nije u funkciji.",Toast.LENGTH_LONG ).show();
    }
    //Twitter login
    public void twitterLogin(View view) {
        Toast.makeText(getApplicationContext(),"Prijava preko Twittera-a još nije u funkciji.",Toast.LENGTH_LONG ).show();
    }
}
