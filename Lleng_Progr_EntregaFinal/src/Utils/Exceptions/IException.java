/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Exceptions;

import static Definitions.Defs.DEBUG;

/**
 *
 * @author pere
 */
public class IException {
  
  public static void print(String file, String method, String err) {
    if (DEBUG) {
      System.err.println(file+"::"+method+"::"+err);
    }
  }
  
}
