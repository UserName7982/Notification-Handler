package notification.popupManager.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import notification.WebSocket.WebSocketConnection;
import notification.popupManager.NotificationManager.NotificationSchedular;
import notification.popupManager.NotificationManager.NotificationStore;
import notification.popupManager.Pojo.messageTemplate;

public class ReaderController {

    @FXML
    private TextArea feedbackArea;
    @FXML
    private Button closeButton;
    @FXML
    private Button regenerateButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button sendButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private TextArea emailArea;
    private String id;
    ObjectMapper mapper = new ObjectMapper();
    private WebSocketConnection connection = WebSocketConnection.getInstance();

    @FXML
    public void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleRegenerate() throws JsonProcessingException {
        // Handle regenerate action
        String feedback = feedbackArea.getText();
        System.out.println("Notification feedback: " + feedback);
        messageTemplate message = new messageTemplate(id, feedback, "regenerate");

        String Response = mapper.writeValueAsString(message);

        connection.sendMessage(Response);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        pause.play();
        NotificationStore.notifications.get(id).setRead(true);
        NotificationStore.remove(id);
        NotificationSchedular.schduledsave();
    }

    @FXML
    public void handleSend() throws JsonProcessingException {
        // Handle send action
        messageTemplate message = new messageTemplate(id, feedbackArea.getText(), "send");
        String Response = mapper.writeValueAsString(message);
        connection.sendMessage(Response);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(event -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        pause.play();
        NotificationStore.notifications.get(id).setRead(true);
        NotificationStore.remove(id);
        NotificationSchedular.schduledsave();
    }

    @FXML
    public void handleSave() {
        // Handle save action
        String feedback = feedbackArea.getText();
        System.out.println("Notification feedback: " + feedback);
    }

    @FXML
    public void setData(String id, String subject, String email) {
        this.id = id;
        subjectLabel.setText(subject);
        emailArea.setText(email);
    }
}