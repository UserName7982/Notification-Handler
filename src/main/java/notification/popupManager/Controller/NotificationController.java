package notification.popupManager.Controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import notification.WebSocket.WebSocketConnection;
import notification.popupManager.NotificationManager.NotificationSchedular;
import notification.popupManager.NotificationManager.NotificationStore;

public class NotificationController {
    @FXML
    public VBox CloseButton;
    @FXML
    private Label message;
    @FXML
    private Label title;
    @FXML
    private HBox PermissionButtons;
    private WebSocketConnection connection = WebSocketConnection.getInstance();
    private String notificationId;
    private String event;
    @FXML
    public void handleClose() {
        NotificationStore.notifications.get(notificationId).setRead(true);
        NotificationStore.remove(notificationId);
        NotificationSchedular.schduledsave();
        CloseButton.getScene().getWindow().hide();
    }

    @FXML
    public void setData(String id,String title, String message,String event) {
        this.notificationId=id;
        this.title.setText(title);
        this.message.setText(message);
        this.event=event;
        if(event.equals("Permission")){
            PermissionButtons.setVisible(true);
            PermissionButtons.setManaged(true);
        }
    }

    @FXML
    public void handleAllow() {
        // Handle allow action
        System.out.println("Permission Allowed for event: " + event);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Allowed");
        response.put("feedback", "");
        response.put("id", notificationId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String Response=mapper.writeValueAsString(response);
            connection.sendMessage(Response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        handleClose();
    }

    @FXML
    public void handleDeny() {
        // Handle deny action
        System.out.println("Permission Denied for event: " + event);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Denied");
        response.put("feedback", "");
        response.put("id", notificationId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String Response=mapper.writeValueAsString(response);
            connection.sendMessage(Response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        handleClose();
    }
}
