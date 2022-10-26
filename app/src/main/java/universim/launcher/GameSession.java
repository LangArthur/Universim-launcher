package universim.launcher;

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
 * Launch minecraft session
 */
public class GameSession {
    private String m_serverName;
    private String m_serverVersion;

    public GameSession(String serverName, String serverVersion) {
        m_serverName = serverName;
        m_serverVersion = serverVersion;
    }

    public void launch(AuthInfos credentials) {
        GameInfos infos = new GameInfos(
            m_serverName,
            new GameVersion(m_serverVersion, GameType.V1_13_HIGHER_FORGE),
            new GameTweak[] {GameTweak.FORGE});
        try {
            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.BASIC, credentials);
            ExternalLauncher launcher = new ExternalLauncher(profile);
    
            launcher.launch();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
