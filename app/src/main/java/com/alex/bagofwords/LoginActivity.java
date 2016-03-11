package com.alex.bagofwords;

        import android.content.Context;
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

    // Hide keyboard method.
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if(view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // Valid username method.
    protected boolean validateUsername(String username) {
        String usernamePattern = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";

        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(username);

        return matcher.matches();
    }

    // Valid Password method.
    protected boolean validatePassword(String password) {
        return password.length() > 6;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputLayout usernameWrap = (TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrap = (TextInputLayout) findViewById(R.id.passwordWrapper);
        Button loginBtnPressed = (Button) findViewById(R.id.button_login);
        TextView signupPressed = (TextView) findViewById(R.id.link_signup);


        // Click listener for login.
        assert loginBtnPressed != null;
        loginBtnPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                assert usernameWrap != null;
                String username = usernameWrap.getEditText().getText().toString();
                assert passwordWrap != null;
                String password = passwordWrap.getEditText().getText().toString();
                if (!validateUsername(username)) {
                    usernameWrap.setError("Invalid Username");
                } else if (!validatePassword(password)) {
                    passwordWrap.setError("Invalid Password");
                } else {
                    usernameWrap.setErrorEnabled(false);
                    passwordWrap.setErrorEnabled(false);
                    Toast.makeText(getApplicationContext(), "Logging into account", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Click listener for create account.
        assert signupPressed != null;
        signupPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                Toast.makeText(getApplicationContext(), "Create Account", Toast.LENGTH_LONG).show();

            }
        });

    }



}
