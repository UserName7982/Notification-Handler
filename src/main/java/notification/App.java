package notification;

import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import notification.WebSocket.WebSocketConnection;
import java.util.logging.Logger;

public class App extends Application {

    private WebSocketConnection connection;
    private static boolean isRunning = false;
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.hide();
        logger.info("Starting JavaFX");
        connection = WebSocketConnection.getInstance();
        connection.connect();
        Platform.setImplicitExit(false);
        isRunning = true;
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (isRunning) {
                try {
                    String input = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(input)) {
                        logger.info("Shuting Down Application.");
                        Platform.exit();
                        break;
                    }
                } catch (Exception e) {
                    logger.severe("Error occurred while reading console input: " + e.getMessage());
                    break;
                }
            }
            Thread.currentThread().interrupt();
            scanner.close();
        });

        consoleThread.setDaemon(true);
        consoleThread.start();
    }

    @Override
    public void stop() {

        logger.info("🛑 stop() called");
        isRunning = false;

        if (connection != null) {
            connection.shutdown();
        }
        logger.info("Exiting application");
        System.exit(0);
    }
}