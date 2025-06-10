package org.example.models;

public class Brand {
    private int _id;
    private String _name;

    public Brand(){}

    public Brand(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
