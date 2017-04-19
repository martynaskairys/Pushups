package com.martynaskairys.pushups;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.martynaskairys.pushups.R.id.pushupsCounter;

public class TimerBattle extends AppCompatActivity {

    TextView timerViewSeconds;
    long startTime = 0;
    int score = 0;
    TextView pushupsPerMinuteTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_battle);

        timerViewSeconds = (TextView) findViewById(R.id.timerSeconds);

        pushupsPerMinuteTextView = (TextView) findViewById(R.id.pushupsPerMinuteTextView);

        TextView startTimerButton = (TextView) findViewById(pushupsCounter);
        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimerPushups();
            }
        });

        Button btnFinish = (Button) findViewById(R.id.finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishTimerPushupsNew();
            }
        });

        Button btnMax = (Button) findViewById(R.id.buttonMax);
        btnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaxValue();
            }
        });

        Button btnAvg = (Button) findViewById(R.id.averageValuesButton);
        btnAvg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAverageValue();
            }
        });


    }


    public void startTimerPushups() {

        score++;

        TextView startPushups = (TextView) findViewById(pushupsCounter);

        //runs without a timer by reposting this handler at the end of the runnable
        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
//                int minutes = seconds / 60;
//            seconds = seconds % 60;

                timerViewSeconds.setText(String.format("%02d", seconds));
//            timerViewSeconds.setText(String.format("%d:%02d", minutes, seconds)); //with minutes and seconds

                timerHandler.postDelayed(this, 500);
            }
        };


        if (startPushups.getText().equals("Start")) {
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
        } else {
        }

        long millis = System.currentTimeMillis() - startTime;
        double secondsNew = millis / 1000;


        timerViewSeconds = (TextView) findViewById(R.id.timerSeconds);

        TextView counter = (TextView) findViewById(pushupsCounter);

        counter.setText(String.valueOf(score));


        double pushupsPerMinute = score * 60 / (secondsNew + 1.01);
//        DecimalFormat df = new DecimalFormat("###.##");
//        String pushupsPerMinuteTwoDigitNum = df.format(pushupsPerMinute);


        TextView tv = (TextView) findViewById(R.id.pushupsPerMinuteTextView);

//        pushupsPerMinuteTextView.setText(String.valueOf(pushupsPerMinuteTwoDigitNum));
//        pushupsPerMinuteTextView.setText(new DecimalFormat ("##.##").format(pushupsPerMinute));
        tv.setText(String.format("%.2f", pushupsPerMinute));


    }


    public void finishTimerPushupsNew() {


        SQLiteDatabase db;

        db = openOrCreateDatabase("PushupsDBTimer", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pushups(seconds INT, totalPushups INT, speed DOUBLE);");

        db.execSQL("INSERT INTO pushups VALUES('" + timerViewSeconds.getText() + "','" + score +
                "','" + pushupsPerMinuteTextView.getText() + "');");

        // Retrieving all records
        Cursor c = db.rawQuery("SELECT * FROM pushups", null);

        db.rawQuery("SELECT MAX(totalPushups) FROM pushups", null);

        // Checking if no records found
        if (c.getCount() == 0) {
            showMessage("Error", "No records found");
            return;
        }
        // Appending records to a string buffer
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("seconds: " + c.getInt(0) + "\n");
            buffer.append("Total Pushups: " + c.getString(1) + "\n");
            buffer.append("Pushups / second: " + c.getString(2) + "\n\n");

        }
        // Displaying all records
        showMessage("Pushup Details", buffer.toString());

    }


    public String showMaxValue() {
        SQLiteDatabase db = openOrCreateDatabase("PushupsDBTimer", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT MAX(totalPushups) FROM pushups", null);
        Cursor d = db.rawQuery("SELECT MAX(speed) FROM pushups", null);
        Cursor e = db.rawQuery("SELECT SUM(totalPushups) FROM pushups", null);

        StringBuffer buffer = new StringBuffer();

        if (c.getCount() > 0) {
            c.moveToFirst();
            String max_id = "max pushups: " + c.getInt(0) + "\n";
            buffer.append(max_id);
        }
c.close();


        if (d.getCount() > 0) {
            d.moveToFirst();
            String max_id = "max speed: " + d.getDouble(0)+ "\n";
            buffer.append(max_id);
        }
        d.close();

        if (e.getCount() > 0) {
            e.moveToFirst();
            String max_id = "Total pushups: " + e.getInt(0);
            buffer.append(max_id);
        }
        e.close();


        db.close();


        showMessage("Pushup Details", buffer.toString());

        return buffer.toString();

    }

    public String showAverageValue() {
        SQLiteDatabase db = openOrCreateDatabase("PushupsDBTimer", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT AVG(totalPushups) FROM pushups", null);
        Cursor d = db.rawQuery("SELECT AVG(speed) FROM pushups", null);
        StringBuffer buffer = new StringBuffer();

        if (c.getCount() > 0) {
            c.moveToFirst();
            String max_id = "max pushups: " + c.getDouble(0) + "\n";
            buffer.append(max_id);
        }
        c.close();


        if (d.getCount() > 0) {
            d.moveToFirst();
            String max_id = "max speed: " + d.getDouble(0);
            buffer.append(max_id);
        }
        d.close();

        db.close();


        showMessage("Pushup Details", buffer.toString());

        return buffer.toString();

    }


    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}
