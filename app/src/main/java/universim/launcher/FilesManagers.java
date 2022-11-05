package universim.launcher;

import java.io.File;

/*
 * manage files on client's disk.
 */
public class FilesManagers {
    private static File getGameDir(String folderName) {
        String os = System.getProperty("os.name").toLowerCase();
        File res;
        if (os.contains("win")) res = new File(System.getenv("appdata") + "/." + folderName);
        else if (os.contains("mac")) res = new File(System.getProperty("user.home") + "/Library/Application Support/" + folderName);
        else res = new File(System.getProperty("user.home") + "/." + folderName);
        return res;
    }
}
