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

    public static boolean addRepair(Repair repair) {
        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);
            int machineId;
            if (repair.getMachineId() != 0) {
                machineId = repair.getMachineId();
            } else {
                int clientId = getOrCreateId(conn, "clients", "name", repair.getClientName(), "contact_info", repair.getContactInfo());
                int brandId = getOrCreateId(conn, "brands", "name", repair.getBrand());
                int yearId = getOrCreateId(conn, "years_creation", "value", String.valueOf(repair.getYear()));
                int countryId = getOrCreateId(conn, "countries_creation", "name", repair.getCountry());
                int dataMachineId = getOrCreateDataMachineId(conn, brandId, yearId, countryId);
                String insertMachin = "INSERT INTO machins (client_id, data_machine_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertMachin, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, clientId);
                    stmt.setInt(2, dataMachineId);
                    stmt.executeUpdate();
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        machineId = rs.getInt(1);
                    } else {
                        throw new Exception("Не удалось создать новый станок");
                    }
                }
            }
            int repairTypeId = getOrCreateRepairTypeId(conn, repair.getRepairType(), repair.getDurationDays(), repair.getCost());
            String sql = "INSERT INTO repairs (machine_id, repair_type_id, start_date) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, machineId);
                stmt.setInt(2, repairTypeId);
                stmt.setString(3, repair.getStartDate());
                stmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getOrCreateId(Connection conn, String table, String field, String value) throws Exception {
        String select = "SELECT id FROM " + table + " WHERE " + field + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        String insert = "INSERT INTO " + table + " (" + field + ") VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, value);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new Exception("Не удалось создать запись в " + table);
    }
    private static int getOrCreateId(Connection conn, String table, String field, String value, String field2, String value2) throws Exception {
        String select = "SELECT id FROM " + table + " WHERE " + field + " = ? AND " + field2 + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, value);
            stmt.setString(2, value2);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        String insert = "INSERT INTO " + table + " (" + field + ", " + field2 + ") VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, value);
            stmt.setString(2, value2);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new Exception("Не удалось создать запись в " + table);
    }
    private static int getOrCreateDataMachineId(Connection conn, int brandId, int yearId, int countryId) throws Exception {
        String select = "SELECT id FROM data_machine WHERE brand_id = ? AND year_id = ? AND country_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setInt(1, brandId);
            stmt.setInt(2, yearId);
            stmt.setInt(3, countryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        String insert = "INSERT INTO data_machine (brand_id, year_id, country_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, brandId);
            stmt.setInt(2, yearId);
            stmt.setInt(3, countryId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new Exception("Не удалось создать data_machine");
    }
    private static int getOrCreateMachineId(Connection conn, int clientId, int dataMachineId) throws Exception {
        String select = "SELECT id FROM machins WHERE client_id = ? AND data_machine_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, dataMachineId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        String insert = "INSERT INTO machins (client_id, data_machine_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, clientId);
            stmt.setInt(2, dataMachineId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new Exception("Не удалось создать машину");
    }

    private static int getOrCreateRepairTypeId(Connection conn, String name, int durationDays, double cost) throws Exception {
        String select = "SELECT id FROM repair_types WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(select)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        String insert = "INSERT INTO repair_types (name, duration_days, cost) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, durationDays);
            stmt.setDouble(3, cost);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new Exception("Не удалось создать тип ремонта");
    }

    public static boolean updateRepair(Repair repair, int oldMachineId, String oldClientName, String oldBrand, int oldYear, String oldCountry) {
        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);
            int clientId = getOrCreateId(conn, "clients", "name", repair.getClientName(), "contact_info", repair.getContactInfo());
            int brandId = getOrCreateId(conn, "brands", "name", repair.getBrand());
            int yearId = getOrCreateId(conn, "years_creation", "value", String.valueOf(repair.getYear()));
            int countryId = getOrCreateId(conn, "countries_creation", "name", repair.getCountry());
            int dataMachineId = getOrCreateDataMachineId(conn, brandId, yearId, countryId);

            int machineId;
            if ( repair.getClientName().equals(oldClientName) &&
                            repair.getBrand().equals(oldBrand) &&
                            repair.getYear() == oldYear &&
                            repair.getCountry().equals(oldCountry))
            {
                machineId = oldMachineId;
            } else {
                machineId = getOrCreateMachineId(conn, clientId, dataMachineId);
            }

            int repairTypeId = getOrCreateRepairTypeId(conn, repair.getRepairType(), repair.getDurationDays(), repair.getCost());
            String updateRepairType = "UPDATE repair_types SET duration_days = ?, cost = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateRepairType)) {
                stmt.setInt(1, repair.getDurationDays());
                stmt.setDouble(2, repair.getCost());
                stmt.setInt(3, repairTypeId);
                stmt.executeUpdate();
            }
            String sql = "UPDATE repairs SET machine_id = ?, repair_type_id = ?, start_date = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, machineId);
                stmt.setInt(2, repairTypeId);
                stmt.setString(3, repair.getStartDate());
                stmt.setInt(4, repair.getRepairId());
                stmt.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRepair(int repairId) {
        String sql = "DELETE FROM repairs WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, repairId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Repair> getAllMachinesForCurrentClient() {
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