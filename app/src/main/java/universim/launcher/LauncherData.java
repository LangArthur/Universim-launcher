package universim.launcher;

/**
 * "back" of the launcher
 */
public class LauncherData {
    private String m_serverName = "Universim";
    private String m_serverVersion = "1.16.5";
    private String m_version = "0.1.0";
    
    public String getLauncherTitle() {
        return m_serverName + " launcher " + m_version;
    }
}
