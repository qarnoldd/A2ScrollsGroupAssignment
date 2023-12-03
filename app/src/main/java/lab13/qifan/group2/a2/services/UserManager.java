package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.UserDatabase;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.utilities.PasswordEncryptor;

import java.util.Scanner;

public class UserManager {

    private final Scanner scanner;
    private final User user;
    private final UserDatabase userDatabase;

    public UserManager(User user) {
        this.scanner = new Scanner(System.in);
        this.user = user;
        this.userDatabase = new UserDatabase();
    }

    public void editProfile(User user) {
        // Allow the user to edit profile details
        System.out.println("-------------- EDIT PROFILE --------------");
        System.out.println("1. Edit Username");
        System.out.println("2. Edit Password");
        System.out.println("3. Edit Email");
        System.out.println("4. Edit Phone");
        System.out.println("5. Return to Home");

        if (scanner.hasNextLine()){
            String response = scanner.nextLine();

            switch (response) {
                case "1":
                    System.out.println("Enter new username:");
                    String newUsername = scanner.nextLine().trim();
                    if (userDatabase.getUser(newUsername) != null || newUsername.equals("")) {
                        if(newUsername.isEmpty()){
                            System.out.println("Username cannot be blank. Please choose a different username.");
                        }else{
                            System.out.println("Username already exists. Please choose a different username.");
                        }
                        editProfile(user);
                        return;
                    }
                    user.setUsername(newUsername);
                    updateUserInDatabase(user);
                    System.out.println("Username updated successfully!");
                    break;
                case "2":
                    System.out.println("Enter new password:");
                    String newPassword = scanner.nextLine().trim();
                    if (newPassword.isEmpty()) {
                        System.out.println("Password cannot be blank. Please choose a different password.");
                        editProfile(user);
                        return;
                    }
                    String hashedPassword = PasswordEncryptor.hashPassword(newPassword);
                    String salt = hashedPassword.substring(0, 32);
                    user.setPassword(hashedPassword);
                    user.setSalt(salt);
                    updateUserInDatabase(user);
                    System.out.println("Password updated successfully!");
                    break;
                case "3":
                    System.out.println("Enter new email:");
                    String newEmail = scanner.nextLine().trim();
                    if (newEmail.isEmpty()) {
                        System.out.println("Email cannot be blank. Please choose a different email.");
                        editProfile(user);
                        return;
                    }
                    user.setEmail(newEmail);
                    updateUserInDatabase(user);
                    System.out.println("Email updated successfully!");
                    break;
                case "4":
                    
                    int newPhone = 0;
                    boolean validPhone = false;
                    while (!validPhone) {
                        System.out.println("Enter new phone number:");
                        String phoneStr = scanner.nextLine();
                        try {
                            newPhone = Integer.parseInt(phoneStr);
                            validPhone = (newPhone >= 0);
                            if(!validPhone){
                                System.out.println("Invalid phone number provided. Please enter a valid phone number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid phone number provided. Please enter a valid phone number.");
                        }
                    }
                    user.setPhone(newPhone);
                    updateUserInDatabase(user);
                    System.out.println("Phone number updated successfully!");
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }

    public void updateUserInDatabase(User user) {
        if(userDatabase.updateUser(user)){
            System.out.println("Updated Successfully!");
        }
        else{
            System.out.println("Update has failed, try later.");
        }

    }

    public User getUser() {
        return user;
    }

}