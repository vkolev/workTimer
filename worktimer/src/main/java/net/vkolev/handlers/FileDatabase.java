package net.vkolev.handlers;

import net.vkolev.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by vlado on 08.12.15.
 */
public class FileDatabase {

    private static FileDatabase instance = null;

    protected FileDatabase() {}

    public static FileDatabase getInstance() {
        if (instance == null) {
            instance = new FileDatabase();
        }
        return instance;
    }

    public boolean setStartDate(String filePath, Date startDate) {
        boolean result = true;
        File workFile = new File(filePath);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(startDate);
        String timeLog = new SimpleDateFormat("dd.MM.yyyy-HH:mm").format(startDate);
        try {
            Scanner scanner = new Scanner(workFile);
            int lineNum = 0;
            boolean isStarted = false;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if(line.startsWith(currentDate)) {
                    System.out.println(ansi().render("@|bold,yellow [NOTICE] |@ You have already started the day!"));
                    isStarted = true;
                    result = false;
                    break;
                }
            }
            if (!isStarted) {
                try {
                    Files.write(workFile.toPath(), timeLog.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    result = false;
                }
            }
            scanner.close();
        } catch (FileNotFoundException fne) {
            result = false;
        }
        return result;
    }

    public boolean setStopDate(String filePath, Date stopDate) {
        boolean result = true;
        File workFile = new File(filePath);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(stopDate);
        String timeLog = new SimpleDateFormat("dd.MM.yyyy-HH:mm").format(stopDate);
        try {
            Scanner scanner = new Scanner(workFile);
            StringBuilder newFile = new StringBuilder();
            int lineNum = 0;
            boolean isStarted = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (!line.startsWith(currentDate)) {
                    newFile.append(line);
                    newFile.append(System.lineSeparator());
                } else {
                    if(line.split(" :: ").length == 1) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(line);
                        sb.append(" :: ");
                        sb.append(timeLog);
                        sb.append(" :: ");
                        sb.append(Utils.getInstance().getDifference(line, timeLog));
                        sb.append(System.lineSeparator());
                        line.replaceAll(line, sb.toString());
                        newFile.append(sb.toString());
                        scanner.close();
                        overWriteFile(filePath, newFile.toString());
                        break;
                    } else {
                        System.out.println(ansi().render("@|bold,yellow [NOTICE] |@ You have already stopped the day!"));
                        result = false;
                    }
                }
            }

        } catch (FileNotFoundException fne) {
            result = false;

        }
        return result;
    }


    public HashMap<Integer, String[]> getDateList(String filePath) {
        HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
        File listFile = new File(filePath);
        try {
            Scanner scanner = new Scanner(listFile);
            int lineNum = 0;
            while(scanner.hasNextLine()) {
                lineNum++;
                String line = scanner.nextLine();
                String[] result = line.split(" :: ");
                map.put(lineNum, result);
            }
        } catch (FileNotFoundException fne) {
            //
        }
        return map;
    }

    private void overWriteFile(String filePath, String newFile) {
        File oldFile = new File(filePath);
        try {
            Files.delete(oldFile.toPath());
            oldFile = new File(filePath);
            oldFile.createNewFile();
            Files.write(oldFile.toPath(), newFile.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer,String[]> getDateList(List<String> files) {
        HashMap<Integer, String[]> map = new HashMap<Integer, String[]>();
        for(int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            try {
                Scanner scanner = new Scanner(file);
                int lineNum = 0;
                while (scanner.hasNextLine()) {
                    lineNum++;
                    String line = scanner.nextLine();
                    String[] result = line.split(" :: ");
                    map.put(lineNum, result);
                }
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }
        }
        return map;
    }
}
