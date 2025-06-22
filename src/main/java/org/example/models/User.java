package org.example.models;

public class User {
    private int id;
    private String login;
    private String role;

    public User(int id, String login, String role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getRole() { return role; }
}
