package MP3;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new Interface();
    }

    public static void main(String[] args) throws InterruptedException {
        launch();
    }
}
