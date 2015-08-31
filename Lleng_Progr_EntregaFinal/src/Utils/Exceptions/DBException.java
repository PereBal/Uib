/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Exceptions;

/**
 *
 * @author pere
 */
public class DBException extends Exception {
  
  public enum errorCode {
    INVALID_NIF, 
    INVALID_ID,
    NO_PHONE,
    NO_CHANGES,
    NULL_COND,
    BAD_COND;
    
    @Override
    public String toString() {
      String s = "";
      switch (ordinal()) {
        case 0:
          s = "El nif no ha passat la validació de Defs.isValidNif";
          break;
        case 1:
          s = "L'id de l'element és Defs.nullId";
          break;
        case 2:
          s = "El professor no té teléfon";
          break;
        case 3:
          s = "Cap dels camps modificables de l'element, ha estat actualitzat";
          break;
        case 4:
          s = "No s'ha especificat cap condició";
          break;
        case 5:
          s = "La condició especificada conté caràcters no acceptats";
      }
      return s;
    }
  }
  
  public DBException(String method, errorCode eC) {
    super("DBI.java::" + method + "::" + eC.toString());
  }
}
