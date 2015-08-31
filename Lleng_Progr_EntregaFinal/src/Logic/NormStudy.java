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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PereBalaguer
 */
public class NormStudy extends Study {

  private Course course = null;
  //private Calendar calendar = null;
  private CalendarComposition calendar = null;
  
  private ArrayList<Subject> subjects = null;

  public NormStudy(final DBI db, String name, StudyType type, Course course) {
    super(db, name, type);
    this.calendar = null;
    this.course = course;
    this.subjects = null;
  }

  public NormStudy(final DBI db, String name, StudyType type, Course course, LinkedList<Subject> subjects, Calendar calendar) {
    super(db, name, type);
    this.calendar = new CalendarComposition(calendar);
    this.course = course;
    this.subjects = new ArrayList<>(subjects.size());
    this.subjects.addAll(subjects);
  }

  public NormStudy(final DBI db, long id, String name, StudyType type, Course course) {
    super(db, id, name, type);
    this.course = course;
  }

  public NormStudy(final DBI db, long id, String name, StudyType type, Course course, Calendar calendar) {
    super(db, id, name, type);
    this.calendar = new CalendarComposition(calendar);
    this.course = course;
  }
  
  public NormStudy(final DBI db, long id, String name, StudyType type, Course course, CalendarComposition calendar) {
    super(db, id, name, type);
    this.calendar = calendar; // lo usamos desde la base de datos para actualizar el id, no problema si hacemos esto
    this.course = course;
  }

  public NormStudy(final DBI db, long id, String name, StudyType type, Course course, LinkedList<Subject> subjects, Calendar calendar) {
    super(db, id, name, type);
    this.calendar = new CalendarComposition(calendar);
    this.course = course;
    this.subjects = new ArrayList<>(subjects.size());
    this.subjects.addAll(subjects);
  }

  public java.util.Iterator<Subject> getSubjects() throws DBException {
    if (subjects == null) {
      this.subjects = getDB().loadNormStudySubjects(this.getId());
    } 
    return subjects.iterator();
  }

  public Course getCourse() throws DBException {
    if (course == null) {
      this.course = getDB().loadCourse(this.getId());
    }
    return course;
  }

  public java.util.Date getStartDate() throws DBException {
    if (course == null) {
      this.course = getDB().loadCourse(this.getId());
    }
    return course.getStartDate();
  }

  public java.util.Date getFinishDate() throws DBException {
    if (course == null) {
      this.course = getDB().loadCourse(this.getId());
    }
    return course.getFinishDate();
  }

  public boolean hasSubjects() {
    return this.subjects != null && !this.subjects.isEmpty();
  }

  public final NormStudy clone(long id) {
    NormStudy s = null;
    try {
      if (calendar == null) {
        s = new NormStudy(this.getDB(), id, this.getName(), this.getType(), this.getCourse());
      } else {
        s = new NormStudy(this.getDB(), id, this.getName(), this.getType(), this.getCourse(), this.getCalendar());
      }
      s.subjects = this.subjects;
    } catch (DBException ex) {
      Logger.getLogger(NormStudy.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  @Override
  public CalendarComposition getCalendar() throws DBException {
    if (calendar == null) {
      ArrayList<Calendar> c = getDB().loadNormStudyCalendar(this.getId());
      if (c != null) {
        this.calendar = new CalendarComposition(c);
      }
    }
    return this.calendar;
  }

  @Override
  public String toString() {
    return "NormStudy{" + super.toString() + ", course=" + this.course + ", calendar=" + this.calendar + ", subjects=" + this.subjects + '}';
  }

}
