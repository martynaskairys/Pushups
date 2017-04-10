package com.martynaskairys.pushups;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startMainActivity(View view){

        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void showResults (View view){

Intent intent = new Intent(StartActivity.this, Results.class);
        startActivity(intent);

    }

}
