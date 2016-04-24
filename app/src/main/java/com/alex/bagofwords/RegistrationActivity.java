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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private static final String REGISTER_URL = "http://www.bagofwords-ca400.com/webservice/RegisterUserV6.php";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //public static final String

    UserSharedPrefHandler userSharedPrefHandler;

    EditText name, username, email, password, comparePassword;
    Button register;
    TextView login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText) findViewById(R.id.input_name);
        username = (EditText) findViewById(R.id.input_username);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        comparePassword = (EditText) findViewById(R.id.compare_input_password);
        register = (Button) findViewById(R.id.button_register);
        login = (TextView) findViewById(R.id.link_login);

        userSharedPrefHandler = new UserSharedPrefHandler(getApplicationContext());


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (!validName(name.getText().toString())) {
                    name.setError("Name is be between 2 and 30 characters");
                    name.requestFocus();
                } else if (!validUsername(username.getText().toString())) {
                    username.setError("Invalid username.");
                    username.requestFocus();

                } else if (!validName(username.getText().toString())) {
                    username.setError("Invalid Username");
                    username.requestFocus();
                } else if (!validEmail(email.getText().toString())) {
                    email.setError("Invalid Email");
                    email.requestFocus();
                } else if (!validPassword(password.getText().toString())) {
                    password.setError("Invalid Password");
                    password.requestFocus();
                } else if (!matchingPassword(password.getText().toString(), comparePassword.getText().toString())) {
                    comparePassword.setError("Passwords don't match");
                    comparePassword.requestFocus();
                } else {
                        //Toast.makeText(RegistrationActivity.this, "Validation Success", Toast.LENGTH_LONG).show();
                        //registerUser(name.getText().toString(), username.getText().toString(), email.getText().toString(), comparePassword.getText().toString());
                        //User user = new User(name.getText().toString(), username.getText().toString(), email.getText().toString(), comparePassword.getText().toString());
                        registerUser(name.getText().toString(), username.getText().toString(), email.getText().toString(), comparePassword.getText().toString());
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    protected boolean validUsername(String username) {

        return !username.matches("");
    }


    protected boolean validName(String name) {
        return (name.length() >= 2 && name.length() <=30);
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

    protected boolean matchingPassword(String password, String comparePassword) {
        return (password.equals(comparePassword));
    }


    private void registerUser(final String name, final String username, final String email, final String password) {

        final EditText usernameEditText= (EditText) findViewById(R.id.input_username);
        final EditText emailEditText= (EditText) findViewById(R.id.input_email);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request;

        request = new StringRequest(Request.Method.POST, REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("response");

                    switch (success) {
                        case "successful":
                            String id = jsonObject.getString("id");
                            String score = jsonObject.getString("score");
                            Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_LONG).show();
                            userSharedPrefHandler.establishUserSession(id, name, username, email, score, password);
                            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            break;
                        case "usernameAndEmailExists":
                            usernameEditText.setError("Username already exists");
                            emailEditText.setError("Email already exists");
                            usernameEditText.requestFocus();
                            emailEditText.requestFocus();
                            break;

                        case "usernameExists":
                            usernameEditText.setError("Username already exists");
                            usernameEditText.requestFocus();
                            break;

                        case "emailExists":
                            emailEditText.setError("Username already exists");
                            emailEditText.requestFocus();
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), "Unsuccessful Registration", Toast.LENGTH_LONG).show();
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
                hashMap.put("name", name);
                hashMap.put("username", username);
                hashMap.put("email", email);
                hashMap.put("password", password);
                return hashMap;
            }
        };
        requestQueue.add(request);

    }


    private void successfulLogin() {
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
