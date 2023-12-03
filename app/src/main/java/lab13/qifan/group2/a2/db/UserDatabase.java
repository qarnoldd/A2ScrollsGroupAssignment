package lab13.qifan.group2.a2.db;

import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;

public class UserDatabase {
    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";

    public UserDatabase() {
        initDb();
    }

    private void initDb() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS users (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "username TEXT NOT NULL UNIQUE," +
                             "password TEXT NOT NULL," +
                             "email TEXT NOT NULL," +
                             "phone INTEGER NOT NULL," +
                             "idKey TEXT NOT NULL UNIQUE," +
                             "userType TEXT NOT NULL," +
                             "salt TEXT NOT NULL)"
             )) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean addUser(User user) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO users(username, password, email, phone, idKey, userType, salt) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getPhone());
            stmt.setString(5, user.getIdKey());
            stmt.setString(6, user.getUserType().toString());
            stmt.setString(7, user.getSalt());
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   public boolean removeUser(User user) {
       try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM users WHERE username = ? AND idKey = ?")) {
           stmt.setString(1, user.getUsername());
           stmt.setString(2, user.getIdKey());
           stmt.execute();
           return true;
       }  catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }

    public User getUser(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(null, null, null, 0, null, null, null);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getInt("phone"));
                user.setIdKey(rs.getString("idKey"));
                user.setUserType(UserType.valueOf(rs.getString("userType")));
                user.setSalt(rs.getString("salt"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkIdKeyExists(String idKey){
        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement("SELECT idKey FROM users WHERE idKey = ?")) {
            stmt.setString(1, idKey);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String username, String password) {
        User user = getUser(username);
        if (user != null) {
            try {
                String userSalt = user.getPassword().substring(0, 32);
                String hashedPassword = user.getPassword().substring(32);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(PasswordEncryptor.hexToBytes(userSalt));
                byte[] hashedEnteredPassword = md.digest(password.getBytes());
                String enteredPasswordHex = PasswordEncryptor.bytesToHex(hashedEnteredPassword);
                return enteredPasswordHex.equals(hashedPassword);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean updateUser(User updatedUser) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, idKey = ?, userType = ?, salt = ? WHERE idKey = ?")) {
            stmt.setString(1, updatedUser.getUsername());
            stmt.setString(2, updatedUser.getPassword());
            stmt.setString(3, updatedUser.getEmail());
            stmt.setInt(4, updatedUser.getPhone());
            stmt.setString(5, updatedUser.getIdKey());
            stmt.setString(6, updatedUser.getUserType().toString());
            stmt.setString(7, updatedUser.getSalt());
            stmt.setString(8, updatedUser.getIdKey());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public void fetchAndPrintAllUsers() {
        System.out.println("\n------------------- ALL USERS -------------------");
        try (Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password"); 
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
                String idKey = rs.getString("idKey");
                String userType = rs.getString("userType");
                String salt = rs.getString("salt");

                System.out.println("ID: " + id + ", Username: " + username + ", Hashed Password: " + password + ", Email: " + email + ", Phone: " + phone + ", ID Key: " + idKey + ", User Type: " + userType + ", Salt: " + salt);
            }
            System.out.println("-------------------------------------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }



    public String getDBURL() {
        return DB_URL;
    }
}