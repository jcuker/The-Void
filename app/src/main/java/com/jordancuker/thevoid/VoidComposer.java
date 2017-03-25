package com.jordancuker.thevoid;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class VoidComposer extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.void_composer);
        sqLiteDatabase = openOrCreateDatabase("TheVoid",MODE_PRIVATE,null);

    }

    public void backToMain(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void sendToVoid(View view){
        EditText composer = (EditText)findViewById(R.id.composer_text_input);
        Void newVoid = new Void(composer.getText().toString(), System.currentTimeMillis());
        sqLiteDatabase.execSQL("INSERT INTO TheVoid VALUES(' "+ newVoid.getText() + "','" + newVoid.getTimeCreated() + "')");
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.void_composer_root_layout), "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
        snackbar.show();
        logDatabase();
    }

    public void logDatabase(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM TheVoid;",null);
        cursor.moveToFirst();
        Log.i("Test Database",cursor.getString(0));
    }
}
