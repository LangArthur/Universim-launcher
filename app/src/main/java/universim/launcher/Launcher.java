package universim.launcher;

import java.util.EnumMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import universim.launcher.ui.IPage;
import universim.launcher.ui.MainPage;

public class Launcher extends Application {

    enum SceneType {
        MAIN
    }

    private LauncherData m_launcherData = new LauncherData();
    private EnumMap<SceneType, IPage> m_pages = new EnumMap<SceneType, IPage>(SceneType.class);
    private SceneType m_currentPage = SceneType.MAIN;

    @Override
    public void start(Stage stage) {
        MainPage mainPage = new MainPage();
        mainPage.setCallBack("play", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                playCallBack();
            }
        });
        m_pages.put(SceneType.MAIN, mainPage);
        if (m_pages.containsKey(m_currentPage)) {
            Scene scene = m_pages.get(m_currentPage).getScene();
            stage.setTitle(m_launcherData.getLauncherTitle());
            stage.setScene(scene);
            stage.show();
        } else {
            System.err.println("Error: scene " + m_currentPage + " cannot be found.");
        }
    }

    private void playCallBack() {
        System.out.println(m_launcherData.getLauncherTitle());
    }

    private void updatePage(SceneType newPage) {
        if (m_currentPage != newPage) {
            m_currentPage = newPage;
            Scene scene = m_pages.get(m_currentPage).getScene();
            
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}