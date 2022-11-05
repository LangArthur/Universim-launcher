package universim.launcher;

import fr.flowarg.openlauncherlib.NewForgeVersionDiscriminator;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;

/**
 * minecraft session utilities
 */
public class GameSession {
    private String m_serverName;
    private String m_serverVersion;
    private AuthInfos m_authInfos;
    private boolean m_isAuth = false;

    public GameSession(String serverName, String serverVersion) {
        m_serverName = serverName;
        m_serverVersion = serverVersion;
    }

    public boolean auth(String username, String pwd) {
        // use microsoft authenticator since each account should be migrated
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            MicrosoftAuthResult rep = authenticator.loginWithCredentials(username, pwd);
            m_authInfos = new AuthInfos(rep.getProfile().getName(), rep.getAccessToken(), rep.getProfile().getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        m_isAuth = true;
        return true;
    }

    public void launch() {
        GameType gameType = GameType.V1_13_HIGHER_FORGE;
        try {
            gameType.setNFVD(new NewForgeVersionDiscriminator(FilesManager.getGameDir(FilesManager.FOLDER_NAME), m_serverVersion, FilesManager.FORGE_VERSION));
            GameInfos infos = new GameInfos(
                m_serverName,
                new GameVersion(m_serverVersion, GameType.V1_13_HIGHER_FORGE),
                new GameTweak[] {});
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, m_authInfos);
            ExternalLauncher launcher = new ExternalLauncher(profile);
    
            launcher.launch();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
