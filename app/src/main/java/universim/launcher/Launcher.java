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
    private String m_serverName = "Universim";
    private String m_serverVersion = "1.16.5";
    private String m_version = "0.1.0";

    private GameSession m_session = new GameSession(m_serverName, m_serverVersion);
    private FilesManager m_filesManager = new FilesManager(m_serverVersion);
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
        stage.setTitle(getLauncherTitle());
    }

    public void launch(int ramValue) {
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Véfification des fichiers locaux ...");
        if (!m_filesManager.checkUpdate()) {
            ErrorManager.errorMessage("Impossible de vérifiez l'intégrité des fichiers locaux.");
            ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("");
            return;
        }
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Lancement du client");
        try {
            m_session.launch(ramValue);
        } catch (Exception e) {
            ErrorManager.errorMessage(e.getMessage());
        }
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("");
    }

    public void login(String username, String pwd) {
        if (username.length() == 0 || pwd.length() == 0) {
            throw new RuntimeException("Remplissez les deux champs");
        }
        if (!m_session.auth(username, pwd)) {
            throw new RuntimeException("Impossible de s'authentifiez, verifiez vos identifiants.");
        }
    }

    public static void main(String[] args) {
        // logger gestion
        // System.setProperty("launcherlog.txt","./launcherlog.txt");
        launch(args);
    }

    private String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }

}