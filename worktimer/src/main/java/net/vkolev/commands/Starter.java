package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;

import java.util.Date;

/**
 * Starter command to start a working day
 *
 * @author Vladimir Kolev
 */
public class Starter {

    private Date startDate;
    private String filePath;
    private static final FileDatabase fdb = FileDatabase.getInstance();

    public Starter(String path, Date date) {
        startDate = date;
        filePath = path;
    }

    public boolean startDay() {
        return fdb.setStartDate(filePath, startDate);
    }

}
