package notification.popupManager.NotificationManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

import notification.popupManager.Pojo.Notification;

public class NotificationStore {
    public static Map<String, Notification> notifications;
    public static BlockingDeque<Notification> notificationQueue;
    public static BlockingDeque<Notification> activeNotifications;
    
    public static void intialize(List<Notification> notification) {
        notifications = new ConcurrentHashMap<>();
        notificationQueue = new java.util.concurrent.LinkedBlockingDeque<>();
        activeNotifications = new java.util.concurrent.LinkedBlockingDeque<>();
        for (Notification notify : notification) {
            notifications.put(notify.getId(), notify);
            notificationQueue.offer(notify);
        }
    }

    public static List<Notification> getAll() {
        return notifications.values().stream().toList();
    }

    public static boolean upsert(Notification notification) {
        notifications.put(notification.getId(), notification);
        return true;
    }

    public static boolean remove(String id) {
        if (notifications.containsKey(id)) {
            notifications.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
