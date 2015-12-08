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

    private static final String VERSION_NUMBER = "v1.0";

    public static void main( String[] args )
    {

        AnsiConsole.systemInstall();

        System.out.println(ansi().render("@|blue,bold \t\tWorkTime " + VERSION_NUMBER + " |@"));
        System.out.println(ansi().render("@|blue,bold ============================================= |@ \n"));
        if (args.length == 1) {
            try {
                String command = args[0].toString();
                Date date = new Date();
                switch (command) {
                    case "start":
                        System.out.println(ansi().render("@|bold,green Starting at: |@") + date.toString() + "\n");
                        break;
                    case "stop":
                        System.out.println(ansi().render("@|bold,green Stopped at: |@") + date.toString() + "\n");
                        break;
                    case "list":
                        System.out.println(ansi().render("@|bold,red [ERROR]: |@  You must specify a month when using the @|bold,white list |@ command."));
                        break;
                    case "income":
                        System.out.println(ansi().render("@|bold,red [ERROR]: |@  A month and a rate for using the @|bold,white income |@ command.."));
                        break;
                    case "help":
                        printUsage();
                        break;
                    default:
                        printUsage();
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
                            System.out.println(ansi().render("@|bold,red [ERROR]: |@ Month can be only a number between @|blue 1 |@ and @|blue 12 |@"));
                        } else {
                            System.out.println("You should get a list of day worked for month: " + monthNumb);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ansi().render("@|bold,red [ERROR] |@ Invalid input for month! Accepted is only a number between 1 and 12"));
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
            printUsage();
        }

        AnsiConsole.systemUninstall();
    }

    private static void printUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("start\t\t\t- Starts a new day/time for the current day of the month\n");
        sb.append("stop\t\t\t- Stops the day/time for the current day of the month\n");
        sb.append("list <month>\t\t- Lists the days you worked for the choosen month\n");
        sb.append("income <month> <rate>\t- Calculates the income for the choosen month\n");
        sb.append("\n");
        sb.append("<month> can only be a number between 1 and 12\n");
        sb.append("<income> can only be a decimal number\n\n");

        System.out.print(sb.toString());

    }
}
