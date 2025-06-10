package org.example.models;

public class RepairType {
    private int _id;
    private String _name;
    private int _duration;
    private double _cost;

    public RepairType(){}

    public RepairType(int _id, String _name, int _duration, double _cost) {
        this._id = _id;
        this._name = _name;
        this._duration = _duration;
        this._cost = _cost;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int _duration) {
        this._duration = _duration;
    }

    public double getCost() {
        return _cost;
    }

    public void setCost(double _cost) {
        this._cost = _cost;
    }
}