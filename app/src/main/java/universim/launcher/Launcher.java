package universim.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public static final Logger logger = LogManager.getLogger("Launcher");

    @Override
    public void start(Stage stage) {
        m_sceneController = new SceneController(stage);
        // load and set callbacks
        MainPage mainPage = new MainPage(this);
        m_sceneController.registerScene(SceneType.MAIN, mainPage);
        ChangelogPage changelogPage = new ChangelogPage(this);
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

    public void launch(int ramValue) {
        m_launcherData.launch(ramValue);
    }

    public void login(String username, String pwd) {
        m_launcherData.login(username, pwd);
    }

    public static void main(String[] args) {
        // logger gestion
        // System.setProperty("launcherlog.txt","./launcherlog.txt");
        launch(args);
    }
}