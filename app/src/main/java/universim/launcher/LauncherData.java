package universim.launcher;

import java.util.LinkedList;

/**
 * "back" of the launcher
 */
public class LauncherData {
    private String m_serverName = "Universim";
    private String m_serverVersion = "1.16.5";
    private String m_version = "0.1.0";
    private GameSession m_session = new GameSession(m_serverName, m_serverVersion);
    private FilesManager m_filesManager = new FilesManager(m_serverVersion);
    private LinkedList<String> m_errorQUeue = new LinkedList<String>();

    public String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }

    public Boolean login(String username, String pwd) {
        if (username.length() == 0 || pwd.length() == 0) {
            m_errorQUeue.push("Remplissez les deux champs");
            return false;
        }
        if (!m_session.auth(username, pwd)) {
            m_errorQUeue.push("Impossible de s'authentifiez, verifiez vos identifiants.");
            return false;
        }
        return true;
    }

    public void launch() {
        System.out.println("checking update ...");
        m_filesManager.checkUpdate();
        m_session.launch();
    }

    public String getError() {
        return m_errorQUeue.pop();
    }
}
