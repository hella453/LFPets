package com.parse.starter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.starter.Fragments.NavigationDrawerFragment;
import com.parse.starter.R;


public class CategoriesActivity extends ActionBarActivity  {

    private Toolbar toolbar;
    public TextView user;
    public ProgressBar spin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        //SetToolbar
        toolbar= (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("   Lost & Found Pets");

        //Set Navigation Drawer
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }


    //On click run LostPetsActivity
    public void onClickLost(View view) {


        Intent lostIntent = new Intent(this, LostPetsActivity.class);
        startActivity(lostIntent);
    }

    public void onClickNadjenSam(View view) {

        Intent foundIntent = new Intent (this, FoundPetsActivity.class);
        startActivity(foundIntent);
    }
}
