package net.vkolev.utils;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Helper functions as singleton
 *
 * @author Vladimir Kolev
 */
public class Utils {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static Utils instance = null;

    private Utils() {}

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    /**
     * Get's the current year
     * @return the current year as Integer
     */
    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Checks if a current path exists
     * @param path String path to a file
     * @return boolean
     */
    public boolean checkFileExists(String path) {
        File test = new File(path);
        return test.exists();
    }

    /**
     * Gets the current file path
     * @return current file as String
     */
    public static String getCurrentFile() {
        String file_path = getCurrentYearDir();
        if (isWindows()) {
            file_path = file_path + "\\";
        } else {
            file_path = file_path + "/";
        }
        file_path =  file_path + "time_" + getMonthDirString() + ".txt";

        try {
            checkFile(file_path);
        } catch (IOException io) {
            System.out.println(io.getLocalizedMessage());
        }

        return file_path;
    }

    /**
     * Check if a path is existing file and if not - creates the file
     * @param file_path Path to a file as String
     * @throws IOException if file can't be created
     */
    private static void checkFile(String file_path) throws IOException {
        File newFile = new File(file_path);
        if (!newFile.exists()) {
            newFile.createNewFile();
        }
    }

    /**
     * Get the Application directory in the user.home
     * if directory doesn't exists it will be created.
     * @return current path to Application directory as String
     */
    private static String getAppDirectory() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("user.home"));
        if (isWindows()) {
            sb.append("\\AppData\\Local\\workTimer");
        }

        if(isLinux()) {
            sb.append("/.config/workTimer");
        }

        return sb.toString();
    }

    private static String getOsName() {
        return System.getProperty("os.name");
    }

    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    private static boolean isLinux() {
        return getOsName().startsWith("Linux");
    }

    private static String getMonthDirString() {
        StringBuilder sb = new StringBuilder();
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        sb.append(month);
        sb.append(year);

        return sb.toString();
    }

    private static String getCurrentYearDir() {
        String path = getAppDirectory();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        if (isWindows()) {
            path =  path + "\\" + year;
        } else {
            path = path + "/" + year;
        }

        checkDir(path);

        return path;
    }

    private static String getYearDir(int year) {
        String path = getAppDirectory();
        if (isWindows()) {
            path =  path + "\\" + year;
        } else {
            path = path + "/" + year;
        }

        checkDir(path);

        return path;
    }

    private static void checkDir(String path) {
        File newPath = new File(path);
        if(!newPath.exists()) {
            newPath.mkdirs();
        }
    }

    /**
     * Calculates the datetime-difference in a line with startDate :: stopDate
     * The result is in human readable format HH:mm
     * @param line a String of the scanned line
     * @param timeLog the stop Date
     * @return Readable difference in format HH:mm
     */
    public String getDifference(String line, String timeLog) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy-HH:mm");
        LocalDateTime start = LocalDateTime.parse(line, formatter);
        LocalDateTime stop = LocalDateTime.parse(timeLog, formatter);
        Duration period = new Duration(start.toDateTime(), stop.toDateTime());

        long seconds = period.getStandardSeconds();
        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);

        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(":");
        sb.append(String.format("%02d", minutes));

        return sb.toString();

    }

    /**
     * Gets the file for specified month
     * @param month Month as an Integer
     * @return Path to the file
     */
    public String getFileForMonth(int month) {
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentYearDir());
        if (isWindows()) {
            sb.append("\\");
        } else {
            sb.append("/");
        }
        sb.append("time_");
        sb.append(month);
        sb.append(Calendar.getInstance().get(Calendar.YEAR));
        sb.append(".txt");

        return sb.toString();
    }

    /**
     * Gets the file with specified month and year
     * @param month Month as an Integer
     * @param year Year as an Integer
     * @return path to the file
     */
    public String getFileForMonthAndYear(int month, int year) {
        StringBuilder sb = new StringBuilder();
        sb.append(getYearDir(year));
        if (isWindows()) {
            sb.append("\\");
        } else {
            sb.append("/");
        }
        sb.append("time_");
        sb.append(month);
        sb.append(year);
        sb.append(".txt");

        return sb.toString();
    }

    /**
     * Get a list of files for specified year
     * @param year Year as an Integer
     * @return List of all files for that year
     */
    public List<String> getFilesForYear(int year) {
        String path = getYearDir(year);
        List<String> files = new ArrayList<String>();
        File dir = new File(path);
        File[] list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isFile()) {
                files.add(list[i].getAbsolutePath());
            }
        }
        return files;
    }

    /**
     * Calculates the income from date-time string in a list with
     * given income rate
     * @param list List of start-stop-hours
     * @param rate current income rate per hour
     * @return calculated total income for the given list
     */
    public double calculateIncome(HashMap<Integer, String[]> list, Double rate) {
        Iterator it = list.entrySet().iterator();
        int sumHours = 0;
        int sumMinutes = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, String[]> pair = (Map.Entry<Integer, String[]>)it.next();
            if (pair.getValue().length == 3) {
                sumHours += Integer.parseInt(pair.getValue()[2].split(":")[0]);
                sumMinutes += Integer.parseInt(pair.getValue()[2].split(":")[1]);
            }
        }
        double income = 0.0;
        income += sumHours * rate.doubleValue();
        income += ((sumMinutes * 100.0) / 6000.0) * rate.doubleValue();

        return income;

    }

    /**
     * Calculates the income for the current working day
     * @param line line from a start-stop-hour list
     * @param rate rate income per hour
     * @return calculated total income
     */
    public double calculateIncome(String line, Double rate) {
        double income = 0.0;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy-HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(line, formatter);
        LocalDateTime stopDate = new LocalDateTime();
        Duration period = new Duration(startDate.toDateTime(), stopDate.toDateTime());

        long seconds = period.getStandardSeconds();
        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);

        income += hours * rate.doubleValue();
        income += ((minutes * 100.0) / 6000.0) * rate.doubleValue();

        return income;
    }
}
