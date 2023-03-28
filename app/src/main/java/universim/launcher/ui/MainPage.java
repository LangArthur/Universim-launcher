package universim.launcher.ui;


import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    private CheckBox m_rememberCheckBox;
    private int m_ram = 1;

    private double m_width = 960;
    private double m_height = 720;

    private String m_ramKey = "ram";
    private String m_userNameKey = "userName";
    private String m_rememberKey = "remember-me";
    private boolean m_launchLock = false;

    public MainPage(Launcher launcher) {
        super(launcher);
        try {
            m_root = FXMLLoader.load(getClass().getResource("/xml/login.xml"));
        } catch (Exception e) {
            Launcher.logger.err(e.getMessage());
            Launcher.logger.err(e.getStackTrace().toString());
            ErrorManager.errorMessage("Impossible de charger la scene.");
        }
        m_isCorrectlyInit = true;
        storeUiElements(launcher.isAuth());
        registerCallBacks();
        m_scene = new Scene(m_root, m_width, m_height);
        String buttonCss = getClass().getResource("/css/button.css").toExternalForm();
        String panelCss = getClass().getResource("/css/panel.css").toExternalForm();
        String commonCss = getClass().getResource("/css/common.css").toExternalForm();
        m_scene.getStylesheets().addAll(buttonCss, panelCss, commonCss);
        m_scene.setOnKeyPressed(event -> {
            KeyCode codeString = event.getCode();
            if (!m_launchLock && codeString == KeyCode.ENTER) {
                m_launchLock = true;
                playCallBack();
            }
        });
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
                playCallBack();
            }
        });
        m_ramSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> selected, String oldValue, String newValue) {
                m_ram = Integer.parseInt(newValue);
            }
        });
    }

    private void playCallBack() {
        Task<Void> launchTask = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                setInfoMessage("Authentification en cours ...");
                Boolean shoudBeRemembered = m_rememberCheckBox.isSelected();
                Pair<String, String> credentials = getCredentials();
                Optional<String> errorMsg = m_launcher.login(credentials.getKey(), credentials.getValue(), shoudBeRemembered);
                if (errorMsg.isPresent()) {
                    setInfoMessage(errorMsg.get());
                } else {
                    setInfoMessage("Authentification reussie");
                    // saving some infos for next time
                    if (shoudBeRemembered) {
                        m_launcher.save(m_rememberKey, String.valueOf(shoudBeRemembered));
                        m_launcher.save(m_ramKey, String.valueOf(m_ram));
                        m_launcher.save(m_userNameKey, credentials.getKey());
                    }
                    m_launcher.launch(m_ram);
                }
                m_launchLock = false;
                return null;
            }
        };
        Thread launchThread = new Thread(launchTask);
        launchThread.start();
    }

    private void storeUiElements(boolean alreadyAuth) {
        m_pwd = (PasswordField) m_root.lookup("#password");
        m_userName = (TextField) m_root.lookup("#username");
        String storedUsername = m_launcher.retrieve(m_userNameKey);
        if (storedUsername != null) {
            m_userName.setText(storedUsername);
            m_userName.setFocusTraversable(false);
            m_pwd.setFocusTraversable(true);
        }
        m_playButton = (Button) m_root.lookup("#launch");
        m_loginInfo = (Text) m_root.lookup("#login-info");
        m_ramSelector = (ComboBox<String>) m_root.lookup("#ram-selector");
        m_ramSelector.getItems().setAll(GameSession.getRamValue());
        String storedRam = m_launcher.retrieve(m_ramKey);
        System.out.println(storedRam);
        if (storedRam != null) {
            m_ramSelector.setValue(storedRam);
            m_ram = Integer.parseInt(storedRam);
        } else {
            m_ramSelector.setValue("1");
        }
        m_progressBar = (ProgressBar) m_root.lookup("#launching-status-bar");
        m_progressBar.setProgress(0);
        m_rememberCheckBox = (CheckBox) m_root.lookup("#remember-me");
        Boolean remembered = Boolean.valueOf(m_launcher.retrieve(m_rememberKey));
        if (remembered != null && remembered) {
            m_rememberCheckBox.setSelected(remembered);
        }
    }
}
