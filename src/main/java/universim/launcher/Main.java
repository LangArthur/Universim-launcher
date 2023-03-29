package universim.launcher;

// this class is a trick to be able to compile JavaFX as a fatJar 
// (see https://stackoverflow.com/questions/52569724/javafx-11-create-a-jar-file-with-gradle)
public class Main {
    public static void main(String[] args) {
        Launcher.main(args);
    }
}
