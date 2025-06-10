package org.example.models;

public class Client {
    private int _id;
    private String _name;
    private String _contact_info;

    public Client() {}

    public Client(int _id, String _name, String _contact_info) {
        this._id = _id;
        this._name = _name;
        this._contact_info = _contact_info;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getContactInfo() {
        return _contact_info;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public void setContactInfo(String _contact_info) {
        this._contact_info = _contact_info;
    }
}
