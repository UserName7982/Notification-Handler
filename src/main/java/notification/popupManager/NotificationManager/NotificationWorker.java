package notification.popupManager.NotificationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import notification.popupManager.Pojo.Notification;
import notification.popupManager.UI.NotificationCard;
import java.util.logging.Logger;
public class NotificationWorker {

    private static volatile boolean isRunning = false;
    private static ExecutorService worker;
    private static final Logger logger = Logger.getLogger(NotificationWorker.class.getName());

    public static void startWorker() {

        if (isRunning) {
            return;
        }

        isRunning = true;

        worker = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("Notification-Worker");
            return t;
        });
        logger.info("Notification Worker Started");
        worker.submit(() -> {
            try {
                while (isRunning) {
                    Notification notify = NotificationStore.notificationQueue.poll(500,
                            java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (notify != null) {
                        Platform.runLater(() -> {
                            NotificationCard card = new NotificationCard();
                            card.show(notify);
                        });
                    }
                }
            } catch (Exception e) {
                logger.severe("🛑 Worker interrupted");
                Thread.currentThread().interrupt();
            } finally {
                logger.info("🛑 Worker Thread Exited");
            }
        });
    }

    public static void stopWorker() {
        logger.info("🛑 Stopping Notification Worker");
        isRunning = false;
        if (worker != null) {
            worker.shutdownNow();
        }
    }
}