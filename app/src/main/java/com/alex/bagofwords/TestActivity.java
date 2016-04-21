package com.alex.bagofwords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    UserSharedPrefHandler userSharedPrefHandler;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());

        TextView id = (TextView) findViewById(R.id.id);
        TextView name = (TextView) findViewById(R.id.name);
        TextView username = (TextView) findViewById(R.id.username);
        TextView email = (TextView) findViewById(R.id.email);
        TextView score = (TextView) findViewById(R.id.score);

        logoutButton = (Button) findViewById(R.id.logoutButton);

        Toast.makeText(getApplicationContext(), "User Login Status: " + userSharedPrefHandler.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        if(userSharedPrefHandler.checkLogin())
            finish();

            HashMap<String, String> user = userSharedPrefHandler.getUserDetails();
            String idSaved = user.get(UserSharedPrefHandler.KEY_ID);
            String nameSaved = user.get(UserSharedPrefHandler.KEY_NAME);
            String usernameSaved = user.get(UserSharedPrefHandler.KEY_USERNAME);
            String emailSaved = user.get(UserSharedPrefHandler.KEY_EMAIL);
            String scoreSaved = user.get(UserSharedPrefHandler.KEY_SCORE);

            id.setText(idSaved);
            name.setText(nameSaved);
            username.setText(usernameSaved);
            email.setText(emailSaved);
            score.setText(scoreSaved);


            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userSharedPrefHandler.logoutUser();
                    //Toast.makeText(TestActivity.this, "Button pressed", Toast.LENGTH_LONG).show();
                }
            });


    }
}
