package net.vkolev.commands;

import net.vkolev.handlers.FileDatabase;
import net.vkolev.utils.Utils;

import java.util.HashMap;

/**
 * Created by vlado on 08.12.15.
 */
public class Lister {

    private int currentMonth;
    private String filePath;
    private static final FileDatabase fdb = FileDatabase.getInstance();

    public Lister(int month) {
        currentMonth = month;
        filePath = Utils.getInstance().getFileForMonth(month);
    }

    public void getMonthList() {

        HashMap<Integer, String[]> map = fdb.getDateList(filePath);


    }
}
