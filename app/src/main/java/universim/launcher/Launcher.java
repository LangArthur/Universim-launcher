package universim.launcher;

import fr.flowarg.openlauncherlib.IForgeArgumentsProvider;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;

public class Launcher {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Launcher().getGreeting());
    }
}
