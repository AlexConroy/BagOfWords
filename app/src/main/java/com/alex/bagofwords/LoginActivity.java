package com.alex.bagofwords;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {

    EditText emailETL, passwordETL;
    Button loginButton;
    TextView registerAccount;

    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailETL = (EditText) findViewById(R.id.input_email);
        passwordETL = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.button_login);
        registerAccount = (TextView) findViewById(R.id.link_register);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailETL.getText().toString();
                String password = passwordETL.getText().toString();

                String storedPassword = loginDataBaseAdapter.getSinlgeEntry(email);

                if(!validEmail(email)) {
                    emailETL.setError("Invalid Email");
                    emailETL.requestFocus();
                } else if (!validPassword(password)) {
                    passwordETL.setError("Invalid Password");
                    passwordETL.requestFocus();
                } else {
                    //Toast.makeText(LoginActivity.this, "Validation Success: ", Toast.LENGTH_LONG).show();
                    if(password.equals(storedPassword)) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                    }
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

    protected boolean checkDbInstance(String value, String dbValue){
        return value.equals(dbValue);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }


}
