package notification.popupManager.UI;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import notification.popupManager.Controller.NotificationContainerController;
import notification.popupManager.Controller.NotificationController;
import notification.popupManager.Pojo.Notification;

public class NotificationCard {

    private final Stage stage;
    private final VBox notificationBox;
    public static NotificationCard instance;

    public NotificationCard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NotificationContainer.fxml"));

        Parent root = loader.load();

        NotificationContainerController controller = loader.getController();
        notificationBox = controller.getNotificationBox();

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
    }

    public static NotificationCard getInstance() {
        try {
            if (instance == null) {
                instance = new NotificationCard();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    public void show(Notification notification) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Notification.fxml"));

        try {
            Parent card = loader.load();
            NotificationController controller = loader.getController();
            controller.setData(notification.getId(), notification.getTitle(), notification.getMessage(),
                    notification.getEvent());
            controller.setOnClose(() -> removeCard(card));
            notificationBox.getChildren().add(card);
            stage.sizeToScene();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMaxX() - stage.getWidth() - 15);
            stage.setY(bounds.getMaxY() - stage.getHeight() - 15);

            card.setOpacity(0);
            card.setTranslateX(350);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), card);
            slideIn.setFromX(350);
            slideIn.setToX(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(350), card);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(e -> removeCard(card));
            SequentialTransition sequence = new SequentialTransition(
                    new ParallelTransition(slideIn, fadeIn),
                    pause);

            sequence.play();
            if (!stage.isShowing()) {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCard(Node card) {

        if (card.getParent() == null) {
            return;
        }

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(350), card);
        slideOut.setToX(350);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(350), card);
        fadeOut.setToValue(0);
        ParallelTransition hide = new ParallelTransition(slideOut, fadeOut);

        hide.setOnFinished(e -> {
            notificationBox.getChildren().remove(card);
            stage.sizeToScene();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMaxX() - stage.getWidth() - 15);
            stage.setY(bounds.getMaxY() - stage.getHeight() - 15);

            if (notificationBox.getChildren().isEmpty()) {
                stage.hide();
            }
        });

        hide.play();
    }
}