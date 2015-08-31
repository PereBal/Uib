/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Utils.Calendar.Calendar;
import Utils.DB.DBI;
import Utils.Exceptions.DBException;
import java.util.Date;

/**
 *
 * @author PereBalaguer
 */
public class Subject {

  private final DBI db;
  private final long id;
  private final String name;
  private Calendar calendar = null;
  private Date startDate;
  private Date finishDate;
  private NormStudy study = null;
  private Teacher teacher = null;
  private Classroom classroom = null;

  public Subject(final DBI db, String name, Date startDate, Date finishDate, NormStudy study, Teacher teacher, Calendar calendar) {
    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.study = study;
    this.teacher = teacher;
    this.calendar = calendar;
    if (this.calendar != null) {
      this.calendar.setSubject(this);
    }
  }

  public Subject(final DBI db, String name, Date startDate, Date finishDate, NormStudy study, Classroom classroom, Calendar calendar) {
    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.study = study;
    this.classroom = classroom;
    this.calendar = calendar;
    if (this.calendar != null) {
      this.calendar.setSubject(this);
    }
  }

  public Subject(final DBI db, String name, Date startDate, Date finishDate, NormStudy study, Teacher teacher, Classroom classroom, Calendar calendar) {
    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.study = study;
    this.classroom = classroom;
    this.teacher = teacher;
    this.calendar = calendar;
    if (this.calendar != null) {
      this.calendar.setSubject(this);
    }
  }

  public Subject(final DBI db, long id, String name, Date startDate, Date finishDate) {
    this.db = db;
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
  }

  public Subject(final DBI db, long id, String name, Date startDate, Date finishDate, Calendar calendar) {
    this.db = db;
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.calendar = calendar;
    if (this.calendar != null) {
      this.calendar.setSubject(this);
    }
  }

  public Subject(final DBI db, long id, String name, Date startDate, Date finishDate, NormStudy study, Teacher teacher, Classroom classroom, Calendar calendar) {
    this.db = db;
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.finishDate = finishDate;
    this.study = study;
    this.classroom = classroom;
    this.teacher = teacher;
    this.calendar = calendar;
    if (this.calendar != null) {
      this.calendar.setSubject(this);
    }
  }

  public long getId() {
    if (this.id == Definitions.Defs.nullId) {
      Utils.Exceptions.IException.print("Subject.java", "getId", "S'està intentant accedir a l'id d'un objecte no llegit de la db");
    }
    return id;
  }

  public String getName() {
    return name;
  }

  public int getHoursPerDay() {
    return this.calendar.getHoursDay();
  }

  public int getDaysPerWeek() {
    return this.calendar.getDaysWeek();
  }

  public Study.Turn getTurn() {
    return this.calendar.getTurn();
  }

  public NormStudy getStudy() throws DBException {
    if (study == null) {
      this.study = db.loadSubjectStudy(id);
    }
    return study;
  }

  public Teacher getTeacher() throws DBException {
    if (teacher == null) {
      this.teacher = db.loadSubjectTeacher(id);
    }
    return teacher;
  }

  public Classroom getClassroom() throws DBException {
    if (classroom == null) {
      this.classroom = db.loadSubjectClassroom(id);
    }
    return classroom;
  }

  public java.util.Date getStartDate() {
    return this.startDate;
  }

  public java.util.Date getFinishDate() {
    return this.finishDate;
  }

  public Calendar getCalendar() throws DBException {
    if (calendar == null) {
      this.calendar = db.loadSubjectCalendar(id);
      if (calendar != null) {
        this.calendar.setSubject(this);
      }
    }
    return calendar;
  }

  public void setTeacher(Teacher teacher) {
    if (teacher != null) {
      this.teacher = teacher;
    } else {
      Utils.Exceptions.IException.print("Subject.java", "setTeacher", "Hem intentat afegir un professor nul");
    }
  }

  public void setClassroom(Classroom classroom) {
    if (classroom != null) {
      this.classroom = classroom;
    } else {
      Utils.Exceptions.IException.print("Subject.java", "setClassroom", "Hem intentat afegir una aula nul·la");
    }
  }

  public void setStudy(NormStudy study) {
    if (study != null) {
      this.study = study;
    } else {
      Utils.Exceptions.IException.print("Subject.java", "setStudy", "Hem intentat afegir un estudi nul");
    }
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
    this.calendar.setSubject(this);
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }
  
  public boolean hasTeacher() {
    return teacher != null;
  }

  public boolean hasClassroom() {
    return classroom != null;
  }

  public boolean hasStudy() {
    return study != null;
  }

  public boolean hasCalendar() {
    return calendar == null;
  }

  @Override
  public String toString() {
    return "Subject{" + "id=" + id + ", name=" + name + ", study=" + study + ", classroom=" + classroom + ", teacher=" + teacher + '}';
  }

  public final Subject clone(long id) {
    return new Subject(this.db, id, this.name, this.startDate, this.finishDate, this.study, this.teacher, this.classroom, this.calendar);
  }

}
