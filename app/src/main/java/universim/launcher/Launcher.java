package universim.launcher;

public class Launcher {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Launcher().getGreeting());
    }
}
