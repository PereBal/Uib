/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Calendar;

import Logic.Study;
import Utils.DB.DBI;
import Utils.Exceptions.DBException;

/**
 *
 * @author pere
 */
public class Calendar {

  private final DBI db;
  private final long id;
  private int hoursDay;
  private int daysWeek;
  private Study.Turn turn;
  private Logic.Subject subject; // per garantir la relacio 1..1 entre calendari i assignatura

  private Day[] days; // basta guardar l'horari de, com a maxim, una setmana.

  public Calendar(final DBI db, int hoursDay, int daysWeek, Study.Turn turn) {
    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.hoursDay = hoursDay;
    this.daysWeek = daysWeek;
    this.turn = turn;
    this.days = new Day[5];
    for (int i = 0; i < 5; i++) {
      this.days[i] = null;
    }
  }

  public Calendar(final DBI db, int hoursDay, int daysWeek, Study.Turn turn, Day[] days) {
    if (days.length > 5 || daysWeek > 5) {
      Utils.Exceptions.IException.print("Calendar.java", "Calendar", "Estam intentant afegir mes de 5 dies, operació innecessària");
    }

    if (hoursDay > 0 && daysWeek == 0) {
      Utils.Exceptions.IException.print("Calendar.java", "Calendar", "Inconsistencia de dades! Reservam hores per dia pero no cap dia!");
    }

    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.hoursDay = hoursDay;
    this.daysWeek = daysWeek;
    this.turn = turn;
    this.days = new Day[5];

    int i;
    for (i = 0; i < daysWeek; i++) {
      if (days[i] == null) {
        break;
      }
      this.days[i] = days[i];
    }

    for (; i < 5; i++) {
      this.days[i] = null;
    }
  }

  public Calendar(final DBI db, long id, int hoursDay, int daysWeek, Study.Turn turn) {
    this.db = db;
    this.id = id;
    this.hoursDay = hoursDay;
    this.daysWeek = daysWeek;
    this.turn = turn;
    this.days = null;
  }

  public Calendar(final DBI db, long id, int hoursDay, int daysWeek, Study.Turn turn, Day[] days) {
    this.db = db;
    this.id = id;
    this.hoursDay = hoursDay;
    this.daysWeek = daysWeek;
    this.turn = turn;
    this.days = new Day[5];

    if (days.length > 5) {
      Utils.Exceptions.IException.print("Calendar.java", "Calendar", "Estam intentant afegir mes de 5 dies, operació innecessària");
    }

    int i;
    for (i = 0; i < days.length && i < 5; i++) {
      if (days[i] == null) {
        break;
      }
      this.days[i] = days[i];
    }

    for (; i < 5; i++) {
      this.days[i] = null;
    }
  }

  public long getId() {
    return id;
  }

  public int getDaysWeek() {
    return daysWeek;
  }

  public Study.Turn getTurn() {
    return turn;
  }

  public Day[] getDays() throws DBException {
    days = db.loadCalDays(id);
    if (days == null) {
      Utils.Exceptions.IException.print("Calendar.java", "getDays()", "El calendari no te dies assignats. Potser a la DB si que en té");
    }
    return days;
  }

  public Day[] getDBDays() {
    return days;
  }

  public int getHoursDay() {
    return hoursDay;
  }

  public Logic.Subject getSubject() throws DBException {
    if (this.subject == null) {
      this.subject = db.loadCalSubject(this);
    }
    return subject;
  }

  public void setHoursDay(int hoursDay) {
    this.hoursDay = hoursDay;
  }

  public void setDaysWeek(int daysWeek) {
    this.daysWeek = daysWeek;
  }

  public void setTurn(Study.Turn turn) {
    this.turn = turn;
  }

  public void setDays(Day[] days) {
    this.days = days;
  }

  public void setSubject(Logic.Subject subject) {
    this.subject = subject;
  }

  public boolean hasDays() {
    return this.days == null;
  }

  public Calendar clone(long id) {
    Calendar c = new Calendar(this.db, id, this.hoursDay, this.daysWeek, this.turn);
    c.days = this.days; // ¿? no hauria de ser privat?XD
    return c;
  }

  @Override
  public String toString() {
    return "Calendar{" + "id=" + id + ", hoursDay=" + hoursDay + ", daysWeek=" + daysWeek + ", turn=" + turn + ", days=" + java.util.Arrays.toString(days) + ", subject=" + this.subject + '}';
  }

}
