package org.example.models;

public class Role {
    private int _id;
    private String _name;

    public Role() {}

    public Role(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setName(String _name) {
        this._name = _name;
    }
}
