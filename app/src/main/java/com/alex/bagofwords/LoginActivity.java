package com.alex.bagofwords;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class LoginActivity extends AppCompatActivity {


    UserSessionHandler userSessionHandler;
    EditText login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Display alert dialog if no network connection
        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }
        // UserSessionHandler object
        userSessionHandler = new UserSessionHandler(getApplicationContext());
        login = (EditText) findViewById(R.id.input_login);
        password = (EditText) findViewById(R.id.input_password);

        // --- Login button pressed -----
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check validation before attempting to post to the database
                assert login != null;
                if(!Validation.fieldNotEmpty(login.getText().toString())) {     // Validation for login field
                    login.setError("Enter username/email");
                    login.requestFocus();
                } else if (!Validation.validPassword(password.getText().toString())) {  // Validation for password field
                    password.setError("Invalid Password");
                    password.requestFocus();
                } else {
                    userLogin(login.getText().toString(), password.getText().toString()); // post entered credentials to verify user presence in database
                    }
                }
        });

        // --- Jump to registration activity ---
        TextView registerAccount = (TextView) findViewById(R.id.link_register);
        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext() , RegistrationActivity.class);
                startActivity(intentRegister);
            }
        });
    }


    // --- Post entered credentials to verify presence in database ---
    private void userLogin(final String login, final String password) {
        final String LOGIN_URL = "http://www.bagofwords-ca400.com/webservice/LoginUserV3.php"; // URL address for login script
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request;
        request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { // Returned response from database
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("successful");

                    if(success) {
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("email");
                        String score = jsonObject.getString("score");
                        userSessionHandler.establishUserSession(id, name, username, email, score, password); // establish user session with fetched data.
                        // Jump to main menu activity
                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();   // Close LoginActivity
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect credentials", Toast.LENGTH_LONG).show(); // Incorrect response message
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { // Error response from database
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error connecting to database, check network connection", Toast.LENGTH_LONG).show();
            }
        }){
            // User entered credential to be posted
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("login", login);        // login
                hashMap.put("password", password);  // password
                return hashMap;
            }
        };
        requestQueue.add(request);  // Post data to server
    }

    // --- Valid if the device has network connection ---
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    // --- Alert dialog requesting to jump to device network settings ---
    public void deviceWifiSettings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Check network settings");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.create().show();
    }
}
