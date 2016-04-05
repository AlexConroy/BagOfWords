package com.alex.bagofwords;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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



public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "http://www.bagofwords-ca400.com/webservice/LoginUser.php";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    EditText login;
    EditText password;

    //UserDataBaseHandler userDataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.input_login);
        password = (EditText) findViewById(R.id.input_password);
        Button loginButton = (Button) findViewById(R.id.button_login);
        TextView registerAccount = (TextView) findViewById(R.id.link_register);

       // userDataBaseHandler = new UserDataBaseHandler(this, null, null, 1);


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
                   // Toast.makeText(LoginActivity.this, "Validation Success", Toast.LENGTH_LONG).show();
                    //userLogin(login.getText().toString(), password.getText().toString());

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean successfulLogin = jsonResponse.getBoolean("successful");
                                if(successfulLogin) {
                                    Toast.makeText(LoginActivity.this, "App: Successful login", Toast.LENGTH_LONG).show();
                                    String name = jsonResponse.getString("name");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("name", name);
                                    LoginActivity.this.startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "App: Incorrect credentials", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginFetch loginFetch = new LoginFetch(login.getText().toString(), password.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginFetch);
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


    private void userLogin(final String e, String p) {

        final String login = e;
        final String password = p;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Successful login")) {
                            Toast.makeText(LoginActivity.this, "App: Successful login", Toast.LENGTH_LONG).show();
                            successfulLogin();
                        } else {
                            Toast.makeText(LoginActivity.this, "App: Incorrect Credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Cannot connect to database, check internet connection.", Toast.LENGTH_LONG).show();
                    }

                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_LOGIN, login);
                map.put(KEY_PASSWORD, password);

                return map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void successfulLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
        finish();

    }



}
