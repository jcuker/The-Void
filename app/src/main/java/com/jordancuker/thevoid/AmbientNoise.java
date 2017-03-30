package com.jordancuker.thevoid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;

import static android.media.ToneGenerator.MAX_VOLUME;


public class AmbientNoise extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    AudioManager audioManager;
    HashMap<String, MediaPlayer> musicMap;
    SeekBar fireSeek, oceanSeek, trainSeek, librarySeek, windSeek, whiteSeek;
    private int runningAudio = 0; //if > 0, audio is running and notification should be displayed

    NotificationCompat.Builder mBuilder;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;
    IntentFilter filter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambient_noise_activity);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


        SharedPreferences sharedPreferences = getSharedPreferences("com.jordancuker.thevoid", MODE_PRIVATE);
        if(!audioManager.isMusicActive()){
            musicMap = new HashMap<>();
            musicMap.put("fire", MediaPlayer.create(this, R.raw.fire));
            musicMap.put("ocean", MediaPlayer.create(this, R.raw.ocean));
            musicMap.put("library", MediaPlayer.create(this, R.raw.library));
            musicMap.put("train", MediaPlayer.create(this, R.raw.train));
            musicMap.put("wind", MediaPlayer.create(this, R.raw.wind));
            musicMap.put("white", MediaPlayer.create(this, R.raw.white_noise));
        }

        //handle seekbars
        fireSeek = (SeekBar)findViewById(R.id.fire_seekbar);
        fireSeek.setProgress(50);
        fireSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
                musicMap.get("fire").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        oceanSeek = (SeekBar) findViewById(R.id.ocean_seekbar);
        oceanSeek.setProgress(50);
        oceanSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = calcVol(progress);
                musicMap.get("ocean").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        trainSeek = (SeekBar) findViewById(R.id.train_seekbar);
        trainSeek.setProgress(50);
        trainSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = calcVol(progress);
                musicMap.get("train").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        librarySeek = (SeekBar)findViewById(R.id.library_seekbar);
        librarySeek.setProgress(50);
        librarySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = calcVol(progress);
                musicMap.get("library").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        windSeek = (SeekBar) findViewById(R.id.wind_seekbar);
        windSeek.setProgress(50);
        windSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = calcVol(progress);
                musicMap.get("wind").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        whiteSeek = (SeekBar) findViewById(R.id.white_seekbar);
        whiteSeek.setProgress(50);
        whiteSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float vol = calcVol(progress);
                musicMap.get("white").setVolume(vol,vol);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //nav menu

        //notif

   /*     mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Ambient noise is playing audio.");
        mBuilder.setContentText("Tap here to kill service.");
        resultIntent = new Intent(this, AmbientNoise.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AmbientNoise.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);*/


        Intent intent = new Intent("close_app");
        PendingIntent pIntent = PendingIntent.getBroadcast(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        filter = new IntentFilter("close_app");
        registerReceiver(mReceiver,filter);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Ambient Noise is playing audio.");
        mBuilder.setContentText("Tap here to kill the service.");
        mBuilder.setContentIntent(pIntent);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            endAudio();
            finishAffinity();
        }
    };
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    public void endAudio(){
        musicMap.get("train").stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        //outState.putSerializable("starttime", startTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(musicMap == null) return;
        if(musicMap.containsKey("fire") && musicMap.get("fire").isPlaying()) findViewById(R.id.fire_noise).setAlpha(1);
        else musicMap.put("fire", MediaPlayer.create(getApplicationContext(), R.raw.fire));
        if(musicMap.containsKey("ocean") && musicMap.get("ocean").isPlaying()) findViewById(R.id.ocean_noise).setAlpha(1);
        else musicMap.put("ocean", MediaPlayer.create(getApplicationContext(), R.raw.ocean));
        if(musicMap.containsKey("train") && musicMap.get("train").isPlaying()) findViewById(R.id.train_noise).setAlpha(1);
        else musicMap.put("train", MediaPlayer.create(getApplicationContext(), R.raw.train));
        if(musicMap.containsKey("library") && musicMap.get("library").isPlaying()) findViewById(R.id.library_noise).setAlpha(1);
        else musicMap.put("library", MediaPlayer.create(getApplicationContext(), R.raw.library));
        if(musicMap.containsKey("wind") && musicMap.get("wind").isPlaying()) findViewById(R.id.wind_noise).setAlpha(1);
        else musicMap.put("wind", MediaPlayer.create(getApplicationContext(), R.raw.wind));
        if(musicMap.containsKey("white") && musicMap.get("white").isPlaying()) findViewById(R.id.white_noise).setAlpha(1);
        else musicMap.put("white", MediaPlayer.create(getApplicationContext(), R.raw.white_noise));
    }

    public float calcVol(int progress){
        return  (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
        else if(id == R.id.about){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void iconClick(View view){
        switch (view.getId()){
            case R.id.fire_noise:
                if(musicMap.get("fire").isPlaying()) {
                    musicMap.get("fire").stop();
                    findViewById(R.id.fire_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else {
                    musicMap.remove("fire");
                    musicMap.put("fire",MediaPlayer.create(getApplicationContext(),R.raw.fire));
                    float vol = calcVol(50);
                    musicMap.get("fire").setVolume(vol,vol);
                    musicMap.get("fire").setLooping(true);
                    fireSeek.setProgress(50);
                    musicMap.get("fire").start();
                    findViewById(R.id.fire_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;

            case R.id.ocean_noise:
                if(musicMap.get("ocean").isPlaying()) {
                    musicMap.get("ocean").stop();
                    findViewById(R.id.ocean_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else {
                    musicMap.remove("ocean");
                    musicMap.put("ocean", MediaPlayer.create(getApplicationContext(),R.raw.ocean));
                    float vol = calcVol(50);
                    musicMap.get("ocean").setVolume(vol,vol);
                    musicMap.get("ocean").setLooping(true);
                    oceanSeek.setProgress(50);
                    musicMap.get("ocean").start();
                    findViewById(R.id.ocean_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;

            case R.id.train_noise:
                if(musicMap.get("train").isPlaying()){
                    musicMap.get("train").stop();
                    findViewById(R.id.train_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else{
                    musicMap.remove("train");
                    musicMap.put("train", MediaPlayer.create(getApplicationContext(), R.raw.train));
                    float vol = calcVol(50);
                    musicMap.get("train").setVolume(vol,vol);
                    musicMap.get("train").setLooping(true);
                    trainSeek.setProgress(50);
                    musicMap.get("train").start();
                    findViewById(R.id.train_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;

            case R.id.library_noise:
                if(musicMap.get("library").isPlaying()){
                    musicMap.get("library").stop();
                    findViewById(R.id.library_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else{
                    musicMap.remove("library");
                    musicMap.put("library",MediaPlayer.create(this, R.raw.library));
                    float vol = calcVol(50);
                    musicMap.get("library").setVolume(vol,vol);
                    musicMap.get("library").setLooping(true);
                    musicMap.get("library").start();
                    librarySeek.setProgress(50);
                    findViewById(R.id.library_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;

            case R.id.wind_noise:
                if(musicMap.get("wind").isPlaying()){
                    musicMap.get("wind").stop();
                    findViewById(R.id.wind_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else{
                    musicMap.remove("wind");
                    musicMap.put("wind",MediaPlayer.create(this, R.raw.wind));
                    float vol = calcVol(50);
                    musicMap.get("wind").setVolume(vol,vol);
                    musicMap.get("wind").setLooping(true);
                    musicMap.get("wind").start();
                    windSeek.setProgress(50);
                    findViewById(R.id.wind_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;

            case R.id.white_noise:
                if(musicMap.get("white").isPlaying()){
                    musicMap.get("white").stop();
                    findViewById(R.id.white_noise).setAlpha(.5f);
                    runningAudio--;
                }
                else{
                    musicMap.remove("white");
                    musicMap.put("white", MediaPlayer.create(this, R.raw.white_noise));
                    float vol = calcVol(50);
                    musicMap.get("white").setVolume(vol,vol);
                    musicMap.get("white").setLooping(true);
                    musicMap.get("white").start();
                    whiteSeek.setProgress(50);
                    findViewById(R.id.white_noise).setAlpha(1f);
                    runningAudio++;
                }
                break;
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(runningAudio == 0){
            mNotificationManager.cancel(1);
        }
        else{
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(1);
    }
}
