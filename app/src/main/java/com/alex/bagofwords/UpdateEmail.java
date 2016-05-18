package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEmail extends AppCompatActivity {

    EditText newEmail;
    EditText confirmEmail;
    EditText password;
    Button updateEmailBtn;
    Button mainMenu;

    UserSessionHandler userSessionHandler;
    static final String EMAIL_CHANGE_URL = "http://www.bagofwords-ca400.com/webservice/UpdateEmail.php";    // URL for script for updating email
    public static final String KEY_OLD_EMAIL = "oldEmail";  // key value for old email
    public static final String KEY_NEW_EMAIL = "newEmail";  // key value for new email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        newEmail = (EditText) findViewById(R.id.new_email);
        confirmEmail = (EditText) findViewById(R.id.compare_new_email);
        password = (EditText) findViewById(R.id.password);
        updateEmailBtn = (Button) findViewById(R.id.update_email);
        mainMenu = (Button) findViewById(R.id.mainMenu);

        userSessionHandler = new UserSessionHandler(getApplicationContext());
        HashMap<String, String> user = userSessionHandler.getUserDetails();
        final String currentEmail = user.get(com.alex.bagofwords.UserSessionHandler.KEY_EMAIL);


        // --- Main Menu Intent ---
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
            }
        });

        // --- Update Email ---
        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Validation.validEmail(newEmail.getText().toString())) {
                    newEmail.setError("Invalid Email");
                    newEmail.requestFocus();
                } else if (!Validation.validEmail(confirmEmail.getText().toString())) {
                    confirmEmail.setError("Invalid Email");
                    confirmEmail.requestFocus();
                } else if (!Validation.matchingEmail(newEmail.getText().toString(), confirmEmail.getText().toString())) {
                    confirmEmail.setError("Email does not match");
                    confirmEmail.requestFocus();
                } else if (!Validation.validPassword(password.getText().toString())) {
                    password.setError("Password must be greater or equal to 6 characters");
                    password.requestFocus();
                } else if (!differentNewEmail(newEmail.getText().toString())) {
                    newEmail.setError("Email must differ from current email");
                    newEmail.requestFocus();
                } else if (!correctPassword(password.getText().toString())) {
                    password.setError("Incorrect password");
                    password.requestFocus();
                } else {
                    updateEmail(currentEmail, confirmEmail.getText().toString());   // call updatePassword to update password in database
                }
            }
        });
    }


    // validates if entered new password is different from current password
    protected boolean differentNewEmail(String newEmail) {
        UserSessionHandler userSessionHandler = new UserSessionHandler(getApplicationContext());
        String usersCurrentEmail = userSessionHandler.getEmail();   // fetch current user email
        return !usersCurrentEmail.equals(newEmail);
    }

    // validates if password entered is correct
    protected boolean correctPassword(String password) {
        UserSessionHandler userSessionHandler = new UserSessionHandler(getApplicationContext());
        String userPassword = userSessionHandler.getPassword();     // fetch current user password
        return password.equals(userPassword);
    }

    // --- Post method for updating password on database ---
    private void updateEmail(final String oldEmail, final String newEmail) {

        final UserSessionHandler userSession = new UserSessionHandler(getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request;

        request = new StringRequest(Request.Method.POST, EMAIL_CHANGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("successful");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Email Changed", Toast.LENGTH_LONG).show();
                                userSession.setEmail(newEmail);     //
                                HashMap<String, String> user = userSessionHandler.getUserDetails();
                                String test = user.get(com.alex.bagofwords.UserSessionHandler.KEY_EMAIL);
                                Toast.makeText(getApplicationContext(), "Email Changed to: "+ test, Toast.LENGTH_LONG).show();
                                // jump to main menu activity
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else { // Email already exists in database
                                Toast.makeText(getApplicationContext(), "Email already taken, try another email", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {  // error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database!!", Toast.LENGTH_LONG).show();
                    }

                }) {
            // key value pairs to be posted to the database
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_OLD_EMAIL, oldEmail);
                params.put(KEY_NEW_EMAIL, newEmail);
                return params;
            }
        };
        requestQueue.add(request);  // post key value pairs to database
    }

}
