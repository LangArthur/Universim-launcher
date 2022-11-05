package universim.launcher;

import java.nio.file.Path;
import java.nio.file.Paths;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.FlowUpdater.FlowUpdaterBuilder;
import fr.flowarg.flowupdater.download.json.OptiFineInfo;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.UpdaterOptions.UpdaterOptionsBuilder;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VanillaVersion.VanillaVersionBuilder;

/*
 * manage files on client's disk.
 */
public class FilesManager {
    private String m_version;

    public static String FORGE_VERSION = "36.2.34";
    public static String OPTIFINE_VERSION = "1.16.5_HD_U_G8";
    public static String FOLDER_NAME = ".universim";

    public FilesManager(String version) {
        m_version = version;
    }

    public static Path getGameDir(String folderName) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
           return Paths.get(System.getenv("appdata"), folderName);
        } else if (os.contains("mac")) {
           return Paths.get(System.getProperty("user.home") , "/Library/Application Support/", folderName);
        } else {
           return Paths.get(System.getProperty("user.home"), "/", folderName);
        }
    }

    public boolean checkUpdate() {
        VanillaVersion version = new VanillaVersionBuilder().withName(m_version).build();
        UpdaterOptions options = new UpdaterOptionsBuilder().build();
        AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
        .withForgeVersion(FORGE_VERSION)
        .withOptiFine(new OptiFineInfo(OPTIFINE_VERSION))
        .build();
        FlowUpdater updater = new FlowUpdaterBuilder()
            .withVanillaVersion(version)
            .withUpdaterOptions(options)
            .withModLoaderVersion(forgeVersion)
            .build();
        // TODO: add custom logger here

        try {
            updater.update(getGameDir(FOLDER_NAME));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
