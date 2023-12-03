package lab13.qifan.group2.a2.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleLoggerTest {

    private SimpleLogger simpleLogger;
    private final String logFilePath = "scroll_tasks_log.txt";
    private File logFile;

    @BeforeEach
    public void setup() {
        simpleLogger = new SimpleLogger();
        logFile = new File(logFilePath);
    }

    @Test
    public void testLog() {
        String testMessage = "Test log message";
        simpleLogger.log(testMessage);

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String lastLine = null, currentLine;

            while ((currentLine = reader.readLine()) != null) {
                lastLine = currentLine;
            }

            // Check if the last line in the log file contains the test message
            assert lastLine != null;
            assertEquals(true, lastLine.contains(testMessage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadCount() {
        simpleLogger.log("Test message 1 - uploaded");
        simpleLogger.log("Test message 2");
        simpleLogger.log("Test message 3 - uploaded");

        int count = simpleLogger.uploadCount();
        assertEquals(2, count);
    }

    @AfterEach
    public void tearDown() {
        // Explicitly delete the log file, though it should be automatically deleted by the @TempDir annotation
        if (logFile.exists()) {
            logFile.delete();
        }
    }
}
