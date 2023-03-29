package universim.launcher;

import java.util.EnumMap;
import javafx.stage.Stage;
import universim.launcher.ui.IPage;

public class SceneController {
    enum SceneType {
        MAIN, CHANGELOG
    }

    private EnumMap<SceneType, IPage> m_screenMap = new EnumMap<SceneType, IPage>(SceneType.class);
    private SceneType m_currentPage = SceneType.MAIN;
    private Stage m_stage;

    public SceneController(Stage stage) {
        m_stage = stage;
    }

    public void registerScene(SceneType type, IPage scene) {
        m_screenMap.put(type, scene);
    }

    public void removeScene(SceneType type) {
        m_screenMap.remove(type);
    }

    public void changeScene(SceneType type) {
        if (m_currentPage != type) {
            if (m_screenMap.containsKey(type)) {
                m_currentPage = type;
                render();
            } else {
                System.err.println("No scene register as " + type);
            }
        }
    }

    public void render() {
        if (m_screenMap.containsKey(m_currentPage)) {
            m_stage.setScene(m_screenMap.get(m_currentPage).getScene());
            m_stage.show();
        } else {
            System.err.println("No main scene register");
        }
    }

    public IPage getScene(SceneType type) {
        if (m_screenMap.containsKey(type)) {
            return m_screenMap.get(type);
        } else {
            return null;
        }
    }

    public IPage getCurrentScene() {
        if (m_screenMap.containsKey(m_currentPage)) {
            return m_screenMap.get(m_currentPage);
        } else {
            return null;
        }
    }

    public void quit() {
        javafx.application.Platform.runLater(new Runnable() {
            public void run() {
                m_stage.close();
            }
        });
    }
}
