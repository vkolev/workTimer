package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;
import net.vkolev.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by vlado on 08.12.15.
 * Incomer is the class for managing the income.
 */
public class Incomer {

    private static final String MONTH_YEAR = "MM.yyyy";
    private static final String DAY_MONTH_YEAR = "dd.MM.yyyy";
    private static final String YEAR = "yyyy";


    private DateTime dateTime;
    private Double rateIncome;
    private String type;
    private static final FileDatabase fdb = FileDatabase.getInstance();
    private static final Utils utils = Utils.getInstance();

    public Incomer(String selectedDate, Double rate) {
        dateTime = parseDate(selectedDate);
        rateIncome = rate;
    }

    private DateTime parseDate(String selectedDate) {
        DateTime result = null;
        try {
            DateTimeFormatter month_year = DateTimeFormat.forPattern(MONTH_YEAR);
            result = month_year.parseDateTime(selectedDate);
            type = "month";
        } catch (IllegalArgumentException ie) {
            //ie.printStackTrace();
        }
         try {
             DateTimeFormatter day_month_year = DateTimeFormat.forPattern(DAY_MONTH_YEAR);
             result = day_month_year.parseDateTime(selectedDate);
             type = "day";
         } catch (IllegalArgumentException nie) {
             //nie.printStackTrace();
         }
        try {
            DateTimeFormatter year = DateTimeFormat.forPattern(YEAR);
            result = year.parseDateTime(selectedDate);
            type = "year";
        } catch (IllegalArgumentException yie) {
            //yie.printStackTrace();
        }
        if (result == null) {
            System.out.println(ansi().render("@|bold,red [ERROR] |@ Unsupported DateTime Format supplied!"));
            System.exit(0);
        }
        return result;
    }

    public void calculateIncome() {
        System.out.println("Calculating for " + dateTime.toString() + " at rate " + Double.toString(rateIncome));
        switch(type) {
            case "month":
                String file = utils.getFileForMonthAndYear(dateTime.getMonthOfYear(), dateTime.getYear());
                HashMap<Integer, String[]> list = fdb.getDateList(file);
                double income = utils.calculateIncome(list, rateIncome);
                System.out.println(String.format("\nYour income to date is: %.2f EUR", income));
                break;
            case "year":
                List<String> files = utils.getFilesForYear(dateTime.getYear());
                list = fdb.getDateList(files);
                income = utils.calculateIncome(list, rateIncome);
                System.out.println(String.format("\nYour income to date is: %.2f EUR", income));
                break;
            case "day":
                String currentFile = utils.getFileForMonthAndYear(dateTime.getMonthOfYear(), dateTime.getYear());
                String line = fdb.getCurrentDate(currentFile, dateTime);
                income = utils.calculateIncome(line, rateIncome);
                System.out.println(String.format("\nYour income for now is: %.2f EUR", income));
                break;
        }

    }
}
