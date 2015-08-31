/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Utils.Calendar.CalendarComposition;
import Utils.DB.DBI;
import Utils.Exceptions.DBException;

/**
 *
 * @author PereBalaguer
 */
public class SpeStudy extends Study {

  /**
   * Les assignatures fantasma, tenen el mateix nom que l'estudi especial del
   * que provenent.
   */
  private Subject phantomS;
  private CalendarComposition cal;

  public SpeStudy(final DBI db, String name, StudyType type) {
    super(db, name, type);
    this.phantomS = null;
  }

  public SpeStudy(final DBI db, String name, StudyType type, Subject phantomS) {
    super(db, name, type);
    if (phantomS.hasStudy()) {
      Utils.Exceptions.IException.print("SpeStudy.java", "SpeStudy", "S'esta intentant crear un objecte estudi especial amb una assignatura que JA te estudi");
    }
    this.phantomS = phantomS;
  }

  public SpeStudy(final DBI db, long id, String name, StudyType type, Subject phantomS) {
    super(db, id, name, type);
    if (phantomS == null) {
      Utils.Exceptions.IException.print("SpeStudy.java", "SpeStudy", "S'esta intentant crear un objecte estudi especial amb una assignatura nula");
    } else if (phantomS.hasStudy()) {
      Utils.Exceptions.IException.print("SpeStudy.java", "SpeStudy", "S'esta intentant crear un objecte estudi especial amb una assignatura que JA te estudi");
    }
    this.phantomS = phantomS;
  }

  public Subject getSpeAttr() {
    return this.phantomS;
  }

  public int getHoursPerDay() throws DBException {
    if (this.phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
    }
    return phantomS.getHoursPerDay();
  }

  public int getDaysPerWeek() throws DBException {
    if (this.phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
    }
    return phantomS.getDaysPerWeek();
  }

  public Turn getTurn() throws DBException {
    if (this.phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
    }
    return phantomS.getTurn();
  }

  public Teacher getTeacher() throws DBException {
    if (phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
    }
    return phantomS.getTeacher();
  }

  public Classroom getClassroom() throws DBException {
    if (phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
    }
    return phantomS.getClassroom();
  }

  @Override
  public CalendarComposition getCalendar() throws DBException {
    if (phantomS == null) {
      this.phantomS = getDB().loadSubject(this.getId());
      if (phantomS != null) {
        cal = new CalendarComposition(phantomS.getCalendar());
      }
    } else {
      cal = new CalendarComposition(phantomS.getCalendar());
    }
    return cal;
  }

  public void setTeacher(Teacher teacher) throws DBException {
    if (teacher != null) {
      if (phantomS == null) {
        this.phantomS = getDB().loadSubject(this.getId());
      }
      this.phantomS.setTeacher(teacher);
    } else {
      Utils.Exceptions.IException.print("SpeStudy.java", "setTeacher", "Estam intentant assignar un professor nul");
    }
  }

  public void setClassroom(Classroom classroom) throws DBException {
    if (classroom != null) {
      if (phantomS == null) {
        this.phantomS = getDB().loadSubject(this.getId());
      }
      this.phantomS.setClassroom(classroom);
    } else {
      Utils.Exceptions.IException.print("SpeStudy.java", "setClassroom", "Estam intentant assignar una aula nul·la");
    }
  }

  public boolean hasTeacher() {
    return phantomS != null && phantomS.hasTeacher();
  }

  public boolean hasClassroom() {
    return phantomS != null && phantomS.hasClassroom();
  }

  public final SpeStudy clone(long id) {
    return new SpeStudy(this.getDB(), id, this.getName(), this.getType(), this.phantomS);
  }

  @Override
  public String toString() {
    return "StudyWNAssign{" + super.toString() + ", spFields=" + phantomS + '}';
  }
}
