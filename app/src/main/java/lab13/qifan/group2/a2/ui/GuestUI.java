package lab13.qifan.group2.a2.ui;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.services.UserManager;

import java.util.Scanner;

public class GuestUI {
    // methods for guest UI functionalities
    private final Scanner scanner;
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private ScrollDisplayUI scrollDisplayUI;
    private User user;
    LoginRegistrationUI loginUI = new LoginRegistrationUI();

    public GuestUI(ScrollManager scrollManager, ScrollSeeker scrollSeeker){
        this.user = new User("Guest","","",0,"guest", UserType.GUEST,null);
        this.scanner = new Scanner(System.in);
        this.scrollManager = scrollManager;
        this.scrollSeeker = scrollSeeker;
        this.scrollDisplayUI = new ScrollDisplayUI(scrollManager, scrollSeeker, user);
    }
    public void setLoginUI(LoginRegistrationUI loginUI) {
        this.loginUI = loginUI;
    }

    public void displayMainUI(){
        System.out.println("-------------- HOME PAGE --------------");
        System.out.println("Hello Guest User!");
        System.out.println("What would you like to do today? (1 ~ 4)");
        System.out.println("1. Seek Scrolls.");
        System.out.println("2. Login as an User.");
        System.out.println("3. Register New User.");
        System.out.println("4. Exit.");

        if (scanner.hasNextLine()) {
            String response = scanner.nextLine();
            try{
                switch (Integer.parseInt(response)) {
                    case 1:
                        scrollDisplayUI.displayGuestScrolls();
                        break;
                    case 2:
                        loginUI.displayLoginScreen();
                        break;
                    case 3:
                        loginUI.displayRegistrationScreen();
                        break;
                    case 4:
                        if(logoutConfirmUI()){
                            return;
                        }
                        displayMainUI();
                        break;
                    default:
                        System.out.println("Invalid input.");
                        displayMainUI();
                        break;
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid input.");
                displayMainUI();
            }
        }
    }

    public boolean logoutConfirmUI() {
        System.out.println("Are you sure you want to exit? (yes/no)");
        if (scanner.hasNextLine()) {
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                System.out.println("Exited Successfully");
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
}