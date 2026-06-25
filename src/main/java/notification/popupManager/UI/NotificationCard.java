package notification.popupManager.UI;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import notification.popupManager.Controller.NotificationController;
import notification.popupManager.Controller.ReaderController;
import notification.popupManager.NotificationManager.NotificationStore;
import notification.popupManager.Pojo.Notification;

public class NotificationCard {
    private static final double WIDTH = 320;
    private static final double HEIGHT = 100;

    public void show(Notification notification) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Notification.fxml"));
        NotificationStore.activeNotifications.offer(notification);
        Notification notification1 = NotificationStore.activeNotifications.peekLast();
        try {
            Parent root = loader.load();
            root.setOnMouseClicked(e -> {
                // Handle click event, e.g., open related content
                FXMLLoader NotificationLoader = new FXMLLoader(getClass().getResource("/NotificationFeedBack.fxml"));

                Parent readerRoot;
                try {
                    readerRoot = NotificationLoader.load();
                    Stage readerStage = new Stage();
                    readerStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
                    // readerStage.initStyle(StageStyle.UNDECORATED);
                    readerStage.setScene(new Scene(readerRoot));
                    readerStage.show();
                    ReaderController readerController = NotificationLoader.getController();
                    readerController.setData(notification1.getId(), notification1.getTitle(), notification1.getMessage());
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            });
            NotificationController controller = loader.getController();
            controller.setData(notification1.getId(), notification1.getTitle(), notification1.getMessage(),
                    notification1.getEvent());
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);

            double finalX = Screen.getPrimary().getVisualBounds().getMaxX()
                    - WIDTH
                    - 10;

            double finalY = Screen.getPrimary().getVisualBounds().getMaxY()
                    - HEIGHT
                    - 40;
            stage.setX(finalX);

            final double gap = 5;
            int index = NotificationStore.activeNotifications.size() - 1;

            stage.setY(finalY - (index * (HEIGHT + gap)));

            root.setOpacity(0);
            root.setTranslateX(WIDTH + 50);

            stage.show();

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), root);

            slideIn.setFromX(WIDTH + 50);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), root);

            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition showAnimation = new ParallelTransition(slideIn, fadeIn);

            PauseTransition stay = new PauseTransition(Duration.seconds(4));

            TranslateTransition slideOut = new TranslateTransition(Duration.millis(400), root);

            slideOut.setFromX(0);
            slideOut.setToX(WIDTH + 50);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);

            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ParallelTransition hideAnimation = new ParallelTransition(slideOut, fadeOut);

            hideAnimation.setOnFinished(e -> {
                stage.close();
                NotificationStore.activeNotifications.pollFirst();
            });

            SequentialTransition sequence = new SequentialTransition(
                    showAnimation,
                    stay,
                    hideAnimation);

            sequence.play();

            // Final visible position
            stage.setX(Screen.getScreens().get(0).getBounds().getMaxX() - WIDTH - 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
