package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.SimpleLogger;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.services.UserManager;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.models.DigitalScroll;


import java.util.Scanner;

public class LoginRegistrationUI {
    private UserDatabase userDatabase;
    private ScrollDatabase scrollDatabase;
    private String username;
    private final Scanner scanner;
    private GuestUI guestUI;
    private DashboardUI dashboardUI;
    private ScrollManager scrollManager; 
    private ScrollSeeker scrollSeeker;
    private SimpleLogger logger;
    private User user;


    public LoginRegistrationUI() {
        this.scrollDatabase = new ScrollDatabase();
        this.userDatabase = new UserDatabase();
        this.scanner = new Scanner(System.in); 
        scrollManager = new ScrollManager();
        scrollSeeker = new ScrollSeeker(scrollManager, user);
        logger = new SimpleLogger();
    }

    public void setGuestUI(GuestUI guestUI) {
        this.guestUI = guestUI;
    }

    public void setDashboardUI(DashboardUI dashboardUI) {
        this.dashboardUI = dashboardUI;
    }

    public void setUserDatabase(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void displayLoginScreen() {
        System.out.println("Press q to exit the program.");
        System.out.println("Do you want to log in as a guest? (yes/no): ");

        // Check if there is another line available before calling nextLine()
        if (scanner.hasNextLine()) {
            String guestDecision = scanner.nextLine();

            if (guestDecision.equalsIgnoreCase("yes")) {
                GuestUI guestUI = new GuestUI(scrollManager, scrollSeeker);
                logger.log("A user logged in as GUEST");
                guestUI.displayMainUI();
                logger.log("A user logged out as GUEST");
                displayLoginScreen();
                return;
            } else if (guestDecision.equalsIgnoreCase("no")) {
            
            } else if (guestDecision.equalsIgnoreCase("q")) {
                return;
            } else {
                System.out.println("Invalid Input.");
                displayLoginScreen();
                return;
            }
        }

        System.out.println("-------------- LOGIN SCREEN --------------");
        System.out.println("Username: ");

        // Check if there is another line available before calling nextLine()
        if (scanner.hasNextLine()) {
            String username = scanner.nextLine();
            System.out.println("You have just inputted: " + username);

            System.out.println("Password: ");

            // Check if there is another line available before calling nextLine()
            if (scanner.hasNextLine()) {
                String password = scanner.nextLine();
                System.out.println("Your pw: " + password);

                if (userDatabase.authenticateUser(username, password)) {
                    System.out.println("Login successful!");
                    UserType type = userDatabase.getUser(username).getUserType();
                    switch (type) {
                        case ADMIN:
                            logger.log("An admin logged in as ADMIN");
                            AdminPanelUI adminUI = new AdminPanelUI(userDatabase.getUser(username), userDatabase, scrollManager, scrollSeeker);
                            adminUI.displayMainUI();
                            logger.log("Exited ADMIN");
                            break;
                        case REGULAR:
                            logger.log("A user logged in as REGULAR");
                            DashboardUI userUI = new DashboardUI(userDatabase.getUser(username),scrollManager, scrollSeeker);
                            userUI.displayMainUI();
                            logger.log("Exited REGULAR");
                            break;
                        case GUEST:
                            GuestUI guestUI = new GuestUI(scrollManager, scrollSeeker);
                            guestUI.displayMainUI();
                            break;
                    }
                } else {
                    System.out.println("Login failed. Incorrect username or password.");
                    System.out.println("Do you want to register? (yes/no): ");

                    // Check if there is another line available before calling nextLine()
                    if (scanner.hasNextLine()) {
                        String decision = scanner.nextLine();

                        if (decision.equalsIgnoreCase("yes")) {
                            displayRegistrationScreen();
                        } else if (!decision.equalsIgnoreCase("no")) {
                            System.out.println("Invalid Input");
                        }
                    }
                }
            }
            displayLoginScreen();
        }

    }

    public void displayRegistrationScreen() {
        System.out.println("-------------- REGISTRATION SCREEN --------------");

        System.out.print("Username: ");

        // Check if there is another line available before calling nextLine()
        if (scanner.hasNextLine()) {
            String username = scanner.nextLine().trim();
            while (userDatabase.getUser(username) != null || username.isEmpty()) {
                if(userDatabase.getUser(username) != null){
                    System.out.println("Username already exists. Please choose a different username.");
                }else{
                    System.out.println("Username cannot be blank.");
                }
                System.out.print("Username: ");
                username = scanner.nextLine();
            }

            System.out.print("Password: ");

            // Check if there is another line available before calling nextLine()
            if (scanner.hasNextLine()) {
                String password = scanner.nextLine().trim();
                while(password.isEmpty()){
                    System.out.println("Password cannot be blank.");
                    System.out.print("Password: ");
                    password = scanner.nextLine();
                }
                System.out.println("The password you just entered is: " + password);

                System.out.print("Email: ");

                // Check if there is another line available before calling nextLine()
                if (scanner.hasNextLine()) {
                    String email = scanner.nextLine().trim();
                    while(email.isEmpty()){
                        System.out.println("Email cannot be blank.");
                        System.out.print("Email: ");
                        email = scanner.nextLine();
                    }

                    int phoneInt = 0;
                    boolean validPhone = false;

                    while (!validPhone) {
                        System.out.print("Phone: ");

                        // Check if there is another line available before calling nextLine()
                        if (scanner.hasNextLine()) {
                            String phoneStr = scanner.nextLine();
                            try {
                                phoneInt = Integer.parseInt(phoneStr);
                                if (phoneInt >= 0) {
                                    validPhone = true;
                                } else {
                                    System.out.println("Invalid phone number provided. Please enter a non-negative phone number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid phone number provided. Please enter a valid phone number.");
                            }
                        }
                    }

                    System.out.print("ID Key (leave empty for default): ");

                    // Check if there is another line available before calling nextLine()
                    if (scanner.hasNextLine()) {
                        String idKey = scanner.nextLine();
                        while (userDatabase.checkIdKeyExists(idKey)){
                            System.out.println("This id key already exists. Please choose a different id key.");
                            System.out.print("ID Key (leave empty for default): ");
                            if (scanner.hasNextLine()) {
                                idKey = scanner.nextLine();
                            }
                        }
//                        if (userDatabase.getUser(idKey) != null) {
//                            System.out.println("This id key already exists. Please choose a different id key.");
//                            displayRegistrationScreen();
//                            return;
//                        }
                        String hashedPassword = PasswordEncryptor.hashPassword(password);
                        String salt = hashedPassword.substring(0, 32);
                        User newUser = new User(username, hashedPassword, email, phoneInt, idKey, UserType.REGULAR, salt);

                        if (userDatabase.addUser(newUser)) {
                            // System.out.println("Registration successful!\n");
                            System.out.println("-------------- YOUR DETAILS --------------");
                            System.out.println("Username: " + username);
                            System.out.println("Password: " + password);
                            System.out.println("Email: " + email);
                            System.out.println("Phone: " + Integer.toString(phoneInt));
                            System.out.println("ID Key: " + newUser.getIdKey() + "\n");
                            System.out.println("Registration successful!\n");
                        } else {
                            System.out.println("Registration failed. Username might be taken.");
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        LoginRegistrationUI ui = new LoginRegistrationUI();
        ui.displayLoginScreen();
    }
}
