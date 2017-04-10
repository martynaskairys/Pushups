package com.martynaskairys.pushups;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        db = openOrCreateDatabase("PushupsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pushups(pushupsno VARCHAR, date VARCHAR);");


        // Retrieving all records
        Cursor c = db.rawQuery("SELECT * FROM pushups", null);
        // Checking if no records found
        if (c.getCount() == 0) {
            showMessage("Error", "No records found");
            return;
        }
        // Appending records to a string buffer
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Pushups: " + c.getString(0) + "\n");
            buffer.append("Date: " + c.getString(1) + "\n\n");

        }
        // Displaying all records


        showMessage("Pushup Details", buffer.toString());


    }
    public void showMessage(String title, String message) {

        TextView textView = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);

        textView3.setText(title);
        textView.setText(message);

    }




}
