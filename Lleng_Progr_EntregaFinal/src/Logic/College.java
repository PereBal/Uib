/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Utils.Exceptions.NotFoundException;
import Utils.Exceptions.IException;
import java.util.ArrayList;

/**
 *
 * @author PereBalaguer
 */
public class College {

  /**
   * Nom del centre
   */
  public final String NAME;
  /**
   * Aules del centre
   */
  private ArrayList<Classroom> classrooms;
  /**
   * Professors del centre
   */
  private ArrayList<Teacher> teachers;
  /**
   * Estudis del centre
   */
  private ArrayList<Study> studies;

  /**
   * Constructor del centre amb el nom especificat
   *
   * @param name Nom del centre
   */
  public College(String name) {
    this.NAME = name;
    classrooms = new ArrayList<>(10);
    teachers = new ArrayList<>(10);
    studies = new ArrayList<>(10);
  }

  /**
   * Retorna la classe que ocupa la posició solicitada per paràmetre
   *
   * @param p Posició
   * @return L'element que ocupa la posició <i>p</i>
   * @throws Utils.Exceptions.NotFoundException si la posició no existeix
   */
  public Classroom getClassroom(int p) throws NotFoundException {
    try {
      return classrooms.get(p);
    } catch (IndexOutOfBoundsException e) {
      throw new NotFoundException("Posició " + p);
    }
  }

  /**
   * Retorna un iterador sobre els elements de la llista classes
   *
   * @return Iterator (Classroom)
   */
  public java.util.Iterator<Classroom> getClassrooms() {
    return classrooms.iterator();
  }

  /**
   * Retorna el professor que ocupa la posició solicitada per paràmetre
   *
   * @param p Posició
   * @return L'element que ocupa la posició <i>p</i>
   * @throws Utils.Exceptions.NotFoundException si la posició no existeix
   */
  public Teacher getTeacher(int p) throws NotFoundException {
    try {
      return teachers.get(p);
    } catch (IndexOutOfBoundsException e) {
      throw new NotFoundException("Posició " + p);
    }
  }

  /**
   * Retorna un iterador sobre els elements de la llista teachers
   *
   * @return Iterator (Teacher)
   */
  public java.util.Iterator<Teacher> getTeachers() {
    return teachers.iterator();
  }

  /**
   * Retorna l'estudi que ocupa la posició solicitada per paràmetre
   *
   * @param p Posició
   * @return L'element que ocupa la posició <i>p</i>
   * @throws Utils.Exceptions.NotFoundException Si la posició no existeix
   *
   */
  public Study getStudy(int p) throws NotFoundException {
    try {
      return studies.get(p);
    } catch (IndexOutOfBoundsException e) {
      throw new NotFoundException("Estudi posició " + p);
    }
  }

  /**
   * Retorna un iterador sobre els elements de la llista studies
   *
   * @return Iterator (Study)
   */
  public java.util.Iterator<Study> getSdudies() {
    return studies.iterator();
  }

  /**
   * Elimina l'aula especificada de la llista
   *
   * @param p Posició
   */
  public void removeClassroom(int p) {
    if (classrooms.remove(p) == null) {
      IException.print("College.java", "removeClasse", "Aula " + p + " no trobada");
    }
  }

  /**
   * Elimina l'aula especificada de la llista
   *
   * @param c Objecte aula
   */
  public void removeClassroom(Classroom c) {
    if (!classrooms.remove(c)) {
      IException.print("College.java", "removeClasse", "Aula " + c + " no trobada");
    }
  }

  /**
   * Elimina <b>totes</b> les aules de la llista
   */
  public void removeAllClassrooms() {
    classrooms.clear();
  }

  /**
   * Elimina el professor especificat de la llista
   *
   * @param p Posició
   */
  public void removeTeacher(int p) {
    if (teachers.remove(p) == null) {
      IException.print("College.java", "removeTeacher", "Professor " + p + " no trobat");
    }
  }

  /**
   * Elimina el professor especificat de la llista
   *
   * @param t Objecte professor
   */
  public void removeTeacher(Teacher t) {
    if (!teachers.remove(t)) {
      IException.print("College.java", "removeTeacher", "Professor " + t + " no trobat");
    }
  }

  /**
   * Elimina <b>tots</b> els professors de la llista
   */
  public void removeAllTeachers() {
    teachers.clear();
  }

  /**
   * Elimina l'estudi especificat de la llista
   *
   * @param p Posició
   */
  public void removeStudy(int p) {
    if (studies.remove(p) == null) {
      IException.print("College.java", "removeStudy", "Study " + p + " no trobat");
    }
  }

  /**
   * Elimina l'estudi especificat de la llista
   *
   * @param s Objecte estudi
   */
  public void removeStudy(Study s) {
    if (!studies.remove(s)) {
      IException.print("College.java", "removeStudy", "Study " + s + " no trobat");
    }
  }

  /**
   * Elimina <b>tots</b> els estudis de la llista
   */
  public void removeAllStudies() {
    studies.clear();
  }

  /**
   * Afegeix l'aula a la fí de la llista
   *
   * @param c Objecte aula
   */
  public void setClassroom(Classroom c) {
    classrooms.add(c);
  }

  /**
   * Afegeix l'aula a la posició indicada de la llista
   *
   * @param p Posició
   * @param c Objecte aula
   */
  public void setClassroom(int p, Classroom c) {
    classrooms.add(p, c);
  }

  /**
   * Afegeix el professor a la fí de la llista
   *
   * @param t Objecte professor
   */
  public void setTeacher(Teacher t) {
    teachers.add(t);
  }

  /**
   * Afegeix el professor a la posició indicada de la llista
   *
   * @param p Posició
   * @param t Objecte professor
   */
  public void setTeacher(int p, Teacher t) {
    teachers.add(p, t);
  }

  /**
   * Afegeix l'estudi a la fí de la llista
   *
   * @param s Objecte estudi
   */
  public void setStudy(Study s) {
    studies.add(s);
  }

  /**
   * Afegeix l'estudi a la posició indicada de la llista
   *
   * @param p Posició
   * @param s Objecte estudi
   */
  public void setStudy(int p, Study s) {
    studies.add(p, s);
  }

  @Override
  public String toString() {
    return "College{" + "NAME=" + NAME + ", classrooms=" + classrooms + ", teachers=" + teachers + ", studies=" + studies + '}';
  }
}
