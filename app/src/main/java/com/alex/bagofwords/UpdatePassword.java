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

    static final String PASSWORD_CHANGE_URL = "http://www.bagofwords-ca400.com/webservice/UpdatePassword.php";  // URL script for updating passwords
    public static final String KEY_USER_ID = "userId";  // key value for user id
    public static final String KEY_NEW_PASSWORD = "newPassword"; // key value for new password
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


        // ------ Main Menu Intent --------
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
            }
        });

        // ------ Update Password  --------
        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validation process
                if(!Validation.validPassword(currentPassword.getText().toString()) ) {
                    currentPassword.setError("Invalid password, minimum of 6 characters required");
                    currentPassword.requestFocus();
                } else if(!Validation.validPassword(newPassword.getText().toString()) ) {
                    newPassword.setError("Invalid password, minimum of 6 characters required");
                    newPassword.requestFocus();
                } else if(!Validation.validPassword(confirmPassword.getText().toString()) ) {
                    confirmPassword.setError("Invalid password, minimum of 6 characters required");
                    confirmPassword.requestFocus();
                } else if(!Validation.differentPasswords(currentPassword.getText().toString(), newPassword.getText().toString())) {
                    newPassword.setError("New password must differ from old password");
                    newPassword.requestFocus();
                } else if(!Validation.matchingPassword(newPassword.getText().toString(), confirmPassword.getText().toString())) {
                    confirmPassword.setError("Passwords do not match.");
                    confirmPassword.requestFocus();
                } else if(!correctPassword(currentPassword.getText().toString())) {
                    currentPassword.setError("Incorrect password");
                    currentPassword.requestFocus();
                } else {
                    updatePassword(confirmPassword.getText().toString());   // call updatePassword to update password in database
                }
            }
        });
    }

    // validate if entered password is correct
    protected boolean correctPassword(String password) {
        UserSessionHandler userSessionHandler = new UserSessionHandler(getApplicationContext());
        String userPassword = userSessionHandler.getPassword();
        return password.equals(userPassword);
    }

    // --- Post method for updating password on database ---
    private void updatePassword(final String newPassword) {

        final UserSessionHandler prefHandler = new UserSessionHandler(getApplicationContext());
        HashMap<String, String> user = prefHandler.getUserDetails();
        final String userID = user.get(com.alex.bagofwords.UserSessionHandler.KEY_ID);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSWORD_CHANGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   // Returned response from database
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Boolean success = jsonObject.getBoolean("successful");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_LONG).show();
                                prefHandler.setPassword(newPassword);       // set new password in user session handler
                                HashMap<String, String> user = userSessionHandler.getUserDetails();
                                String test = user.get(com.alex.bagofwords.UserSessionHandler.KEY_PASSWORD);
                                Toast.makeText(getApplicationContext(), "Passwords Changed to: "+ test, Toast.LENGTH_LONG).show();
                                // Jump to main menu activity
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {    //
                                Toast.makeText(getApplicationContext(), "Error changing password, check network connection", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {      // Error response from database
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error connecting to database!", Toast.LENGTH_LONG).show();
                    }
                }) {
            // key value pairs to be posted to the database
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_USER_ID, userID);
                params.put(KEY_NEW_PASSWORD, newPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);    // post key value pairs to database
    }

}
