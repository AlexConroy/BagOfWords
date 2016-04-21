package com.alex.bagofwords;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserDetailsPostFetch extends StringRequest {

    private static final String LOGIN_URL = "http://www.bagofwords-ca400.com/webservice/LoginUserV2.php";
    private static final String REGISTER_URL = "http://www.bagofwords-ca400.com/webservice/RegisterUserV5.php";
    private Map<String, String> paramas;

    private static final String KEY_LOGIN = "login";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    public UserDetailsPostFetch(String login, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_URL, listener, null);
        paramas = new HashMap<>();
        paramas.put(KEY_LOGIN, login);
        paramas.put(KEY_PASSWORD, password);
    }

    public UserDetailsPostFetch(String name, String username, String email, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_URL, listener, null);
        paramas = new HashMap<>();
        paramas.put(KEY_NAME, name);
        paramas.put(KEY_USERNAME, username);
        paramas.put(KEY_EMAIL, email);
        paramas.put(KEY_PASSWORD, password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return paramas;
    }
}
