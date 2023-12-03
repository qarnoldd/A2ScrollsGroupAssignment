package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;

import java.io.File;
import lab13.qifan.group2.a2.models.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ScrollManager {
    // methods for scroll management: create, read, update, delete scrolls.

    private List<DigitalScroll> scrolls;
    private SimpleLogger logger;
    private Scanner scanner;
    private ScrollDatabase scrollDatabase;
    private static final String BINARY_FILE_FOLDER = "virtualLibrary";

    public ScrollManager() {
        scrollDatabase = new ScrollDatabase();
        this.scrolls = scrollDatabase.getArrayScrolls();
        this.logger = new SimpleLogger();
        this.scanner = new Scanner(System.in);
    }

    public List<DigitalScroll> getAllScrolls() {
        return scrolls;
    }
    public List<DigitalScroll> getUserScrolls(User user) {
        
        logger.log("Fetched scrolls for user with ID: " + user.getIdKey());

        List<DigitalScroll> userScrolls = new ArrayList<DigitalScroll>();
        for (DigitalScroll s : scrolls) {
            if (s.getUserIdKey().equals(user.getIdKey()) || user.getUserType() == UserType.ADMIN) {
                userScrolls.add(s);
            }
        }
        return userScrolls;
    }

    public void removeYourScroll(User user) {
        Scanner scanner = new Scanner(System.in);
        // Functionality to remove the digital scroll
        System.out.println("Your scrolls are the following: " + getUserScrolls(user).size());
        for(int i = 0; i < getUserScrolls(user).size(); i++)
        {
            System.out.println((i + 1) + ". " + getUserScrolls(user).get(i).getName() + " (ID: " + getUserScrolls(user).get(i).getId() + ")");
        }
        System.out.println((getUserScrolls(user).size() + 1) + ". Exit");
        int choice = -1;
            while(true) {
                try {
                    System.out.print("Select a scroll to remove: ");
                    if(scanner.hasNextLine()) {
                        choice = Integer.parseInt(scanner.nextLine());
                        if (choice < 0 || choice > getUserScrolls(user).size()+1) {
                            System.out.println("Invalid choice. Select again");
                        } else {
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
            if(choice != getUserScrolls(user).size() + 1) {
                choice--;
                String path = getUserScrolls(user).get(choice).getFilePath();
                File file = new File(path);
                String oldID;
                if (file.exists()) {
                    if (file.delete()) {
                        oldID = getUserScrolls(user).get(choice).getId();
                        scrollDatabase.deleteScroll(getUserScrolls(user).get(choice));
                        for (int i = 0; i < scrolls.size(); i++) {
                            if (scrolls.get(i).getId().equals(getUserScrolls(user).get(choice).getId())){
                                scrolls.remove(i);
                                break;
                            }
                        }
                        System.out.println("Scroll deleted from library.");
                        logger.log("User with ID " + user.getIdKey() + " removed scroll with ID: " + oldID);
                    } else {
                        System.out.println("Scroll deletion failed.");
                        logger.log("Scroll removal was cancelled by user with ID: " + user.getIdKey());
                    }
                } else {
                    System.out.println("Error occurred. Scroll cannot be found in library.");
                }
            }
    }

    public void addNewScroll(User user) {
        System.out.println("Entering addNewScroll method...");

        System.out.print("Enter the name of the new scroll: ");
        if (scanner.hasNextLine()) {
            String scrollName = scanner.nextLine().trim();

            System.out.println("Checking uniqueness of the scroll name...");
            if (!isUniqueName(scrollName)) {
                logger.log("User with ID " + user.getIdKey() + " attempt to add scroll with duplicate name: " + scrollName);
                System.out.println("Error: Scroll with the same name already exists. Please choose a unique name.");
                return;
            }

            if (scrollName.isEmpty()) {
                logger.log("User with ID " + user.getIdKey() + " attempt to add scroll with empty name.");
                System.out.println("Error: Scroll name cannot be blank. Please choose a unique name.");
                return;
            }

            System.out.print("Enter the id of the new scroll: ");
            String scrollId = scanner.nextLine().trim();

            System.out.println("Checking uniqueness of the scroll id...");
            if (!isUniqueID(scrollId)) {
                logger.log("User with ID " + user.getIdKey() + " attempt to add scroll with duplicate ID: " + scrollId);
                System.out.println("Error: Scroll with the same id already exists. Please choose a unique id.");
                return;
            }
            if (scrollId.isEmpty()) {
                logger.log("User with ID " + user.getIdKey() + " attempt to add scroll with empty ID.");
                System.out.println("Error: Scroll id cannot be blank. Please choose a unique id.");
                return;
            }

            String idKey = user.getIdKey();

            System.out.println("Opening file chooser...");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a binary file");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary files (.bin)", "bin");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                System.out.println("File chooser approved...");
                File selectedFile = fileChooser.getSelectedFile();
                if (user.getUserType() == UserType.GUEST) {
                    try {
                        List<String> lines = Files.readAllLines(selectedFile.toPath(), StandardCharsets.ISO_8859_1);
                        if (lines.size() > 10) {
                            System.out.println("Error: Guest users can only upload binary files with up to 10 lines.");
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error reading file lines.");
                        return;
                    }
                }
                try {
                    String packagePath = BINARY_FILE_FOLDER.replace(".", File.separator);
//                    Path destination = Path.of(packagePath, selectedFile.getName());
                    Path destination = Paths.get(packagePath, selectedFile.getName());
                    Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                    String filePath = BINARY_FILE_FOLDER + "/" + selectedFile.getName();

                    DigitalScroll newScroll = new DigitalScroll(scrollId, scrollName, filePath, idKey, 0);
                    createLog(newScroll, user);
                    scrolls.add(newScroll);
                    scrollDatabase.addScroll(newScroll);
                    System.out.println("Scroll added successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error: Failed to copy the binary file.");
                }
            } else {
                System.out.println("Scroll addition cancelled.");
            }
            System.out.println("Exiting addNewScroll method...");
        }
    }
    public boolean isUniqueName(String name) {
        for (DigitalScroll scroll : scrolls) {
            if (scroll.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean isUniqueID(String id) {
        for (DigitalScroll scroll : scrolls) {
            if (scroll.getId().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public void addScroll(DigitalScroll scroll, User user){
        if(!checkIdExists(scroll.getId())){
            this.scrolls.add(scroll);
            logger.log("User with ID " + user.getIdKey() + " added a new scroll with ID: " + scroll.getId());

        } else {
            logger.log("User with ID " + user.getIdKey() + " attempt to add a scroll with duplicate ID: " + scroll.getId());
        }
        System.out.println("The Scroll ID " + scroll.getId() + " has been taken.");
    }

    public void createLog(DigitalScroll newScroll, User user) {
        logger.log("User with ID " + user.getIdKey() + " uploaded scroll " + newScroll.getId());
    }

    public boolean checkIdExists(String id){
        for(DigitalScroll s : scrolls){
            if(s.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

}
