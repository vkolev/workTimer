package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;
import net.vkolev.utils.Utils;

import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by vlado on 08.12.15.
 */
public class Lister {

    private int currentMonth;
    private String filePath;
    private static final FileDatabase fdb = FileDatabase.getInstance();
    private static final Utils utils = Utils.getInstance();

    public Lister(int month) {
        currentMonth = month;
        filePath = Utils.getInstance().getFileForMonth(month);
    }

    public void getMonthList() {

        HashMap<Integer, String[]> list = fdb.getDateList(filePath);
        String format = "| %-17s | %-17s | %-10s |\n";
        String monthString = new DateFormatSymbols().getMonths()[currentMonth - 1];
        System.out.println(ansi().render("Hours for @|bold,green " + monthString + "/" + utils.getCurrentYear() + " |@\n"));
        System.out.println("+----------------------------------------------------+");
        System.out.println("|  Start            |  End              |  Hours     |");
        System.out.println("+----------------------------------------------------+");
        int sumHours = 0;
        int sumMinutes = 0;
        Iterator it = list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String[]> pair = (Map.Entry)it.next();
            sumHours += Integer.parseInt(pair.getValue()[2].split(":")[0]);
            sumMinutes += Integer.parseInt(pair.getValue()[2].split(":")[1]);
            System.out.print(String.format(format, pair.getValue()[0], pair.getValue()[1], pair.getValue()[2]));
        }
        System.out.println("+====================================================+");
        sumHours += sumMinutes / 60;
        sumMinutes = sumMinutes % 60;
        String sum = sumHours + ":" + String.format("%02d", sumMinutes);
        System.out.println("|                                  Sum  |" + String.format(" %-10s |", sum));
        System.out.println("+----------------------------------------------------+\n");
    }
}
