/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Calendar;

import Definitions.Defs;

/**
 *
 * @author PereBalaguer
 */
public class Day {

    public enum DayOfWeek {

        Monday, Tuesday, Wednesday, Thursday, Friday;
    }

    private final long id;
    private final DayOfWeek name;
    private int startHour;
    private int endHour;

    public Day(DayOfWeek name, int startHour, int endHour) {
        this.id = Defs.nullId;
        this.name = name;
        this.startHour = startHour;
        this.endHour = endHour;

    }

    public Day(long id, DayOfWeek name, int startHour, int endHour) {
        this.id = id;
        this.name = name;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public static int dayOfWeekToNumber(DayOfWeek d) {
        if (d == DayOfWeek.Monday) {
            return 0;
        } else if (d == DayOfWeek.Tuesday) {
            return 1;
        } else if (d == DayOfWeek.Wednesday) {
            return 2;
        } else if (d == DayOfWeek.Thursday) {
            return 3;
        }

        return 4;
    }

    public long getId() {
        return id;
    }

    public DayOfWeek getName() {
        return name;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public Day clone(long id) {
        return new Day(id, this.name, this.startHour, this.endHour);
    }

    @Override
    public String toString() {
        return "Day{" + "name=" + name + ", startHour=" + startHour + ", endHour=" + endHour + '}';
    }

}
