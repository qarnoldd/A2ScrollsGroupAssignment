package lab13.qifan.group2.a2.ui;
import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.models.UserType;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import lab13.qifan.group2.a2.services.SimpleLogger;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class ScrollDisplayUI {
    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private ScrollDatabase scrollDatabase;
    private User user;
    private Scanner scanner;
    public ScrollDisplayUI(ScrollManager scrollManager, ScrollSeeker scrollSeeker, User user) {
        this.scrollDatabase = new ScrollDatabase()
        ;
        this.scrollManager = scrollManager;
        this.scrollSeeker = scrollSeeker;
        this.scanner = new Scanner(System.in);
        this.user = user;
    }

    public void displayUserScrolls(String userId){
        Scanner scan = new Scanner(System.in);
        int i = 1;
        List<DigitalScroll> userScrolls = scrollManager.getUserScrolls(user);
        System.out.println("-------------- YOUR SCROLLS --------------");
        for (DigitalScroll s : userScrolls){
            System.out.println((i) + ". " + s.getName() + " (ID: " + s.getId() + ")");
            i++;
        }
        System.out.println((i) + ". Exit.");

        int scroll;
        while(true){
            try {
                System.out.println("Select a Scroll to edit: ");
                if(scan.hasNextLine()) {
                    scroll = Integer.parseInt(scan.nextLine());
                    if (scroll < 1 || scroll > userScrolls.size() + 1) {
                        System.out.println("Invalid choice. Please select again.");
                    } else {
                        break;
                    }
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
        if(scroll != userScrolls.size() + 1) {
            DigitalScroll selectedScroll = userScrolls.get(scroll - 1);
            String originalID = selectedScroll.getId();
            System.out.println("Selected Scroll: " + selectedScroll.getName());
            int choice;
            while (true) {
                try {
                    System.out.println("What would you like to update?");
                    System.out.println("1. Name");
                    System.out.println("2. ID");
                    System.out.println("3. Cancel");
                    choice = Integer.parseInt(scan.nextLine());
                    if (choice < 1 || choice > 3) {
                        System.out.println("Invalid choice. Please select again.");
                    } else {
                        break;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
            }
            switch (choice) {
                case 1:
                    System.out.println("Please enter a new name: ");
                    String name = scan.nextLine();
                    while (!scrollManager.isUniqueName(name) || name.equals("")) {
                        if(name.equals("")){
                            System.out.println("Scrolll name cannot be blank. Please enter one.");
                        }else{
                            System.out.println("This name already exists. Please choose another.");
                        }
                        System.out.println("Please enter a new name: ");
                        name = scan.nextLine();
                    }
                    selectedScroll.setName(name);
                    scrollDatabase.updateScroll(selectedScroll,originalID);
                    System.out.println("Scroll name successfully updated to: " + selectedScroll.getName());

                    break;
                case 2:
                    List<DigitalScroll> allScrolls = scrollManager.getAllScrolls();
                    String newID = null;
                    boolean exists = true;
                    System.out.println("Please enter a new ID key: ");
                    newID = scan.nextLine();
                    while (scrollManager.checkIdExists(newID) || newID.equals("")) {
                        if(newID.equals("")){
                            System.out.println("Scroll ID cannot be blank. Please enter one.");
                        }else{
                            System.out.println("This ID key already exists. Please choose another.");
                        }
                        System.out.println("Please enter a new ID key: ");
                        newID = scan.nextLine();
                    }

                    selectedScroll.setId(newID);
                    scrollDatabase.updateScroll(selectedScroll,originalID);
                    System.out.println("Scroll ID successfully updated to: " + selectedScroll.getId());
                    break;
                case 3:
                    return;
            }
            scrollDatabase.updateScroll(selectedScroll,originalID);
            displayUserScrolls(userId);
        }
    }

    public void displayScrolls(User user) {
        List<DigitalScroll> scrolls = scrollManager.getAllScrolls();
        int page = 0;
        while (true) {
            System.out.println("-------------- SCROLL SEEKER --------------");
            System.out.println("List of available scrolls:");
            for (int i = page * 10; i < page * 10 + 10 && i < scrolls.size(); i++) {
                System.out.println((i + 1) + ". " + scrolls.get(i).getName() + " (ID: " + scrolls.get(i).getId() + ")");
            }
            if(scrolls.size() > 0){
                double pages = scrolls.size();
                pages = pages / 10;
                System.out.println("You are now at page " + (page + 1) + "/" + (int) Math.ceil(pages) + ".");
            }else{
                System.out.println("You are now at page " + (page + 1) + "/1.");
            }

            System.out.println("Total Scrolls: " + scrolls.size());

            System.out.println("\nOptions:");
            System.out.println("1. View Scroll Details");
            System.out.println("2. Add New Scroll");
            System.out.println("3. Download Scroll");
            System.out.println("4. Search Scrolls");
            System.out.println("5. Preview Scroll");
            System.out.println("6. Next Page");
            System.out.println("7. Previous Page");
            System.out.println("8. Quit");

            System.out.print("Enter your choice: ");

            int choice;
            if (scanner.hasNextLine()) {
                try {

                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        // View Scroll Details
                        scrollSeeker.viewScrollDetails(user);
                        break;
                    case 2:
                        // Download Scroll
                        scrollManager.addNewScroll(user);
                        break;
                    case 3:
                        // Download Scroll
                        scrollSeeker.downloadScroll(user);
                        break;
                    case 4:
                        // Search Scrolls
                        scrollSeeker.searchScrolls(scrolls, user);
                        break;
                    case 5:
                        // Preview Scroll
                        System.out.println("Which scroll would you like to preview? (Name)");
                        if (scanner.hasNextLine()) {
                            int i = -1;
                            try {
                                String name = scanner.nextLine();
                                for (DigitalScroll scroll : scrolls) {
                                    if (scroll.getName().equals(name)) {
                                        i = scrolls.indexOf(scroll);
                                    }
                                }
                                if (i >= 0) {
                                    scrollSeeker.previewScroll(scrolls.get(i), user);
                                } else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                System.out.println("Scroll does not exist.");
                            }
                        }
                        break;
                    case 6:
                        double pages = scrolls.size();
                        pages = pages / 10;
                        if (scrolls.size() > 0 && (int) Math.ceil(pages) > page + 1) {
                            page++;
                        }
                        break;
                    case 7:
                        if (page > 0) {
                            page--;
                        }
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("\nInvalid Input.");
                        break;
                }
            }
        }

    }

    public void displayGuestScrolls() {
        List<DigitalScroll> scrolls = scrollManager.getAllScrolls();
        

        // if (scrolls.isEmpty()) {
        //     System.out.println("No scrolls available.");
        //     return;
        // }
        int page = 0;
        while (true) {
            System.out.println("-------------- SCROLL SEEKER --------------");
            System.out.println("List of available scrolls:");
            for (int i = page * 10; i < page * 10 + 10 && i < scrolls.size(); i++) {
                System.out.println((i + 1) + ". " + scrolls.get(i).getName() + " (ID: " + scrolls.get(i).getId() + ")");
            }
            if(scrolls.size() > 0){
                double pages = scrolls.size();
                pages = pages / 10;
                System.out.println("You are now at page " + (page + 1) + "/" + (int) Math.ceil(pages) + ".");
            }else{
                System.out.println("You are now at page " + (page + 1) + "/1.");
            }

            System.out.println("Total Scrolls: " + scrolls.size());

            System.out.println("\nOptions:");
            System.out.println("1. View Scroll Details");
            System.out.println("2. Add New Scroll");
            System.out.println("3. Search Scrolls");
            System.out.println("4. Preview Scroll");
            System.out.println("5. Next Page");
            System.out.println("6. Previous Page");
            System.out.println("7. Quit");

            System.out.print("Enter your choice: ");
            
            int choice;
            if (scanner.hasNextLine()) {
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        // View Scroll Details
                        scrollSeeker.viewScrollDetails(user);
                        break;
                    case 2:
                        // View Scroll Details
                        scrollManager.addNewScroll(user);
                        break;
                    case 3:
                        // Search Scrolls
                        scrollSeeker.searchScrolls(scrolls, user);
                        break;
                    case 4:
                        // Preview Scroll
                        System.out.println("Which scroll would you like to preview? (Name)");
                        if (scanner.hasNextLine()) {
                            int i = -1;
                            try {
                                String name = scanner.nextLine();
                                for (DigitalScroll scroll : scrolls) {
                                    if (scroll.getName().equals(name)) {
                                        i = scrolls.indexOf(scroll);
                                    }
                                }
                                if (i >= 0) {
                                    scrollSeeker.previewScroll(scrolls.get(i), user);
                                } else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                System.out.println("Scroll does not exist.\n");
                            }
                        }
                        break;
                    case 5:
                        double pages = scrolls.size();
                        pages = pages / 10;
                        if (scrolls.size() > 0 && (int) Math.ceil(pages) > page + 1) {
                            page++;
                        }
                        break;
                    case 6:
                        if (page > 0) {
                            page--;
                        }
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("\nInvalid Input.");
                        break;
                }
            }
        }
    }


    
}
