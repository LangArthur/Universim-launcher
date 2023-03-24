package universim.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.flowarg.openlauncherlib.NoFramework;
import fr.flowarg.openlauncherlib.NoFramework.ModLoader;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;

/**
 * minecraft session utilities
 */
public class GameSession {
    private String m_serverVersion;
    private String m_forgeVersion;
    private AuthInfos m_authInfos;
    private boolean m_isAuth = false;

    public GameSession(String serverVersion, String forgeVersion) {
        m_serverVersion = serverVersion;
        m_forgeVersion = forgeVersion;
    }

    public boolean auth(String username, String pwd) {
        // use microsoft authenticator since each account should be migrated
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            MicrosoftAuthResult rep = authenticator.loginWithCredentials(username, pwd);
            m_authInfos = new AuthInfos(rep.getProfile().getName(), rep.getAccessToken(), rep.getProfile().getId());
        } catch (Exception e) {
            return false;
        }
        m_isAuth = true;
        return true;
    }

    public void launch(int ramValue) {
        if (!m_isAuth) {
            Launcher.logger.warn("Try to launch the launcher without being logged");
            return;
        }
        List<String> vmArguments = Arrays.asList(formatRamArgument(ramValue));
        NoFramework launcher = new NoFramework(FilesManager.getGameDir(FilesManager.FOLDER_NAME), m_authInfos, GameFolder.FLOW_UPDATER_1_19_SUP, vmArguments, new ArrayList<>());
        try {
            launcher.launch(m_serverVersion, m_forgeVersion, ModLoader.FORGE);
        } catch (Exception e) {
            ErrorManager.errorMessage(e);
            Launcher.logger.error(e.getMessage());
            Launcher.logger.error(e.getStackTrace());
        }
    }

    private String formatRamArgument(int ramValue) {
        return "-Xmx" + String.valueOf(ramValue) + "G";
    }

    public static String[] getRamValue() {
        return new String[] { "1", "2", "4", "8", "16" };
    }
}
