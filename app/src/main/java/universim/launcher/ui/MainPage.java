package universim.launcher.ui;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Pair;
import universim.launcher.ErrorManager;
import universim.launcher.GameSession;
import universim.launcher.Launcher;

public class MainPage extends APage {

    private Button m_playButton;
    private Parent m_root;
    private TextField m_userName;
    private PasswordField m_pwd;
    private Text m_loginInfo;
    private ComboBox<String> m_ramSelector;
    private ProgressBar m_progressBar;
    private int m_ram = 1;

    private double m_width = 960;
    private double m_height = 720;

    public MainPage(Launcher launcher) {
        super(launcher);
        try {
            m_root = FXMLLoader.load(getClass().getResource("/xml/login.xml"));
        } catch (Exception e) {
            Launcher.logger.error(e.getMessage());
            Launcher.logger.error(e.getStackTrace());
            ErrorManager.errorMessage("Impossible de charger la scene.");
        }
        m_isCorrectlyInit = true;
        storeUiElements();
        registerCallBacks();
        m_scene = new Scene(m_root, m_width, m_height);
        String buttonCss = getClass().getResource("/css/button.css").toExternalForm();
        String panelCss = getClass().getResource("/css/panel.css").toExternalForm();
        String commonCss = getClass().getResource("/css/common.css").toExternalForm();
        m_scene.getStylesheets().addAll(buttonCss, panelCss, commonCss);
    }

    public Scene getScene() {
        return m_scene;
    }

    public Pair<String, String> getCredentials() {
        return new Pair<String,String>(m_userName.getText(), m_pwd.getText());
    }

    public void setInfoMessage(String msg) {
        m_loginInfo.setText(msg);
    }

    public void setProgress(float progress) {
        m_progressBar.setProgress(progress);
    }

    public void bindMessage(ObservableValue<? extends String> observable) {
        m_loginInfo.textProperty().bind(observable);
    }

    public void unbindMessage() {
        m_loginInfo.textProperty().unbind();
    }

    private void registerCallBacks() {
        m_playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Task<Void> launchTask = new Task<Void>() {
                    @Override public Void call() throws InterruptedException {
                        playCallBack();
                        return null;
                    }
                };
                Thread launchThread = new Thread(launchTask);
                launchThread.start();
            }
        });
        m_ramSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                m_ram = Integer.parseInt(newValue);
            }
        });
    }

    private void playCallBack() {
        setInfoMessage("Authentification en cours ...");
        Pair<String, String> credentials = getCredentials();
        boolean login = false;
        try {
            m_launcher.login(credentials.getKey(), credentials.getValue());
            login = true;
        } catch (Exception e) {
            setInfoMessage(e.getMessage());
        }
        if (login) {
            setInfoMessage("Authentification reussie");
            m_launcher.launch(m_ram);
        }
    }

    private void storeUiElements() {
        m_userName = (TextField) m_root.lookup("#username");
        m_pwd = (PasswordField) m_root.lookup("#password");
        m_playButton = (Button) m_root.lookup("#launch");
        m_loginInfo = (Text) m_root.lookup("#login-info");
        m_ramSelector = (ComboBox<String>) m_root.lookup("#ram-selector");
        m_ramSelector.getItems().setAll(GameSession.getRamValue());
        m_ramSelector.setValue("1");
        m_progressBar = (ProgressBar) m_root.lookup("#launching-status-bar");
        m_progressBar.setProgress(0);
    }
}
