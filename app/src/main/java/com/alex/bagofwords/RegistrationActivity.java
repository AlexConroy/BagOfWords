package com.alex.bagofwords;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {


    EditText nameET, emailET, passwordET, comparePasswordET;
    Button register;
    TextView login;
    LoginDataBaseAdapter loginDataBaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        nameET = (EditText) findViewById(R.id.input_name);
        emailET = (EditText) findViewById(R.id.input_email);
        passwordET = (EditText) findViewById(R.id.input_password);
        comparePasswordET = (EditText) findViewById(R.id.compare_input_password);
        Button register = (Button) findViewById(R.id.button_register);
        TextView login = (TextView) findViewById(R.id.link_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String comparePassword = comparePasswordET.getText().toString();

                String storedPassword = loginDataBaseAdapter.getSinlgeEntry(email);

                if (!validName(name)) {
                    nameET.setError("Name is be between 2 and 30 characters");
                    nameET.requestFocus();
                } else if (!validEmail(email)) {
                    emailET.setError("Invalid Email");
                    emailET.requestFocus();
                } else if (!validPassword(password)) {
                    passwordET.setError("Invalid Password");
                    passwordET.requestFocus();
                } else if (!matchingPassword(password, comparePassword)) {
                    comparePasswordET.setError("Passwords don't match");
                    comparePasswordET.requestFocus();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Account created.\r\nWelcome: "+ name, Toast.LENGTH_LONG).show();
                    loginDataBaseAdapter.insertEntry(email, password);
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

    protected boolean validName(String name) {
       /* String namePattern = "[^a-zA-Z]";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return (name.length() >= 2 && name.length() <=30) && matcher.matches(); */
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }


}
