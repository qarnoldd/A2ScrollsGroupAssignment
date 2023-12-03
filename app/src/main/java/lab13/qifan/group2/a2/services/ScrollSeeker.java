package lab13.qifan.group2.a2.services;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScrollSeeker {
    private ScrollDatabase scrollDatabase;
    private SimpleLogger logger;
    private ScrollManager scrollManager;
    private Scanner scanner = new Scanner(System.in);

    public ScrollSeeker(ScrollManager scrollManager, User user) {
        this.logger = new SimpleLogger();
        this.scrollDatabase = new ScrollDatabase();
        this.scrollManager = scrollManager;
    }

    public void viewScrollDetails(User user) {
        // Functionality to view the details of the digital scroll
        logger.log("User with ID " + user.getIdKey() + " viewed the details of the digital scroll.");
    }

    public void downloadScroll(User user) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the scroll to download: ");
        if (scanner.hasNextLine()) {
            String scrollName = scanner.nextLine();

            DigitalScroll scrollToDownload = findScrollByName(scrollName);



            if (scrollToDownload != null) {
                try {
                    byte[] binaryData = Files.readAllBytes(Paths.get(scrollToDownload.getFilePath()));

                    String downloadPath = "DownloadedScrolls" +
                            File.separator + scrollToDownload.getName() + ".bin";

                    try (FileOutputStream fos = new FileOutputStream(downloadPath)) {
                        fos.write(binaryData);
                        String originalScrollID = scrollToDownload.getId();
                        scrollToDownload.setDownloadCount(scrollToDownload.getDownloadCount() + 1);
                        logger.log("Downloaded scroll " + scrollToDownload.getId());
                        scrollDatabase.updateScroll(scrollToDownload, originalScrollID);
                        System.out.println("Scroll downloaded successfully to: DownloadedScrolls");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error: Failed to download the scroll.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error: Failed to read the binary file.");
                }
            } else {
                System.out.println("Error: Scroll not found.");
            }
        }
    }

    public void searchScrolls(List<DigitalScroll> scrolls, User user) {
        System.out.println("What would you like to perform the search on?");
        System.out.println("1. Uploader ID");
        System.out.println("2. Scroll ID");
        System.out.println("3. Scroll Name");
        System.out.println("4. Date Uploaded");
        System.out.println("5. Quit");
        if (scanner.hasNextLine()) {
            int response;
            try {
                response = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                searchScrolls(scrolls, user);
                return;
            }
            switch(response){
                case 1:
                    System.out.print("Enter Uploader's ID: ");
                    if(scanner.hasNextLine()){
                        String id = scanner.nextLine();
                        List<DigitalScroll> result = new ArrayList<DigitalScroll>();
                        for (DigitalScroll scroll: scrolls){
                            if(scroll.getUserIdKey().equals(id) || scroll.getUserIdKey().contains(id)){
                                result.add(scroll);
                            }
                        }
                        printScrolls(result);
                    }
                    break;
                case 2:
                    System.out.print("Enter Scroll's ID: ");
                    if(scanner.hasNextLine()){
                        String id = scanner.nextLine();
                        List<DigitalScroll> result = new ArrayList<DigitalScroll>();
                        for (DigitalScroll scroll: scrolls){
                            if(scroll.getId().equals(id) || scroll.getId().contains(id)){
                                result.add(scroll);
                            }
                        }
                        printScrolls(result);
                    }
                    break;
                case 3:
                    System.out.print("Enter Scroll's Name: ");
                    if(scanner.hasNextLine()){
                        String name = scanner.nextLine();
                        List<DigitalScroll> result = new ArrayList<DigitalScroll>();
                        for (DigitalScroll scroll: scrolls){
                            if(scroll.getName().equals(name) || scroll.getName().contains(name)){
                                result.add(scroll);
                            }
                        }
                        printScrolls(result);
                    }
                    break;
                case 4:
                    System.out.print("Enter Upload date (YYYY-MM-DD): ");
                    if(scanner.hasNextLine()){
                        String input = scanner.nextLine();
                        try{
                            LocalDate date = LocalDate.parse(input);
                            if(date.isAfter(LocalDate.now())){
                                throw new Exception();
                            }
                            List<DigitalScroll> result = new ArrayList<DigitalScroll>();
                            for (DigitalScroll scroll: scrolls){
                                if(scroll.getDate().equals(date)){
                                    result.add(scroll);
                                }
                            }
                            printScrolls(result);
                        } catch (Exception e){
                            System.out.println("Invalid Input.");
                        }
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Input.");
                    break;
            }
            searchScrolls(scrolls, user);
        }
    }

    public void previewScroll(DigitalScroll scroll, User user) {
        File bin = new File(scroll.getFilePath());
        try {
            // create a reader for data file
            FileInputStream read = new FileInputStream(bin);

            // the variable will be used to read one byte at a time
            int byt;
            String output = "";
            while ((byt = read.read()) != -1) {
                String binary = Integer.toBinaryString(byt & 255 | 256).substring(1);
                output += binary + " ";
            }
            System.out.println("-------------- SCROLL PREVIEW --------------");
            System.out.println(output);
            System.out.println("--------------------------------------------\n");
            
            logger.log("User with ID " + user.getIdKey() + " previewed scroll " + scroll.getId());

            read.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printScrolls(List<DigitalScroll> list){
        int page = 0;
        while(true){

            System.out.println("\n-------------- SEARCH RESULT --------------");
            for (int i = page * 10; i < page * 10 + 10 && i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i).getName() + " (ID: " + list.get(i).getId() + ")");
            }
            if(list.size() > 0){
                double pages = list.size();
                pages = pages / 10;
                System.out.println("You are now at page " + (page + 1) + "/" + (int) Math.ceil(pages) + ".");
            }else{
                System.out.println("You are now at page " + (page + 1) + "/1.");
            }
            System.out.println("Press 0/1 to go to the previous/next page.");
            System.out.println("Press q to quit.");
            if(scanner.hasNextLine()){
                String response = scanner.nextLine();
                if(response.equals("0")){
                    if(page > 0){
                        page--;
                    }
                    continue;
                }
                if(response.equals("1")){
                    double pages = list.size();
                        pages = pages / 10;
                        if (list.size() > 0 && (int) Math.ceil(pages) > page + 1){
                            page++;
                        }
                        continue;
                }
                if(response.equals("q")){
                    return;
                }
            }
        }
    }

    private DigitalScroll findScrollByName(String name) {
        List<DigitalScroll> allScrolls = scrollManager.getAllScrolls();
        for (DigitalScroll scroll : allScrolls) {
            if (scroll.getName().equals(name)) {
                return scroll;
            }
        }
        return null;
    }
}
