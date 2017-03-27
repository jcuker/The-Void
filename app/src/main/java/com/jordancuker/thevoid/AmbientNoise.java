package com.jordancuker.thevoid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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
    SeekBar fireSeek, oceanSeek, trainSeek;
    private int runningAudio = 0; //if > 0, audio is running and notification should be displayed

    NotificationCompat.Builder mBuilder;
    Intent resultIntent;
    TaskStackBuilder stackBuilder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ambient_noise_content);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


        musicMap = new HashMap<>();
        musicMap.put("fire", MediaPlayer.create(this, R.raw.fire));
        musicMap.put("ocean", MediaPlayer.create(this,R.raw.ocean));
        musicMap.put("train", MediaPlayer.create(this, R.raw.train));

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



        //notif
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Ambient noise is playing audio.");
        mBuilder.setContentText("Tap here to return to the app.");
        resultIntent = new Intent(this, AmbientNoise.class);
        stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AmbientNoise.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);


        /*//probably wrong but oh well
        SharedPreferences sharedPreferences = getSharedPreferences("com.jordancuker.thevoid",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("firePlaying",false)) fireSeek.setVisibility(View.VISIBLE);
        if(sharedPreferences.getBoolean("trainPlaying",false)) trainSeek.setVisibility(View.VISIBLE);
        if(sharedPreferences.getBoolean("oceanPlaying",false)) oceanSeek.setVisibility(View.VISIBLE);*/
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
        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);;
        if(runningAudio == 0){
            mNotificationManager.cancel(1);
        }
        else{
            mNotificationManager.notify(1, mBuilder.build());
        }
    }



   /* @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("com.jordancuker.thevoid",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("firePlaying",musicMap.get("fire").isPlaying())
                .putBoolean("trainPlaying",musicMap.get("train").isPlaying())
                .putBoolean("oceanPlaying",musicMap.get("ocean").isPlaying())
                .apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("com.jordancuker.thevoid",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("firePlaying",musicMap.get("fire").isPlaying())
                .putBoolean("trainPlaying",musicMap.get("train").isPlaying())
                .putBoolean("oceanPlaying",musicMap.get("ocean").isPlaying())
                .apply();
    }*/
}
