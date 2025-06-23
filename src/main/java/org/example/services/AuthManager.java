package org.example.services;

import org.example.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthManager {
    public static User login(String login, String password) throws SQLException {
        String sql = "SELECT u.id, u.login, r.name as role FROM users u JOIN roles r ON u.role_id = r.id WHERE u.login = ? AND u.password = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, hashPassword(password));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("login"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    public static boolean register(String login, String password) throws SQLException {
        // Проверка на уникальность логина
        String checkSql = "SELECT id FROM users WHERE login = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, login);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return false; // Логин уже занят
                }
            }
        }
        // Регистрация нового пользователя с ролью "USER" (id=2, например)
        String insertSql = "INSERT INTO users (login, password, role_id) VALUES (?, ?, 2)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, login);
            insertStmt.setString(2, hashPassword(password));
            return insertStmt.executeUpdate() > 0;
        }
    }

    public static boolean updateUser(int userId, String newLogin, String newPassword) throws SQLException {
        String sql = "UPDATE users SET login = ?, password = ? WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newLogin);
            stmt.setString(2, hashPassword(newPassword));
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Хеширование пароля через
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}