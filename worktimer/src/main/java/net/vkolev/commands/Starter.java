package net.vkolev.commands;

import java.util.Date;

/**
 * Created by vlado on 08.12.15.
 */
public class Starter {

    private Date startDate;

    public Starter() {
        startDate = new Date();
    }

    public Date startDay() {
        return startDate;
    }

}
