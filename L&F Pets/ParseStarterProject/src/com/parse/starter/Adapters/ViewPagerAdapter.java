package com.parse.starter.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.parse.starter.Fragments.FavoritiFragment;
import com.parse.starter.Fragments.LostFoundPetsFragment;
import com.parse.starter.Fragments.MojiOglasiFragment;

/**
 * Created by Helena on 4/22/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
        String tipOglasa;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, String mTipOglasa) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.tipOglasa = mTipOglasa;

            }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                if(tipOglasa=="MojiOglasi"){
                    Bundle argsMojiOglasi = new Bundle();

                    argsMojiOglasi.putString("tipOglasa","Izgubljen sam");
                    MojiOglasiFragment oglasi = new MojiOglasiFragment();
                    oglasi.setArguments(argsMojiOglasi);
                    return oglasi;
                }
                else if(tipOglasa=="Favoriti"){
                    Bundle argsFavoritiOglasi = new Bundle();

                    argsFavoritiOglasi.putString("tipOglasa","Izgubljen sam");
                    FavoritiFragment oglasi = new FavoritiFragment();
                    oglasi.setArguments(argsFavoritiOglasi);
                    return oglasi;}
                else {
                    Bundle argsDog = new Bundle();
                    argsDog.putString("Vrsta", "Psi");
                    argsDog.putString("tipOglasa", tipOglasa);
                    LostFoundPetsFragment dogs = new LostFoundPetsFragment();
                    dogs.setArguments(argsDog);
                    return dogs;

                }
            case 1:
                if(tipOglasa=="MojiOglasi"){

                    Bundle argsMojiOglasi = new Bundle();

                    argsMojiOglasi.putString("tipOglasa","Nađen sam");
                    MojiOglasiFragment oglasi = new MojiOglasiFragment();
                    oglasi.setArguments(argsMojiOglasi);
                    return oglasi;

                }
                if(tipOglasa=="Favoriti"){
                    Bundle argsFavoritiOglasi = new Bundle();

                    argsFavoritiOglasi.putString("tipOglasa","Nađen sam");
                    FavoritiFragment oglasi = new FavoritiFragment();
                    oglasi.setArguments(argsFavoritiOglasi);
                    return oglasi;}
                else {
                    Bundle argsCat = new Bundle();
                    argsCat.putString("Vrsta", "Mačke");
                    argsCat.putString("tipOglasa", tipOglasa);
                    LostFoundPetsFragment cats = new LostFoundPetsFragment();
                    cats.setArguments(argsCat);
                    return cats;
                }
            case 2:
                Bundle argsGlodavci = new Bundle();
                argsGlodavci.putString("Vrsta","Glodavci");
                argsGlodavci.putString("tipOglasa",tipOglasa);
                LostFoundPetsFragment glodavci = new LostFoundPetsFragment();
                glodavci.setArguments(argsGlodavci);
                return glodavci;

            case 3:
                Bundle argsGmazovi = new Bundle();
                argsGmazovi.putString("Vrsta","Gmazovi");
                argsGmazovi.putString("tipOglasa",tipOglasa);
                LostFoundPetsFragment gmazovi = new LostFoundPetsFragment();
                gmazovi.setArguments(argsGmazovi);
                return gmazovi;
            case 4:
                Bundle argsOstalo = new Bundle();
                argsOstalo.putString("Vrsta","Ostali");
                argsOstalo.putString("tipOglasa",tipOglasa);
                LostFoundPetsFragment ostalo = new LostFoundPetsFragment();
                ostalo.setArguments(argsOstalo);
                return ostalo;

            default:
                return null;

        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
            return Titles[position];
            }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
            return NumbOfTabs;
            }
}
