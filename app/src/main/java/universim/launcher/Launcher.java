package universim.launcher;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import universim.launcher.SceneController.SceneType;
import universim.launcher.ui.ChangelogPage;
import universim.launcher.ui.MainPage;

public class Launcher extends Application {
    private LauncherData m_launcherData = new LauncherData();
    private SceneController m_sceneController;

    @Override
    public void start(Stage stage) {
        m_sceneController = new SceneController(stage);
        // load and set callbacks
        MainPage mainPage = new MainPage();
        mainPage.setCallBack("play", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                playCallBack();
            }
        });
        m_sceneController.registerScene(SceneType.MAIN, mainPage);
        ChangelogPage changelogPage = new ChangelogPage();
        changelogPage.setCallBack("back", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                m_sceneController.changeScene(SceneType.MAIN);
            }
        });
        m_sceneController.registerScene(SceneType.CHANGELOG, changelogPage);
        m_sceneController.render();
        stage.setTitle(m_launcherData.getLauncherTitle());
    }

    private void playCallBack() {
        m_sceneController.changeScene(SceneType.CHANGELOG);
        // System.out.println(m_launcherData.getLauncherTitle());
    }

    public static void main(String[] args) {
        launch(args);
    }
}