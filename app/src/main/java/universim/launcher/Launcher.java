package universim.launcher;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Launcher extends Application {

    private LauncherData m_launcherData = new LauncherData();

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Button playButton = new Button("Jouer !");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override  
            public void handle(ActionEvent arg0) {
                playCallBack();
            }
        });
        StackPane root = new StackPane(
            l,
            playButton
        );
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle(m_launcherData.getLauncherTitle());
        stage.setScene(scene);
        stage.show();
    }

    private void playCallBack() {
        System.out.println(m_launcherData.getLauncherTitle());
    }

    public static void main(String[] args) {
        launch(args);
    }
}