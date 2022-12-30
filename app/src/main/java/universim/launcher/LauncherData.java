package universim.launcher;

/**
 * "back" of the launcher
 */
public class LauncherData {
    private String m_serverName = "Universim";
    private String m_serverVersion = "1.16.5";
    private String m_version = "0.1.0";
    private GameSession m_session = new GameSession(m_serverName, m_serverVersion);
    private FilesManager m_filesManager = new FilesManager(m_serverVersion);

    public String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }

    public void login(String username, String pwd) {
        if (username.length() == 0 || pwd.length() == 0) {
            throw new RuntimeException("Remplissez les deux champs");
        }
        if (!m_session.auth(username, pwd)) {
            throw new RuntimeException("Impossible de s'authentifiez, verifiez vos identifiants.");
        }
    }

    public void launch(int ramValue) {
        System.out.println("checking update ...");
        m_filesManager.checkUpdate();
        m_session.launch(ramValue);
    }
}
