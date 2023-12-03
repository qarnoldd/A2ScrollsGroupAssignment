package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.SimpleLogger;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.services.UserManager;
import lab13.qifan.group2.a2.models.DigitalScroll;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Scanner;

public class AdminPanelUI {
    // methods for admin panel UI functionalities
    private Scanner scanner;
    private final User user;
    private UserDatabase userDB;
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private ScrollDisplayUI scrollDisplayUI;
    private UserManager userManager;
    private SimpleLogger logger;

    // public AdminPanelUI(User user, UserDatabase userDB){
    //     this.scanner = new Scanner(System.in);
    //     this.user = user;
    //     this.userDB = userDB;
    // }

    public AdminPanelUI(User user, UserDatabase userDB, ScrollManager scrollManager, ScrollSeeker scrollSeeker) {
        this.scanner = new Scanner(System.in);
        this.user = user;
        this.userDB = userDB;
        this.scrollManager = scrollManager;
        this.scrollSeeker = scrollSeeker;
        this.scrollDisplayUI = new ScrollDisplayUI(scrollManager,scrollSeeker,user);
        this.logger = new SimpleLogger();
        this.userManager = new UserManager(user);
    }

    public void displayMainUI(){
        System.out.println("------------===# Admin Interface #===------------");
        System.out.println("Hello " + this.user.getUsername() + " " + this.user.getUserType() + "!");
        System.out.println("What would you like to do today? (1 ~ 8)");
        System.out.println("1. Manage your profile.");
        System.out.println("2. Edit your Scrolls.");
        System.out.println("3. Remove your Scrolls.");
        System.out.println("4. Seek Scrolls.");
        System.out.println("5. Delete Users.");
        System.out.println("6. Statistics.");
        System.out.println("7. Make new users.");
        System.out.println("8. Print all users.");
        System.out.println("9. Logout");
        if(scanner.hasNextLine()) {
            String response = scanner.nextLine();
            try {
                switch (Integer.parseInt(response)) {
                    case 1:
                        // implement UserManager here
                        userManager.editProfile(this.user);
                        break;
                    case 2:
                        // implement ScrollManager here
                        scrollDisplayUI.displayScrolls(this.user);
                        break;
                    case 3:
                        scrollManager.removeYourScroll(this.user);
                        break;
                    case 4:
                        // implement ScrollSeeker here
                        scrollDisplayUI.displayScrolls(this.user);
                        break;
                    case 5:
                        manageUser();
                        break;
                    case 6:
                        scrollStatistics(scrollManager.getAllScrolls(), logger.uploadCount());
                        break;
                    case 7:
                        addUser();
                        break;
                    case 8:
                        userDB.fetchAndPrintAllUsers();
                        break;
                    case 9:
                        if (logoutConfirmUI()) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid input.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
            displayMainUI();
        }
    }

    public void manageUser()
    {
        this.scanner = new Scanner(System.in);
        System.out.println("------------===#   Delete User   #===------------");
        System.out.println("Hello " + this.user.getUsername() + " " + this.user.getUserType() + "!");
        System.out.println("ALL USERS:");
        userDB.fetchAndPrintAllUsers();
        while(true) {
            System.out.println("Input the name of the user (/b to return): ");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                try {
                    if (input.equals("/b")) {
                        System.out.println("Returning to previous menu...");
                        break;
                    } else {
                        User user = userDB.getUser(input);
                        System.out.println("Are you sure you want to delete " + user.getUsername() + "? (yes/no)");
                        if (scanner.hasNextLine()) {
                            String response = scanner.nextLine();
                            try {
                                switch (response.toLowerCase()) {
                                    case "yes":
                                        try (Connection connection = DriverManager.getConnection(userDB.getDBURL());
                                             PreparedStatement stmt = connection.prepareStatement(
                                                     "DELETE FROM users WHERE username = ? AND idKey = ?")) {
                                            stmt.setString(1, user.getUsername());
                                            stmt.setString(2, user.getIdKey());
                                            stmt.execute();
                                            System.out.println("User deleted successfully!");
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "no":
                                        System.out.println("Returning...");
                                        break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid Input.");
                            }
                        }
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                }
            }
        }
    }

    public void addUser()
    {
        scanner = new Scanner(System.in);
        System.out.println("------------===#   Add User   #===------------");
        String username = null;
        String password = null;
        String email = null;
        String IDKey;
        boolean valid = false;
        while(!valid) {
            System.out.println("Enter Username: ");
            if (scanner.hasNextLine()) {
                username = scanner.nextLine().trim();
                if (userDB.getUser(username) != null) {
                    System.out.println("Username already exists. Please choose a different username.");
                    continue;
                }
                if (username.isEmpty()) {
                    System.out.println("Username can't be empty.");
                    continue;
                }
                valid = true;
            }
        }
        valid = false;
        while(!valid) {
            System.out.println("Password: ");
            if (scanner.hasNextLine()) {
                password = scanner.nextLine().trim();
                if (password.isEmpty()) {
                    System.out.println("Password cant be empty.");
                    continue;
                }
                else
                {
                    valid = true;
                }
            }
        }
        System.out.println("The password you just entered is: " + password);

        valid = false;
        while(!valid) {
            System.out.println("Email: ");
            if (scanner.hasNextLine()) {
                email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    System.out.println("Email cant be empty.");
                    continue;
                }
                else
                {
                    valid = true;
                }
            }
        }
        int phoneInt = 0;
        boolean validPhone = false;
        while (!validPhone) {
            System.out.println("Phone: ");
            String phoneStr = "";
            if (scanner.hasNextLine()) {
                phoneStr = scanner.nextLine();
            }

            try {
                phoneInt = Integer.parseInt(phoneStr);
                if(phoneInt < 0)
                {
                    System.out.println("Phone number cannot be negative.");
                }
                else {
                    validPhone = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid phone number provided. Please enter a valid phone number.");
            }
        }
        System.out.println("IDKey (leave empty for default): ");
        IDKey = null;
        if (scanner.hasNextLine()) {
            IDKey = scanner.nextLine().trim();
        }

        if (userDB.getUser(IDKey) != null) {
            System.out.println("This id key already exists. User add failed.");
            addUser();
            return;
        }
        String hashedPassword = PasswordEncryptor.hashPassword(password);
        String salt = hashedPassword.substring(0, 32);

        User user = new User(username, hashedPassword, email, phoneInt, IDKey, UserType.REGULAR, salt);
        if (userDB.addUser(user)) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("User add failed. Username might be taken.");
        }
    }

    public boolean logoutConfirmUI(){
        scanner = new Scanner(System.in);

        System.out.println("Are you sure you want to logout? (yes/no)");
        if (scanner.hasNextLine()) {
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                System.out.println("Logout Successfully");
                return true;
            } else if (response.equalsIgnoreCase("no")) {
                return false;
            } else {
                System.out.println("Invalid input.");
                return false;
            }
        }
        return false;
    }

    public void scrollStatistics(List<DigitalScroll> scrolls, int uploadCount)
    {
        scanner = new Scanner(System.in);
        int page = 0;
        while (true) {
            System.out.println("-------------- SCROLL STATISTICS --------------");
            System.out.println("Scrolls available: " + scrolls.size());
            System.out.println("Scrolls uploaded: " + uploadCount);
            System.out.println("------------------------------------------------");

            System.out.println("List of available scrolls:");
            for (int i = page * 10; i < page * 10 + 10 && i < scrolls.size(); i++) {
                System.out.println((i + 1) + ". " + scrolls.get(i).getName() + " (ID: " + scrolls.get(i).getId() + ") DOWNLOAD COUNT: " + scrolls.get(i).getDownloadCount());
            }
            if(scrolls.size() > 0){
                double pages = scrolls.size();
                pages = pages / 10;
                System.out.println("You are now at page " + (page + 1) + "/" + (int) Math.ceil(pages) + ".");
            }else{
                System.out.println("You are now at page " + (page + 1) + "/1.");
            }

            System.out.println("\nOptions:");
            System.out.println("1. Next Page");
            System.out.println("2. Previous Page");
            System.out.println("3. Quit");

            System.out.print("Enter your choice:\n");

            int choice = -1;
            try {

                if (scanner.hasNextLine()) {
                    choice = Integer.parseInt(scanner.nextLine());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }
            switch (choice) {
                case 1:
                    if (scrolls.size() > 0 && (int) Math.ceil(scrolls.size() / 10) > page + 1){
                        page++;
                    }
                    break;
                case 2:
                    if (page > 0){
                        page--;
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid Input.");
            }

        }
    }
}
