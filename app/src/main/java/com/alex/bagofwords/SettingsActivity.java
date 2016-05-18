package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.HashMap;


public class SettingsActivity extends AppCompatActivity {

    Button mainMenu, leaderboard;
    Button updatePasswordBtn;
    Button updateEmailBtn;
    UserSessionHandler userSessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        userSessionHandler = new UserSessionHandler(getApplicationContext());

        // ---- Main Menu Intent ----
        mainMenu = (Button) findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

        // ----- Leader Board Intent ------
        leaderboard = (Button) findViewById(R.id.leaderBoard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), LeaderBoardActivity.class);
                startActivity(mainMenuIntent);
                finish();
            }
        });

        // ---- Jump to update password activity -----
        updatePasswordBtn = (Button) findViewById(R.id.update_passwordBtn);
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePasswordIntent = new Intent(getApplicationContext(), UpdatePassword.class);
                startActivity(updatePasswordIntent);
            }
        });

        // ---- Jump to update email activity -----
        updateEmailBtn = (Button) findViewById(R.id.update_emailBtn);
        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateEmailIntent = new Intent(getApplicationContext(), UpdateEmail.class);
                startActivity(updateEmailIntent);
            }
        });


        if(userSessionHandler.checkLogin())
            finish();


    }

    // -------- Log out icon -----------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // --------- Log out button clicked ------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_icon:
                Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                userSessionHandler.logoutUser();    // clear user details and logout user
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}