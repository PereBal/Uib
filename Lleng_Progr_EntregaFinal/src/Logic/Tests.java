package Logic;

import Definitions.Defs;
import Utils.Calendar.Calendar;
import Utils.Calendar.Day;
import Utils.DB.DBI;
import Utils.Exceptions.DBException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pere
 */
public class Tests {

  private static ArrayList<Teacher> StoreTeachers(DBI db, int ntests, Random rnd, boolean verbose) throws DBException {
    Teacher t;
    ArrayList<Teacher> tal = new ArrayList<>(ntests);
    String nif;
    String name;
    String lastname;
    String phone;
    float ida;
    int talidx = 0;

    // Store test
    for (int i = 0; i < ntests; i++) {
      int id = rnd.nextInt(1000000);
      nif = "";
      for (int j = 0; j < Defs.nifLength - 1; j++) {
        nif += rnd.nextInt(10);
      }
      nif += "k";

      if (rnd.nextInt(2) == 0) {
        lastname = rnd.nextInt(1000000) + "Lastname";
      } else {
        lastname = null;
      }

      phone = "" + rnd.nextInt(1000000);
      // workarround because names can't contain digits
      ida = id;
      name = "";
      while (ida < 100) {
        ida = rnd.nextInt(1000000);
      }
      while (ida > 1) {
        name += (char) ('a' + ((int) ida % 10));
        ida /= 10;
      }
      name += "T";
      t = new Teacher(db, name, id + "Firstname", lastname, nif, phone);
      if (verbose) {
        System.out.println("Teacher before insert " + t);
      }
      t = db.storeTeacher(t);
      if (verbose) {
        System.out.println("Teacher after insert " + t + "\n");
      }
      tal.add(t);
    }
    if (verbose) {
      System.out.print("\n");
    }

    return tal;
  }

  private static ArrayList<Classroom> StoreClassrooms(DBI db, int ntests, Random rnd, boolean verbose) {
    Classroom c;
    ArrayList<Classroom> cal = new ArrayList<>(ntests);
    int cap;
    Classroom.ClassroomType type;

    for (int i = 0; i < ntests; i++) {
      cap = rnd.nextInt(1000);
      type = Classroom.ClassroomType.valueOf("" + ((char) ('A' + rnd.nextInt(5))));

      c = new Classroom(db, cap, type);
      if (verbose) {
        System.out.println("Classroom before insert: " + c);
      }
      c = db.storeClassroom(c);
      if (verbose) {
        System.out.println("Classroom after insert: " + c + "\n");
      }
      cal.add(c);
    }
    if (verbose) {
      System.out.print("\n");
    }

    return cal;
  }

  private static ArrayList<Calendar> StoreCalendar(DBI db, int ntests, Random rnd, boolean verbose) {
    ArrayList<Calendar> cal = new ArrayList<>(10);
    Calendar c;
    Day[] cdays = new Day[5];
    int sh;
    int hd;
    int dw;
    Day.DayOfWeek[] dow = Day.DayOfWeek.values();
    Study.Turn[] trn = Study.Turn.values();
    for (int i = 0; i < ntests; i++) {
      hd = rnd.nextInt(8);
      dw = 0;
      for (int j = 0; j < cdays.length; j++) {
//        if (rnd.nextInt(6) < 5) {
          sh = rnd.nextInt(21);
          while (sh + hd > 20) {
            sh--;
          }
          
          cdays[j] = new Day(dow[j], sh, sh + hd);
          dw++;
//        } else {
//          break; // el constructor de calendar posara la resta (cdays.len - dw) de dies a nul
//        }
      }

      c = new Calendar(db, hd, dw, trn[rnd.nextInt(trn.length)], cdays);
      if (verbose) {
        System.out.println("Calendar before insert: " + c);
      }
      c = db.storeCalendar(c);
      if (verbose) {
        System.out.println("Calendar after insert: " + c);
      }
      cal.add(c);
    }
    if (verbose) {
      System.out.print("\n");
    }

    return cal;
  }

  private static ArrayList<NormStudy> StoreNormStudy(DBI db, int ntests, Random rnd, boolean verbose) {
    NormStudy ns;
    Course c;
    Study.StudyType[] sty = Study.StudyType.values();
    String name;
    Date date = new Date();

    ArrayList<NormStudy> nal = new ArrayList<>(ntests);
    for (int i = 0; i < ntests; i++) {
      c = new Course(date, date);
      if (verbose) {
        System.out.println("Course before insert: " + c);
      }
      c = db.storeCourse(c);
      if (verbose) {
        System.out.println("Course after insert: " + c);
      }

      if (verbose) {
        System.out.println("\n");
      }

      name = "nm" + rnd.nextInt(1000000);
      // recordar que els estudis normals son de master o especialitzacio (que son els 2 primers de l'enumerat)
      ns = new NormStudy(db, name, sty[rnd.nextInt(2)], c);
      if (verbose) {
        System.out.println("NormStudy before insert: " + ns);
      }
      ns = db.storeNormStudy(ns);
      if (verbose) {
        System.out.println("NormStudy after insert: " + ns);
      }
      nal.add(ns);
    }
    if (verbose) {
      System.out.print("\n");
    }

    return nal;
  }

  private static ArrayList<Subject> StoreSubjects(DBI db, int ntests, boolean verbose,
          ArrayList<Teacher> teachers, ArrayList<Classroom> classrooms, ArrayList<NormStudy> nstudies, ArrayList<Calendar> calendars) {
    Subject s;
    String name;
    Random rnd = new Random();
    Date date = new Date();
    final int clsz = classrooms.size();
    final int tsz = teachers.size();
    final int nsz = (nstudies == null) ? 0 : nstudies.size();
    final int csz = calendars.size();
    List lst;

    ArrayList<Subject> sal = new ArrayList<>(ntests);
    for (int i = 0; i < ntests; i++) {
      name = "" + rnd.nextInt(1000000) + "nm";
      s = new Subject(db, name, date, date,
              (nstudies == null) ? null : nstudies.get(rnd.nextInt(nsz)), teachers.get(rnd.nextInt(tsz)), classrooms.get(rnd.nextInt(clsz)), calendars.get(rnd.nextInt(csz)));
      if (verbose) {
        System.out.println("Subject before insert: " + s);
      }
      s = db.storeSubject(s);
      if (verbose) {
        System.out.println("Subject after insert: " + s);
      }
      sal.add(s);
    }
    if (verbose) {
      System.out.println("\n");
    }

    return sal;
  }

  private static ArrayList<SpeStudy> StoreSpeStudy(DBI db, int ntests, Random rnd, boolean verbose, ArrayList<Subject> subjects) {
    ArrayList<SpeStudy> sal = new ArrayList<>(ntests);
    SpeStudy sp;
    Study.StudyType[] st = Study.StudyType.values();
    String name;
    for (int i = 0; i < ntests; i++) {
      name = rnd.nextInt(1000000) + "nm";
      // recordem que els estudis especials sols poden agafar els darrers 3 valors de l'enum StudyType
      sp = new SpeStudy(db, name, st[st.length - rnd.nextInt(3) - 1], subjects.get(i));
      if (verbose) {
        System.out.println("SpeStudy before insert: " + sp);
      }
      sp = db.storeSpeStudy(sp);
      if (verbose) {
        System.out.println("SpeStudy after insert: " + sp);
      }
      sal.add(sp);
    }
    if (verbose) {
      System.out.println("\n");
    }

    return sal;
  }

  private static void CleanTeachers(DBI db, boolean verbose) throws SQLException {
    // clean tested table
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from teacher;");
    if (verbose) {
      System.out.println("delete from teacher; ---> DONE\n\n");
    }
  }

  private static void CleanClassrooms(DBI db, boolean verbose) throws SQLException {
    // clean tested table
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from classroom;");
    if (verbose) {
      System.out.println("delete from classroom; ---> DONE\n\n");
    }
  }

  private static void CleanCalendar(DBI db, boolean verbose) throws SQLException {
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from day;");
    if (verbose) {
      System.out.println("delete from day ---> DONE!\n\n");
    }
    st.executeUpdate("delete from calendar;");
    if (verbose) {
      System.out.println("delete from calendar ---> DONE!\n\n");
    }
  }

  private static void CleanNormStudy(DBI db, boolean verbose) throws SQLException {
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from norm_study;");
    if (verbose) {
      System.out.println("delete from norm_study; ---> DONE!\n\n");
    }
    st.executeUpdate("delete from course;");
    if (verbose) {
      System.out.println("delete from course; ---> DONE!\n\n");
    }
  }

  private static void CleanSubjects(DBI db, boolean verbose) throws SQLException {
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from subject;");
    if (verbose) {
      System.out.println("delete from subject; ---> DONE!\n\n");
    }
    CleanTeachers(db, verbose);
    CleanClassrooms(db, verbose);
    CleanCalendar(db, verbose);
    CleanNormStudy(db, verbose);
  }

  private static void CleanSpeStudy(DBI db, boolean verbose) throws SQLException {
    Statement st = db.getConn().createStatement();
    st.executeUpdate("delete from spe_study;");
    if (verbose) {
      System.out.println("delete form spe_study; ---> DONE!\n\n");
    }
    CleanSubjects(db, verbose);
  }

  // Store, find, load & update
  public static ArrayList<Teacher> ModularTeacherOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose) throws SQLException {
    ArrayList<Teacher> tal = null;
    try {
      String nm;
      String fnm;
      String lnm;
      Random rnd = new Random();
      Teacher t;
      LinkedList<Teacher> tll;
      String lastname;
      String phone;
      int talidx = 0;
      Iterator<Teacher> teachit;

      // Store test
      tal = StoreTeachers(db, ntests, rnd, verbose);

      t = tal.get(talidx++);

      if (t == null) {
        System.err.println("FUUUCK");
        return null;
      }

      // find by name test
      nm = t.getName();
      nm = nm.substring(0, 1);
      tll = db.findTeacher(nm);
      if (verbose) {
        System.out.println("Teachers with name like: " + nm);
      }
      if (tll != null) {
        teachit = tll.iterator();
        while (teachit.hasNext()) {
          if (verbose) {
            System.out.println(teachit.next());
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      // find by name and firstname test
      fnm = t.getFirstName();
      fnm = fnm.substring(0, 2);
      tll = db.findTeacher(nm, fnm);
      if (verbose) {
        System.out.println("Teachers with name like: " + nm + " and firstname like: " + fnm);
      }
      if (tll != null) {
        teachit = tll.iterator();
        while (teachit.hasNext()) {
          if (verbose) {
            System.out.println(teachit.next());
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      // find by name and lastname test
      lnm = t.getLastName();
      while (lnm == null) {
        lnm = tal.get(talidx++).getLastName();
      }
      nm = tal.get(talidx - 1).getName();
      nm = nm.substring(0, 1);
      fnm = tal.get(talidx - 1).getFirstName();
      fnm = fnm.substring(0, 2);
      lnm = lnm.substring(0, 3);
      tll = db.findTeacher(nm, fnm, lnm);
      if (verbose) {
        System.out.println("Teachers with name like: " + nm + " and firstname like: " + fnm + " and lastname like: " + lnm);
      }
      if (tll != null) {
        teachit = tll.iterator();
        while (teachit.hasNext()) {
          if (verbose) {
            System.out.println(teachit.next());
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      // load Test
      teachit = tal.iterator();
      if (verbose) {
        System.out.println("Teachers by nif: ");
      }
      while (teachit.hasNext()) {
        t = teachit.next();
        if (verbose) {
          System.out.print("Nif: " + t.getNif() + " ");
        }
        t = db.loadTeacher(t.getNif());
        if (verbose) {
          System.out.println("Teacher: " + t);
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      // update Test
      if (verbose) {
        System.out.println("Teacher update: ");
      }
      teachit = tal.iterator();
      talidx = 0;
      while (teachit.hasNext()) {
        t = teachit.next();
        if (verbose) {
          System.out.print("Before update: " + t);
        }
        lastname = rnd.nextInt(1000000) + "Lastname";
        phone = "";
        for (int i = 0; i < 9; i++) {
          phone += rnd.nextInt(10);
        }
        t.setLastName(lastname);
        t.setPhone(phone);
        if (verbose) {
          System.out.print("After update: " + t);
        }

        if (db.updateTeacher(t)) {
          if (verbose) {
            System.out.println(" ---> Success!");
          }
          tal.set(talidx, t);
        } else {
          if (verbose) {
            System.out.println(" ---> Failure!");
          }
        }
        talidx++;
      }
      if (verbose) {
        System.out.println("\n");
      }

      // after update test
      if (verbose) {
        System.out.println("Teachers adter update: ");
      }
      for (Teacher te : tal) {
        if (verbose) {
          System.out.println("Teacher: " + db.loadTeacher(te.getNif()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }
    } catch (Exception ex) {
      Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
    } finally {

      if (cleanData) {
        CleanTeachers(db, verbose);
        return null;
      }
      return tal;
    }
  }

  // Store, load & update
  public static ArrayList<Classroom> ModularClassroomOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose) throws SQLException {
    ArrayList<Classroom> cal = null;
    try {
      Classroom c;
      int calidx;
      int cap;
      Classroom.ClassroomType type;
      Random rnd = new Random();

      cal = StoreClassrooms(db, ntests, rnd, verbose);

      if (verbose) {
        System.out.println("Classrooms by id: ");
      }
      for (Classroom cl : cal) {
        c = db.loadClassroom(cl.getId());
        if (verbose) {
          System.out.println(c);
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Update Classrooms: ");
      }
      Iterator<Classroom> calit = cal.iterator();
      calidx = 0;
      while (calit.hasNext()) {
        c = calit.next();
        if (verbose) {
          System.out.print("oldCl: " + c);
        }
        cap = rnd.nextInt(1000);
        type = Classroom.ClassroomType.valueOf("" + (char) ('A' + rnd.nextInt(5)));
        c.setCapacity(cap);
        c.setType(type);
        if (verbose) {
          System.out.print(" newCl: " + c);
        }

        if (db.updateClassroom(c)) {
          if (verbose) {
            System.out.println(" ---> Success!");
          }
          cal.set(calidx, c);
        } else {
          if (verbose) {
            System.out.println(" ---> Failure!");
          }
        }
        calidx++;
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Classrooms by id after updates: ");
      }
      for (Classroom cl : cal) {
        if (verbose) {
          System.out.println(db.loadClassroom(cl.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }
    } catch (Exception ex) {
      Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (cleanData) {
        CleanClassrooms(db, verbose);
        return null;
      }
      return cal;
    }
  }

  // Store
  public static ArrayList<Calendar> ModularCalendarOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose) throws SQLException {
    ArrayList<Calendar> cal;
    Random rnd = new Random();

    cal = StoreCalendar(db, ntests, rnd, verbose);

    if (cleanData) {
      CleanCalendar(db, verbose);
      return null;
    } else {
      return cal;
    }
  }

  // Store, load
  public static ArrayList<NormStudy> ModularNormStudyOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose) throws SQLException {
    ArrayList<NormStudy> nal = null;
    try {
      Random rnd = new Random();

      nal = StoreNormStudy(db, ntests, rnd, verbose);

      if (verbose) {
        System.out.println("Load operations: ");
      }
      for (NormStudy n : nal) {
        if (verbose) {
          System.out.println(db.loadCourse(n.getCourse().getId()));
        }
        if (verbose) {
          System.out.println(db.loadNormStudy(n.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }
    } catch (Exception ex) {
      Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (cleanData) {
        CleanNormStudy(db, verbose);
        return null;
      }
      return nal;
    }
  }

  // Store, find, load & update
  public static ArrayList<Subject> ModularSubjectOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose,
          ArrayList<Teacher> teachers, ArrayList<Classroom> classrooms, ArrayList<NormStudy> nstudies, ArrayList<Calendar> calendars) throws SQLException {
    ArrayList<Subject> sal = null;
    try {
      String name;
      Random rnd = new Random();
      int salidx;
      final int clsz = classrooms.size();
      final int tsz = teachers.size();
      List lst;

      sal = StoreSubjects(db, ntests, verbose, teachers, classrooms, nstudies, calendars);

      if (verbose) {
        System.out.println("Load subjects: ");
      }
      for (Subject su : sal) {
        if (verbose) {
          System.out.println(db.loadSubject(su.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      name = "" + rnd.nextInt(10);
      if (verbose) {
        System.out.println("Find subjects by name: " + name);
      }
      lst = db.findSubject(name);
      if (lst != null) {
        for (Object su : lst) {
          if (verbose) {
            System.out.println(su);
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Update subject");
      }
      salidx = 0;
      for (Subject su : sal) {
        if (verbose) {
          System.out.print("Before: " + su + " --> ");
        }
        su.setClassroom(classrooms.get(rnd.nextInt(clsz)));
        su.setTeacher(teachers.get(rnd.nextInt(tsz)));
        if (verbose) {
          System.out.print("After: " + su);
        }

        if (db.updateSubject(su)) {
          if (verbose) {
            System.out.println(" ---> Success!");
          }
          sal.set(salidx, su);
        } else {
          if (verbose) {
            System.out.println(" ---> Failure!");
          }
        }
        salidx++;
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Load after update: ");
      }
      for (Subject su : sal) {
        if (verbose) {
          System.out.println(db.loadSubject(su.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

    } catch (Exception ex) {
      Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (cleanData) {
        CleanSubjects(db, verbose);
        return null;
      }
      return sal;
    }
  }

  // Store, find, load & update
  public static ArrayList<SpeStudy> ModularSpeStudyOpsTest(DBI db, int ntests, boolean cleanData, boolean verbose, ArrayList<Subject> subjects) throws SQLException {
    ArrayList<SpeStudy> sp = null;
    try {
      Random rnd = new Random();
      String nm;
      List lst;
      final int spsz;
      sp = StoreSpeStudy(db, ntests, rnd, verbose, subjects);
      spsz = sp.size();

      if (verbose) {
        System.out.println("Load test: ");
      }
      for (SpeStudy s : sp) {
        if (verbose) {
          System.out.println(db.loadSpeStudy(s.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      nm = "" + rnd.nextInt(10);
      if (verbose) {
        System.out.println("Find by name: " + nm);
      }
      lst = db.findSpeStudy(nm);
      if (lst != null) {
        for (Object s : lst) {
          if (verbose) {
            System.out.println(s);
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Update test: ");
      }
      for (SpeStudy s : sp) {
        if (verbose) {
          System.out.print("Before: " + s + " ---> ");
        }
        s.setClassroom(sp.get(rnd.nextInt(spsz)).getClassroom());
        s.setTeacher(sp.get(rnd.nextInt(spsz)).getTeacher());
        if (verbose) {
          System.out.print("After: " + s);
        }
        if (db.updateSpeStudy(s)) {
          if (verbose) {
            System.out.println(" ---> Success!");
          }
        } else {
          if (verbose) {
            System.out.println("Failure!");
          }
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

      if (verbose) {
        System.out.println("Load after update: ");
      }
      for (SpeStudy s : sp) {
        if (verbose) {
          System.out.println(db.loadSpeStudy(s.getId()));
        }
      }
      if (verbose) {
        System.out.println("\n");
      }

    } catch (Exception ex) {
      Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      if (cleanData) {
        CleanSpeStudy(db, verbose);
        return null;
      }
      return sp;
    }
  }

  public static void IntegrationTests(DBI db) throws SQLException {
    List lst;
    System.out.println("Teachers tests: ");
    LinkedList<Teacher> teachers = db.findTeacher("a");
    if (teachers != null) {
      for (Teacher t : teachers) {
        try {
          System.out.println("Full teacher: " + t.getNif() + ", " + t.getName() + ", " + t.getFirstName()
                  + ", " + t.getLastName() + ", " + t.getPhone() + ", " + ((t.getSubjects() == null) ? null : t.getSubjects().toString())
                  + ", " + t.getCalendar());
        } catch (DBException ex) {
          Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
    System.out.println("\n");

    System.out.println("Classroom tests: ");
    LinkedList<Classroom> classrooms = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      try {
        lst = db.findSubject(""+i);
        if (lst != null) {
          for(Object subj : lst) {
            classrooms.add(db.loadClassroom(((Subject)subj).getClassroom().getId()));
          }
        }
        for (Classroom cl : classrooms) {
          System.out.println("Full classroom: "+cl.getId() +", " +cl.getType() + ", "+cl.getCapacity() + ", "+cl.getCalendar());
        }
        classrooms.clear();
      } catch (DBException ex) {
        Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void main(String[] args) throws DBException, SQLException {
    // Testign
    DBI db = new DBI();
    Random rnd = new Random();
    List lst;
    final boolean verbose = true;
    final boolean auxVerbose = false;
    final boolean cleanData = false;
    final int ntests = 20;
    
//    lst = ModularTeacherOpsTest(db, ntests, cleanData, verbose);
//    if (lst != null) {
//      for (Object t : lst) {
//        if (verbose) System.out.println(t);
//      }
//      if (verbose) System.out.println("\n");
//    }
//
//    lst = ModularClassroomOpsTest(db, ntests, cleanData, verbose);
//    if (lst != null) {
//      for (Object c : lst) {
//        if (verbose) System.out.println(c);
//      }
//      if (verbose) System.out.println("\n");
//    }
//
//    lst = ModularCalendarOpsTest(db, ntests, cleanData, verbose);
//    if (lst != null) {
//      for (Object c : lst) {
//        if (verbose) System.out.println(c);
//      }
//      if (verbose) System.out.println("\n");
//    }
//    
//    lst = ModularNormStudyOpsTest(db, ntests, cleanData, verbose);
//    if (lst != null) {
//      for (Object ns : lst) {
//        if (verbose) System.out.println(ns);
//      }
//      if (verbose) System.out.println("\n");
//    }
    
//    lst = ModularSubjectOpsTest(db, ntests, cleanData, verbose,
//            StoreTeachers(db, ntests, rnd, auxVerbose), StoreClassrooms(db, ntests, rnd, auxVerbose), 
//            StoreNormStudy(db, ntests, rnd, auxVerbose), StoreCalendar(db, ntests, rnd, auxVerbose));
//    if (lst != null) {
//      for (Object s : lst) {
//        if (verbose) {
//          System.out.println(s);
//        }
//      }
//      if (verbose) {
//        System.out.println("\n");
//      }
//    }
//    lst = ModularSpeStudyOpsTest(db, ntests, cleanData, verbose,
//            ModularSubjectOpsTest(db, ntests, false, auxVerbose,
//                    StoreTeachers(db, ntests, rnd, auxVerbose), StoreClassrooms(db, ntests, rnd, auxVerbose),
//                    null, StoreCalendar(db, ntests, rnd, auxVerbose)));
//    if (lst != null) {
//      for (Object sp : lst) {
//        if (verbose) {
//          System.out.println(sp);
//        }
//      }
//    }
//    if (verbose) {
//      System.out.println("\n");
//    }

    // hi ha d'haver un conjunt ben format d'elements a la db
    //IntegrationTests(db);
//    IntegrationTests(db);
    db.closeDB();
  }
}
