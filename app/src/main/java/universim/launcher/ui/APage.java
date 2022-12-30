package universim.launcher.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import universim.launcher.Launcher;

public abstract class APage implements IPage {
    protected Parent m_layout;
    protected Scene m_scene;
    protected Launcher m_launcher;

    public APage(Launcher launcher) {
        m_launcher = launcher;
        try {
            m_layout = FXMLLoader.load(getClass().getResource("xml/base.xml"));            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
