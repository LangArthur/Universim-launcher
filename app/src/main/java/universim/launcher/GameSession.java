package universim.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import fr.flowarg.openlauncherlib.NoFramework;
import fr.flowarg.openlauncherlib.NoFramework.ModLoader;
import fr.litarvan.openauth.microsoft.AuthTokens;
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
    /* if the user is currently authenticate */
    private boolean m_isAuth = false;
    private MicrosoftAuthenticator m_authenticator = new MicrosoftAuthenticator();


    public GameSession(String serverVersion, String forgeVersion) {
        m_serverVersion = serverVersion;
        m_forgeVersion = forgeVersion;
    }

    public boolean isAuth() {
        return m_isAuth;
    }

    public String playerUsername() {
        return m_authInfos.getUsername();
    }

    public AuthTokens auth(String username, String pwd) {
        // use microsoft authenticator since each account should be migrated
        MicrosoftAuthResult rep;
        try {
            rep = m_authenticator.loginWithCredentials(username, pwd);
            m_authInfos = new AuthInfos(rep.getProfile().getName(), rep.getAccessToken(), rep.getProfile().getId());
        } catch (Exception e) {
            return null;
        }
        m_isAuth = true;
        return new AuthTokens(rep.getAccessToken(), rep.getRefreshToken());
    }

    public void authWithToken(String accessToken, String refreshToken) {
        AuthTokens tokens = new AuthTokens(accessToken, refreshToken);
        try {
            m_authenticator.loginWithTokens(tokens);
        } catch (Exception e) {
            m_isAuth = true;
        }
        m_isAuth = false;
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
            Launcher.logger.err(e.getMessage());
            Launcher.logger.err(e.getStackTrace().toString());
        }
    }

    private String formatRamArgument(int ramValue) {
        return "-Xmx" + String.valueOf(ramValue) + "G";
    }

    public static String[] getRamValue() {
        int[] possibleValues = {1, 2, 4, 8, 16, 32, 64, 128};
        ArrayList<String> availableRamValues = new ArrayList<String>();
        long memoryAvailable = 64l; /* in GB */
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean)osBean;
            memoryAvailable = sunOsBean.getTotalPhysicalMemorySize() / 1000000000;
        } catch (Exception e) {
            Launcher.logger.warn("sun's jvm OSMXBean is not available, using default value for ram");
        }
        for (int memoryValue : possibleValues) {
            if (memoryValue <= memoryAvailable) {
                availableRamValues.add(String.valueOf(memoryValue));
            }
        }
        return availableRamValues.toArray(new String[availableRamValues.size()]);
    }
}
