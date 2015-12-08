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
                        System.out.println(ansi().render("@|green Stopped at: |@") + date.toString());
                        break;
                    case "list":
                        System.out.println(ansi().render("@|red [ERROR]: |@  You must specify a month when using the list command."));
                        break;
                    case "income":
                        System.out.println(ansi().render("@|red [ERROR]: |@  A month and a rate for using the income command.."));
                        break;
                    default:
                        System.out.println("Unknown command!");
                }
            } catch (Exception e) {
                System.out.println("An Exception accured!" + e.getLocalizedMessage());
            }
        }
        if (args.length > 1 && args.length <= 3) {
            String command = args[0];
            int argsSize = args.length;
            switch (command) {
                case "list":
                    try {
                        int monthNumb = Integer.parseInt(args[1]);
                        if (monthNumb < 1 || monthNumb > 12) {
                            System.out.println(ansi().render("@|red [ERROR]: |@ Month can be only a number between @|blue 1 |@ and @|blue 12 |@"));
                        } else {
                            System.out.println("You should get a list of day worked for month: " + monthNumb);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    break;
                case "income":
                    if (args.length != 3) {
                        System.out.println("You must specify income rate!");
                    } else {
                        System.out.println("Income for month " + args[1] + " in rate " + args[2] + " EUR/month");
                    }
                    break;
                default:
                    System.out.println("Wrong arguments...");
            }
        }

        if (args.length < 1 || args.length > 3) {
            System.out.println("You should see the usage here ...");
        }

        AnsiConsole.systemUninstall();
    }
}
