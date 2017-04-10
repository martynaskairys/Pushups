package com.martynaskairys.pushups;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener {


    Button btnFinish;
    TextView textView;
    Button btnView;
    SQLiteDatabase db;

    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFinish = (Button) findViewById(R.id.buttonFinish);
        textView = (TextView) findViewById(R.id.textView);
        btnView = (Button) findViewById(R.id.btnView);

        btnFinish.setOnClickListener(this);


        db = openOrCreateDatabase("PushupsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pushups(pushupsno VARCHAR, date VARCHAR);");


    }



    String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());


    public void addOne(View view) {

        score++;

        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(String.valueOf(score));

    }

    public void onClick(View view) {

// Adding a record
        if (view == btnFinish) {
            // Checking empty fields
            if (textView.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter all values");
                return;
            }

            // Inserting record


            db.execSQL("INSERT INTO pushups VALUES('" + textView.getText() + "','" + currentDate +
                    "');");
            showMessage("Success", "Record added");
            clearText();

            Intent mIntent = getIntent();
            finish();
            startActivity(mIntent);
        }


        // Viewing all records
        if (view == btnView) {
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


    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        textView.setText("");

    }

}
