package universim.launcher.ui;

import universim.launcher.ui.IPage;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MainPage extends APage {

    private Button m_playButton;
    private Parent m_root;
    private TextField m_userName;
    private PasswordField m_pwd;

    private double m_width = 640;
    private double m_height = 480;

    public MainPage() {
        try {
            m_root = FXMLLoader.load(getClass().getResource("/xml/login.xml"));
        } catch (Exception e) {
            System.err.println("Error: " + e);
            // TODO: handle exception
        }
        storeUiElements();
        m_scene = new Scene(m_root, m_width, m_height);
    }

    public Scene getScene() {
        return m_scene;
    }

    public void setCallBack(String callBackName, EventHandler<ActionEvent> callBack) {
        if (callBackName == "play") {
            m_playButton.setOnAction(callBack);
        }
    }

    private void storeUiElements() {
        m_userName = (TextField) m_root.lookup("#username");
        m_pwd = (PasswordField) m_root.lookup("#password");
        m_playButton = (Button) m_root.lookup("#launch");
    }
}
