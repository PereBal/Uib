/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Body.Consultations;

import Definitions.Defs;
import GUI.Body.BodyGUI;
import GUI.Body.Calendar.CalendarPaneGUI;
import static GUI.Main.connection;
import GUI.Utils.Dialogs;
import GUI.Utils.SpringUtilities;
import Logic.Teacher;
import Utils.Calendar.Intersection;
import Utils.Exceptions.DBException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dobleme
 */
public class ConsultationTeacherGUI extends javax.swing.JPanel {

  public ConsultationTeacherGUI(BodyGUI body, Teacher teacher) {
    this.teacher = teacher;
    this.body = body;
    initComponents();
  }

  private void initComponents() {

    jLabelName = new javax.swing.JLabel();
    jLabelTeacherName = new javax.swing.JLabel();
    jLabelFirstName = new javax.swing.JLabel();
    jLabelTeacherFirstName = new javax.swing.JLabel();
    jLabelLastName = new javax.swing.JLabel();
    jLabelTeacherLastName = new javax.swing.JLabel();
    jLabelNif = new javax.swing.JLabel();
    jLabelTeacherNif = new javax.swing.JLabel();
    jLabelPhone = new javax.swing.JLabel();
    jLabelTeacherPhone = new javax.swing.JLabel();
    jButtonModify = new javax.swing.JButton();
    jButtonDrop = new javax.swing.JButton();
    try {
      cal = new CalendarPaneGUI(teacher.getSubjects(),teacher.getCalendar(),0);
    } catch (DBException ex) {
      Logger.getLogger(ConsultationTeacherGUI.class.getName()).log(Level.SEVERE, null, ex);
    }

    setMinimumSize(new java.awt.Dimension(1150, 670));
    setPreferredSize(new java.awt.Dimension(1150, 670));

    jLabelName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelName.setText("Nom:");

    jLabelTeacherName.setText(teacher.getName());
    jLabelTeacherName.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabelTeacherName.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabelTeacherName.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabelFirstName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelFirstName.setText("Cognom:");

    jLabelTeacherFirstName.setText(teacher.getFirstName());
    jLabelTeacherFirstName.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabelTeacherFirstName.setMinimumSize(new java.awt.Dimension(0, 15));
    jLabelTeacherFirstName.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabelNif.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelNif.setText("NIF:");

    jLabelTeacherNif.setText(teacher.getNif());
    jLabelTeacherNif.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabelTeacherNif.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabelTeacherNif.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabelLastName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelLastName.setText("Cognom:");

    jLabelTeacherLastName.setText(teacher.getLastName());
    jLabelTeacherLastName.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabelTeacherLastName.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabelTeacherLastName.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabelPhone.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelPhone.setText("Telefon:");

    jLabelTeacherPhone.setText(teacher.getPhone());
    jLabelTeacherPhone.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabelTeacherPhone.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabelTeacherPhone.setName(""); // NOI18N
    jLabelTeacherPhone.setPreferredSize(new java.awt.Dimension(184, 15));

    jButtonModify.setBackground(new java.awt.Color(241, 241, 241));
    jButtonModify.setText("Modificar");
    jButtonModify.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonModifyActionPerformed(evt);
      }
    });

    jButtonDrop.setBackground(new java.awt.Color(255, 51, 51));
    jButtonDrop.setText("Tramitar Baixa");
    jButtonDrop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDropActionPerformed(evt);
      }
    });
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(81, 81, 81)
                                    .addComponent(jButtonDrop))
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(100, 100, 100)
                                    .addComponent(jButtonModify))
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(30, 30, 30)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabelLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabelTeacherLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabelNif)
                                                            .addComponent(jLabelName)
                                                            .addComponent(jLabelFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabelPhone))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(jLabelTeacherPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabelTeacherNif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabelTeacherName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabelTeacherFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGap(12, 12, 12)
                    .addComponent(cal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabelTeacherLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelLastName)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelTeacherName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabelTeacherFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(115, 115, 115)
                                    .addComponent(jLabelTeacherNif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabelTeacherPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelName)
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabelFirstName)
                                    .addGap(115, 115, 115)
                                    .addComponent(jLabelNif)
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabelPhone)))
                    .addGap(100, 100, 100)
                    .addComponent(jButtonModify)
                    .addGap(54, 54, 54)
                    .addComponent(jButtonDrop)
                    .addContainerGap(141, Short.MAX_VALUE))
            .addComponent(cal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
  }

  private void jButtonDropActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      if (Intersection.isBusy(teacher) || Intersection.willBeBusy(teacher)) {
        Dialogs.attemptDropBusy(this, "Professor");
      } else {
        connection.deleteTeacher(teacher.getNif());
        Dialogs.successfulUniqueDrop(this, "Professor");
        body.showHome();
      }
    } catch (DBException ex) {
      Dialogs.notSuccessfulDrop(this, "Professor");
      //Logger.getLogger(ConsultationTeacherGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {
    javax.swing.JLabel jLabelUpdateLastName = new javax.swing.JLabel("Segón Cognom:", javax.swing.JLabel.TRAILING);
    jLabelUpdateLastName.setFont(new java.awt.Font("Dialog", 0, 12));
    javax.swing.JLabel jLabelUpdatePhone = new javax.swing.JLabel("Telèfon:", javax.swing.JLabel.TRAILING);
    jLabelUpdatePhone.setFont(new java.awt.Font("Dialog", 0, 12));

    javax.swing.JTextField jTextFieldUpdateLastName = new javax.swing.JTextField(30);
    javax.swing.JFormattedTextField jFormattedTextFieldUpdatePhone = null;
    try {
      jFormattedTextFieldUpdatePhone = new javax.swing.JFormattedTextField(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("### ### ###")));
    } catch (ParseException ex) {
      Logger.getLogger(ConsultationTeacherGUI.class.getName()).log(Level.SEVERE, null, ex);
    }

    javax.swing.JPanel panel = new javax.swing.JPanel(new javax.swing.SpringLayout());

    panel.add(jLabelUpdateLastName);
    jLabelUpdateLastName.setLabelFor(jTextFieldUpdateLastName);
    panel.add(jTextFieldUpdateLastName);

    panel.add(jLabelUpdatePhone);
    jLabelUpdatePhone.setLabelFor(jFormattedTextFieldUpdatePhone);
    panel.add(jFormattedTextFieldUpdatePhone);

    SpringUtilities.makeCompactGrid(panel, 2, 2, 6, 6, 6, 6);

    boolean okUpdate = true;

    if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, panel, "Modificació", JOptionPane.OK_CANCEL_OPTION)) {
      if (jFormattedTextFieldUpdatePhone.getText().matches("(\\d){3}\\s(\\d){3}\\s(\\d){3}")) {
        teacher.setPhone(Defs.formatPhone(jFormattedTextFieldUpdatePhone.getText()));
      }
      if (jTextFieldUpdateLastName.getText().matches("[a-zA-Z]+")) {
        teacher.setLastName(Defs.formatString(jTextFieldUpdateLastName.getText()));
      }
      try {
        okUpdate = connection.updateTeacher(teacher);
      } catch (DBException ex) {
        Logger.getLogger(ConsultationTeacherGUI.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        if (okUpdate) {
          jLabelTeacherPhone.setText(teacher.getPhone());
          jLabelTeacherLastName.setText(teacher.getLastName());
          repaint();
        } else {
          String msg = "<html>"
                  + "<h4>Hi ha hagut un problema al connectar-se a la base de dades local.</h4>"
                  + "</html>";
          javax.swing.JLabel label = new javax.swing.JLabel(msg);
          label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
          javax.swing.JOptionPane.showMessageDialog(null, label, "Error connexió BD", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
      }

    }
  }

  private javax.swing.JButton jButtonDrop;
  private javax.swing.JButton jButtonModify;
  private javax.swing.JLabel jLabelTeacherFirstName;
  private javax.swing.JLabel jLabelTeacherNif;
  private javax.swing.JLabel jLabelTeacherLastName;
  private javax.swing.JLabel jLabelTeacherPhone;
  private javax.swing.JLabel jLabelFirstName;
  private javax.swing.JLabel jLabelNif;
  private javax.swing.JLabel jLabelName;
  private javax.swing.JLabel jLabelLastName;
  private javax.swing.JLabel jLabelTeacherName;
  private javax.swing.JLabel jLabelPhone;
  private javax.swing.JPanel cal;
  private final Teacher teacher;
  
  private final BodyGUI body;
}
