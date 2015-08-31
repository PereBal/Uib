/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lleng_progr_ex2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author pere
 */
public class Lleng_Progr_ex2 extends javax.swing.JFrame {

  private JPanel jpCenter;
  private JTextField jtfInput;
  private final Calc_Body cb;

  private final String[] jbNames = {"C", "A", "e", "/", "7", "8", "9", "*", "4", "5", "6", "+", "1", "2", "3", "-", "0", ".", "%", "="};
  private JButton[] jb;

  private final int prHeight = 450;
  private final int prWidth = 400;
  private final int jtextWeight = 9;
  private final Font f;
  private final Font f2;
  private int prAction = -1;

  private boolean enter = true;

  public Lleng_Progr_ex2() {
    cb = new Calc_Body();
    f = new Font("Times New Roman", Font.PLAIN, prHeight / jtextWeight);
    f2 = new Font("Times New Roman", Font.PLAIN, prHeight / (jtextWeight * 2));
    initComponents();
  }

  private void initComponents() {
    this.setTitle("Calculadora");
    this.getContentPane().setLayout(new GridBagLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(prWidth, prHeight);

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 4;
    c.weightx = 0.5;
    c.weighty = (float) (10 - jtextWeight) / 10;

    jtfInput = new JTextField("0.0");
    jtfInput.setEditable(false);
    //jtfInput.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    jtfInput.setFont(f);
    jtfInput.setBorder(BorderFactory.createLoweredBevelBorder());
    jtfInput.setBackground(new Color(219, 218, 215));
    this.getContentPane().add(jtfInput, c);

    ActionListener al;
    al = (ActionEvent ae) -> {
      String s = ae.getActionCommand();
      boolean stIni;
      if (prAction == 4) {
        jtfInput.setText("0.0");
        stIni = true;
      } else {
        stIni = jtfInput.getText().equals("0.0");
      }
      enter = (!stIni && !jtfInput.getText().contains(".")) || prAction < 5;
      switch (s) {
        case "C":
          cb.clear();
          jtfInput.setText("0.0");
          prAction = 0;
          break;
        case "A":
          cb.acOn();
          prAction = 1;
          break;
        case "e":
          jtfInput.setText("e");
          cb.e();
          prAction = 2;
          break;
        case "/":
        case "*":
        case "+":
        case "-":
        case "%":
          if (prAction == 2 || prAction == 6) {
            if (cb.push(s.charAt(0))) {
              jtfInput.setText("" + cb.top());
            } else {
              jtfInput.setText(jtfInput.getText() + s);
            }
            prAction = 3;
          }
          break;
        case "=":
          if (prAction == 2 || prAction == 6) {
            cb.push('=');
            jtfInput.setText("" + cb.top());
            prAction = 4;
          }
          break;
        case ".":
          if (stIni) {
            jtfInput.setText("0.");
          } else {
            jtfInput.setText(jtfInput.getText() + ".");
          }
          cb.beginDecPart();
          prAction = 5;
          break;
        default: // 0..9
          if (prAction != 2) {
            if (prAction == 4) {
              cb.clearStack();
            }
            if (stIni) {
              jtfInput.setText(s);
            } else {
              jtfInput.setText(jtfInput.getText() + s);
            }
            if (enter) {
              cb.actTermeEnter(Integer.parseInt(s));
            } else {
              cb.actTermeDecimal(Integer.parseInt(s));
            }
            prAction = 6;
          }
      }
    };

    Gest_Mouse num_tecl = new Gest_Mouse(new Color(105, 102, 102), new Color(66, 66, 66), Color.black);
    Gest_Mouse op_tecl = new Gest_Mouse(new Color(5, 202, 245), new Color(2, 175, 214), Color.black);
    Gest_Mouse remainder = new Gest_Mouse(new Color(20, 240, 0), new Color(17, 207, 0), Color.black);

    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 0.5;
    c.weighty = (float) jtextWeight / 10;

    jpCenter = new JPanel();
    jpCenter.setLayout(new GridLayout(5, 4));
    jb = new JButton[20];

    for (int i = 0; i < 20; i++) {
      jb[i] = new JButton(jbNames[i]);
      jb[i].setName(jbNames[i]);
      jb[i].setBorder(null);
      jb[i].setFont(f2);
      jb[i].setFocusPainted(false);
      if (i < 3) {
        jb[i].addMouseListener(remainder);
        jb[i].setBackground(remainder.defaultColor());
      } else if (((jbNames[i].charAt(0) >= '0' && jbNames[i].charAt(0) <= '9') || jbNames[i].charAt(0) == '.' || jbNames[i].charAt(0) == '%')) {
        jb[i].setForeground(Color.WHITE);
        jb[i].addMouseListener(num_tecl);
        jb[i].setBackground(num_tecl.defaultColor());
      } else if (jbNames[i].charAt(0) == '/' || jbNames[i].charAt(0) == '+' || jbNames[i].charAt(0) == '-' || jbNames[i].charAt(0) == '*' || jbNames[i].charAt(0) == '=') {
        jb[i].addMouseListener(op_tecl);
        jb[i].setBackground(op_tecl.defaultColor());
      }
      jb[i].addActionListener(al);
      jpCenter.add(jb[i]);
    }

    /* Manera alternativa de hacer lo mismo usando un GridBagLayout (segun el lookAndFeel seria estrictamente necesario usar este 2º metodo)*/
//    jpCenter.setLayout(new GridBagLayout());
//
//    GridBagConstraints ic = new GridBagConstraints();
//    int i;
//
//    JPanel jpDummy = new JPanel();
//    jpDummy.setLayout(new GridLayout(1, 3));
//    jpDummy.setBackground(Color.gray);
//    ic.fill = GridBagConstraints.BOTH;
//    ic.gridx = 0;
//    ic.gridy = 0;
//    ic.gridwidth = 3;
//    ic.gridheight = 1;
//    ic.weightx = (float) 3 / 4;
//    ic.weighty = (float) 1 / 5;
//
//    for (i = 0; i < 3; i++) {
//      jb[i] = new JButton(jbNames[i]);
//      jb[i].setBorder(null);
//      jb[i].setFont(f2);
//      jb[i].setBackground(jpDummy.getBackground());
//      jb[i].addActionListener(al);
//      jb[i].addMouseListener(remainder);
//      jpDummy.add(jb[i]);
//    }
//    jpCenter.add(jpDummy, ic);
//
//    jpDummy = new JPanel();
//    jpDummy.setLayout(new GridLayout(5, 1));
//    jpDummy.setBackground(Color.yellow);
//    ic.fill = GridBagConstraints.BOTH;
//    ic.gridx = GridBagConstraints.RELATIVE;
//    ic.gridy = 0;
//    ic.gridwidth = 1;
//    ic.gridheight = 5;
//    ic.weightx = (float) 1 / 4;
//    ic.weighty = 1.0;
//
//    for (i = 3; i < 20; i++) {
//      if (jbNames[i].charAt(0) == '/' || jbNames[i].charAt(0) == '+' || jbNames[i].charAt(0) == '-' || jbNames[i].charAt(0) == '*' || jbNames[i].charAt(0) == '=') {
//        jb[i] = new JButton(jbNames[i]);
//        jb[i].setBorder(null);
//        jb[i].setFont(f2);
//        jb[i].setBackground(jpDummy.getBackground());
//        jb[i].addActionListener(al);
//        jb[i].addMouseListener(op_tecl);
//        jpDummy.add(jb[i]);
//      }
//    }
//    jpCenter.add(jpDummy, ic);
//
//    jpDummy = new JPanel();
//    jpDummy.setLayout(new GridLayout(4, 3));
//    jpDummy.setBackground(Color.gray);
//    ic.fill = GridBagConstraints.BOTH;
//    ic.gridx = 0;
//    ic.gridy = 1;
//    ic.gridwidth = 3;
//    ic.gridheight = 4;
//    ic.weightx = (float) 3 / 4;
//    ic.weighty = (float) 4 / 5;
//
//    for (i = 4; i < 20; i++) {
//      if (((jbNames[i].charAt(0) >= '0' && jbNames[i].charAt(0) <= '9') || jbNames[i].charAt(0) == '.' || jbNames[i].charAt(0) == '%')) {
//        jb[i] = new JButton(jbNames[i]);
//        jb[i].setBorder(null);
//        jb[i].setFont(f2);
//        jb[i].setBackground(jpDummy.getBackground());
//        jb[i].addActionListener(al);
//        jb[i].addMouseListener(num_tecl);
//        jpDummy.add(jb[i]);
//      }
//    }
//    jpCenter.add(jpDummy, ic);
    this.getContentPane().add(jpCenter, c);
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Lleng_Progr_ex2().setVisible(true);
      }
    });
  }

}
