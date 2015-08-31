/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public class Builder {

  public final static String build_Msg(Object[] args) throws ParseException {
    String s = "{";
    int i, len = args.length;
    if ((len % 2) != 0) {
      throw new ParseException(0);
    }
    for (i = 0; i < len - 1; i++) {
      if ((i % 2) == 0) {
        s += "\"" + args[i].toString() + "\": ";
      } else {
        s += "\"" + args[i].toString() + "\", ";
      }
    }
    s += "\"" + args[i] + "\"}";
    return s;
  }

  public final static JSONObject build_JSONObject(Object[] args) throws ParseException {
    int i, len = args.length;
    if ((len % 2) != 0) {
      throw new ParseException(0);
    }
    Object key = null;
    JSONObject obj = new JSONObject();
    for (i = 0; i < len; i++) {
      if ((i % 2) == 0) {
        key = args[i];
      } else {
        obj.put(key, args[i]);
      }
    }
    return obj;
  }

  public synchronized final static String getUniqueId() {
    /*if (Builder.idGenerator == (Long.MAX_VALUE - 1)) {
     Builder.idGenerator = 0;
     System.err.println("UniqueId Overflow, starting again at zero");
     } else {
     Builder.idGenerator++;
     }
     return "" + Builder.idGenerator;
     */
    //A lo bruto per poder seguir fent feina de moment << temporal
    return ""+Math.random()*10000;
  }

  // Mas adelante usare algo asi
  public synchronized final static String getNextUniqueId(String id) {
    long currentId = Integer.parseInt(id);
    if (currentId == (Integer.MAX_VALUE - 1)) {
      System.err.println("UniqueId Overflow, starting again at zero");
    } else {
      currentId++;
    }
    return "" + currentId;
  }
}
