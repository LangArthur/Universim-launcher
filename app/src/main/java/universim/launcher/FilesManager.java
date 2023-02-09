package universim.launcher;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.FlowUpdater.FlowUpdaterBuilder;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.OptiFineInfo;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.utils.UpdaterOptions.UpdaterOptionsBuilder;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VanillaVersion.VanillaVersionBuilder;
import fr.theshark34.openlauncherlib.util.Saver;

/*
 * manage files on client's disk.
 */
public class FilesManager {
    private Launcher m_launcher;
    public Saver saver;
    
    // TODO: encapsulate this correctly
    public static String FORGE_VERSION = "36.2.34";
    public static String OPTIFINE_VERSION = "1.16.5_HD_U_G8";
    public static String FOLDER_NAME = ".universim";
    public static String SETTING_FILE_NAME = "optionsLauncher.txt";
    public static String LOG_FILE = "universim.log";

    public FilesManager(Launcher launcher) {
        // create launcher folder
        try {
            new File(getGameDir(FOLDER_NAME).toString()).mkdirs();
            saver = new Saver(Paths.get(getGameDir(FOLDER_NAME).toString(), "/", SETTING_FILE_NAME));
        } catch (Exception e) {
            System.err.println(e);
        }
        m_launcher = launcher;
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

    public static Path getLogPath() {
        Path logPath = Paths.get(getGameDir(FOLDER_NAME).toString(), "/logs/", LOG_FILE);
        return logPath;
    }

    private static void createFile(Path file) {
        try
        {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        } catch (Throwable e)
        {
            throw new RuntimeException("Cannot create " + file.toString() + " file.");
        }
    }

    public boolean checkUpdate() {
        VanillaVersion version = new VanillaVersionBuilder().withName(m_launcher.gameVersion()).build();
        UpdaterOptions options = new UpdaterOptionsBuilder().build();
        AbstractForgeVersion forgeVersion = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
            .withForgeVersion(FORGE_VERSION)
            .withOptiFine(new OptiFineInfo(OPTIFINE_VERSION))
            .build();
        FlowUpdater updater = new FlowUpdaterBuilder()
            .withVanillaVersion(version)
            .withUpdaterOptions(options)
            .withModLoaderVersion(forgeVersion)
            .withProgressCallback(new IProgressCallback() {
                @Override
                public void update(DownloadList.DownloadInfo info) {
                    m_launcher.setMessage("Telechargement des assets du jeu : [" + info.getDownloadedFiles() + '/' + info.getTotalToDownloadFiles() + ']');
                    float percent = BigDecimal
                        .valueOf(((float)info.getDownloadedFiles() / (float)info.getTotalToDownloadFiles()))
                        .setScale(2, RoundingMode.HALF_UP)
                        .floatValue();
                    m_launcher.setProgressBar(percent);
                }

                @Override
                public void step(Step step) {
                    switch (step) {
                        case READ:
                            m_launcher.setMessage("Lecture de la configuration");
                            break;
                        case DL_LIBS:
                            m_launcher.setMessage("Telechargement des librairies");
                        case DL_ASSETS:
                            m_launcher.setMessage("Telechargement des assets du jeu");
                        case EXTRACT_NATIVES:
                            m_launcher.setMessage("Extraction des fichiers");
                        case MOD_LOADER:
                            m_launcher.setMessage("Telechargement du mod loader");
                        case MODS:
                            m_launcher.setMessage("Telechargement des mods");
                        case EXTERNAL_FILES:
                            m_launcher.setMessage("Telechargement de fichiers externes");
                        default:
                            break;
                    }
                }
            })
            .build();
        try {
            updater.update(getGameDir(FOLDER_NAME));
        } catch (Exception e) {
            ErrorManager.errorMessage(e);
            Launcher.logger.error(e.getMessage());
            Launcher.logger.error(e.getStackTrace());
            return false;
        }
        m_launcher.setMessage("Installation terminee avec succes !");
        return true;
    }
}
