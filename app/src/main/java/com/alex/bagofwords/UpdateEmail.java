package com.alex.bagofwords;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
    EditText compareEmail;
    EditText password;
    Button updateEmailBtn;
    Button mainMenu;

    UserSharedPrefHandler userSharedPrefHandler;
    static final String EMAIL_CHANGE_URL = "http://www.bagofwords-ca400.com/webservice/UpdateEmail.php";
    public static final String KEY_OLD_EMAIL = "oldEmail";
    public static final String KEY_NEW_EMAIL = "newEmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        newEmail = (EditText) findViewById(R.id.new_email);
        compareEmail = (EditText) findViewById(R.id.compare_new_email);
        password = (EditText) findViewById(R.id.password);
        updateEmailBtn = (Button) findViewById(R.id.update_email);
        mainMenu = (Button) findViewById(R.id.mainMenu);

        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
        HashMap<String, String> user = userSharedPrefHandler.getUserDetails();
        final String currentEmail = user.get(UserSharedPrefHandler.KEY_EMAIL);
        //Toast.makeText(getApplicationContext(), "User email: " + currentEmail, Toast.LENGTH_LONG).show();

        //EditText displayCurrentEmail = (EditText) findViewById(R.id.password);
        //displayCurrentEmail.setText(currentEmail);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
            }
        });

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validEmail(newEmail.getText().toString())) {
                    newEmail.setError("Invalid Email");
                    newEmail.requestFocus();
                } else if (!validEmail(compareEmail.getText().toString())) {
                    compareEmail.setError("Invalid Email");
                    compareEmail.requestFocus();
                } else if (!matchingEmail(newEmail.getText().toString(), compareEmail.getText().toString())) {
                    compareEmail.setError("Email does not match");
                    compareEmail.requestFocus();
                } else if (!validPassword(password.getText().toString())) {
                    password.setError("Password must be greater or equal to 6 characters");
                    password.requestFocus();
                } else if (newEmail(currentEmail, newEmail.getText().toString())) {
                    newEmail.setError("Email must differ from old email");
                    newEmail.requestFocus();
                } else if (!correctPassword(password.getText().toString())) {
                    password.setError("Incorrect password");
                    password.requestFocus();
                } else {
                    //Toast.makeText(getApplicationContext(), "Successfully updated email", Toast.LENGTH_LONG).show();
                    //updateEmail(currentEmail, newEmail.getText().toString());
                    updateEmail(currentEmail, compareEmail.getText().toString());
                }

            }
        });

    }


    protected boolean compareEmail(String oldPassword, String newPassword) {
        if(oldPassword.equals(newPassword)) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean matchingEmail(String password, String comparePassword) {
        return (password.equals(comparePassword));
    }

    protected boolean validEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; // Email regular expression.
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    protected boolean validPassword(String password) {
        return password.length() >= 6;
    }

    protected boolean newEmail(String oldEmail, String newEmail) {
        return (oldEmail.equals(newEmail));
    }

    protected boolean correctPassword(String password) {
        UserSharedPrefHandler userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());
        String userPassword = userSharedPrefHandler.getPassword();
        return password.equals(userPassword);
    }


    private void updateEmail(final String oldEmail, final String newEmail) {

        final UserSharedPrefHandler prefHandler = new UserSharedPrefHandler(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_CHANGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("successful")) {
                            Toast.makeText(getApplicationContext(), "Email Changed", Toast.LENGTH_LONG).show();
                            prefHandler.setEmail(newEmail);
                            HashMap<String, String> user = userSharedPrefHandler.getUserDetails();
                            String test = user.get(UserSharedPrefHandler.KEY_EMAIL);
                            Toast.makeText(getApplicationContext(), "Email Changed to: "+ test, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database!!", Toast.LENGTH_LONG).show();
                    }

                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_OLD_EMAIL, oldEmail);
                params.put(KEY_NEW_EMAIL, newEmail);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
