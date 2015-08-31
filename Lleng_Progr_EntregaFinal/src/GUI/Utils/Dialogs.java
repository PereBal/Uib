package GUI.Utils;

import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

/**
 *
 * @author Lluc Martorell
 */
public class Dialogs {

  /**
   *
   * @param component
   * @param element
   * @param nameOfElement
   */
  public static void successfulAdd(java.awt.Component component, String element, String nameOfElement) {
    javax.swing.JOptionPane.showMessageDialog(component, element + " " + nameOfElement + " s'ha donat d'alta correctament.",
            "Alta Correcte", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   *
   * @param component
   * @param element
   */
  public static void notSuccessfulAdd(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>" + element + " no s'ha donat d'alta correctament.</h4>"
            + "<p>"
            + "Problema de connexió amb la BD."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Error Alta", javax.swing.JOptionPane.ERROR_MESSAGE);

  }

  /**
   *
   * @param component
   * @param element
   */
  public static void notSuccessfulDrop(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>" + element + " no s'ha donat de baixa correctament.</h4>"
            + "<p>"
            + "Problema de connexió amb la BD."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Error Baixa", javax.swing.JOptionPane.ERROR_MESSAGE);

  }

  /**
   *
   * @param component
   * @param element
   */
  public static void notSuccessfulMod(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>" + element + " no s'ha modificat correctament.</h4>"
            + "<p>"
            + "Problema de connexió amb la BD."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Error Baixa", javax.swing.JOptionPane.ERROR_MESSAGE);

  }

  /**
   *
   * @param component
   */
  public static void wrongFormat(java.awt.Component component) {
    String msg = "<html>"
            + "<h4>El format d'algun camp es inocrrecte o no l'ha introduit</h4>"
            + "<p>"
            + "Revisi tots els camps, i segueixi les indicacions que ha de seguir pel format."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Error Format", javax.swing.JOptionPane.ERROR_MESSAGE);

  }

  /**
   *
   * @param componenet
   */
  public static void wrongInputForm(java.awt.Component componenet) {
    String msg = "<html>"
            + "<h4>No es pot assignar per que algun camp està mal introduit.</h4>"
            + "<p>"
            + "Revisi que tots els camps s'hagin introduit.<br>Cap data pot estar en dia no laboral.<br>La data d'inici ha de ser anterior a la data final."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(componenet, label, "Error Alta", javax.swing.JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Dialog error bixa masiva.
   *
   * @param compoment
   * @param element
   */
  public static void attemptDropBusy(java.awt.Component compoment, String element) {
    String msg = "<html>"
            + "<h4>Algun(a) " + element + " que has intentat donar de baixa (o modificar), esta ocupat.</h4>"
            + "<p>"
            + "Només es donaran de baixa aquells elements que no estiguin ocupats."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(compoment, label, "Error Baixa", javax.swing.JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Dialog error bixa masiva.
   *
   * @param compoment
   * @param element
   */
  public static void attemptStudyDropFutureBusy(java.awt.Component compoment, String element) {
    String msg = "<html>"
            + "<h4>Algun(a) estudi que has intentat donar de baixa, estara ocupat en un futur.</h4>"
            + "<p>"
            + "Es donaran de baixa i s'eliminaran totes les seves assignatures(si es un estudi normal)."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(compoment, label, "Error Baixa", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Dialog exit baixa masiva.
   *
   * @param component
   * @param element
   */
  public static void successfulStudyDrop(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>S'han donat de baixat tots els(les) " + element + ".</h4>"
            + "<p>"
            + "Es donaran de baixa i s'eliminaran totes les seves assignatures(si es un estudi normal)."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Baixa Correcte", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Dialog exit baixa masiva.
   *
   * @param component
   * @param element
   */
  public static void successfulDrop(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>S'han donat de baixat tots els(les) " + element + ".</h4>"
            + "</html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Baixa Correcte", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Dialog exit baixa masiva.
   *
   * @param component
   * @param element
   */
  public static void successfulUniqueDrop(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>S'ha dona de baixa el(la) " + element + ".</h4>"
            + "</html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Baixa Correcte", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   *
   * @param component
   * @param element
   */
  public static void emptyElement(java.awt.Component component, String element) {
    String msg = "<html>"
            + "<h4>No hi ha cap " + element + " donada d'alta que compleixin els requeriments.</h4>"
            + "<p>"
            + "Afegeix algun(a) " + element + "."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "No hi ha elements", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   *
   * @param component
   */
  public static void succesfulModSubject(java.awt.Component component) {
    String msg = "<html>"
            + "<h4>L'assignatura s'ha recolocat.</h4></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "Modificat", javax.swing.JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   *
   * @param component
   */
  public static void notSuccesfulModSubject(java.awt.Component component) {
    String msg = "<html>"
            + "<h4>L'assignatura no es pot recolocar amb aquestes dates.</h4>"
            + "<p>"
            + "Prova de reasignar un professor nou i una aula."
            + "</p></html>";
    javax.swing.JLabel label = new javax.swing.JLabel(msg);
    label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
    javax.swing.JOptionPane.showMessageDialog(component, label, "No es pot modificar", javax.swing.JOptionPane.ERROR_MESSAGE);
  }
}
