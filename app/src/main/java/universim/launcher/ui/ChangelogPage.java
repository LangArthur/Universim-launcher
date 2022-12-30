package universim.launcher.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import universim.launcher.Launcher;

public class ChangelogPage extends APage {
    private Label m_label;
    private StackPane m_root;
    private Button m_backButton;
    private Scene m_scene;

    private double m_width = 640;
    private double m_height = 480;

    public ChangelogPage(Launcher launcher) {
        super(launcher);
        m_label = new Label("Changelog page");
        m_backButton = new Button("retour");
        m_root = new StackPane(
            m_label,
            m_backButton
        );
        m_scene = new Scene(m_root, m_width, m_height);
    }

    public Scene getScene() {
        return m_scene;
    }

    public void setCallBack(String callBackName, EventHandler<ActionEvent> callBack) {
        if (callBackName == "back") {
            m_backButton.setOnAction(callBack);
        }
    }
}
