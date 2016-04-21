package com.alex.bagofwords;

/**
 * Created by Alex on 04/04/2016.
 */
public class User {

    private String _name;
    private String _username;
    private String _email;
    private String _password;

    public User(String name, String username, String email, String password) {
        this._name = name;
        this._username = username;
        this._email = email;
        this._password = password;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

}
