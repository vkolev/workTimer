package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;

import java.util.Date;

/**
 * Stopper command to stop a working day
 *
 * @author Vladimir Kolev
 */
public class Stopper {

    private Date endDate;
    private String filePath;
    private static final FileDatabase fdb = FileDatabase.getInstance();

    public Stopper(String path, Date end) {
        filePath = path;
        endDate = end;
    }

    public boolean stopDay() {
        return fdb.setStopDate(filePath, endDate);
    }
}
