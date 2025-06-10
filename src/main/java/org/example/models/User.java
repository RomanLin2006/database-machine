package org.example.models;

public class User {
    private int _id;
    private Role _role;
    private String _login;
    private String _password;

    public User() {
    }

    public User(int _id, Role _role, String _login, String _password) {
        this._id = _id;
        this._role = _role;
        this._login = _login;
        this._password = _password;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public Role getRole() {
        return _role;
    }

    public void setRole(Role _role) {
        this._role = _role;
    }

    public String getLogin() {
        return _login;
    }

    public void setLogin(String _login) {
        this._login = _login;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }
}