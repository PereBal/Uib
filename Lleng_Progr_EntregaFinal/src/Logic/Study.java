/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Utils.DB.DBI;

/**
 *
 * @author PereBalaguer
 */
public abstract class Study {

  public static enum Turn {

    morning, evening, morningAndEvening
  }

  public static enum StudyType {

    master, specialization, workshop, conference, roundTable
  }

  private final DBI db;
  private final long id;
  private final String name;
  private final StudyType type;

  /**
   * Constructor per donar d'alta estudis temporals. La idea d'aquest constructor és que sols sigui
   * usat per instanciar objectes estudi que posteriorment seran gravats a la base de dades.
   *
   * @param db
   * @param name
   * @param type
   */
  public Study(final DBI db, String name, StudyType type) {
    this.db = db;
    this.id = Definitions.Defs.nullId;
    this.name = name;
    this.type = type;
  }

  /**
   * Constructor emprat per la Base de Dades. Aquest es el constructor que emprarà la base de dades
   * per retornar objectes estudi ben formats.
   *
   * @param id
   * @param name
   * @param type
   */
  public Study(final DBI db, long id, String name, StudyType type) {
    this.db = db;
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public String getTypeToString() {
    switch (type) {
      case specialization:
        return "Curs d'especialització";
      case workshop:
        return "Taller";
      case conference:
        return "Conferencia";
      case roundTable:
        return "Taual Rodona";
    }
    return "Master";
  }

  /**
   * @return id de l'estudi o -1 si l'objecte no s'ha llegit de la DB prèviament
   */
  public long getId() {
    if (this.id == Definitions.Defs.nullId) {
      Utils.Exceptions.IException.print("Study.java", "getId", "S'està intentant accedir a l'id d'un objecte no llegit de la db");
    }
    return id;
  }

  public String getName() {
    return name;
  }

  public StudyType getType() {
    return type;
  }

  protected DBI getDB() {
    return db;
  }

  public abstract Utils.Calendar.CalendarComposition getCalendar() throws Utils.Exceptions.DBException;

  @Override
  public String toString() {
    return "Study{" + "id=" + id + ", name=" + name + ", type=" + type + '}';
  }
}
