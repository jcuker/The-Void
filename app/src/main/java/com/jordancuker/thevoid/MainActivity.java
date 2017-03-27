package com.jordancuker.thevoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPreferences; //used to determine first launch
    ArrayList<Void> allVoids;
    VoidAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VoidComposer.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        allVoids = new ArrayList<>();


        sharedPreferences = getSharedPreferences("com.jordancuker.thevoid", MODE_PRIVATE);
        sqLiteDatabase = openOrCreateDatabase("TheVoid",MODE_PRIVATE,null);
        if(sharedPreferences.getBoolean("first_run",true)){
            sqLiteDatabase.execSQL("CREATE TABLE TheVoid (\n" +
                    "  CONTENT\t\tTEXT\tNOT NULL,\n" +
                    "  DATE_CREATED\tBIGINT\tNOT NULL\n" +
                    "  );\n");
            sharedPreferences.edit().putBoolean("first_run",false).apply();
            showFirstRunHelp();
        }
        else{
            setUpRecyclerView();
        }



        /*
        DATABASE SCHEMA:
        TABLE TheVoid(
                CONTENT     TEXT   NOT NULL
                DATE_CREATED    BIGINT  NOT NULL
                );
         */




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ambient_noise) {
            Intent intent = new Intent(getApplicationContext(), AmbientNoise.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFirstRunHelp(){

    }

    public void setUpRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.void_composer_root_layout);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TheVoid",null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount();i++){
            Void builder = new Void();
            builder.setText(cursor.getString(i));
            builder.setTimeCreated(cursor.getLong(i));
            allVoids.add(builder);
        }
        Log.i("test",allVoids.toString());
    }
}
