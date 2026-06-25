package notification.handleMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import notification.popupManager.NotificationManager.NotificationSchedular;
import notification.popupManager.NotificationManager.NotificationStore;
import notification.popupManager.Pojo.Notification;

public class handleMessage {
    private ObjectMapper mapper = new ObjectMapper();

    public void HandleMessage(String txt) throws Exception {
        Notification Notify;
        try {
            Notify = mapper.readValue(txt, Notification.class);
            if (Notify.getId().isEmpty()|| Notify.getMessage().isEmpty() || Notify.getEvent().isEmpty()) {
                Exception e = new Exception("ERROR: Empty Message");
                throw e;
            }
            NotificationStore.upsert(Notify);
            NotificationStore.notificationQueue.offer(Notify);
            NotificationSchedular.schduledsave();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
