package com.parse.starter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.starter.Activities.CategoriesActivity;
import com.parse.starter.Activities.FavoritiActivity;
import com.parse.starter.Activities.LoginActivity;
import com.parse.starter.Activities.MojiOglasiActivity;
import com.parse.starter.Activities.PredajOglasActivity;
import com.parse.starter.Activities.RegisterActivity;
import com.parse.starter.NavigationDrawer.RecyclerViewRow;
import com.parse.starter.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Helena on 4/20/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context mcontext;
    private LayoutInflater inflater;
    private boolean loggedIn;
    List<RecyclerViewRow> data = Collections.emptyList();

    public RecyclerViewAdapter(Context context, List<RecyclerViewRow> data, boolean mLoggedIn) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.mcontext = context;
        this.loggedIn = mLoggedIn;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_draw_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecyclerViewRow current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);
//   ImageView icon = (ImageView)v.findViewById(R.id.listIcon);
        //icon.setImageResource(R.drawable.ic_home_blue);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
            title.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (getAdapterPosition()) {
                case 0:
                    icon.setImageResource(R.drawable.ic_home_blue);
                    title.setTextColor(v.getResources().getColor(R.color.accentColor));
                    Intent home = new Intent(mcontext, CategoriesActivity.class);
                    mcontext.startActivity(home);
                    break;
                case 1:
                    if (loggedIn) {
                        icon.setImageResource(R.drawable.ic_add_box_blue);
                        title.setTextColor(v.getResources().getColor(R.color.accentColor));
                        Intent predajOglas = new Intent(mcontext, PredajOglasActivity.class);
                        mcontext.startActivity(predajOglas);
                    } else {
                        icon.setImageResource(R.drawable.ic_person_blue);
                        title.setTextColor(v.getResources().getColor(R.color.accentColor));
                        Intent prijaviSe = new Intent(mcontext, LoginActivity.class);
                        mcontext.startActivity(prijaviSe);
                    }
                    break;
                case 2:
                    if (loggedIn) {
                        icon.setImageResource(R.drawable.ic_perm_media_blue);
                        title.setTextColor(v.getResources().getColor(R.color.accentColor));
                        Intent mojiOglasi = new Intent(mcontext, MojiOglasiActivity.class);
                        mcontext.startActivity(mojiOglasi);
                    } else {
                        icon.setImageResource(R.drawable.ic_person_outline_blue);
                        title.setTextColor(v.getResources().getColor(R.color.accentColor));
                        Intent registrirajSe = new Intent(mcontext, RegisterActivity.class);
                        mcontext.startActivity(registrirajSe);

                    }
                    break;
                case 4:
                    icon.setImageResource(R.drawable.ic_grade_blue);
                    title.setTextColor(v.getResources().getColor(R.color.accentColor));
                    Intent favoriti = new Intent(mcontext, FavoritiActivity.class);
                    mcontext.startActivity(favoriti);


                    break;

                case 6:
                    title.setTextColor(v.getResources().getColor(R.color.accentColor));
                    ParseUser.logOut();
                    Intent i = new Intent(mcontext, CategoriesActivity.class);
                    mcontext.startActivity(i);
                    break;
                default:

                    Toast.makeText(mcontext, "Ova opcija još nije u funkciji", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
