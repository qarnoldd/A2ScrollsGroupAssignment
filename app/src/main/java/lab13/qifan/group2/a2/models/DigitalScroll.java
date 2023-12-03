package lab13.qifan.group2.a2.models;

import java.time.LocalDate;

public class DigitalScroll {
    private String id;
    private String name;
    private int downloadCount;
    private String userIdKey;
    private String filepath; // actual data of the scroll
    private LocalDate date;

    // Constructor
    public DigitalScroll(String id, String name, String filepath, String userIdKey, int downloadCount) {
        this.id = id;
        this.name = name;
        this.downloadCount = downloadCount;
        this.filepath = filepath;
        this.userIdKey = userIdKey;
        this.date = LocalDate.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public String getUserIdKey() {return this.userIdKey;}
    public void setUserIdKey(String userIdKey) {
        this.userIdKey = userIdKey;
    }

    public int getDownloadCount() {
        return downloadCount;
    }
    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
    public String getFilePath(){
        return this.filepath;
    }
    public void setFilePath(String filePath){
        this.filepath = filePath;
    }
    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}
