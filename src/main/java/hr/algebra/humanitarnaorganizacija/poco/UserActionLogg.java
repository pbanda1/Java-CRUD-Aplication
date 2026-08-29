package hr.algebra.humanitarnaorganizacija.poco;

public class UserActionLogg {


    private String timestamp;
    private String username;
    private String action;
    private String details;


    public UserActionLogg(String action, String username, String timestamp, String details) {
        this.action = action;
        this.username = username;
        this.timestamp = timestamp;
        this.details = details;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
