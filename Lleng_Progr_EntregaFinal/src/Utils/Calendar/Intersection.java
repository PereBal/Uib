package Utils.Calendar;

import static GUI.Main.connection;
import Logic.Study;
import Utils.Exceptions.DBException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dobleme
 */
public class Intersection {

  /**
   * S'hi introdueix una composicio de calendaris i retorna una matriu 5 dias i 14 hores (5x14
   * booleanes) amb l'ocupacio de dita composicio. Es retorna la composicio dintre una fecha en
   * especific.
   *
   * @param cals
   * @param startDate
   * @param finishDate
   * @return
   */
  public static boolean[][] mapOfCalendars(CalendarComposition cals, java.util.Date startDate, java.util.Date finishDate) {
    boolean allWeek[][] = new boolean[5][14];
    for (boolean[] allWeek1 : allWeek) {
      for (int j = 0; j < allWeek1.length; j++) {
        allWeek1[j] = true;
      }
    }

    if (cals != null) {
      cals.getCalElem().stream().forEach((c) -> {
        try {
          if (startDate.compareTo(c.getSubject().getFinishDate()) < 1 && finishDate.compareTo(c.getSubject().getStartDate()) > -1) {
          //if (startDate.compareTo(c.getSubject().getFinishDate()) == -1 && finishDate.compareTo(c.getSubject().getStartDate()) == 1) {
            Day days[] = c.getDBDays();
            if (days == null) {
              days = c.getDays();
            }
            if (days != null) {
              for (int i = 0; i < c.getDaysWeek(); i++) { //Canviar aixo per dies no nuls
                for (int j = days[i].getStartHour(); j < days[i].getEndHour(); j++) {
                  if (c.getTurn() == Logic.Study.Turn.morning) {
                    allWeek[Day.dayOfWeekToNumber(days[i].getName())][j] = false;
                  } else if (c.getTurn() == Logic.Study.Turn.evening) {
                    allWeek[Day.dayOfWeekToNumber(days[i].getName())][j + 7] = false;
                  } else {
                    allWeek[Day.dayOfWeekToNumber(days[i].getName())][j] = false;
                    allWeek[Day.dayOfWeekToNumber(days[i].getName())][j + 7] = false;
                  }
                }
              }
            }
          }
        } catch (DBException ex) {
        }
      });
    }

    return allWeek;
  }

  /**
   * Unio de dues composicions de calendaris.
   *
   * @param cals1
   * @param cals2
   * @param startDate
   * @param finishDate
   * @return
   */
  public static boolean[][] unionOfCalendars(CalendarComposition cals1, CalendarComposition cals2, java.util.Date startDate, java.util.Date finishDate) {
    boolean map1[][] = Intersection.mapOfCalendars(cals1, startDate, finishDate);
    boolean map2[][] = Intersection.mapOfCalendars(cals2, startDate, finishDate);

    boolean allWeek[][] = new boolean[5][14];
    for (int i = 0; i < allWeek.length; i++) {
      for (int j = 0; j < allWeek[i].length; j++) {
        allWeek[i][j] = map1[i][j] && map2[i][j];
      }
    }

    return allWeek;
  }

  public static ArrayList<Logic.Classroom> availableClassrooms(Study.Turn turn, int startHour, int endHour, java.util.Calendar startDate, java.util.Calendar finishDate) {
    int offsetTeachers = 0;
    ArrayList<Logic.Classroom> listOfClassrooms = new ArrayList<>();
    ArrayList<Logic.Classroom> aux = GUI.Main.connection.loadClassrooms(offsetTeachers, 20);
    while (aux != null) {
      listOfClassrooms.addAll(aux);
      offsetTeachers += aux.size();
      aux = GUI.Main.connection.loadClassrooms(offsetTeachers, 20);
    }
    
    ArrayList<Logic.Classroom> finalList = new ArrayList<>();
    listOfClassrooms.stream().forEach((c) -> {
      try {
        boolean ok = true;
        for (Calendar cc : c.getCalendar().getCalElem()) {
          if (startDate.getTime().compareTo(cc.getSubject().getFinishDate()) < 1 && finishDate.getTime().compareTo(cc.getSubject().getStartDate()) > -1) {
            Day days[] = cc.getDays();
            for (Day d : days) {
              if (containsDay(Day.dayOfWeekToNumber(d.getName()) + 2, startDate, finishDate)) {
                if (cc.getTurn().equals(turn) || turn.equals(Study.Turn.morningAndEvening) || cc.getTurn().equals(Study.Turn.morningAndEvening)) {
                  if (startHour < d.getEndHour() && endHour > d.getStartHour()) {
                    ok = false; break;
                  }
                } 
              }
            }
          }
        }
        if (ok) {
          finalList.add(c);
        } else {
          System.out.println("tira");
        }
      } catch (DBException ex) {
        Logger.getLogger(Intersection.class.getName()).log(Level.SEVERE, null, ex);
      }
    });
    
    if (finalList.isEmpty()) {
      return null;
    }
    return finalList;
  }
  
  public static boolean availableChangeDates(Logic.Subject subject, Logic.Teacher teacher, Logic.Classroom classroom, java.util.Date startDate, java.util.Date finishDate) {
    try {
      ArrayList<Calendar> cal1 = new ArrayList<>();
      for (Calendar c : teacher.getCalendar().getCalElem()) {
        if (c.getId() != subject.getId()) {
          cal1.add(c);
        }
      }
      CalendarComposition cc1 = new CalendarComposition(cal1);

      ArrayList<Calendar> cal2 = new ArrayList<>();
      for (Calendar c : classroom.getCalendar().getCalElem()) {
        if (c.getId() != subject.getId()) {
          cal2.add(c);
        }
      }
      CalendarComposition cc2 = new CalendarComposition(cal2);

      boolean newWeek[][] = unionOfCalendars(cc1, cc2, startDate, finishDate);

      if (availableTime(newWeek, subject)) {
        reallocateSubjectCalendar(newWeek, subject);
      } else {
        return false;
      }
    } catch (DBException ex) {
      Logger.getLogger(Intersection.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
    return true;
  }

  public static boolean availableTime(boolean allWeek[][], Logic.Subject subject) throws DBException {
    Day[] days = subject.getCalendar().getDBDays();

    int castDaysEndHour[] = {0, 0, 0, 0, 0};
    if (days != null) {
      for (int i = 0; i < subject.getCalendar().getDaysWeek(); i++) {
        castDaysEndHour[Day.dayOfWeekToNumber(days[i].getName())] = days[i].getEndHour();
      }
    }

    for (int i = 0; i < allWeek.length; i++) {
      int j = 0, times = 0;
      while (j < allWeek[i].length / 2 && times != castDaysEndHour[i]) {
        if (subject.getTurn() == Logic.Study.Turn.morning) {
          if (allWeek[i][j]) {
            times++;
          } else {
            times = 0;
          }
        } else if (subject.getTurn() == Logic.Study.Turn.evening) {
          if (allWeek[i][j + 7]) {
            times++;
          } else {
            times = 0;
          }
        } else {
          if (allWeek[i][j] && allWeek[i][j + 7]) {
            times++;
          } else {
            times = 0;
          }
        }
        j++;
      }

      if (castDaysEndHour[i] != times) {
        return false;
      }
    }

    return true;
  }

  public static void reallocateSubjectCalendar(boolean allWeek[][], Logic.Subject subject) throws DBException {
    Day[] days = subject.getCalendar().getDBDays();
    if (days == null) {
      days = subject.getCalendar().getDays();
    }

    int castDaysEndHour[] = {0, 0, 0, 0, 0};
    for (int i = 0; i < subject.getCalendar().getDaysWeek(); i++) {
      castDaysEndHour[Day.dayOfWeekToNumber(days[i].getName())] = days[i].getEndHour();
    }

    int nDay = 0;
    for (int i = 0; i < allWeek.length; i++) {
      int j = 0, times = 0;
      while (j < allWeek[i].length / 2 && times != castDaysEndHour[i]) {
        if (subject.getTurn() == Study.Turn.morning) {
          if (allWeek[i][j]) {
            times++;
          } else {
            times = 0;
          }
        } else if (subject.getTurn() == Study.Turn.evening) {
          if (allWeek[i][j + 7]) {
            times++;
          } else {
            times = 0;
          }
        } else {
          if (allWeek[i][j] && allWeek[i][j + 7]) {
            times++;
          } else {
            times = 0;
          }
        }
        j++;
      }

      if (castDaysEndHour[i] != 0) {
        days[nDay].setStartHour(j - times);
        days[nDay].setEndHour(j);
        nDay++;
      }
    }

    subject.getCalendar().setDays(days);
  }

  public static boolean overlapedSubjects(Logic.Subject subject, Logic.Subject subject1) throws DBException {
    Day[] days = subject.getCalendar().getDays();
    Day[] days1 = subject1.getCalendar().getDays();

//    if (days != null && days1 != null && subject.getStartDate().compareTo(subject1.getFinishDate()) == -1
//            && subject.getFinishDate().compareTo(subject1.getFinishDate()) == 1) {
    if (days != null && days1 != null && subject.getStartDate().compareTo(subject1.getFinishDate()) < 1
            && subject.getFinishDate().compareTo(subject1.getStartDate()) > -1) {
      for (Day d : days) {
        for (Day d1 : days1) {
          if (d.getName() == d1.getName()) {
            if (subject.getTurn() == subject1.getTurn() || subject.getTurn() == Logic.Study.Turn.morningAndEvening
                    || subject1.getTurn() == Logic.Study.Turn.morningAndEvening) {
              if (d.getStartHour() > d1.getEndHour() && d.getEndHour() < d1.getStartHour()) {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  public static boolean isSpeStudy(Logic.Subject subject) throws DBException {
    ArrayList<Logic.SpeStudy> aux = new ArrayList<>();
    int i = 0;
    while (aux != null) {
      aux = GUI.Main.connection.loadSpeStudies(i, 1);
      if (aux != null && aux.get(0).getSpeAttr().getId() == subject.getId()) {
        return true;
      }
      i++;
    }

    return false;
  }

  /**
   * Miram si l'aula esta ocupada en aquests moments
   *
   * @param classroom
   * @return
   * @throws DBException
   */
  public static boolean isBusy(Logic.Classroom classroom) throws DBException {
    try {
      for (Calendar cal : classroom.getCalendar().getCalElem()) {
        if (java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getFinishDate()) < 1
                && java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getStartDate()) > -1) {
          return true;
        }
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Miram si el professor esta ocupat en aquests moments.
   *
   * @param teacher
   * @return
   * @throws DBException
   */
  public static boolean isBusy(Logic.Teacher teacher) throws DBException {
    try {
      for (Calendar cal : teacher.getCalendar().getCalElem()) {
        if (java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getFinishDate()) < 1
                && java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getStartDate()) > -1) {
          return true;
        }
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param normalStudy
   * @return
   * @throws DBException
   */
  public static boolean isBusy(Logic.NormStudy normalStudy) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(normalStudy.getCourse().getFinishDate()) < 1
              && java.util.Calendar.getInstance().getTime().compareTo(normalStudy.getCourse().getStartDate()) > -1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param specialStudy
   * @return
   * @throws DBException
   */
  public static boolean isBusy(Logic.SpeStudy specialStudy) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(specialStudy.getSpeAttr().getFinishDate()) < 1
              && java.util.Calendar.getInstance().getTime().compareTo(specialStudy.getSpeAttr().getStartDate()) > -1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param subject
   * @return
   * @throws DBException
   */
  public static boolean isBusy(Logic.Subject subject) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(subject.getFinishDate()) < 1
              && java.util.Calendar.getInstance().getTime().compareTo(subject.getStartDate()) > -1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Miram si l'aula esta ocupada en aquests moments
   *
   * @param classroom
   * @return
   * @throws DBException
   */
  public static boolean willBeBusy(Logic.Classroom classroom) throws DBException {
    try {
      for (Calendar cal : classroom.getCalendar().getCalElem()) {
        if (java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getStartDate()) < 1) {
          return true;
        }
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Miram si el professor esta ocupat en aquests moments.
   *
   * @param teacher
   * @return
   * @throws DBException
   */
  public static boolean willBeBusy(Logic.Teacher teacher) throws DBException {
    try {
      for (Calendar cal : teacher.getCalendar().getCalElem()) {
        if (java.util.Calendar.getInstance().getTime().compareTo(cal.getSubject().getStartDate()) < 1) {
          return true;
        }
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param normalStudy
   * @return
   * @throws DBException
   */
  public static boolean willBeBusy(Logic.NormStudy normalStudy) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(normalStudy.getCourse().getStartDate()) < 1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param specialStudy
   * @return
   * @throws DBException
   */
  public static boolean willBeBusy(Logic.SpeStudy specialStudy) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(specialStudy.getSpeAttr().getStartDate()) < 1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   *
   * @param subject
   * @return
   * @throws DBException
   */
  public static boolean willBeBusy(Logic.Subject subject) throws DBException {
    try {
      if (java.util.Calendar.getInstance().getTime().compareTo(subject.getStartDate()) < 1) {
        return true;
      }
    } catch (NullPointerException e) {
    }

    return false;
  }
  
  private static boolean containsDay(int day, java.util.Calendar startDate, java.util.Calendar finishDate) {
    java.util.Calendar aux = (java.util.Calendar) startDate.clone();
    int days = 0;
    while (aux.compareTo(finishDate) < 1 && aux.get(java.util.Calendar.DAY_OF_WEEK) != day && days < 7) {
      days++;
      aux.add(java.util.Calendar.DATE, 1);
    }
    
    return aux.get(java.util.Calendar.DAY_OF_WEEK) == day;
  }
}
