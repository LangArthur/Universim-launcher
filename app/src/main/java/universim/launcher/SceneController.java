package universim.launcher;

import java.util.HashMap;

import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import universim.launcher.Launcher.SceneType;

public class SceneController {
    private HashMap<SceneType, Pane> m_screenMap = new HashMap<>();
    private Scene m_main;

    public SceneController(Scene mainScene) {
        m_main = mainScene;
    }
}
