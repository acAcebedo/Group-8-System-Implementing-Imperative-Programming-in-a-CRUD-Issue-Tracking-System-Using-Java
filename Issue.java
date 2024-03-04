// Issue.java
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private String status;
    private String assignedDeveloper;
    private String dateReported;

    public Issue(String title, String description, String status, String assignedDeveloper) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedDeveloper = assignedDeveloper;
        this.dateReported = getCurrentDateTime();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedDeveloper() {
        return assignedDeveloper;
    }

    public void setAssignedDeveloper(String assignedDeveloper) {
        this.assignedDeveloper = assignedDeveloper;
    }

    public String getDateReported() {
        return dateReported;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    @Override
    public String toString() {
        return "Title: " + title +
                "\nDescription: " + description +
                "\nStatus: " + status +
                "\nAssigned Developer: " + assignedDeveloper +
                "\nDate Reported: " + dateReported;
    }
}
