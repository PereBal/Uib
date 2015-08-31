/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.DB;

import Definitions.Defs;
import Utils.Exceptions.DBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PereBalaguer
 */
public class DBI {

  private static final String DBNAME = "pbg162_lmm795";
  private static final String URL = "jdbc:mysql://localhost:3306/" + DBNAME;
  // a l'entrega posar root
  private static final String USER = "root";
  private static final String PASSWORD = "root";

  /**
   * Noms de les taules a la db
   */
  private enum tTyp {

    classroom, course, subject, norm_study, spe_study, calendar, day, teacher;
  }

  private final long[] nextID = new long[tTyp.values().length];

  private Connection con = null;
  private ResultSet rs = null;

  /**
   * Crea un objecte DataBaseInterface. Aquest objecte permet a tots els altres llegir i escriure de
   * la base de dades. Tot i que no és estrictament necessari, es recomana que tots els objectes
   * emprin la mateixa instància de DBI, així a l'hora de tancar la connexió sols s'haurà de fer un
   * <i>closeDB</i>. Altrament s'hauria de mantenir un registre d'objectes DBI actius.
   *
   * @throws SQLException
   */
  public DBI() throws SQLException {
    con = DriverManager.getConnection(URL, USER, PASSWORD);

    // inicializamos los siguientes valores del id de los objetos para optimizar las querys posteriores.
    PreparedStatement pst = con.prepareStatement("SHOW TABLE STATUS FROM " + DBNAME + " LIKE ?;");
    tTyp[] al = tTyp.values();
    ResultSet r;
    for (int i = 0; i < nextID.length; i++) {
      pst.setString(1, al[i].toString());
      r = pst.executeQuery();
      if (r.next()) {
        nextID[i] = r.getLong("Auto_increment");
      } else {
        throw new SQLException("No s'han pogut recuperar els indexos de la base de dades");
      }
    }
  }

  /**
   * <b>Tanca</b> la connexió a la <b>BD</b>. Es <b>necessari</b> cridar a aquest <i>metode</i> cada
   * cop que un objecte <i>DBI</i>
   * deixi de ser emprat.
   */
  public void closeDB() {
    try {
      if (rs != null) {
        rs.close();
      }
      if (con != null) {
        con.close();
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Solo se usara para testear, una vez se acabe la fase de testeo deberia eliminarse
  public Connection getConn() {
    return this.con;
  }

  /**
   * Llegeix un objecte professor de la base de dades.
   *
   * @param nif String (nif del professor)
   * @return Objecte professor o <b>null</b> si s'ha produit un error
   * @throws DBException si el nif del professor no passa la validació de
   * <i>Defs.isValidNif</i>
   */
  public Logic.Teacher loadTeacher(String nif) throws DBException {
    if (!Defs.isValidNif(nif)) {
      throw new DBException("readTeacher", DBException.errorCode.INVALID_NIF);
    }
    Logic.Teacher t = null;
    String query = "SELECT name, firstname, lastname, phone FROM " + DBNAME + ".teacher WHERE nif='" + nif + "'";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        t = new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), nif, rs.getString("phone"));
      }

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return t;
  }

  /**
   * Llegeix n objectes professor de la base de dades. Els elements carregats son des del primer
   * fins al n-éssim.
   *
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Teacher> loadTeachers(int nelem) {
    return loadTeachers(0, nelem);
  }

  /**
   * Llegeix n objectes professor de la base de dades. Els elements carregats son des del indicat a
   * offset+1 fins al n-éssim (o+1 perque els indexos comencen amb zero).
   *
   * @param offset
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Teacher> loadTeachers(long offset, int nelem) {
    java.util.ArrayList<Logic.Teacher> tal = null;
    String query = "SELECT nif, name, firstname, lastname, phone FROM " + DBNAME + ".teacher ORDER BY teacher.nif LIMIT " + offset + ", " + nelem + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        tal = new java.util.ArrayList<>(nelem);
        do {
          tal.add(new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("nif"), rs.getString("phone")));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return tal;
  }

  /**
   * Retorna el calendari del professor
   *
   * @param nif String
   * @return Objecte calendari o <b>null</b> si s'ha produit un error.
   * @throws DBException Si el nif del professor no ha passat la validació de
   * <i>Defs.isValidNif</i>
   */
  public java.util.ArrayList<Utils.Calendar.Calendar> loadTeacherCalendar(String nif) throws DBException {
    if (!Defs.isValidNif(nif)) {
      throw new DBException("loadTeacherCalendar", DBException.errorCode.INVALID_NIF);
    }

    return loadCalendar(DBNAME + ".subject INNER JOIN " + DBNAME + ".teacher ON teacher.nif='" + nif + "' AND subject.teacher=teacher.nif");
  }

  /**
   * Retorna les assignatures del professor.
   *
   * @param nif String (nif del professor)
   * @return ArrayList(Subject) o <b>null</b> si s'ha produit un error
   * @throws DBException Si el nif no ha pogut ser validat mitjançant
   * <i>Defs.isValidNif</i>
   */
  public java.util.ArrayList<Logic.Subject> loadTeacherSubjects(String nif) throws DBException {
    if (!Defs.isValidNif(nif)) {
      throw new DBException("loadTeacherSubjects", DBException.errorCode.INVALID_NIF);
    }

    String cond = "subject.teacher='" + nif + "'";
    return loadSubjects(cond, 10);
  }

  /**
   * Llegeix un objecte aula de la base de dades.
   *
   * @param id (Id de l'aula)
   * @return Objecte aula o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Logic.Classroom loadClassroom(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("readClassroom", DBException.errorCode.INVALID_ID);
    }
    Logic.Classroom c = null;

    String query = "SELECT type,capacity FROM " + DBNAME + ".classroom WHERE id=" + id;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        c = new Logic.Classroom(this, id, rs.getInt("capacity"), Logic.Classroom.ClassroomType.valueOf(rs.getString("type")));
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return c;
  }

  /**
   * Llegeix n objectes aula de la base de dades. Els elements carregats son des de l'inicial fins
   * al n-éssim.
   *
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Classroom> loadClassrooms(int nelem) {
    return loadClassrooms(0, nelem);
  }

  /**
   * Llegeix n objectes aula de la base de dades. Els elements carregats son des del indicat a
   * offset+1 fins al n-éssim (o+1 perque els indexos comencen amb zero).
   *
   * @param offset
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Classroom> loadClassrooms(int offset, int nelem) {
    java.util.ArrayList<Logic.Classroom> cal = null;

    String query = "SELECT id, type, capacity FROM " + DBNAME + ".classroom ORDER BY id LIMIT " + offset + "," + nelem + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        cal = new java.util.ArrayList<>(nelem);
        do {
          cal.add(new Logic.Classroom(this, rs.getLong("id"), rs.getInt("capacity"), Logic.Classroom.ClassroomType.valueOf(rs.getString("type"))));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return cal;
  }

  /**
   * Retorna el calendari de l'aula
   *
   * @param id Long
   * @return Objecte calendari o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <b>Defs.nullId</b>
   */
  public java.util.ArrayList<Utils.Calendar.Calendar> loadClassroomCalendar(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadClassroomCalendar", DBException.errorCode.INVALID_ID);
    }

    return loadCalendar(DBNAME + ".subject INNER JOIN " + DBNAME + ".classroom ON classroom.id=" + id + " AND subject.classroom=classroom.id");
  }

  /**
   * Llegeix un objecte curs de la base de dades.
   *
   * @param id (Id del curs)
   * @return Objecte curs o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Logic.Course loadCourse(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("readCourse", DBException.errorCode.INVALID_ID);
    }
    Logic.Course c = null;
    String query = "SELECT start, finish FROM " + DBNAME + ".course WHERE id=" + id;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        c = new Logic.Course(id, rs.getDate("start"), rs.getDate("finish"));
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return c;
  }

  /**
   * Llegeix un objecte assignatura de la base de dades.
   *
   * @param id (Id de l'assignatura)
   * @return Objecte assignatura o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Logic.Subject loadSubject(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("readSubject", DBException.errorCode.INVALID_ID);
    }
    Logic.Subject s = null;
    Utils.Calendar.Calendar c;
    String query = "SELECT name, start, finish, hoursday, daysweek, turn, calendar.id AS cal_id FROM " + DBNAME + "."
            + "subject INNER JOIN " + DBNAME + ".calendar ON subject.id=" + id + " AND subject.calendar=calendar.id";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
        s = new Logic.Subject(this, id, rs.getString("name"), rs.getDate("start"), rs.getDate("finish"), c);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  /**
   * Llegeix n objectes assignatura de la base de dades. Els elements carregats son des de l'inicial
   * fins al n-éssim
   *
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Subject> loadSubjects(int nelem) {
    return loadSubjects(0, nelem);
  }

  /**
   * Llegeix n objectes assignatura de la base de dades. Els elements carregats son des del indicat
   * a offset+1 fins al n-éssim (o+1 perque els indexos comencen amb zero).
   *
   * @param offset
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.Subject> loadSubjects(int offset, int nelem) {
    java.util.ArrayList<Logic.Subject> sal = null;
    Utils.Calendar.Calendar c;
    String query = "SELECT subject.id AS s_id, name, start, finish, hoursday, daysweek, turn, calendar.id AS cal_id FROM " + DBNAME + "."
            + "subject INNER JOIN " + DBNAME + ".calendar ON subject.calendar=calendar.id ORDER BY subject.id LIMIT " + offset + "," + nelem + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        sal = new java.util.ArrayList<>(nelem);
        do {
          c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
          sal.add(new Logic.Subject(this, rs.getLong("s_id"), rs.getString("name"), rs.getDate("start"), rs.getDate("finish"), c));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sal;
  }

  /**
   * Retorna l'estudi de l'assignatura.
   *
   * @param id Long
   * @return Objecte estudi o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <i>Defs.nullId</i>
   */
  public Logic.NormStudy loadSubjectStudy(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadSubjectStudy", DBException.errorCode.INVALID_ID);
    }
    Logic.NormStudy s = null;
    String query = "SELECT norm_study.id AS s_id, norm_study.name, type, course AS c_id, start, finish FROM " + DBNAME + ".norm_study "
            + "INNER JOIN " + DBNAME + ".subject ON subject.id=" + id + " AND subject.study=norm_study.id "
            + "INNER JOIN " + DBNAME + ".calendar ON calendar.id=subject.calendar;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        Logic.Course c = new Logic.Course(rs.getLong("c_id"), rs.getDate("start"), rs.getDate("finish"));
        s = new Logic.NormStudy(this, rs.getLong("s_id"), rs.getString("name"), Logic.Study.StudyType.valueOf(rs.getString("type")), c);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  /**
   * Retorna el professor de l'assignatura.
   *
   * @param id Long
   * @return Objecte professor o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <i>Defs.nullId</i>
   */
  public Logic.Teacher loadSubjectTeacher(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadSubjectTeacher", DBException.errorCode.INVALID_ID);
    }
    Logic.Teacher t = null;
    String query = "SELECT nif, teacher.name, firstname, lastname, phone FROM " + DBNAME + ".teacher "
            + "INNER JOIN " + DBNAME + ".subject ON subject.id=" + id + " AND subject.teacher=teacher.nif;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        t = new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("nif"), rs.getString("phone"));
      }
    } catch (SQLException ex) {
      return null;
      //Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return t;
  }

  /**
   * Retorna l'aula de l'assignatura.
   *
   * @param id Long
   * @return Objecte aula o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <i>Defs.nullId</i>
   */
  public Logic.Classroom loadSubjectClassroom(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadSubjectClassroom", DBException.errorCode.INVALID_ID);
    }
    Logic.Classroom c = null;
    String query = "SELECT classroom.id, type, capacity FROM " + DBNAME + ".classroom "
            + "INNER JOIN " + DBNAME + ".subject ON subject.id=" + id + " AND subject.classroom=classroom.id;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        c = new Logic.Classroom(this, rs.getLong("id"), rs.getInt("capacity"), Logic.Classroom.ClassroomType.valueOf(rs.getString("type")));
      }
    } catch (SQLException ex) {
      return null;
      //Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return c;
  }

  /**
   * Retorna el calendari de l'assignatura.
   *
   * @param id Long
   * @return Objecte calendari o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <i>Defs.nullId</i>
   */
  public Utils.Calendar.Calendar loadSubjectCalendar(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadSubjectCalendar", DBException.errorCode.INVALID_ID);
    }

    java.util.ArrayList<Utils.Calendar.Calendar> cal = loadCalendar(DBNAME + ".subject ON subject.id=" + id);

    return (cal == null) ? null : cal.get(0);
  }

  /**
   * Llegeix un objecte estudi normal de la base de dades.
   *
   * @param id
   * @return Objecte estudi normal o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Logic.NormStudy loadNormStudy(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("readNormStudy", DBException.errorCode.INVALID_ID);
    }
    Logic.NormStudy s = null;
    String query = "SELECT name, type, course.id AS c_id, start, finish FROM " + DBNAME + "."
            + "norm_study INNER JOIN " + DBNAME + ".course ON norm_study.id=" + id + " AND norm_study.course=course.id;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        Logic.Course c = new Logic.Course(rs.getLong("c_id"), rs.getDate("start"), rs.getDate("finish"));
        s = new Logic.NormStudy(this, id, rs.getString("name"), Logic.Study.StudyType.valueOf(rs.getString("type")), c);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  /**
   * Llegeix n objectes estudi normal de la base de dades. Els elements carregats son des de
   * l'element inicial fins al n-éssim
   *
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.NormStudy> loadNormStudies(int nelem) {
    return loadNormStudies(0, nelem);
  }

  /**
   * Llegeix n objectes estudi normal de la base de dades. Els elements carregats son des del
   * indicat a offset+1 fins al n-éssim (o+1 perque els indexos comencen amb zero).
   *
   * @param offset
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.NormStudy> loadNormStudies(int offset, int nelem) {
    java.util.ArrayList<Logic.NormStudy> nal = null;
    String query = "SELECT norm_study.id as n_id, name, type, course.id AS c_id, start, finish FROM " + DBNAME + "."
            + "norm_study INNER JOIN " + DBNAME + ".course ON norm_study.course=course.id LIMIT " + offset + "," + nelem + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        nal = new java.util.ArrayList<>(nelem);
        Logic.Course c;
        do {
          c = new Logic.Course(rs.getLong("c_id"), rs.getDate("start"), rs.getDate("finish"));
          nal.add(new Logic.NormStudy(this, rs.getLong("n_id"), rs.getString("name"), Logic.Study.StudyType.valueOf(rs.getString("type")), c));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return nal;
  }

  /**
   * Retorna el calendari de l'estudi de tipus normal
   *
   * @param id Long
   * @return Objecte calendari o <b>null</b> si s'ha produit un error
   * @throws DBException Si l'id és <i>Defs.nullId</i>
   */
  public java.util.ArrayList<Utils.Calendar.Calendar> loadNormStudyCalendar(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadClassroomCalendar", DBException.errorCode.INVALID_ID);
    }

    return loadCalendar(DBNAME + ".subject INNER JOIN " + DBNAME + ".norm_study ON norm_study.id=" + id + " AND subject.study=norm_study.id");

  }

  /**
   * Retorna les assignatures de l'estudi de tipus normal.
   *
   * @param id
   * @return ArrayList(Subject) o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public java.util.ArrayList<Logic.Subject> loadNormStudySubjects(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadNormStudySubjects", DBException.errorCode.INVALID_ID);
    }
    String cond = "subject.study=" + id;
    return loadSubjects(cond, 20);
  }

  /**
   * Retorna les assignatures de la DB que satisfàn la condició indicada a cond. La condició
   * esperada es tipus: subject INNER JOIN {COND} COND -> ON subject.foreign_key=key
   *
   * @param s ArrayList(Subject) que contendrà les assignatures que satisfan COND (si n'hi ha) o
   * valdrà <b>null</b>
   * @param cond String
   * @param initCap int Capacitat inicial de l'AL. Si val menys de, o 0, s'assignara 20
   * @return <b>true</b> si tot ha anat bé o <b>false</b> altrament
   * @throws DBException Si <i>cond</i> és <b>null</b> o si no passa la validació
   * <i>[^A-Za-z0-9\._]</i>
   */
  private java.util.ArrayList<Logic.Subject> loadSubjects(String cond, int initCap) throws DBException {
    if (cond == null) {
      throw new DBException("loadSubjects", DBException.errorCode.NULL_COND);
    }
    if (cond.matches("[^a-zA-Z0-9\\._]")) {
      throw new DBException("loadSubjects", DBException.errorCode.BAD_COND);
    }

    if (initCap <= 0) {
      initCap = 20;
    }

    java.util.ArrayList<Logic.Subject> s = null;
    String query = "SELECT subject.id AS s_id, name, start, finish, calendar.id AS cal_id, hoursday, daysweek, turn FROM " + DBNAME + "."
            + "subject INNER JOIN " + DBNAME + ".calendar ON " + cond + " AND subject.calendar=calendar.id;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        s = new java.util.ArrayList<>(initCap); // tamany arbitrari per evitar els resizes inicials
        Utils.Calendar.Calendar c;
        do {
          c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
          s.add(new Logic.Subject(this, rs.getLong("s_id"), rs.getString("name"), rs.getDate("start"), rs.getDate("finish"), c));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  /**
   * Retorna les assignatures de la DB que satisfàn la condició indicada a cond. La condició
   * esperada es tipus: subject INNER JOIN {COND} COND -> ON subject.foreign_key=key
   *
   * @param s LinkedList(Subject) que contendrà les assignatures que satisfan COND (si n'hi ha) o
   * valdrà <b>null</b>
   * @param cond String
   * @param initCap int Capacitat inicial de l'AL. Si val menys de, o 0, s'assignara 20
   * @return <b>true</b> si tot ha anat bé o <b>false</b> altrament
   * @throws DBException Si <i>cond</i> és <b>null</b> o si no passa la validació
   * <i>[^A-Za-z0-9\._]</i>
   */
  private java.util.LinkedList<Logic.Subject> loadSubjects(String cond) throws DBException {
    if (cond == null) {
      throw new DBException("loadSubjects", DBException.errorCode.NULL_COND);
    }
    if (cond.matches("[^a-zA-Z0-9\\._]")) {
      throw new DBException("loadSubjects", DBException.errorCode.BAD_COND);
    }

    java.util.LinkedList<Logic.Subject> s = null;
    String query = "SELECT subject.id AS s_id, name, start, finish, calendar.id AS cal_id, hoursday, daysweek, turn FROM " + DBNAME + "."
            + "subject INNER JOIN " + DBNAME + ".calendar ON " + cond + " AND subject.calendar=calendar.id;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        s = new java.util.LinkedList<>(); // tamany arbitrari per evitar els resizes inicials
        Utils.Calendar.Calendar c;
        do {
          c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
          s.add(new Logic.Subject(this, rs.getLong("s_id"), rs.getString("name"), rs.getDate("start"), rs.getDate("finish"), c));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }

  /**
   * Llegeix un objecte estudi especial de la base de dades.
   *
   * @param id
   * @return Objecte estudi especial o <b>null</b> si s'ha produit un error
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Logic.SpeStudy loadSpeStudy(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadSpeStudy", DBException.errorCode.INVALID_ID);
    }
    Logic.SpeStudy stu = null;
    Logic.Subject s;
    Utils.Calendar.Calendar c;
    String query = "SELECT spe_study.name as st_name, type, subject.id as s_id, start, finish, calendar.id as cal_id, hoursday, daysweek, turn "
            + "FROM " + DBNAME + ".calendar INNER JOIN "
            + "(" + DBNAME + ".subject INNER JOIN " + DBNAME + ".spe_study ON spe_study.id=" + id + " AND spe_study.speattr=subject.id) "
            + "ON subject.calendar=calendar.id;";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
        s = new Logic.Subject(this, rs.getLong("s_id"), rs.getString("st_name"), rs.getDate("start"), rs.getDate("finish"), c); // les assign fantasma tenen el mateix nom que l'estudi
        stu = new Logic.SpeStudy(this, id, rs.getString("st_name"), Logic.Study.StudyType.valueOf(rs.getString("type")), s);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return stu;
  }

  /**
   * Llegeix n objectes estudi especial de la base de dades. Els elements carregats son des de
   * l'indicat fins al n-éssim
   *
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.SpeStudy> loadSpeStudies(int nelem) {
    return loadSpeStudies(0, nelem);
  }

  /**
   * Llegeix n objectes estudi especial de la base de dades. Els elements carregats son des del
   * indicat a offset+1 fins al n-éssim (o+1 perque els indexos comencen amb zero).
   *
   * @param offset
   * @param nelem
   * @return ArrayList amb els n elements o <b>null</b> si s'ha produit un error
   */
  public java.util.ArrayList<Logic.SpeStudy> loadSpeStudies(int offset, int nelem) {
    java.util.ArrayList<Logic.SpeStudy> sal = null;
    String query = "SELECT spe_study.id as ss_id, spe_study.name as st_name, type, subject.id as s_id, start, finish, calendar.id as cal_id, hoursday, daysweek, turn "
            + "FROM " + DBNAME + ".calendar INNER JOIN "
            + "(" + DBNAME + ".subject INNER JOIN " + DBNAME + ".spe_study ON spe_study.speattr=subject.id) "
            + "ON subject.calendar=calendar.id ORDER BY spe_study.id LIMIT " + offset + "," + nelem + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        Logic.Subject s;
        Utils.Calendar.Calendar c;
        sal = new java.util.ArrayList<>(nelem);
        do {
          c = new Utils.Calendar.Calendar(this, rs.getLong("cal_id"), rs.getInt("hoursday"), rs.getInt("daysweek"), Logic.Study.Turn.valueOf(rs.getString("turn")));
          s = new Logic.Subject(this, rs.getLong("s_id"), rs.getString("st_name"), rs.getDate("start"), rs.getDate("finish"), c); // les assign fantasma tenen el mateix nom que l'estudi
          sal.add(new Logic.SpeStudy(this, rs.getLong("ss_id"), rs.getString("st_name"), Logic.Study.StudyType.valueOf(rs.getString("type")), s));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sal;
  }

  /**
   * Retorna un objecte calendari ben format. Metode d'ús intern exclusivament!
   *
   * @param s
   * @return
   */
  protected java.util.ArrayList<Utils.Calendar.Calendar> loadCalendar(String s) {
    // les regex son per garantitzar la seguretat davant injeccions sql
    String regex1 = DBNAME.toLowerCase() + "\\.subject inner join " + DBNAME.toLowerCase() + "\\.[a-z_]+ on [a-z\\.]+=['a-z0-9\\.]+ and [a-z\\.]+=['a-z0-9\\.]+";
    String regex2 = DBNAME.toLowerCase() + "\\.subject where subject\\.id=[0-9]+";

    if (!s.toLowerCase().matches(regex1) && !s.toLowerCase().matches(regex2)) {
      //Utils.Exceptions.IException.print("DBI.java", "loadCalendar", "S'ha intentat fer una query que no compleix les regex");
      //return null;
    }
    java.util.ArrayList<Utils.Calendar.Calendar> cal = null;
    Utils.Calendar.Calendar c;
    Logic.Subject sub;
    String query = "SELECT subject.id AS s_id, subject.name as s_name, start, finish, calendar.id AS cal_id, hoursday, daysweek, turn, day.id AS d_id, day, starthour, endhour FROM "
            + DBNAME + ".day INNER JOIN ("
            + DBNAME + ".calendar INNER JOIN "
            + s
            + " AND subject.calendar=calendar.id "
            + ") ON calendar.id=day.calendar;";

    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);

      if (rs.next()) {
        cal = new ArrayList<>(20); // valor estimado

        int i;
        boolean hasNext;
        long calid;
        int hday;
        int dweek;
        Logic.Study.Turn t;
        Utils.Calendar.Day days[];

        // coses del inner join i de que un element pot tenir 1+ assignatures i per tant, 1+ calendaris
        do {
          calid = rs.getLong("cal_id");
          hday = rs.getInt("hoursday");
          dweek = rs.getInt("daysweek");
          t = Logic.Study.Turn.valueOf(rs.getString("turn"));

          sub = new Logic.Subject(this, rs.getLong("s_id"), rs.getString("s_name"), rs.getDate("start"), rs.getDate("finish"));

          days = new Utils.Calendar.Day[5];
          i = 0;
          // per carregar els dies associats al calendari
          // el i<5 es per si mos l'han liat a l'hora de fer inserts, NO hauria de ser necessari
          do {
            days[i++] = new Utils.Calendar.Day(rs.getLong("d_id"), Utils.Calendar.Day.DayOfWeek.valueOf(rs.getString("day")),
                    rs.getInt("starthour"), rs.getInt("endhour"));
            hasNext = rs.next();
          } while (hasNext && rs.getLong("cal_id") == calid && i < 5);

          // amb aixo en construir l'objecte, posarem els altres dies a null
          if (i < 5) {
            days[i] = null;
          }
          c = new Utils.Calendar.Calendar(this, calid, hday, dweek, t, days);

          // amb aixo, rompem la dependencia circular assignatura - calendari ja que en parametritzar 'c', 
          // l'assignatura s'encarrega de que 'c' reb com a assignatura ella mateixa.
          sub.setCalendar(c);

          cal.add(c);
        } while (hasNext);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return cal;
  }

  /**
   * Carrega els dies del calendari.
   *
   * @param id Id del calendari
   * @return Array d'objectes dia o <b>null</b> si s'ha produit un error.
   * @throws DBException si l'id es <i>Defs.nullId</i>
   */
  public Utils.Calendar.Day[] loadCalDays(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("loadCalDays", DBException.errorCode.INVALID_ID);
    }

    ArrayList<Utils.Calendar.Day> d = null;

    //String query = "SELECT id, day, starthour, endhour, COUNT(id) AS lenght FROM " + DBNAME + ".day_ocup WHERE day_ocup.calendar=" + id + ";";
    String query = "SELECT id, day, starthour, endhour FROM " + DBNAME + ".day WHERE day.calendar=" + id + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
//        final int length = rs.getInt("length");
//        if (length > 5) {
//          Utils.Exceptions.IException.print("DBI.java", "loadCalDays", "Un calendari te mes de 5 dies guardats");
//        }

        int i = 0;
        d = new ArrayList<>();
        do {
          d.add(new Utils.Calendar.Day(rs.getLong("id"), Utils.Calendar.Day.DayOfWeek.valueOf(rs.getString("day")),
                  rs.getInt("starthour"), rs.getInt("endhour")));
        } while (rs.next());
      }

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }

    if (d == null || d.isEmpty()) {
      return null;
    }
    return d.toArray(new Utils.Calendar.Day[d.size()]);
  }

  /**
   * Carregam l'assignatura associada al calendari. Aquest mètode es degut a la dependència 1..1
   * entre les assignatures i el calendari, cosa que deriva en haver de fer algunes... "xapusses".
   *
   * @param subjCal Objecte calendari que esta carregant l'assignatura
   * @return Objecte assignatura o <b>null</b> si s'ha produit un error
   * @throws DBException si id == <i>Defs.nullId</i>
   */
  public Logic.Subject loadCalSubject(Utils.Calendar.Calendar subjCal) throws DBException {
    if (subjCal.getId() == Defs.nullId) {
      throw new Utils.Exceptions.DBException("loadCalSubject", DBException.errorCode.INVALID_ID);
    }

    Logic.Subject s = null;

    String query = "SELECT subject.id AS s_id, name, start, finish FROM " + DBNAME + ".subject WHERE subject.calendar=" + subjCal.getId() + ";";
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        s = new Logic.Subject(this, rs.getLong("s_id"), rs.getString("name"), rs.getDate("start"), rs.getDate("finish"), subjCal);
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }

    return s;
  }

  /**
   * Escriu el professor a la base de dades.
   *
   * @param t Objecte professor
   * @return Objecte professor sense modificar (sols per mantenir la coherència dels mètodes) o
   * <b>null</b> si s'ha produit un error
   * @throws DBException Si el professor no te assignat un número de teléfon o el nif no satisfà la
   * validació <i>Defs.isValidNif</i>.
   */
  public Logic.Teacher storeTeacher(Logic.Teacher t) throws DBException {
    if (!t.hasPhone()) {
      throw new DBException("storeTeacher", DBException.errorCode.NO_PHONE);
    }

    final String nif = t.getNif();
    if (!Defs.isValidNif(nif)) {
      throw new DBException("storeTeacher", DBException.errorCode.INVALID_NIF);
    }

    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".teacher (nif, name, firstname, lastname, phone) VALUES (?, ?, ?, ?, ?);");) {
      st.setString(1, Defs.formatNif(nif));
      st.setString(2, Defs.formatName(t.getName()));
      st.setString(3, Defs.formatString(t.getFirstName()));
      if (t.hasLastName()) {
        st.setString(4, Defs.formatString(t.getLastName()));
      } else {
        st.setNull(4, java.sql.Types.VARCHAR);
      }
      st.setString(5, Defs.formatPhone(t.getPhone()));

      st.executeUpdate();
      return t;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  /**
   * Escriu l'aula a la base de dades
   *
   * @param c Objecte aula
   * @return Objecte aula amb l'id actualitzat o <b>null</b> si s'ha produit un error
   */
  public Logic.Classroom storeClassroom(Logic.Classroom c) {
    Logic.Classroom newC = null;
    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".classroom (type, capacity) VALUES (?, ?);");) {
      st.setString(1, c.getType().toString());
      st.setInt(2, c.getCapacity());

      st.executeUpdate();

      // actualitzar l'id segons el que ha posat la DB
      newC = c.clone(nextID[tTyp.classroom.ordinal()]++);
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return newC;
  }

  /**
   * Escriu el curs a la base de dades
   *
   * @param c Objecte curs
   * @return Objecte curs amb l'id actualitzat o <b>null</b> si s'ha produit un error
   */
  // Lo ideal seria fer una funcio que comproves que els valors inici fi no pertanyen ja a un altre curs
  // si aixi fos, no caldria reinserir la taula -> PENDENT!
  public Logic.Course storeCourse(Logic.Course c) {
    Logic.Course newC = null;
    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".course (start, finish) VALUES (?, ?);");) {
      st.setDate(1, new java.sql.Date(c.getStartDate().getTime()));
      st.setDate(2, new java.sql.Date(c.getFinishDate().getTime()));

      st.executeUpdate();

      newC = c.clone(nextID[tTyp.course.ordinal()]++);
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return newC;
  }

  /**
   * Escriu l'assignatura a la base de dades
   *
   * @param s Objecte assignatura
   * @return Objecte assignatura amb l'id actualitzat o <b>null</b> si s'ha produit un error
   */
  public Logic.Subject storeSubject(Logic.Subject s) {
    if (s == null) {
      return null;
    }
    Logic.Subject newS = null;

    Utils.Calendar.Calendar c;

    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".subject (name, start, finish, study, calendar, classroom, teacher) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);");) {

      c = storeCalendar(s.getCalendar());
      if (c == null) { // si s'ha produit un error guardant el calendari, no podem seguir
        return null;
      }
      con.setAutoCommit(false);

      st.setString(1, Defs.formatString(s.getName()));
      st.setDate(2, new java.sql.Date(s.getStartDate().getTime()));
      st.setDate(3, new java.sql.Date(s.getFinishDate().getTime()));
      if (s.hasStudy()) {
        st.setLong(4, s.getStudy().getId());
      } else {
        st.setNull(4, java.sql.Types.BIGINT);
      }
      st.setLong(5, c.getId()); // escrivim el calendari amb l'id ja actualitzat

      if (s.hasClassroom()) {
        st.setLong(6, s.getClassroom().getId());
      } else {
        st.setNull(6, java.sql.Types.BIGINT);
        //st.setLong(6, -1);
      }

      if (s.hasTeacher()) {
        st.setString(7, s.getTeacher().getNif());
      } else {
        st.setNull(7, java.sql.Types.CHAR);
      }

      st.executeUpdate();

      newS = s.clone(nextID[tTyp.subject.ordinal()]++);

      con.commit();
      con.setAutoCommit(true);
    } catch (SQLException | DBException ex) {
      try {
        con.rollback();
        con.setAutoCommit(true);
      } catch (SQLException ex1) {
        Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex1);
      }
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }

    return newS;
  }

  /**
   * Escriu el calendari a la DB.
   *
   * @param c Objecte Calendari
   * @return Objecte calendari amb l'id actualitzat o <b>null</b> si s'ha produit algun error.
   */
  public Utils.Calendar.Calendar storeCalendar(Utils.Calendar.Calendar c) {
    if (c.getDBDays() == null) {
      Utils.Exceptions.IException.print("DBI.java", "storeCalendar", "Estam intentant guardar un calendari sense dies");
      return null;
    }

    Utils.Calendar.Calendar newC = null;

    try (PreparedStatement stc = con.prepareStatement("INSERT INTO " + DBNAME + ".calendar (hoursday, daysweek, turn) VALUES (?, ?, ?);");
            PreparedStatement std = con.prepareStatement("INSERT INTO " + DBNAME + ".day (day, starthour, endhour, calendar) VALUES (?, ?, ?, ?);");) {
      con.setAutoCommit(false);

      stc.setInt(1, c.getHoursDay());
      stc.setInt(2, c.getDaysWeek());
      stc.setString(3, c.getTurn().toString());

      stc.executeUpdate();

      newC = c.clone(nextID[tTyp.calendar.ordinal()]++);
      final long internalID = newC.getId();

      // en teoria com que els arrays son referencies a memoria, les modificacions
      // sobre ds quedaran reflectides sobre newC.days
      Utils.Calendar.Day[] ds = newC.getDBDays();
      Utils.Calendar.Day d;
      final int max = ds.length;
      int i = 0;

      while (i < max && ds[i] != null) {
        d = ds[i];
        std.setString(1, d.getName().toString());
        std.setInt(2, d.getStartHour());
        std.setInt(3, d.getEndHour());
        std.setLong(4, internalID);

        std.executeUpdate();

        ds[i++] = d.clone(nextID[tTyp.day.ordinal()]);

      }
      con.commit();
      con.setAutoCommit(true);
    } catch (SQLException ex) {
      try {
        con.rollback();
        con.setAutoCommit(true);
      } catch (SQLException ex1) {
        Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex1);
      }
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return newC;
  }

  /**
   * Escriu l'estudi de tipus normal (màster o especialització) a la base de dades.
   *
   * @param s Objecte estudi normal
   * @return Objecte estudi normal amb l'id actualitzat o <b>null</b> si s'ha produit un error
   */
  public Logic.NormStudy storeNormStudy(Logic.NormStudy s) {
    Logic.NormStudy newS = null;
    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".norm_study (name, type, course) VALUES (?, ?, ?);");) {
      st.setString(1, Defs.formatString(s.getName()));
      st.setString(2, s.getType().toString());
      st.setLong(3, s.getCourse().getId());

      st.executeUpdate();

      newS = s.clone(nextID[tTyp.norm_study.ordinal()]++);
    } catch (SQLException | DBException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return newS;
  }

  /**
   * Escriu l'estudi de tipus especial (taller de treball, conferència o taula rodona) a la base de
   * dades.
   *
   * @param s Objecte estudi especial
   * @return Objecte estudi especial amb l'id actualitzat o <b>null</b> si s'ha produit algún error.
   */
  public Logic.SpeStudy storeSpeStudy(Logic.SpeStudy s) {
    Logic.SpeStudy newS = null;

    Logic.Subject subj = s.getSpeAttr();
    if (subj == null) { // si no hem pogut guardar els atributs especials de l'estudi, no cal seguir
      Utils.Exceptions.IException.print("DBI.java", "storeSpeStudy", "S'esta intentant guardar un estudi especial SENSE especificar-ne els attr-specials");
      return null;
    }

    try (PreparedStatement st = con.prepareStatement("INSERT INTO " + DBNAME + ".spe_study (name, type, speattr) VALUES (?, ?, ?);");) {

      subj = storeSubject(subj);
      if (subj == null) { // si no hem pogut guardar els atributs especials de l'estudi, no cal seguir
        return null;
      }
      con.setAutoCommit(false);

      st.setString(1, Defs.formatString(s.getName()));
      st.setString(2, s.getType().toString());
      st.setLong(3, subj.getId());

      st.executeUpdate();

      newS = s.clone(nextID[tTyp.spe_study.ordinal()]++);
      con.commit();
      con.setAutoCommit(true);
    } catch (SQLException ex) {
      try {
        con.rollback();
        con.setAutoCommit(true);
      } catch (SQLException ex1) {
        Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex1);
      }
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return newS;
  }

  /**
   * Actualitza un professor. Els camps a actualitzar son el teléfon i/o el 2n cognom
   *
   * @param newT Objecte professor
   * @return <b>true</b> si tot ha anat bé, <b>false</b> altrament.
   * @throws DBException si el nif del professor no passa la validació
   * <i>Defs.isValidNif</i> o no té teléfon
   */
  public boolean updateTeacher(Logic.Teacher newT) throws DBException {

    if (!newT.hasPhone()) {
      throw new DBException("updateTeacher", DBException.errorCode.NO_PHONE);
    }

    if (!Defs.isValidNif(newT.getNif())) {
      throw new DBException("updateTeacher", DBException.errorCode.INVALID_NIF);
    }

    String query = "UPDATE " + DBNAME + ".teacher SET phone=" + newT.getPhone();
    if (newT.hasLastName()) {
      query += ", lastname='" + Defs.formatString(newT.getLastName()) + "'";
    }
    query += " WHERE teacher.nif='" + newT.getNif() + "'";

    boolean res = false;
    try (Statement st = con.createStatement()) {
      res = st.executeUpdate(query) > 0;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      return res;
    }
  }

  /**
   * Actualitza una aula. Els camps a actualitzar poden ser el tipus i l'ocupació
   *
   * @param newC Objecte aula
   * @return <b>true</b> si tot ha anat bé, <b>false</b> altrament.
   * @throws DBException Si l'id de l'aula és <i>Defs.nullId</i>
   */
  public boolean updateClassroom(Logic.Classroom newC) throws DBException {
    if (newC.getId() == Defs.nullId) {
      throw new DBException("updateClassroom", DBException.errorCode.INVALID_ID);
    }

    boolean res = false;
    try (PreparedStatement st = con.prepareStatement("UPDATE " + DBNAME + ".classroom "
            + "SET type=?, capacity=? WHERE classroom.id=" + newC.getId() + ";")) {
      st.setString(1, newC.getType().toString());
      st.setInt(2, newC.getCapacity());

      res = st.executeUpdate() > 0;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      return res;
    }
  }

  /**
   * Actualitza una assignatura. Aquest mètode <b>sols</b> ha de ser cridat si un professor o classe
   * ha estat assignat a l'assignatura en cuestió
   *
   * @param newS Objecte assignatura
   * @return <b>true</b> si tot ha anat bé, <b>false</b> altrament.
   * @throws Utils.Exceptions.DBException Si l'id de l'assignatura és
   * <i>Defs.nullId</i> o no s'ha afegit un professor/aula o el professor té un nif no validat per
   * <i>Defs.isValidNif</i>
   */
  public boolean updateSubject(Logic.Subject newS) throws DBException {

    if (newS.getId() == Defs.nullId) {
      throw new DBException("updateSubject", DBException.errorCode.INVALID_ID);
    }

    boolean hasClassroom = newS.hasClassroom();
    boolean hasTeacher = newS.hasTeacher();
    if (!hasClassroom && !hasTeacher) {
      throw new DBException("updateSubject", DBException.errorCode.NO_CHANGES);
    }

    if (newS.hasCalendar()) {
      updateCalendar(newS.getCalendar());
    }

    String query = "UPDATE " + DBNAME + ".subject SET ";
    if (hasTeacher) {
      String nif = newS.getTeacher().getNif();
      if (!Defs.isValidNif(nif)) {
        throw new DBException("updateSubject", DBException.errorCode.INVALID_NIF);
      }
      query += "teacher='" + nif + "'";
      if (hasClassroom) {
        query += ", classroom=" + newS.getClassroom().getId();
      }
    } else if (hasClassroom) {
      query += "classroom=" + newS.getClassroom().getId();
    }
    query += " WHERE subject.id=" + newS.getId();

    boolean res = false;
    try (Statement st = con.createStatement()) {
      res = st.executeUpdate(query) > 0;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      return res;
    }
  }

  /**
   *
   * @param newS
   * @return
   * @throws DBException
   */
  public boolean updateStudySubject(Logic.Subject newS) throws DBException {

    if (newS.getId() == Defs.nullId) {
      throw new DBException("updateSubject", DBException.errorCode.INVALID_ID);
    }

    boolean hasStudy = newS.hasStudy();
    if (!hasStudy) {
      throw new DBException("updateSubject", DBException.errorCode.NO_CHANGES);
    }

    String query = "UPDATE " + DBNAME + ".subject SET ";

    if (hasStudy) {
      query += "study=" + newS.getStudy().getId();
    }
    query += " WHERE subject.id=" + newS.getId();

    boolean res = false;
    try (Statement st = con.createStatement()) {
      res = st.executeUpdate(query) > 0;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      return res;
    }
  }

  /**
   *
   * @param newS
   * @return
   * @throws DBException
   */
  public boolean updateDatesSubject(Logic.Subject newS) throws DBException {

    if (newS.getId() == Defs.nullId) {
      throw new DBException("updateSubject", DBException.errorCode.INVALID_ID);
    }

    String query = "UPDATE " + DBNAME + ".subject SET start=?, finish=? WHERE subject.id=?;";

    boolean res = false;
    try (PreparedStatement pst = con.prepareStatement(query)) {
      pst.setDate(1, new java.sql.Date(newS.getStartDate().getTime()));
      pst.setDate(2, new java.sql.Date(newS.getFinishDate().getTime()));
      pst.setLong(3, newS.getId());
      res = pst.executeUpdate() > 0;
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      return res;
    }
  }

  /**
   * Actualitza un calendari a la base de dades. Si aquest objecte calendari te dies assignats,
   * també els actualitzara (encara que no calgui).
   *
   * @param newC
   * @return
   * @throws Utils.Exceptions.DBException
   */
  public boolean updateCalendar(Utils.Calendar.Calendar newC) throws DBException {
    if (newC.getId() == Defs.nullId) {
      throw new DBException("updateCalendar", DBException.errorCode.INVALID_ID);
    }

    boolean res = false;
    String calQuery = "UPDATE " + DBNAME + ".calendar "
            + "SET hoursday=" + newC.getHoursDay() + ", daysweek=" + newC.getDaysWeek() + ", turn=" + newC.getTurn() + " "
            + "WHERE calendar.id=" + newC.getId() + ";";
    String dayQuery = "UPDATE " + DBNAME + ".day "
            + "SET day=?, starthour=?, endhour=?"
            + "WHERE day.id=?";
    try (PreparedStatement pst = con.prepareStatement(dayQuery);
            Statement st = con.createStatement()) {
      con.setAutoCommit(false);

      if (newC.hasDays()) {
        for (Utils.Calendar.Day d : newC.getDBDays()) {
          pst.setString(1, d.getName().toString());
          pst.setInt(2, d.getStartHour());
          pst.setInt(3, d.getEndHour());
          pst.setLong(4, d.getId());

          pst.executeUpdate();
        }
      }

      res = st.executeUpdate(calQuery) > 0;

      con.commit();
      con.setAutoCommit(true);
    } catch (SQLException ex) {
      try {
        con.rollback();
        con.setAutoCommit(true);
      } catch (SQLException ex1) {
        Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex1);
      }
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return res;
  }

  /**
   * Actualitza un estudi especial (taller de treball, conferència o taula rodona). Els camps
   * alterables són el professor i/o l'aula
   *
   * @param newS Objecte estudi especial
   * @return <b>true</b> si tot ha anat bé, <b>false</b> altrament.
   * @throws DBException si l'id de l'estudi es <i>Defs.nullId</i> o no s'ha actualitzat cap dels
   * camps o el nif del professor no ha passat el test
   * <i>Defs.isValidNif</i>
   */
  public boolean updateSpeStudy(Logic.SpeStudy newS) throws DBException {

    if (newS.getId() == Defs.nullId) {
      throw new DBException("updateSpeStudy", DBException.errorCode.INVALID_ID);
    }

    return updateSubject(newS.getSpeAttr());
  }

  /**
   * Cerca professors segons el seu nom.
   *
   * @param name
   * @return LinkedList(Teacher) amb tots els professors amb un nom tal que
   * <i>name LIKE name%</i> o <b>null</b> si s'ha produit un error.
   */
  public java.util.LinkedList<Logic.Teacher> findTeacher(String name) {
    String query = "SELECT nif, name, firstname, lastname, phone FROM " + DBNAME + ".teacher WHERE name LIKE '" + Defs.formatName(name) + "%';";
    java.util.LinkedList<Logic.Teacher> t = null;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        t = new java.util.LinkedList<>();
        do {
          t.add(new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("nif"), rs.getString("phone")));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return t;
  }

  /**
   * Cerca professors segons el seu nom.
   *
   * @param name
   * @param fname
   * @return LinkedList(Teacher) amb tots els professors amb un nom tal que
   * <i>name LIKE name% AND firstname LIKE fname%</i> o <b>null</b> si s'ha produit un error.
   */
  public java.util.LinkedList<Logic.Teacher> findTeacher(String name, String fname) {
    String query = "SELECT nif, name, firstname, lastname, phone FROM " + DBNAME + ".teacher "
            + "WHERE name LIKE '" + Defs.formatName(name) + "%' AND firstname LIKE '" + Defs.formatString(fname) + "%';";
    java.util.LinkedList<Logic.Teacher> t = null;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        t = new java.util.LinkedList<>();
        do {
          t.add(new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("nif"), rs.getString("phone")));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return t;
  }

  /**
   * Cerca a professors segons el seu nom.
   *
   * @param name
   * @param fname
   * @param lname
   * @return LinkedList(Teacher) amb tots els professors amb un nom tal que
   * <i>name LIKE name% AND firstname LIKE fname% AND lastname LIKE lname%</i> o
   * <b>null</b> si s'ha produit un error.
   */
  public java.util.LinkedList<Logic.Teacher> findTeacher(String name, String fname, String lname) {
    String query = "SELECT nif, name, firstname, lastname, phone FROM " + DBNAME + ".teacher "
            + "WHERE name LIKE '" + Defs.formatName(name) + "%' "
            + "AND firstname LIKE '" + Defs.formatString(fname) + "%' "
            + "AND lastname LIKE '" + Defs.formatString(lname) + "%';";
    java.util.LinkedList<Logic.Teacher> t = null;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);
      if (rs.next()) {
        t = new java.util.LinkedList<>();
        do {
          t.add(new Logic.Teacher(this, rs.getString("name"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("nif"), rs.getString("phone")));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return t;
  }

  /**
   * Cerca assignatures segons el seu nom.
   *
   * @param name
   * @return LinkedList(Subject) amb totes les assignatures amb un nom tal que
   * <i>name LIKE name%</i> o <b>null</b> si s'ha produit un error.
   * @throws Utils.Exceptions.DBException Segons el que passi al métode loadSubjects
   */
  public java.util.LinkedList<Logic.Subject> findSubject(String name) throws DBException {
    String cond = "subject.name LIKE '" + Defs.formatString(name) + "%'";
    return loadSubjects(cond);
  }

  /**
   * Cerca estudis normals segons el seu nom.
   *
   * @param name
   * @return LinkedList(NormStudy) amb tots els estudis normals amb un nom tal que <i>name LIKE
   * name%</i> o <b>null</b> si s'ha produit un error.
   */
  public java.util.LinkedList<Logic.NormStudy> findNormStudy(String name) {
    String query = "SELECT norm_study.id AS nst_id, name, type, course.id AS c_id, start, finish FROM " + DBNAME + "."
            + "norm_study INNER JOIN " + DBNAME + ".course ON norm_study.name LIKE '" + name + "%' AND norm_study.course=course.id;";
    java.util.LinkedList<Logic.NormStudy> n = null;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);

      if (rs.next()) {
        Logic.Course c;
        n = new java.util.LinkedList<>();
        do {
          c = new Logic.Course(rs.getLong("c_id"), (java.sql.Date) rs.getDate("start"), (java.sql.Date) rs.getDate("finish"));
          n.add(new Logic.NormStudy(this, rs.getLong("nst_id"), rs.getString("name"), Logic.Study.StudyType.valueOf(rs.getString("type")), c));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return n;
  }

  /**
   * Cerca estudis especials segons el seu nom.
   *
   * @param name
   * @return LinkedList(SpeStudy) amb tots els estudis especials amb un nom tal que <i>name LIKE
   * name%</i> o <b>null</b> si s'ha produit un error.
   */
  public java.util.LinkedList<Logic.SpeStudy> findSpeStudy(String name) {
    String query = "SELECT spe_study.id AS spe_id, spe_study.name AS name, type, subject.id AS s_id, start, finish "
            + "FROM " + DBNAME + ".spe_study INNER JOIN " + DBNAME + ".subject ON spe_study.name LIKE '" + name + "%' AND spe_study.speattr=subject.id";
    java.util.LinkedList<Logic.SpeStudy> sp = null;
    try (Statement st = con.createStatement()) {
      rs = st.executeQuery(query);

      if (rs.next()) {
        Logic.Subject s;
        sp = new java.util.LinkedList<>();

        do {
          s = new Logic.Subject(this, rs.getLong("s_id"), rs.getString("name"), (java.sql.Date) rs.getDate("start"), (java.sql.Date) rs.getDate("finish"));
          sp.add(new Logic.SpeStudy(this, rs.getLong("spe_id"), rs.getString("name"), Logic.Study.StudyType.valueOf(rs.getString("type")), s));
        } while (rs.next());
      }
    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sp;
  }

  /**
   * Elimina un professor de la base de dades. També actualitza les assignatures coresponents.
   *
   * @param nif
   * @throws DBException si el nif no passa la validació <i>Defs.isValidNif</i>
   */
  public void deleteTeacher(String nif) throws DBException {
    if (!Defs.isValidNif(nif)) {
      throw new DBException("updateTeacher", DBException.errorCode.INVALID_NIF);
    }

    String updateQuery = "UPDATE " + DBNAME + ".subject SET subject.teacher=? WHERE subject.teacher=?;";
    String delQuery = "DELETE FROM " + DBNAME + ".teacher WHERE teacher.nif='" + nif + "';";
    try (PreparedStatement ps = con.prepareStatement(updateQuery);
            Statement st = con.createStatement()) {

      ps.setNull(1, java.sql.Types.CHAR);
      ps.setString(2, nif);

      ps.executeUpdate();

      st.executeUpdate(delQuery);

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Elimina una aula de la base de dades. També actualitza les assignatures corresponents.
   *
   * @param id
   * @throws DBException si id es <i>Defs.nullId</i>
   */
  public void deleteClassroom(long id) throws DBException {
    if (id == Defs.nullId) {
      throw new DBException("deleteClassroom", DBException.errorCode.INVALID_ID);
    }

    String updateQuery = "UPDATE " + DBNAME + ".subject SET subject.classroom=? WHERE subject.classroom=?;";
    String delQuery = "DELETE FROM " + DBNAME + ".classroom WHERE classroom.id=" + id + ";";
    try (PreparedStatement ps = con.prepareStatement(updateQuery);
            Statement st = con.createStatement()) {

      ps.setNull(1, java.sql.Types.BIGINT);
      ps.setLong(2, id);

      ps.executeUpdate();

      st.executeUpdate(delQuery);

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Elimina un estudi normal(i el curs associat). També elimina les assignatures pertinents
   *
   * @param s
   * @throws DBException si Study.id es <i>Defs.nullId</i> o Course.id es
   * <i>Defs.nullId</i>
   */
  public void deleteNormStudy(Logic.NormStudy s) throws DBException {
    if (s.getId() == Defs.nullId || s.getCourse().getId() == Defs.nullId) {
      throw new DBException("deleteClassroom", DBException.errorCode.INVALID_ID);
    }

    String delQuery = "DELETE FROM " + DBNAME + ".subject WHERE subject.study=" + s.getId() + ";";
    String delQuery1 = "DELETE FROM " + DBNAME + ".norm_study WHERE norm_study.id=" + s.getId() + ";";
    String delQuery2 = "DELETE FROM " + DBNAME + ".course WHERE course.id=" + s.getCourse().getId() + ";";

    try (Statement st = con.createStatement()) {

      st.executeUpdate(delQuery);
      st.executeUpdate(delQuery1);
      st.executeUpdate(delQuery2);

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Elimina un estudi especial i la corresponent assignatura associada.
   *
   * @param s
   * @throws DBException si Study.id es <i>Defs.nullId</i> o Subject.id es
   * <i>Defs.nullId</i>
   */
  public void deleteSpeStudy(Logic.SpeStudy s) throws DBException {
    if (s.getId() == Defs.nullId || s.getSpeAttr().getId() == Defs.nullId) {
      throw new DBException("deleteSpeStudy", DBException.errorCode.INVALID_ID);
    }

    String delQuery = "DELETE FROM " + DBNAME + ".spe_study WHERE spe_study.id=" + s.getId() + ";";
    String delQuery1 = "DELETE FROM " + DBNAME + ".subject WHERE subject.id=" + s.getSpeAttr().getId() + ";";

    try (Statement st = con.createStatement()) {

      st.executeUpdate(delQuery);
      st.executeUpdate(delQuery1);

    } catch (SQLException ex) {
      Logger.getLogger(DBI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
