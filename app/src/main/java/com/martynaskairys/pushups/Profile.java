package com.martynaskairys.pushups;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SQLiteDatabase db = openOrCreateDatabase("PushupsDBTimer", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pushups(seconds INT, totalPushups INT, speed DOUBLE);");

        Cursor e = db.rawQuery("SELECT SUM(totalPushups) FROM pushups", null);
        StringBuffer buffer = new StringBuffer();
        if (e.getCount() > 0) {
            e.moveToFirst();
            String max_id = "Total pushups: " + e.getInt(0);
            buffer.append(max_id);
        }


        db.close();

        ImageView img = (ImageView) findViewById(R.id.badge100);
        TextView txt = (TextView) findViewById(R.id.badgeText);



        if (e.getInt(0) >100){

            img.setImageResource(R.drawable.badge);
            txt.setText("great, you are the king");


        }

        else{

            img.setImageResource(R.drawable.alna);
            txt.setText("there are things to be achieved");
        }


    }
}
