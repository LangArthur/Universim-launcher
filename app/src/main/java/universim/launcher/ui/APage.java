package universim.launcher.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class APage implements IPage {
    protected Parent m_layout;
    protected Scene m_scene;

    public APage() {
        try {
            m_layout = FXMLLoader.load(getClass().getResource("xml/base.xml"));            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
