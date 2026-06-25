package notification.DB.Storage;

import java.util.List;

import notification.Exception.exception;
import notification.popupManager.Pojo.Notification;

public interface StorageService {
    void saveNotification(List<Notification> notification) throws exception;

    List<Notification> loadNotifications() throws exception;

    boolean deleteNotification(int id);
}
