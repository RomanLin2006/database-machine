package org.example.services;

import org.example.containers.RepairsContainer;
import org.example.models.Repair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RepairManager {
    public static void loadAllRepairs() {
        String sql = "SELECT r.id, c.name AS client_name, c.contact_info AS contact_info, m.id AS machine_id, b.name AS brand, y.value AS year, cc.name AS country, " +
                "rt.name AS repair_type, r.start_date, rt.duration_days, rt.cost " +
                "FROM repairs r " +
                "JOIN machins m ON r.machine_id = m.id " +
                "JOIN clients c ON m.client_id = c.id " +
                "JOIN data_machine dm ON m.data_machine_id = dm.id " +
                "JOIN brands b ON dm.brand_id = b.id " +
                "JOIN years_creation y ON dm.year_id = y.id " +
                "JOIN countries_creation cc ON dm.country_id = cc.id " +
                "JOIN repair_types rt ON r.repair_type_id = rt.id";
        List<Repair> repairs = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int repairId = rs.getInt("id");
                String clientName = rs.getString("client_name");
                int machineId = rs.getInt("machine_id");
                String brand = rs.getString("brand");
                int year = rs.getInt("year");
                String repairType = rs.getString("repair_type");
                String startDate = rs.getString("start_date");
                int durationDays = rs.getInt("duration_days");
                double cost = rs.getDouble("cost");
                String contactInfo = rs.getString("contact_info");
                String country = rs.getString("country");
                Repair repair = new Repair(
                        repairId, clientName, machineId, brand, year, repairType, startDate, durationDays, cost, contactInfo, country
                );
                repairs.add(repair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RepairsContainer.getInstance().setRepairs(repairs);
    }

    public static List<Repair> getAllMachinesForCurrentClient() { // Возвращение списка ремонтов для каждого станка
        List<Repair> all = RepairsContainer.getInstance().getRepairs();
        List<Repair> unique = new ArrayList<>();
        for (Repair r : all) {
            boolean exists = false;
            for (Repair u : unique) {
                if (u.getMachineId() == r.getMachineId()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) unique.add(r);
        }
        return unique;
    }
}
