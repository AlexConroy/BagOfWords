package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeMessage = (TextView) findViewById(R.id.welcomeText);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        String message = name + " welcome to Bag of Words.";
        welcomeMessage.setText(message);


    }
}
