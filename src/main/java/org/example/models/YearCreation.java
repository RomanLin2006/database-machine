package org.example.models;

public class YearCreation {
    private int _id;
    private int _yearValue;

    public YearCreation() {
    }

    public YearCreation(int id, short yearValue) {
        this._id = id;
        this._yearValue = yearValue;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getYearValue() {
        return _yearValue;
    }

    public void setYearValue(short _yearValue) {
        this._yearValue = _yearValue;
    }
}
