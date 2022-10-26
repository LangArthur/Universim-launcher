package universim.launcher.ui;

import universim.launcher.ui.IPage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainPage implements IPage {

    private Button m_playButton;
    private StackPane m_root;
    private Label m_userNameLabel;
    private TextField m_userName = new TextField();
    private Label m_pwdLabel;
    private TextField m_pwd = new TextField();

    private double m_width = 640;
    private double m_height = 480;

    public MainPage() {
        m_userNameLabel = new Label("Identifiant");
        HBox usernameBox = new HBox();
        usernameBox.getChildren().addAll(m_userNameLabel, m_userName);
        usernameBox.setSpacing(10);
        m_pwdLabel = new Label("Mot de passe");
        m_playButton = new Button("Jouer !");
        m_root = new StackPane(
            usernameBox,
            m_playButton
        );
    }

    public Scene getScene() {
        return new Scene(m_root, m_width, m_height);
    }

    public void setCallBack(String callBackName, EventHandler<ActionEvent> callBack) {
        if (callBackName == "play") {
            m_playButton.setOnAction(callBack);
        }
    }

}
