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
public class Teacher {

  private final DBI db;
  private final String name;
  private final String firstName;
  private String lastName = null;
  private final String nif;
  private String phone = null;
  private CalendarComposition calendar;

  // no s'escriu a la db, es per gestionar internament
  private ArrayList<Subject> subjects = null;

  public Teacher(final DBI db, String name, String firstname, String lastname, String nif, String phone) {
    this.db = db;
    this.name = name;
    this.firstName = firstname;
    this.lastName = lastname;
    this.nif = nif;
    this.phone = phone;
  }

  public Teacher(final DBI db, String name, String firstname, String nif, String phone, java.util.LinkedList<Subject> subjects) {
    this.db = db;
    this.name = name;
    this.firstName = firstname;
    this.nif = nif;
    this.phone = phone;
    this.subjects = new ArrayList<>(subjects.size());
    this.subjects.addAll(subjects);
  }

  public Teacher(final DBI db, String name, String firstname, String lastname, String nif, String phone, Calendar calendar, java.util.LinkedList<Subject> subjects) {
    this.db = db;
    this.name = name;
    this.firstName = firstname;
    this.lastName = lastname;
    this.nif = nif;
    this.phone = phone;
    this.calendar = new CalendarComposition(calendar);
    this.subjects = new ArrayList<>(subjects.size());
    this.subjects.addAll(subjects);
  }

  public String getName() {
    return name;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getNif() {
    return nif;
  }

  public String getPhone() {
    return phone;
  }

  public CalendarComposition getCalendar() throws DBException {
    if (calendar == null) {
      ArrayList<Calendar> c = db.loadTeacherCalendar(this.nif);
      if (c != null) {
        this.calendar = new CalendarComposition(c);
      }
    }
    return calendar;
  }

  public java.util.Iterator<Subject> getSubjects() throws DBException {
    if (this.subjects == null) {
      this.subjects = db.loadTeacherSubjects(this.nif);
    }
    // no hauria de ser necessari...
    return (this.subjects == null) ? null : this.subjects.iterator();
  }

  public void setLastName(String lastName) {
    if (lastName != null && !lastName.equals("")) {
      this.lastName = lastName;
    } else {
      Utils.Exceptions.IException.print("Teacher.java", "setLastName", "Estam intentant guardar un llinatge nul o buit");
    }
  }

  public void setPhone(String phone) {
    if (phone != null && !phone.equals("")) {
      this.phone = phone;
    } else {
      Utils.Exceptions.IException.print("Teacher.java", "setPhone", "Estam intentant guardar un teléfon nul o buit");
    }
  }

  public void setCalendar(Calendar calendar) {
    if (calendar != null) {
      this.calendar = new CalendarComposition(calendar);
    } else {
      Utils.Exceptions.IException.print("Teacher.java", "setCalendar", "Estam intentant guardar un calendari nul");
    }
  }

  public void setCalendar(CalendarComposition calendar) {
    if (calendar != null) {
      this.calendar = new CalendarComposition(calendar.getCalElem());
    } else {
      Utils.Exceptions.IException.print("Teacher.java", "setCalendar", "Estam intentant guardar un calendari nul");
    }
  }

  public boolean hasLastName() {
    return this.lastName != null;
  }

  public boolean hasPhone() {
    return this.phone != null;
  }

  public boolean hasSubjects() {
    return this.subjects != null && !this.subjects.isEmpty();
  }

  @Override
  public String toString() {
    return "Teacher{" + "name=" + name + ", nif=" + nif + ", firstname=" + firstName + ", lastname=" + lastName + ", phone=" + phone + ", calendar=" + calendar + '}';
  }
}
