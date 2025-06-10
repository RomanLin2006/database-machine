package org.example.models;

public class DataMachine {
    private int _id;
    private YearCreation _year;
    private CountryCreation _country;
    private Brand _brand;

    public DataMachine() {}

    public DataMachine(int _id, YearCreation _year, CountryCreation _country, Brand _brand) {
        this._id = _id;
        this._year = _year;
        this._country = _country;
        this._brand = _brand;
    }

    public int getId() {
        return _id;
    }

    public YearCreation getYear() {
        return _year;
    }

    public CountryCreation getCountry() {
        return _country;
    }

    public Brand getBrand() {
        return _brand;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setYear(YearCreation _year) {
        this._year = _year;
    }

    public void setCountry(CountryCreation _country) {
        this._country = _country;
    }

    public void setBrand(Brand _brand) {
        this._brand = _brand;
    }
}
