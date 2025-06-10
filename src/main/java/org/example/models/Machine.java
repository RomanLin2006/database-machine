package org.example.models;

public class Machine {
    private int _id;
    private Client _client;
    private DataMachine _data_machine;

    public Machine(){}

    public Machine(int _id, Client _client, DataMachine _data_machine) {
        this._id = _id;
        this._client = _client;
        this._data_machine = _data_machine;
    }

    public int getId() {
        return _id;
    }

    public Client getClient() {
        return _client;
    }

    public DataMachine getDataMachine() {
        return _data_machine;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setClient(Client _client) {
        this._client = _client;
    }

    public void setDataMachine(DataMachine _data_machine) {
        this._data_machine = _data_machine;
    }
}