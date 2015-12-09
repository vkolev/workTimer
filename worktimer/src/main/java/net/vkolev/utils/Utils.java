package net.vkolev.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Created by vlado on 08.12.15.
 */
public class Utils {

    static final int MINUTES_PER_HOUR = 60;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static Utils instance = null;

    protected Utils() {}

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

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

    private static void checkFile(String file_path) throws IOException {
        File newFile = new File(file_path);
        if (!newFile.exists()) {
            boolean result = newFile.createNewFile();
        }
    }

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

    private static void checkDir(String path) {
        File newPath = new File(path);
        if(!newPath.exists()) {
            newPath.mkdirs();
        }
    }

    public String getDifference(String line, String timeLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm");
        LocalDateTime start = LocalDateTime.parse(line, formatter);
        LocalDateTime stop = LocalDateTime.parse(timeLog, formatter);
        Duration period = Duration.between(start, stop);

        long seconds = period.getSeconds();
        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);

        StringBuilder sb = new StringBuilder();
        sb.append(hours);
        sb.append(":");
        sb.append(String.format("%02d", minutes));

        System.out.println(sb.toString());

        return sb.toString();

    }

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
}
