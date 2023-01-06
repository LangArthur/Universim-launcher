package universim.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import universim.launcher.SceneController.SceneType;
import universim.launcher.ui.ChangelogPage;
import universim.launcher.ui.MainPage;

public class Launcher extends Application {
    private String m_serverName = "Universim";
    private String m_gameVersion = "1.16.5";
    private String m_version = "0.1.0";

    private GameSession m_session = new GameSession(m_serverName, m_gameVersion);
    private FilesManager m_filesManager = new FilesManager(this);
    private SceneController m_sceneController;

    public static final Logger logger = LogManager.getLogger("Launcher");

    String version() { return m_version; }
    String gameVersion() { return m_gameVersion; }
    String serverName() { return m_serverName; }

    @Override
    public void start(Stage stage) {
        m_sceneController = new SceneController(stage);
        // load and set callbacks
        MainPage mainPage = new MainPage(this);
        if (!mainPage.m_isCorrectlyInit) {
            return;
        }
        ChangelogPage changelogPage = new ChangelogPage(this);
        m_sceneController.registerScene(SceneType.MAIN, mainPage);
        changelogPage.setCallBack("back", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                m_sceneController.changeScene(SceneType.MAIN);
            }
        });
        m_sceneController.registerScene(SceneType.CHANGELOG, changelogPage);
        m_sceneController.render();
        stage.setTitle(getLauncherTitle());
        stage.getIcons().add(getLogo());
    }

    public void launch(int ramValue) {
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Véfification des fichiers locaux ...");
        if (!m_filesManager.checkUpdate()) {
            ErrorManager.errorMessage("Impossible de vérifier l'integrité des fichiers locaux.");
            ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("");
            return;
        }
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Lancement du client");
        try {
            m_session.launch(ramValue);
        } catch (Exception e) {
            ErrorManager.errorMessage(e.getMessage());
        }
        Utils.sleep(1);
        m_sceneController.quit();
    }

    public void login(String username, String pwd) {
        if (username.length() == 0 || pwd.length() == 0) {
            throw new RuntimeException("Remplissez les deux champs");
        }
        if (!m_session.auth(username, pwd)) {
            throw new RuntimeException("Impossible de s'authentifiez, verifiez vos identifiants.");
        }
    }

    public void setMessage(String msg) {
        javafx.application.Platform.runLater(new Runnable() {
            public void run() {
                ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage(msg);
            }
        });
    }

    public void setProgressBar(float progress) {
        javafx.application.Platform.runLater(new Runnable() {
            public void run() {
                ((MainPage)m_sceneController.getCurrentScene()).setProgress(progress);
            }
        });
    }

    public static void main(String[] args) {
        // logger gestion
        // System.setProperty("launcherlog.txt","./launcherlog.txt");
        launch(args);
    }

    private String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }

    private Image getLogo() {
        return new Image("/images/universim_rounded_logo_32px.png");
    }

}