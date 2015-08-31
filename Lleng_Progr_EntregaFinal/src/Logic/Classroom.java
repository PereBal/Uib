/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Utils.Calendar.Calendar;
import Utils.Calendar.CalendarComposition;
import Utils.DB.DBI;
import Utils.Exceptions.DBException;
import java.util.ArrayList;

/**
 *
 * @author PereBalaguer
 */
public class Classroom {

    public static enum ClassroomType {

        A, B, C, D, E
    };

    private final DBI db;
    private final long id;
    private int capacity;
    private ClassroomType type;
    private CalendarComposition calendar;

    public Classroom(final DBI db) {
        this.db = db;
        this.id = Definitions.Defs.nullId;
    }

    public Classroom(final DBI db, int capacity, ClassroomType type) {
        this.db = db;
        this.id = Definitions.Defs.nullId;
        this.capacity = capacity;
        this.type = type;
    }

    public Classroom(final DBI db, long id, int capacity, ClassroomType type) {
        this.db = db;
        this.id = id;
        this.capacity = capacity;
        this.type = type;
    }

    public Classroom(final DBI db, long id, int capacity, ClassroomType type, Calendar calendar) {
        this.db = db;
        this.id = id;
        this.capacity = capacity;
        this.type = type;
        this.calendar = new CalendarComposition(calendar);
    }

    public Classroom(final DBI db, long id, int capacity, ClassroomType type, CalendarComposition calendar) {
        this.db = db;
        this.id = id;
        this.capacity = capacity;
        this.type = type;
        this.calendar = calendar;
    }

    public long getId() {
        if (this.id == Definitions.Defs.nullId) {
            Utils.Exceptions.IException.print("Classroom.java", "getId", "S'està intentant accedir a l'id d'un objecte no llegit de la db");
        }
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public ClassroomType getType() {
        return type;
    }

    public CalendarComposition getCalendar() throws DBException {
        if (calendar == null) {
            ArrayList<Calendar> c = db.loadClassroomCalendar(id);
            if (c != null) {
                this.calendar = new CalendarComposition(c);
            }
        }
        return calendar;
    }

    public void setCapacity(int capacity) {
        if (capacity >= 0) {
            this.capacity = capacity;
        } else {
            Utils.Exceptions.IException.print("Classroom.java", "setCapacity", "S'ha intentat assignar una capacitat negativa a una aula");
        }
    }

    public void setType(ClassroomType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Classroom{" + "id=" + id + ", capacity=" + capacity + ", type=" + type + ", calendar=" + calendar + '}';
    }

    public final Classroom clone(long id) {
        return new Classroom(this.db, id, this.capacity, this.type, this.calendar);
    }

    public static ClassroomType stringToEnum(String classroomType) {
        switch (classroomType) {
            case "A":
                return ClassroomType.A;
            case "B":
                return ClassroomType.B;
            case "C":
                return ClassroomType.C;
            case "D":
                return ClassroomType.D;
        }

        return ClassroomType.E;
    }
}
