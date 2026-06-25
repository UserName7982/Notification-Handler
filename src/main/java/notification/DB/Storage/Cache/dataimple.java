package notification.DB.Storage.Cache;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import notification.DB.Storage.StorageService;
import notification.Exception.exception;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import notification.popupManager.Pojo.Notification;

public class dataimple implements StorageService {
    public String File = "notification.json";
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void saveNotification(List<Notification> notifications) throws exception {
        try {
            mapper.writeValue(new File(File), notifications);
        } catch (JacksonException as) {
            throw new exception("ERROR: Unable to save data to file in saveData", as);
        } catch (IOException e) {
            throw new exception("ERROR: Unable to save data to file in savedata", e);
        }
    }

    @Override
    public List<Notification> loadNotifications() throws exception {
        try {
            File file = new File(File);
            if (!file.exists()) {
                System.out.println("File does not exist");
                return new ArrayList<Notification>();
            }
            return mapper.readValue(new File(File), new TypeReference<List<Notification>>() {
            });
        } catch (Exception e) {
            throw new exception("ERROR: Unable to load data from file in savedata", e);
        }
    }

    @Override
    public boolean deleteNotification(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteNotification'");
    }
}
