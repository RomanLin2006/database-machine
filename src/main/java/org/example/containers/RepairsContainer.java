package org.example.containers;

import org.example.models.Repair;
import java.util.ArrayList;
import java.util.List;

public class RepairsContainer {
    private static RepairsContainer instance;
    private List<Repair> repairs;

    private RepairsContainer() {
        repairs = new ArrayList<>();
    }

    public static RepairsContainer getInstance() {
        if (instance == null) {
            instance = new RepairsContainer();
        }
        return instance;
    }

    public List<Repair> getRepairs() { return repairs; }

    public void setRepairs(List<Repair> repairs) { this.repairs = repairs; }

    public void clear() { repairs.clear(); }
}