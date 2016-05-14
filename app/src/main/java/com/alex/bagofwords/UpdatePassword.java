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

public class UpdatePassword extends AppCompatActivity {

    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button updatePasswordBtn;
    Button mainMenu;

    static final String PASSWORD_CHANGE_URL = "http://www.bagofwords-ca400.com/webservice/UpdatePassword.php";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NEW_Password = "newPassword";
    UserSessionHandler userSessionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        currentPassword = (EditText) findViewById(R.id.input_old_password);
        newPassword = (EditText) findViewById(R.id.input_new_password);
        confirmPassword = (EditText) findViewById(R.id.compare_new_input_password);
        updatePasswordBtn = (Button) findViewById(R.id.update_password);
        mainMenu = (Button) findViewById(R.id.mainMenu);

        userSessionHandler = new UserSessionHandler(getApplicationContext());
        HashMap<String, String> user = userSessionHandler.getUserDetails();
        final String currentPasswordDisplay = user.get(com.alex.bagofwords.UserSessionHandler.KEY_PASSWORD);
        Toast.makeText(getApplicationContext(), "User password: " + currentPasswordDisplay, Toast.LENGTH_LONG).show();


        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validPassword(currentPassword.getText().toString()) ) {
                    currentPassword.setError("Invalid password, minimum of 6 characters required");
                    currentPassword.requestFocus();
                } else if(!validPassword(newPassword.getText().toString()) ) {
                    newPassword.setError("Invalid password, minimum of 6 characters required");
                    newPassword.requestFocus();
                } else if(!validPassword(confirmPassword.getText().toString()) ) {
                    confirmPassword.setError("Invalid password, minimum of 6 characters required");
                    confirmPassword.requestFocus();
                } else if(!confirmPassword(currentPassword.getText().toString(), newPassword.getText().toString())) {
                    newPassword.setError("New password must differ from old password");
                    newPassword.requestFocus();
                } else if(!matchingPassword(newPassword.getText().toString(), confirmPassword.getText().toString())) {
                    confirmPassword.setError("Passwords do not match.");
                    confirmPassword.requestFocus();
                } else if(!correctPassword(currentPassword.getText().toString())) {
                    currentPassword.setError("Incorrect password");
                    currentPassword.requestFocus();
                } else {
                    updatePassword(confirmPassword.getText().toString());
                }

            }
        });


    }

    protected boolean validPassword(String password) {
        return password.length() >= 6;
    }

    protected boolean confirmPassword(String currentPassword, String newPassword) {
        if(currentPassword.equals(newPassword)) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean matchingPassword(String password, String confirmPassword) {
        return (password.equals(confirmPassword));
    }

    protected boolean correctPassword(String password) {
        UserSessionHandler userSessionHandler = new UserSessionHandler(getApplicationContext());
        String userPassword = userSessionHandler.getPassword();
        return password.equals(userPassword);
    }


    private void updatePassword(final String newPassword) {

        final UserSessionHandler prefHandler = new UserSessionHandler(getApplicationContext());
        HashMap<String, String> user = prefHandler.getUserDetails();
        final String userID = user.get(com.alex.bagofwords.UserSessionHandler.KEY_ID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSWORD_CHANGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("successful");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                                prefHandler.setPassword(newPassword);
                                HashMap<String, String> user = userSessionHandler.getUserDetails();
                                String test = user.get(com.alex.bagofwords.UserSessionHandler.KEY_PASSWORD);
                                Toast.makeText(getApplicationContext(), "Email Changed to: "+ test, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error connecting to database!!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put(KEY_USER_ID, userID);
                params.put(KEY_NEW_Password, newPassword);
                return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
