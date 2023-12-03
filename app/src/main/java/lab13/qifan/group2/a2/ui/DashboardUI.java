package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.UserManager;
import lab13.qifan.group2.a2.ui.LoginRegistrationUI;
import lab13.qifan.group2.a2.services.UserManager;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.models.DigitalScroll;

import java.util.Scanner;

public class DashboardUI {
    // methods for main dashboard UI functionalities
    private final Scanner scanner;
    private final User user;
    private final UserManager userManager;
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private ScrollDisplayUI scrollDisplayUI;

    public DashboardUI(User user, ScrollManager scrollManager, ScrollSeeker scrollSeeker){
        this.scanner = new Scanner(System.in);
        this.user = user;
        this.scrollManager = scrollManager;
        this.scrollSeeker = scrollSeeker;
        this.scrollDisplayUI = new ScrollDisplayUI(scrollManager, scrollSeeker, user);
        userManager = new UserManager(this.user);
    }

    public void displayMainUI(){
        System.out.println("-------------- HOME PAGE --------------");
        System.out.println("Hello " + this.user.getUsername() + "! (" + this.user.getUserType() + ")");
        System.out.println("What would you like to do today? (1 ~ 4)");
        System.out.println("1. Manage your profile.");
        System.out.println("2. Edit your Scrolls.");
        System.out.println("3. Remove your Scrolls.");
        System.out.println("4. Seek Scrolls.");
        System.out.println("5. Logout");
        if (scanner.hasNextLine()){
            String response = scanner.nextLine();
            try{
                switch(Integer.parseInt(response)) {
                    case 1:
                        userManager.editProfile(this.user);
                        break;
                    case 2:
                        // implement ScrollManager here
                        String currentUserId = this.user.getIdKey();
                        scrollDisplayUI.displayUserScrolls(currentUserId);
                        break;
                    case 3:
                        scrollManager.removeYourScroll(this.user);
                        break;
                    case 4:
                        // implement ScrollSeeker here
                        scrollDisplayUI.displayScrolls(this.user);
                        break;
                    case 5:
                        if (logoutConfirmUI()) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid input.");
                        break;
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid input.");
            }
            displayMainUI();
        }
    }

    public boolean logoutConfirmUI() {  
        System.out.println("Are you sure you want to logout? (yes/no)");
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
}