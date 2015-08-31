/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Definitions;

/**
 *
 * @author PereBalaguer
 */
public class Defs {

  // Constants -> MAJUSCULES
  // Variables -> camelCase
  // Metodes -> camelCase
  // Classes -> CamelCase
  // Llenguatge per noms de variables i clases -> Angles
  public static boolean DEBUG = true;
  // database values
  public static final long nullId = -1;
  public static final int nifLength = 9;
  public static final int phoneLength = 14;
  public static final int nameLength = 30;
  public static final int stringLength = 60;

  /**
   * Valida el nif. Perqué un nif sigui vàlid ha de complir:
   * <ol>
   * <li>llongitut == <i>Defs.nifLength</i></li>
   * <li>charset <b>in</b> <i>[^a-z0-9]</i></li>
   * </ol>
   *
   * @param nif
   * @return <b>true</b> si el nif satisfà les condicions.
   */
  public static final boolean isValidNif(String nif) {
    //return nif.length() == nifLength && !nif.toLowerCase().matches("[^a-z0-9]");
    return nif.length() == nifLength && !nif.toLowerCase().matches("[^a-z0-9]");
  }

  /**
   * Valida i sanititza el nif. El formateig consisteix en:
   * <ol>
   * <li>limitar la llongitut a <i>Defs.nifLength</i></li>
   * <li>fer la substitució <i>[^a-z0-9]</i> per <i>0</i></li>
   * </ol>
   *
   * @param nif String
   * @return String
   */
  public static final String formatNif(String nif) {
    if (nif.length() < nifLength) {
      nif = new StringBuilder(nifLength).append(nif.toLowerCase()).toString();
    } else if (nif.length() > nifLength) {
      nif = nif.substring(0, nifLength).toLowerCase();
    }
    return nif.replaceAll("[^a-z0-9]", "0");
  }

  /**
   * Valida i sanititza l'String. El formateig consisteix en:
   * <ol>
   * <li>limitar la llongitut a <i>Defs.phoneLength</i></li>
   * <li>
   * fer la substitució <i>[^0-9]</i> per <i>""</i> a fi de permetre separadors
   * a l'input
   * </li>
   * </ol>
   *
   * @param phone String
   * @return String
   */
  public static final String formatPhone(String phone) {
    phone = phone.replaceAll("[^0-9]", ""); // asi se permiten inputs tipo XXX {SEP} XXX {SEP} XXX 
    return ((phone.length() > phoneLength) ? phone.substring(0, phoneLength) : phone);
  }

  /**
   * Valida i sanititza l'String. Noti's que aquest mètode sols ha de ser emprat
   * per validar el nom de <b>persones</b>. Per els altres noms, emprar
   * <i>formatString</i>. El formateig consisteix en:
   * <ol>
   * <li>limitar la llongitut a <i>Defs.nameLength</i></li>
   * <li>fer la substitució <i>[^A-Za-z]</i> per <i>""</i></li>
   * </ol>
   *
   * @param name String
   * @return String
   */
  public static final String formatName(String name) {
    return ((name.length() > nameLength) ? name.substring(0, nameLength) : name).replaceAll("[^A-Za-z]", "");
  }

  /**
   * Valida i sanititza l'String. El formateig consisteix en:
   * <ol>
   * <li>limitar la llongitut a <i>Defs.stringLength</i></li>
   * <li>fer la substitució <i>[^A-Za-z0-9]</i> per <i>""</i></li>
   * </ol>
   *
   * @param others String
   * @return String
   */
  public static final String formatString(String others) {
    return ((others.length() > stringLength) ? others.substring(0, stringLength) : others).replaceAll("[^A-Za-z0-9]", "");
  }
}
