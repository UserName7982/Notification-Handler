package notification.popupManager.Pojo;

public class messageTemplate {
    public String id;
    public String feedback;
    public String action;

    
    public messageTemplate() {

    }
    
    public messageTemplate(String id, String feedback, String action) {
        this.id = id;
        this.feedback = feedback;
        this.action = action;
    }
    
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getFeedback() {
        return feedback;
    }


    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "messageTemplate [id=" + id + ", feedback=" + feedback + ", action=" + action + "]";
    }
}
