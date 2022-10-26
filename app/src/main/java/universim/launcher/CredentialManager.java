package universim.launcher;

/**
 * Provide utilities for managing credentials
 */
public class CredentialManager {

    public String getPlayerName() {
        return m_playerName;
    }

    public String getPlayerUuid() {
        return m_playerUuid;
    }

    public String getAccessToken() {
        return m_playerAccessToken;
    }

    /* Player name */
    private String m_playerName;
    /* Player uuid */
    private String m_playerUuid;
    /* Access token */
    private String m_playerAccessToken;    
}
