package universim.launcher.ui;

import javafx.scene.Scene;

public interface IPage {
    public abstract Scene getScene();
    public abstract void setCallBack(String callBackName);
}
