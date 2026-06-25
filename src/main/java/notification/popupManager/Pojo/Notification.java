package notification.popupManager.Pojo;

import java.sql.Date;

public class Notification {

    public String id;
    public String message;
    public String event;
    public String timeStamp=Date.valueOf(java.time.LocalDate.now()).toString();
    public boolean isRead=false;
    public String title;


    public Notification() {}
    public Notification(String id, String message, String event,
                        String timeStamp, boolean isRead, String title) {
        this.id = id;
        this.message = message;
        this.event = event;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "Notification [id=" + id + ", message=" + message + ", event=" + event + ", timeStamp=" + timeStamp
                + ", isRead=" + isRead + ", title=" + title + "]";
    }
}

