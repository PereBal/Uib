/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Body.Forms.Add;

import Definitions.Defs;
import GUI.Body.BodyGUI;
import static GUI.Main.connection;
import Logic.Course;
import Logic.NormStudy;
import Logic.SpeStudy;
import Logic.Study.StudyType;
import Logic.Subject;
import java.awt.Color;
import java.util.Calendar;

/**
 *
 * @author dobleme
 */
public class FormAddStudyGUI extends javax.swing.JPanel {

  /**
   * Creates new form FormAltaEstudi
   *
   * @param body
   */
  public FormAddStudyGUI(BodyGUI body) {
    this.body = body;
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabelTitle = new javax.swing.JLabel();
    jSeparatorTitle = new javax.swing.JSeparator();
    jLabelStudyType = new javax.swing.JLabel();
    jLabelName = new javax.swing.JLabel();
    jComboBoxStudyType = new javax.swing.JComboBox();
    jLabelFinishDate = new javax.swing.JLabel();
    jLabelStartDate = new javax.swing.JLabel();
    jButtonNext = new javax.swing.JButton();
    jTextFieldName = new javax.swing.JTextField();
    jButtonCancel = new javax.swing.JButton();
    jDateChooserFinishDate = new com.toedter.calendar.JDateChooser();
    jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
    jLabelWarningName = new javax.swing.JLabel();

    setBackground(new java.awt.Color(255, 255, 255));
    setMinimumSize(new java.awt.Dimension(1150, 670));

    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabelTitle.setForeground(new java.awt.Color(255, 51, 51));
    jLabelTitle.setText("Alta Estudi");

    jSeparatorTitle.setForeground(new java.awt.Color(241, 241, 241));

    jLabelStudyType.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelStudyType.setText("Tipus d'Estudi:");

    jLabelName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelName.setText("Nom:");

    jComboBoxStudyType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Master", "Curs d'Especialització", "Taller", "Conferència", "Taula Rodona" }));
    jComboBoxStudyType.setMinimumSize(new java.awt.Dimension(550, 24));
    jComboBoxStudyType.setPreferredSize(new java.awt.Dimension(550, 24));

    jLabelFinishDate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelFinishDate.setText("Data de Final:");

    jLabelStartDate.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabelStartDate.setText("Data d'Inici:");

    jButtonNext.setBackground(new java.awt.Color(241, 241, 241));
    jButtonNext.setText("Següent");
    jButtonNext.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonNextActionPerformed(evt);
      }
    });

    jTextFieldName.setMinimumSize(new java.awt.Dimension(550, 28));
    jTextFieldName.setPreferredSize(new java.awt.Dimension(550, 28));
    jTextFieldName.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldNameFocusLost(evt);
      }
    });

    jButtonCancel.setBackground(new java.awt.Color(255, 51, 51));
    jButtonCancel.setText("Cancelar");
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCancelActionPerformed(evt);
      }
    });

    jDateChooserFinishDate.setEnabled(false);
    jDateChooserFinishDate.setMinSelectableDate(Calendar.getInstance().getTime());
    jDateChooserFinishDate.setPreferredSize(new java.awt.Dimension(90, 28));

    jDateChooserStartDate.setMinSelectableDate(Calendar.getInstance().getTime());
    jDateChooserStartDate.setPreferredSize(new java.awt.Dimension(90, 28));
    jDateChooserStartDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        jDateChooserStartDatePropertyChange(evt);
      }
    });

    jLabelWarningName.setFont(new java.awt.Font("Dialog", 2, 11)); // NOI18N
    jLabelWarningName.setText("(Màxim 60 caràcters)");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addComponent(jLabelTitle)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jSeparatorTitle)
        .addGap(38, 38, 38))
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(237, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabelStartDate)
          .addComponent(jLabelName)
          .addComponent(jLabelStudyType))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabelWarningName)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jComboBoxStudyType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
              .addComponent(jButtonCancel)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jButtonNext))
            .addGroup(layout.createSequentialGroup()
              .addComponent(jDateChooserStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGap(45, 45, 45)
              .addComponent(jLabelFinishDate)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
              .addComponent(jDateChooserFinishDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        .addContainerGap(254, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jSeparatorTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelTitle))
        .addGap(50, 50, 50)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabelName)
          .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabelWarningName)
        .addGap(60, 60, 60)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabelStartDate)
          .addComponent(jLabelFinishDate)
          .addComponent(jDateChooserFinishDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jDateChooserStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(80, 80, 80)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabelStudyType)
          .addComponent(jComboBoxStudyType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(80, 80, 80)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonNext)
          .addComponent(jButtonCancel))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
      Calendar startDate = jDateChooserStartDate.getCalendar();
      startDate.set(Calendar.HOUR_OF_DAY, 0);
      startDate.set(Calendar.MINUTE, 0);
      startDate.set(Calendar.SECOND, 0);
      startDate.set(Calendar.MILLISECOND, 0);
      
      Calendar finishDate = jDateChooserFinishDate.getCalendar();
      finishDate.set(Calendar.HOUR_OF_DAY, 0);
      finishDate.set(Calendar.MINUTE, 0);
      finishDate.set(Calendar.SECOND, 0);
      finishDate.set(Calendar.MILLISECOND, 0);

      if (wrongFormat <= 0 && startDate != null && finishDate != null && startDate.get(Calendar.DAY_OF_WEEK) != 7 && startDate.get(Calendar.DAY_OF_WEEK) != 1 && jComboBoxStudyType.getSelectedIndex() != -1
              && finishDate.get(Calendar.DAY_OF_WEEK) != 7 && finishDate.get(Calendar.DAY_OF_WEEK) != 1 && startDate.compareTo(finishDate) <= 0) {
        if (jComboBoxStudyType.getSelectedIndex() < 2) {
          NormStudy ns = new NormStudy(connection, Defs.formatString(jTextFieldName.getText()), getStudyType(), new Course(startDate.getTime(), finishDate.getTime()));
          body.showFormAssignationsNormStudy(ns);
        } else {
          Subject s = new Subject(connection, Defs.formatString(jTextFieldName.getText()), startDate.getTime(), finishDate.getTime(), null, null, null, null);
          SpeStudy ss = new SpeStudy(connection, Defs.formatString(jTextFieldName.getText()), getStudyType(), s);
          body.showFormAddSpecialStudy(ss);
        }
      } else {
        GUI.Utils.Dialogs.wrongInputForm(this);
      }
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
      body.showHome();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jTextFieldNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNameFocusLost
      if (jTextFieldName.getText().length() > Defs.stringLength || jTextFieldName.getText().length() == 0) {
        jLabelWarningName.setForeground(Color.red);
        wrongFormat = 1;
      } else {
        jLabelWarningName.setForeground(Color.black);
        wrongFormat = 0;
      }
    }//GEN-LAST:event_jTextFieldNameFocusLost

  private void jDateChooserStartDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooserStartDatePropertyChange
    if (jDateChooserStartDate.getCalendar() != null) {
      jDateChooserFinishDate.setEnabled(true);
    }
  }//GEN-LAST:event_jDateChooserStartDatePropertyChange

  private StudyType getStudyType() {
    if (jComboBoxStudyType.getSelectedIndex() == 0) {
      return StudyType.master;
    } else if (jComboBoxStudyType.getSelectedIndex() == 1) {
      return StudyType.specialization;
    } else if (jComboBoxStudyType.getSelectedIndex() == 2) {
      return StudyType.workshop;
    } else if (jComboBoxStudyType.getSelectedIndex() == 3) {
      return StudyType.conference;
    }

    return StudyType.roundTable;
  }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButtonCancel;
  private javax.swing.JButton jButtonNext;
  private javax.swing.JComboBox jComboBoxStudyType;
  private com.toedter.calendar.JDateChooser jDateChooserFinishDate;
  private com.toedter.calendar.JDateChooser jDateChooserStartDate;
  private javax.swing.JLabel jLabelFinishDate;
  private javax.swing.JLabel jLabelName;
  private javax.swing.JLabel jLabelStartDate;
  private javax.swing.JLabel jLabelStudyType;
  private javax.swing.JLabel jLabelTitle;
  private javax.swing.JLabel jLabelWarningName;
  private javax.swing.JSeparator jSeparatorTitle;
  private javax.swing.JTextField jTextFieldName;
  // End of variables declaration//GEN-END:variables

  private final BodyGUI body;
  private int wrongFormat = 1;
}
