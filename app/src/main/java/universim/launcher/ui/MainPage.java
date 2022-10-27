package universim.launcher.ui;

import universim.launcher.ui.IPage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MainPage implements IPage {

    private Button m_playButton;
    private StackPane m_root;
    private TextField m_userName;
    private TextField m_pwd;

    private double m_width = 640;
    private double m_height = 480;

    public MainPage() {
        GridPane credentialsGrid = new GridPane();
        credentialsGrid.setPadding(new Insets(10, 5, 10, 5));
        credentialsGrid.setVgap(5);
        credentialsGrid.setHgap(5);
        m_userName = new TextField();
        m_userName.setPromptText("Identifiant");
        m_pwd = new TextField();
        m_pwd.setPromptText("Mot de passe");
        GridPane.setConstraints(m_userName, 0, 0);
        GridPane.setConstraints(m_pwd, 0, 1);
        credentialsGrid.getChildren().add(m_userName);
        credentialsGrid.getChildren().add(m_pwd);
        m_playButton = new Button("Jouer !");
        GridPane.setConstraints(m_playButton, 0, 2);
        credentialsGrid.getChildren().add(m_playButton);
        m_root = new StackPane(
            credentialsGrid
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
