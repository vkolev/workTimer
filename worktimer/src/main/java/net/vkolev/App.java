package net.vkolev;

import java.util.Date;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        AnsiConsole.systemInstall();

        System.out.println(ansi().render("@|blue,bold \t\tWorkTime v.1.0 |@"));
        System.out.println(ansi().render("@|blue,bold ============================================= |@ \n"));
        if (args.length == 1) {
            try {
                String command = args[0].toString();
                Date date = new Date();
                switch (command) {
                    case "start":
                        System.out.println(ansi().render("@|green Starting at: |@") + date.toString());
                        break;
                    case "stop":
                        System.out.println(ansi().render("@|red Stopped at: |@") + date.toString());
                        break;
                    default:
                        System.out.println("Unknown command!");
                }
            } catch (Exception e) {
                System.out.println("An Exception accured!" + e.getLocalizedMessage());
            }
        }
        if (args.length > 1 && args.length <= 3) {
            System.out.println("Command with options:");
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i] + " | ");
            }
        }

        AnsiConsole.systemUninstall();
    }
}
