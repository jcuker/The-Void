package com.jordancuker.thevoid;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;


public class VoidComposer extends AppCompatActivity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPreferences;
    int databaseID;
    Void newVoid;
    Dialog dialog;
    ConstraintLayout rootView;
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.void_composer);
        sqLiteDatabase = openOrCreateDatabase("TheVoid",MODE_PRIVATE,null);
        sharedPreferences = getSharedPreferences("com.jordancuker.thevoid",MODE_PRIVATE);
        databaseID = sharedPreferences.getInt("database_id",0);
        newVoid = new Void();
        rootView = (ConstraintLayout) findViewById(R.id.void_composer_root_layout);
        final GestureDetector gdt = new GestureDetector(getApplicationContext(), new GestureListener());
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
        button = (FloatingActionButton) findViewById(R.id.voidbutton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialog = new Dialog(this, R.style.myDialog);
        dialog.setContentView(R.layout.void_dialog);
        dialog.show();
    }

    public void backToMain(View view) {
        backToMain();
    }

    public void backToMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void sendToVoid(View view){
        EditText composer = (EditText)findViewById(R.id.composer_text_input);
        newVoid.setText(composer.getText().toString());
        newVoid.setTimeCreated(new Date());
        sqLiteDatabase.execSQL("INSERT INTO TheVoid VALUES('" + databaseID + "','" + newVoid.getMood() + "','" + newVoid.getText() + "','" + newVoid.getTimeCreated() + "')");
        databaseID++;
        backToMain();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.edit().putInt("database_id",databaseID).apply();
    }

    public void emojiClick(View view){
        switch(view.getId()) {
            case R.id.smile:
                newVoid.setMood(0);
                dialog.dismiss();
                break;
            case R.id.neutral:
                newVoid.setMood(1);
                dialog.dismiss();
                break;
            case R.id.crying:
                newVoid.setMood(2);
                dialog.dismiss();
                break;
            case R.id.angry:
                newVoid.setMood(3);
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                EditText composer = (EditText)findViewById(R.id.composer_text_input);
                if(composer.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(),"Must enter text",Toast.LENGTH_SHORT).show();
                    return false;
                }
                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_to_void_fab);
                fab.hide();
                button.animate().alphaBy(1f).scaleXBy(100f).scaleYBy(100f).setDuration(1200).start();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.callOnClick();
                    }
                }, 1200);
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            return false;
        }
    }
}
