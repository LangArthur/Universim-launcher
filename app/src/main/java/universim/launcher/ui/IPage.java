package universim.launcher.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public interface IPage {
    public abstract Scene getScene();
    public abstract void setCallBack(String callBackName, EventHandler<ActionEvent> callBack); // TODO: check callback type
}
