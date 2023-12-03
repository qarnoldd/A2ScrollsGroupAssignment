package lab13.qifan.group2.a2.services;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileReader;

public class SimpleLogger {

    private final String logFilePath;

    public SimpleLogger() {
        this.logFilePath = "scroll_tasks_log.txt";
    }

    public void log(String message) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = dateFormat.format(now);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(timeStamp + " - " + message + "\n");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public int uploadCount()
    {
        int numUpload = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("uploaded")) {
                    numUpload++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numUpload;
    }
}
