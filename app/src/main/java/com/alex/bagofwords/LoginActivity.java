package com.alex.bagofwords;

import android.app.AlertDialog;
import android.app.DownloadManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "http://www.bagofwords-ca400.com/webservice/LoginUserV3.php";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    UserSharedPrefHandler userSharedPrefHandler;

    EditText login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!isNetworkAvailable(getApplicationContext())) {
            deviceWifiSettings();
        }

        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());

        login = (EditText) findViewById(R.id.input_login);
        password = (EditText) findViewById(R.id.input_password);
        Button loginButton = (Button) findViewById(R.id.button_login);
        TextView registerAccount = (TextView) findViewById(R.id.link_register);

        Toast.makeText(getApplicationContext(), "User login Status: " + userSharedPrefHandler.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert login != null;
                if(login.getText().toString().matches("")) {
                    login.setError("Enter username/email");
                    login.requestFocus();
                } else if (!validPassword(password.getText().toString())) {
                    password.setError("Invalid Password");
                    password.requestFocus();
                } else {
                    userLogin(login.getText().toString(), password.getText().toString());
                    }

                }

        });

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext() , RegistrationActivity.class);
                startActivity(intentRegister);

            }
        });


    }

    // Validation of email method.
    protected boolean validEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; // Email regular expression.
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // Validation of password method (i.e. must be getter than 5)
    protected boolean validPassword(String password) {
        return password.length() >= 6;
    }


    private void userLogin(final String login, final String password) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request;

        request = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("successful");

                    if(success) {
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("email");
                        String score = jsonObject.getString("score");
                        userSharedPrefHandler.establishUserSession(id, name, username, email, score, password);

                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect credentials", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error connecting to database, check network connection", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("login", login);
                hashMap.put("password", password);
                return hashMap;
            }
        };
        requestQueue.add(request);


    }

    private void successfulLogin() {
        Intent intent = new Intent(this, MainMenu.class);
        //intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
        finish();

    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

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
