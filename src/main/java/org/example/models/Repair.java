package org.example.models;

import java.util.Date;

public class Repair {
    private int _id;
    private Machine _machine;
    private RepairType _repair_type;
    private Date _start_date;

    public Repair() {}

    public Repair(int _id, Machine _machine, RepairType _repair_type, Date _start_date) {
        this._id = _id;
        this._machine = _machine;
        this._repair_type = _repair_type;
        this._start_date = _start_date;
    }

    public int getId() {
        return _id;
    }

    public Machine getMachine() {
        return _machine;
    }

    public RepairType getRepairType() {
        return _repair_type;
    }

    public Date getStartDate() {
        return _start_date;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setMachine(Machine _machine) {
        this._machine = _machine;
    }

    public void setRepairType(RepairType _repair_type) {
        this._repair_type = _repair_type;
    }

    public void setStartDate(Date _start_date) {
        this._start_date = _start_date;
    }
}
