package universim.launcher;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import universim.launcher.SceneController.SceneType;
import universim.launcher.ui.ChangelogPage;
import universim.launcher.ui.MainPage;
import fr.flowarg.flowlogger.ILogger;
import fr.litarvan.openauth.microsoft.AuthTokens;

public class Launcher extends Application {
    private String m_serverName = "Universim";
    private String m_gameVersion = "1.19.3";
    private String m_forgeVersion = "44.1.23";
    private String m_optifineVersion = "OptiFine_1.19.3_HD_U_I3";
    private String m_version = "0.2.0 beta";

    /* minecraft session */
    private GameSession m_session = new GameSession(m_gameVersion, m_forgeVersion);
    private FilesManager m_filesManager = new FilesManager(this);
    /* renderer */
    private SceneController m_sceneController;

    public static final ILogger logger = new fr.flowarg.flowlogger.Logger("Universim", FilesManager.getLogPath(), true);

    /* getters */
    public String version() { return m_version; }
    public String gameVersion() { return m_gameVersion; }
    public String serverName() { return m_serverName; }
    public String userName() { return m_session.playerUsername(); }

    @Override
    public void start(Stage stage) {
        logWithToken();
        m_sceneController = new SceneController(stage);
        // load and set callbacks
        MainPage mainPage = new MainPage(this);
        if (!mainPage.m_isCorrectlyInit) {
            return;
        }
        m_sceneController.registerScene(SceneType.MAIN, mainPage);
        // TODO: this will change really soon.
        ChangelogPage changelogPage = new ChangelogPage(this);
        changelogPage.setCallBack("back", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                m_sceneController.changeScene(SceneType.MAIN);
            }
        });
        m_sceneController.registerScene(SceneType.CHANGELOG, changelogPage);
        logger.debug("All scenes are register");
        m_sceneController.render();
        stage.setTitle(getLauncherTitle());
        stage.getIcons().add(getLogo());
        logger.debug("Opening the Launcher");
    }

    public void launch(int ramValue) {
        if (!m_session.isAuth()) {
            ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Veuillez entrer vos identifiants.");
            return;
        }
        logger.debug("Checking local files");
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Verification des fichiers locaux ...");
        if (!m_filesManager.checkUpdate(m_forgeVersion, m_optifineVersion)) {
            ErrorManager.errorMessage("Impossible de verifier l'integrite des fichiers locaux.");
            ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("");
            return;
        }
        ((MainPage)m_sceneController.getCurrentScene()).setInfoMessage("Lancement du client");
        try {
            logger.debug("Launching Minecraft");
            m_session.launch(ramValue);
        } catch (Exception e) {
            ErrorManager.errorMessage(e);
        }
        Utils.sleep(5);
        logger.debug("Quitting launcher");
        m_sceneController.quit();
    }

    public Optional<String> login(boolean savedCredentials) {
        AuthTokens tokens = m_session.auth();
        if (tokens == null) {
            return Optional.of("Impossible de s'authentifiez, verifiez vos identifiants.");
        } else if (savedCredentials) {
            m_filesManager.save("refreshToken", tokens.getRefreshToken());
        }
        return Optional.empty();
    }

    public void disconnect() {
        m_session.disconnect();
        m_filesManager.remove("refreshToken");
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

    public void save(String key, String value) {
        m_filesManager.save(key, value);
    }

    public String retrieve(String key) {
        return m_filesManager.retrieve(key);
    }

    public boolean isAuth() {
        return m_session.isAuth();
    }

    private void logWithToken() {
        String refreshToken = m_filesManager.retrieve("refreshToken");
        if (refreshToken != null) {
            AuthTokens tokens = m_session.refreshToken(refreshToken);
            m_filesManager.save("refreshToken", tokens.getRefreshToken());
        }
    }

    private String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }

    private Image getLogo() {
        return new Image("/images/universim_rounded_logo_32px.png");
    }

    public static void main(String[] args) {
        // launcher
        launch(args);
    }

}