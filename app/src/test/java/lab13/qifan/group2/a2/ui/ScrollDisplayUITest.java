package lab13.qifan.group2.a2.ui;

import lab13.qifan.group2.a2.db.ScrollDatabase;
import lab13.qifan.group2.a2.models.DigitalScroll;
import lab13.qifan.group2.a2.models.User;
import lab13.qifan.group2.a2.services.ScrollManager;
import lab13.qifan.group2.a2.services.ScrollSeeker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static lab13.qifan.group2.a2.models.UserType.REGULAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScrollDisplayUITest {

    private ScrollManager scrollManager;
    private ScrollSeeker scrollSeeker;
    private User user;
    private ScrollDisplayUI scrollDisplayUI;

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        user = new User("testUser", "testPassword", "test@email.com", 123456789, "10", REGULAR, "mockSaltValue");
        scrollManager = new ScrollManager();
        scrollSeeker = new ScrollSeeker(scrollManager, user);

        scrollDisplayUI = new ScrollDisplayUI(scrollManager, scrollSeeker, user);
        System.setOut(new PrintStream(outputStream));

        List<DigitalScroll> scrolls = new ArrayList<>();
        scrolls.add(new DigitalScroll("ScrollID1", "Scroll1", "scroll1", "f1", 0));
        scrolls.add(new DigitalScroll("ScrollID2", "Scroll2", "scroll2", "f2", 0));

        for (DigitalScroll scroll: scrolls){
            scrollManager.addScroll(scroll, user);
        }
    }

    // @Test
    // public void testDisplayUserScrolls() {


    //     String input = "1\n1\n2\nNewName\n3\nNewID\n3\n";
    //     provideInput(input);

    //     scrollDisplayUI.displayUserScrolls(user.getIdKey());

    //     String expectedOutput = "-------------- YOUR SCROLLS --------------\n" +
    //             "1. Scroll1 (ID: ID1)\n" +
    //             "2. Scroll2 (ID: ID2)\n" +
    //             "3. Exit.\n" +
    //             "Select a Scroll to edit: \n" +
    //             "Selected Scroll: Scroll1\n" +
    //             "What would you like to update?\n" +
    //             "1. Name\n" +
    //             "2. ID\n" +
    //             "3. Cancel\n" +
    //             "Please enter a new name: \n" +
    //             "Scroll name successfully updated to: NewName\n";

    //     assertEquals(expectedOutput, outputStream.toString());
    // }

    // @Test
    // public void testDisplayScrolls() {

    //     String input = "1\n2\n3\n4\n5\n6\n8\n";
    //     provideInput(input);

    //     scrollDisplayUI.displayScrolls(user);

    //     String expectedOutput = "The Scroll ID ScrollID1 has been taken.\n" +
    //             "The Scroll ID ScrollID2 has been taken.\n" +
    //             "-------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" +
    //             "6. Scroll2 (ID: ScrollID2)\n" +
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: -------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" + 
    //             "6. Scroll2 (ID: ScrollID2)\n" +
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: Entering addNewScroll method...\n" +
    //             "Enter the name of the new scroll: -------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" +
    //             "6. Scroll2 (ID: ScrollID2)\n" +
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: -------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" + 
    //             "6. Scroll2 (ID: ScrollID2)\n" + 
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: What would you like to perform the search on?\n" +
    //             "1. Uploader ID\n" +
    //             "2. Scroll ID\n" +
    //             "3. Scroll Name\n" +
    //             "4. Date Uploaded\n" +
    //             "5. Quit\n" +
    //             "-------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" +
    //             "6. Scroll2 (ID: ScrollID2)\n" +
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: Which scroll would you like to preview? (Name)\n" +
    //             "Scroll does not exist.\n" +
    //             "-------------- SCROLL SEEKER --------------\n" +
    //             "List of available scrolls:\n" +
    //             "1. removeScroll (ID: removeScroll)\n" +
    //             "2. 4f (ID: 4f)\n" +
    //             "3. testing3 (ID: r3)\n" +
    //             "4. g4 (ID: 4g)\n" +
    //             "5. Scroll1 (ID: ScrollID1)\n" + 
    //             "6. Scroll2 (ID: ScrollID2)\n" + 
    //             "You are now at page 1/1.\n" +
    //             "Total Scrolls: 6\n" +
    //             "\n" +
    //             "Options:\n" +
    //             "1. View Scroll Details\n" +
    //             "2. Add New Scroll\n" +
    //             "3. Download Scroll\n" +
    //             "4. Search Scrolls\n" +
    //             "5. Preview Scroll\n" +
    //             "6. Next Page\n" +
    //             "7. Previous Page\n" +
    //             "8. Quit\n" +
    //             "Enter your choice: ";

    //     assertEquals(expectedOutput, outputStream.toString());
    // }
    
    @Test
    public void testDisplayGuestScrolls() {


        String input = "1\n2\n3\n4\n5\n6\n7\n";
        provideInput(input);

        scrollDisplayUI.displayGuestScrolls();

        // Add your expected output here
    }

    private void provideInput(String data) {
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            scrollDisplayUI = new ScrollDisplayUI(scrollManager, scrollSeeker, user);
        } finally {
            System.setIn(stdin);
        }
    }
}
