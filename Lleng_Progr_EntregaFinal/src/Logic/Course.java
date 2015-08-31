/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.util.Date;

/**
 *
 * @author PereBalaguer
 */
public class Course {

  private final long id;
  private final Date startDate;
  private final Date finishDate;

  public Course(Date startDate, Date finishDate) {
    this.id = Definitions.Defs.nullId;
    this.startDate = startDate;
    this.finishDate = finishDate;
  }

  public Course(long id, Date startDate, Date finishDate) {
    this.id = id;
    this.startDate = startDate;
    this.finishDate = finishDate;
  }

  public long getId() {
    if (this.id == Definitions.Defs.nullId) {
      Utils.Exceptions.IException.print("Course.java", "getId", "S'està intentant accedir a l'id d'un objecte no llegit de la db");
    }
    return id;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getFinishDate() {
    return finishDate;
  }

  @Override
  public String toString() {
    return "Course{" + "id=" + id + ", startDate=" + startDate + ", endDate=" + finishDate + '}';
  }

  public final Course clone(long id) {
    return new Course(id, this.startDate, this.finishDate);
  }
}
