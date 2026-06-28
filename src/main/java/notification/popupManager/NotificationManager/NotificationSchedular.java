package notification.popupManager.NotificationManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import notification.DB.Storage.StorageService;
import notification.Exception.exception;
import java.util.logging.Logger;
public class NotificationSchedular {
    private static final Logger logger = Logger.getLogger(NotificationSchedular.class.getName());
    private static final ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor();

    private static ScheduledFuture<?> pendingTask;

    public static void schduledsave() {
        if (pendingTask != null && !pendingTask.isDone()) {
            pendingTask.cancel(false);
        }
        StorageService storageService = new notification.DB.Storage.Cache.dataimple();
        pendingTask = scheduledExecutorService.schedule(() -> {
            try {
                logger.info("Saving notifications to file...");
                storageService.saveNotification(NotificationStore.getAll());
            } catch (exception e) {
                logger.severe("Error occurred while saving notifications: " + e.getMessage());
            }
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
    }
}
