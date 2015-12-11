package net.vkolev;

import net.vkolev.commands.Incomer;
import net.vkolev.commands.Lister;
import net.vkolev.commands.Starter;
import net.vkolev.commands.Stopper;
import net.vkolev.utils.Utils;
import org.fusesource.jansi.AnsiConsole;

import java.util.Calendar;
import java.util.Date;

import static org.fusesource.jansi.Ansi.ansi;


/**
 * workTimer is an app to keep track of your working hours.
 * Currently the app can be used to start and stop a working day,
 * calculate the income and list the worked days for a month.
 */
class App
{

    private static final String VERSION_NUMBER = "v1.0";

    public static void main( String[] args )
    {

        AnsiConsole.systemInstall();

        Utils utils = Utils.getInstance();


        System.out.println(ansi().render("@|blue,bold \t\t\tWorkTime " + VERSION_NUMBER + " |@"));
        System.out.println(ansi().render("@|blue,bold ================================================================== |@"));
        System.out.println(ansi().render("@|italic,cyan Current file: |@ @|italic,white " + utils.getCurrentFile() + "|@ \n"));
        if (args.length == 1) {
            /*
            Work with the one word arguments like start|stop|list
             */
            try {
                String command = args[0].toString();
                Date date = new Date();
                switch (command) {
                    case "start":
                        Starter starter = new Starter(utils.getCurrentFile(), new Date());
                        if(starter.startDay()) {
                            System.out.println(ansi().render("@|bold,green Starting at: |@") + date.toString() + "\n");
                        }
                        break;
                    case "stop":
                        Stopper stopper = new Stopper(utils.getCurrentFile(), new Date());
                        if(stopper.stopDay()) {
                            System.out.println(ansi().render("@|bold,green Stopped at: |@") + date.toString() + "\n");
                        }
                        break;
                    case "list":
                        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                        Lister lister = new Lister(month);
                        lister.getMonthList();
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
                System.out.println("An Exception occur!" + e.getLocalizedMessage());
            }
        }
        if (args.length > 1 && args.length <= 3) {
            /*
            Work with the multi-argument commands like list|income
             */
            String command = args[0];
            switch (command) {
                case "list":
                    try {
                        int monthNumb = Integer.parseInt(args[1]);
                        if (monthNumb < 1 || monthNumb > 12) {
                            System.out.println(ansi().render("@|bold,red [ERROR]: |@ Month can be only a number between @|blue 1 |@ and @|blue 12 |@"));
                        } else {
                            String filemonth = utils.getFileForMonth(monthNumb);
                            if(utils.checkFileExists(filemonth)) {
                                Lister lister = new Lister(monthNumb);
                                lister.getMonthList();
                            } else {
                                System.out.println("@|bold,yellow [NOTICE] |@ There is no data for this month!");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(ansi().render("@|bold,red [ERROR] |@ Invalid input for month! Accepted is only a number between 1 and 12"));
                    }
                    break;
                case "income":
                    if (args.length != 3) {
                        System.out.println("You must specify income rate!");
                    } else {
                        String selectedDate = args[1];
                        Double rate = Double.valueOf(args[2].toString());
                        Incomer incomer = new Incomer(selectedDate, rate);
                        incomer.calculateIncome();
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
