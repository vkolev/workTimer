package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;

import java.util.Date;

/**
 * Created by vlado on 08.12.15.
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
