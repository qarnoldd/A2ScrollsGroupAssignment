package lab13.qifan.group2.a2.db;

import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ScrollDatabase {
    private static final String DB_URL = "jdbc:sqlite:mydatabase.db";

    public ScrollDatabase() {
        initDb();
    }

    private void initDb() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS scrolls (" +
                             "idDB INTEGER PRIMARY KEY AUTOINCREMENT," +
                             "id TEXT NOT NULL UNIQUE," +
                             "name TEXT NOT NULL UNIQUE," +
                             "downloadCount INTEGER NOT NULL," +
                             "filepath TEXT NOT NULL," +
                             "userIdKey TEXT NOT NULL," +
                             "date DATE NOT NULL)"
             )) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //addMockScrolls();
        //deleteMockScrolls();
    }

    public boolean addScroll(DigitalScroll scroll) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO scrolls(id, name, downloadCount, filepath, userIdKey, date) VALUES(?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, scroll.getId());
            stmt.setString(2, scroll.getName());
            stmt.setInt(3,scroll.getDownloadCount());
            stmt.setString(4, scroll.getFilePath());
            stmt.setString(5, scroll.getUserIdKey());
            stmt.setDate(6, java.sql.Date.valueOf(scroll.getDate()));
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public DigitalScroll getScroll(String scrollName) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT * FROM scrolls WHERE name = ?")) {
            stmt.setString(1, scrollName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DigitalScroll scroll = new DigitalScroll(rs.getString("id"),rs.getString("name"),rs.getString("filepath"),rs.getString("userIdKey"),rs.getInt("downloadCount"));
                java.sql.Date sqldate = rs.getDate("date");
                LocalDate date = sqldate.toLocalDate();
                scroll.setDate(date);
                return scroll;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkIdKeyExists(String id){
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT id FROM scrolls WHERE id = ?")) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateScroll(DigitalScroll updatedScroll, String originalID) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE scrolls SET id = ?, name = ?, downloadCount = ?, filepath = ?, userIdKey = ?, date = ? WHERE id = ?")) {
            stmt.setString(1, updatedScroll.getId());
            stmt.setString(2, updatedScroll.getName());
            stmt.setInt(3, updatedScroll.getDownloadCount());
            stmt.setString(4, updatedScroll.getFilePath());
            stmt.setString(5, updatedScroll.getUserIdKey());
            stmt.setDate(6, java.sql.Date.valueOf(updatedScroll.getDate()));
            stmt.setString(7, originalID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteScroll(DigitalScroll scroll) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement(
                     "DELETE FROM scrolls WHERE name = ? AND id = ?")) {
            stmt.setString(1, scroll.getName());
            stmt.setString(2, scroll.getId());
            stmt.execute();
            System.out.println("Scroll deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void fetchAndPrintAllScrolls() {
//        try (Connection connection = DriverManager.getConnection(DB_URL);
//             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users")) {
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String username = rs.getString("username");
//                String password = rs.getString("password");
//                String email = rs.getString("email");
//                int phone = rs.getInt("phone");
//                String idKey = rs.getString("idKey");
//                String userType = rs.getString("userType");
//
//                System.out.println("ID: " + id + ", Username: " + username + ", Hashed Password: " + password + ", Email: " + email + ", Phone: " + phone + ", ID Key: " + idKey + ", User Type: " + userType);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Error fetching users: " + e.getMessage());
//        }
//    }
//    "CREATE TABLE IF NOT EXISTS scrolls (" +
//            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//            "name TEXT NOT NULL UNIQUE," +
//            "downloadCount INTEGER NOT NULL," +
//            "filepath TEXT NOT NULL" +
//            "userIdKey TEXT NOT NULL UNIQUE," +
//            "date DATE NOT NULL)"
    public ArrayList<DigitalScroll> getArrayScrolls()
    {
        ArrayList<DigitalScroll> digitalScrolls = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM scrolls")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DigitalScroll scroll = new DigitalScroll(rs.getString("id"),rs.getString("name"),rs.getString("filepath"),rs.getString("userIdKey"),rs.getInt("downloadCount"));
                java.sql.Date sqldate = rs.getDate("date");
                LocalDate date = sqldate.toLocalDate();
                scroll.setDate(date);
                digitalScrolls.add(scroll);
            }
            return digitalScrolls;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching Scroll: " + e.getMessage());
        }
        return null;
    }
    public String getDBURL() {
        return DB_URL;
    }

//    public void addMockScrolls()
//    {
//        // ADD EXISTING FILES AS SCROLLS (TEST SCROLLS)
//        DigitalScroll digitalScroll1 = new DigitalScroll("1","Scroll1","virtualLibrary/Scroll1.bin","a",0);
//        LocalDate date = LocalDate.of(2020,12,15);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll1);
//        DigitalScroll digitalScroll2 = new DigitalScroll("2","Scroll2","virtualLibrary/Scroll2.bin","a",1);
//        date = LocalDate.of(2023,7,20);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll2);
//        DigitalScroll digitalScroll3 = new DigitalScroll("3","Scroll3","virtualLibrary/Scroll3.bin","j",3);
//        date = LocalDate.of(2023,4,3);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll3);
//        DigitalScroll digitalScroll4 = new DigitalScroll("4","Scroll4","virtualLibrary/Scroll4.bin","j",5);
//        date = LocalDate.of(2023,5,21);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll4);
//        DigitalScroll digitalScroll5 = new DigitalScroll("5","Scroll5","virtualLibrary/Scroll5.bin","adminIDKey",0);
//        date = LocalDate.of(2022,4,8);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll5);
//        DigitalScroll digitalScroll6 = new DigitalScroll("6","Scroll6","virtualLibrary/Scroll6.bin","adminIDKey",12);
//        date = LocalDate.of(2021,5,5);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll6);
//        DigitalScroll digitalScroll7 = new DigitalScroll("7","Scroll7","virtualLibrary/Scroll7.bin","j",0);
//        date = LocalDate.of(2021,5,5);
//        digitalScroll1.setDate(date);
//        addScroll(digitalScroll7);
//        System.out.println("NEW SCROLLS INITIALISED -----------------------");
//    }
//    private void deleteMockScrolls()
//    {
//        // ADD EXISTING FILES AS SCROLLS (TEST SCROLLS)
//        DigitalScroll digitalScroll1 = new DigitalScroll("1","Scroll1","virtualLibrary/Scroll1.bin","a",0);
//        LocalDate date = LocalDate.of(2020,12,15);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll1);
//        DigitalScroll digitalScroll2 = new DigitalScroll("2","Scroll2","virtualLibrary/Scroll2.bin","a",1);
//        date = LocalDate.of(2023,7,20);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll2);
//        DigitalScroll digitalScroll3 = new DigitalScroll("3","Scroll3","virtualLibrary/Scroll3.bin","j",3);
//        date = LocalDate.of(2023,4,3);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll3);
//        DigitalScroll digitalScroll4 = new DigitalScroll("4","Scroll4","virtualLibrary/Scroll4.bin","j",5);
//        date = LocalDate.of(2023,5,21);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll4);
//        DigitalScroll digitalScroll5 = new DigitalScroll("5","Scroll5","virtualLibrary/Scroll5.bin","adminIDKey",0);
//        date = LocalDate.of(2022,4,8);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll5);
//        DigitalScroll digitalScroll6 = new DigitalScroll("6","Scroll6","virtualLibrary/Scroll6.bin","adminIDKey",12);
//        date = LocalDate.of(2021,5,5);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll6);
//        DigitalScroll digitalScroll7 = new DigitalScroll("7","Scroll7","virtualLibrary/Scroll7.bin","j",0);
//        date = LocalDate.of(2021,5,5);
//        digitalScroll1.setDate(date);
//        deleteScroll(digitalScroll7);
//        System.out.println("NEW SCROLLS REMOVED -----------------------");
//    }
}