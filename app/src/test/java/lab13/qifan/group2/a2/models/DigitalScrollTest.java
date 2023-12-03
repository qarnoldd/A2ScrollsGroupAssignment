package lab13.qifan.group2.a2.models;

import lab13.qifan.group2.a2.models.DigitalScroll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DigitalScrollTest {

    private DigitalScroll testScroll;

    @BeforeEach
    void setUp() {
        testScroll = new DigitalScroll("testId", "Test Scroll", "testFilePath", "testUserId", 0);
    }

    @Test
    void constructorAndGetters() {
        assertEquals("testId", testScroll.getId());
        assertEquals("Test Scroll", testScroll.getName());
        assertEquals("testFilePath", testScroll.getFilePath());
        assertEquals("testUserId", testScroll.getUserIdKey());
        assertEquals(0, testScroll.getDownloadCount());
        assertNotNull(testScroll.getDate());
    }

    @Test
    void setters() {
        testScroll.setId("newId");
        testScroll.setName("New Scroll");
        testScroll.setFilePath("newFilePath");
        testScroll.setUserIdKey("newUserId");
        testScroll.setDownloadCount(10);
        LocalDate newDate = LocalDate.of(2023, 1, 1);
        testScroll.setDate(newDate);

        assertEquals("newId", testScroll.getId());
        assertEquals("New Scroll", testScroll.getName());
        assertEquals("newFilePath", testScroll.getFilePath());
        assertEquals("newUserId", testScroll.getUserIdKey());
        assertEquals(10, testScroll.getDownloadCount());
        assertEquals(newDate, testScroll.getDate());
    }

    @Test
    void testDownloadCount() {
        assertEquals(0, testScroll.getDownloadCount());

        testScroll.setDownloadCount(5);
        assertEquals(5, testScroll.getDownloadCount());

        testScroll.setDownloadCount(10);
        assertEquals(10, testScroll.getDownloadCount());
    }
}
