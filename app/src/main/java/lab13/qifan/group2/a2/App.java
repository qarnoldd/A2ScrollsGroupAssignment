package lab13.qifan.group2.a2;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.ui.LoginRegistrationUI;

public class App {
    
    private LoginRegistrationUI loginUI;

    // Dependency Injection
    public App(LoginRegistrationUI loginUI) {
        this.loginUI = loginUI;
    }

    public void runApp() {
        loginUI.displayLoginScreen();
    }

    public static void main(String[] args) {
        
        LoginRegistrationUI loginUI = new LoginRegistrationUI();
        new App(loginUI).runApp();


//         This is for checking the database
//          UserDatabase userDb = new UserDatabase();
//          userDb.fetchAndPrintAllUsers();
    }

    
}
