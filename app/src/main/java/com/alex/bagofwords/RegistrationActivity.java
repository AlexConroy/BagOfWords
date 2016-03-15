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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final EditText name = (EditText) findViewById(R.id.input_name);
        final EditText email = (EditText) findViewById(R.id.input_email);
        final EditText password = (EditText) findViewById(R.id.input_password);
        final EditText comparePassword = (EditText) findViewById(R.id.compare_input_password);
        Button register = (Button) findViewById(R.id.button_register);
        TextView login = (TextView) findViewById(R.id.link_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validName(name.getText().toString())) {
                    name.setError("Name is be between 2 and 30 characters");
                    name.requestFocus();
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
                    Toast.makeText(RegistrationActivity.this, "Validation Success", Toast.LENGTH_LONG).show();
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


}
