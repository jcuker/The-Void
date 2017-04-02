package com.jordancuker.thevoid;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VoidComposer.class);
                startActivity(intent);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS TheVoid (" +
                "  ID INT NOT NULL, " +
                "  MOOD INT NOT NULL, " +
                "  CONTENT TEXT NOT NULL, " +
                "  DATE_CREATED BIGINT NOT NULL, " +
                "  PRIMARY KEY(ID) " +
                "  )");





    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView();
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
            overridePendingTransition(0,0);
        }
        else if (id==R.id.about){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFirstRunHelp(){

    }

    public void setUpRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.void_viewer);
        if (recyclerView.getChildCount() != 0) return;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TheVoid",null);
        if(cursor.getColumnCount() == 0) {
            //show null
        }
        else{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Void temp = new Void();
                temp.setText(cursor.getString(2));
                temp.setTimeCreated(cursor.getLong(3));
                temp.setMood(cursor.getInt(1));
                allVoids.add(temp);
                cursor.moveToNext();
            }
        }
        Log.i("test",allVoids.toString());
        adapter = new VoidAdapter(getApplicationContext(),allVoids);
        adapter.setOnItemClickListener(new VoidAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(getApplication(),"testShort",Toast.LENGTH_LONG).show();
                /*
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
tweetIntent.setType("text/plain");

PackageManager packManager = getPackageManager();
List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

boolean resolved = false;
for(ResolveInfo resolveInfo: resolvedInfoList){
    if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
        tweetIntent.setClassName(
            resolveInfo.activityInfo.packageName,
            resolveInfo.activityInfo.name );
        resolved = true;
        break;
    }
}
if(resolved){
    startActivity(tweetIntent);
}else{
    Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
}
                 */
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Toast.makeText(getApplication(),"testLong",Toast.LENGTH_LONG).show();
                final int i = position;
                if(!allVoids.get(position).isLocked()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
                    builder.setTitle("test1");
                    builder.setMessage("test2");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeEntry(allVoids.get(i).getDatabaseID());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        cursor.close();
    }

    public void removeEntry(int databaseID){
        sqLiteDatabase.execSQL("DELETE FROM TheVoid WHERE ID=" + databaseID);
    }
}
