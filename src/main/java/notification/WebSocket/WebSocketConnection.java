package notification.WebSocket;

import java.util.List;
import java.util.concurrent.Executors;
import okhttp3.*;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Platform;
import notification.DB.Storage.StorageService;
import notification.Exception.exception;
import notification.handleMessage.handleMessage;
import notification.popupManager.NotificationManager.NotificationStore;
import notification.popupManager.NotificationManager.NotificationWorker;
import notification.popupManager.Pojo.Notification;
import java.util.logging.Logger;

public class WebSocketConnection {
    private WebSocket webSocket;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private OkHttpClient client;
    private static final Logger logger = Logger.getLogger(WebSocketConnection.class.getName());
    private volatile boolean isConnected = false;
    private String URI = "ws://localhost:8000/notification/ws";
    private int max_retry = 5;
    private handleMessage handler = new handleMessage();
    private StorageService storageService;
    private List<Notification> notifications;
    public static WebSocketConnection instance;

    public WebSocketConnection() {
        client = new OkHttpClient.Builder().pingInterval(10, java.util.concurrent.TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
    }

    public static WebSocketConnection getInstance() {
        if (instance == null) {
            instance = new WebSocketConnection();
        }
        return instance;
    }

    public void connect() {
        Request request = new Request.Builder().url(URI).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(WebSocket ws, Response response) {
                isConnected = true;
                System.out.println("Connected to server" + " " + response.message());
                storageService = new notification.DB.Storage.Cache.dataimple();
                try {
                    notifications = storageService.loadNotifications();
                } catch (exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                NotificationStore.intialize(notifications);
                NotificationWorker.startWorker();
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                logger.info("Received: " + text);
                try {
                    handler.HandleMessage(text);
                } catch (Exception e) {
                    logger.severe("Error occurred while handling message: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(WebSocket ws, Throwable t, Response response) {
                isConnected = false;
                logger.severe("Connection failed: " + t.getMessage());
                try {
                    reconnect();
                } catch (Throwable e) {
                    logger.severe("Error occurred while attempting to reconnect: " + e.getMessage());
                }
            }

            @Override
            public void onClosed(WebSocket ws, int code, String reason) {
                isConnected = false;
                logger.info("Connection closed: " + reason);
            }
        });
    }

    private synchronized void reconnect() throws Throwable {
        if (max_retry <= 0) {
            shutdown();
            logger.info(" Max reconnection attempts reached. Giving up.");
            return;
        }
        max_retry--;
        try {
            logger.info("⏳ Reconnecting in 5 seconds" + " " + "Remaining attempts: " + max_retry);
            scheduledExecutorService.schedule(() -> {
                logger.info(" Reconnecting");
                connect();
                if (isConnected) {
                    max_retry = 20;
                }
            }, 5, java.util.concurrent.TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.severe("Error occurred while attempting to reconnect: " + e.getMessage());
        }
    }

    public void shutdown() {
        isConnected = false;
        try {
            scheduledExecutorService.shutdownNow();
            NotificationWorker.stopWorker();
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();

            if (webSocket != null) {
                webSocket.close(1000, "Shutdown");
            }
            Platform.runLater(() -> {
                logger.info(" Closing JavaFX");
                Platform.exit();
            });
        } catch (Exception e) {
            logger.severe("Error occurred while shutting down: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (isConnected) {
            webSocket.send(message);
            logger.info(" Sent: " + message);
        } else {
            logger.info(" Cannot send, not connected");
        }
    }
}
