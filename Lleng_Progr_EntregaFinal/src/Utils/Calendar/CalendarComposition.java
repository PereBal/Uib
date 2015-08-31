/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Calendar;

import java.util.ArrayList;

/**
 *
 * @author pere
 */
public class CalendarComposition {
  private ArrayList<Calendar> cal;
  
  public CalendarComposition(Calendar calendar) {
    this.cal = new ArrayList<>();
    this.cal.add(calendar);
  }
  
  public CalendarComposition(ArrayList<Calendar> calendar) {
    this.cal = new ArrayList<>(calendar.size());
    this.cal.addAll(calendar);
  }
  
  public void addCalElem(Calendar calendar) {
    this.cal.add(calendar);
  }
  
  public void addAllCalElem(ArrayList<Calendar> calendar) {
    if (calendar != null) {
      this.cal.addAll(calendar);
    }
  }
  
  public ArrayList<Calendar> getCalElem() {
    return this.cal;
  }
  
  public String toString() {
    if (cal == null || cal.isEmpty()) {
      return "null";
    }
    String s = "CalendarComposition{";
    for (Calendar c : cal) {
      s += c.toString() + ",";
    }
    s = s.substring(0, s.length() - 1);
    s += '}';
    return s;
  }
}
