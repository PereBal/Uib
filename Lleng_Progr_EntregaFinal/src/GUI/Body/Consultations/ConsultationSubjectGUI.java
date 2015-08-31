package GUI.Body.Consultations;

import GUI.Body.BodyGUI;
import GUI.Body.Calendar.CalendarPaneGUI;
import GUI.Utils.Dialogs;
import GUI.Utils.SpringUtilities;
import Logic.Subject;
import Utils.Calendar.CalendarComposition;
import Utils.Calendar.Intersection;
import Utils.Exceptions.DBException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConsultationSubjectGUI extends javax.swing.JPanel {

  public ConsultationSubjectGUI(BodyGUI body, Subject subject) {
    this.subject = subject;
    this.body = body;
    initComponents();
  }

  private void initComponents() {
    try {
//      buttonDelete = new javax.swing.JButton();
      buttonModify = new javax.swing.JButton();
      paneCalendar = new CalendarPaneGUI(null, new CalendarComposition(subject.getCalendar()), 2);
      labelStartDate = new javax.swing.JLabel();
      jLabel5 = new javax.swing.JLabel();
      labelTeacher = new javax.swing.JLabel();
      jLabel7 = new javax.swing.JLabel();
      labelFinishDate = new javax.swing.JLabel();
      jLabel9 = new javax.swing.JLabel();
      labelSubject = new javax.swing.JLabel();
      jLabel2 = new javax.swing.JLabel();
      labelClassroom = new javax.swing.JLabel();

      setMinimumSize(new java.awt.Dimension(1150, 670));
      setPreferredSize(new java.awt.Dimension(1150, 670));

//      buttonDelete.setBackground(new java.awt.Color(255, 51, 51));
//      buttonDelete.setText("Tramitar Baixa");
//      buttonDelete.addActionListener(new java.awt.event.ActionListener() {
//        public void actionPerformed(java.awt.event.ActionEvent evt) {
//          jButtonDeleteActionPerformed(evt);
//        }
//      });
      buttonModify.setBackground(new java.awt.Color(241, 241, 241));
      buttonModify.setText("Modificar");
      buttonModify.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
          jButtonModifyActionPerformed(evt);
        }
      });

      paneCalendar.setBackground(new java.awt.Color(255, 255, 255));
      paneCalendar.setMinimumSize(new java.awt.Dimension(850, 670));
      paneCalendar.setPreferredSize(new java.awt.Dimension(850, 670));

      labelStartDate.setText(subject.getStartDate().toLocaleString().split(" ")[0]);
      labelStartDate.setMaximumSize(new java.awt.Dimension(184, 15));
      labelStartDate.setMinimumSize(new java.awt.Dimension(1, 15));
      labelStartDate.setPreferredSize(new java.awt.Dimension(184, 15));

      jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel5.setText("Inici:");

      if (subject.getTeacher() == null) {
        labelTeacher.setText("Cap assignat");
      } else {
        labelTeacher.setText(subject.getTeacher().getName() + " " + subject.getTeacher().getFirstName());
      }
      labelTeacher.setMaximumSize(new java.awt.Dimension(184, 15));
      labelTeacher.setMinimumSize(new java.awt.Dimension(1, 15));
      labelTeacher.setPreferredSize(new java.awt.Dimension(184, 15));

      jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel7.setText("Professor:");

      labelFinishDate.setText(subject.getFinishDate().toLocaleString().split(" ")[0]);
      labelFinishDate.setMaximumSize(new java.awt.Dimension(184, 15));
      labelFinishDate.setMinimumSize(new java.awt.Dimension(1, 15));
      labelFinishDate.setName(""); // NOI18N
      labelFinishDate.setPreferredSize(new java.awt.Dimension(184, 15));

      jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel9.setText("Final:");

      labelSubject.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
      labelSubject.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
      labelSubject.setText("<html><p align = center>" + subject.getName() + "</p></html>");
      labelSubject.setMaximumSize(new java.awt.Dimension(276, 3999));
      labelSubject.setMinimumSize(new java.awt.Dimension(276, 99));
      labelSubject.setPreferredSize(new java.awt.Dimension(276, 99));

      jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
      jLabel2.setText("Aula:");

      labelClassroom.setText("Aula " + subject.getClassroom().getId() + " - " + subject.getClassroom().getType());
      labelClassroom.setMaximumSize(new java.awt.Dimension(184, 15));
      labelClassroom.setMinimumSize(new java.awt.Dimension(184, 15));
      labelClassroom.setPreferredSize(new java.awt.Dimension(184, 15));

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(layout.createSequentialGroup()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                              .addGroup(layout.createSequentialGroup()
                                                      .addGap(81, 81, 81)
                                              /*.addComponent(buttonDelete)*/)
                                              .addGroup(layout.createSequentialGroup()
                                                      .addGap(100, 100, 100)
                                                      .addComponent(buttonModify))
                                              .addGroup(layout.createSequentialGroup()
                                                      .addGap(22, 22, 22)
                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                              .addComponent(jLabel5)
                                                              .addComponent(jLabel9)
                                                              .addComponent(jLabel7)
                                                              .addComponent(jLabel2))
                                                      .addGap(18, 18, 18)
                                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                              .addComponent(labelFinishDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                              .addComponent(labelStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                              .addComponent(labelTeacher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                              .addComponent(labelClassroom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                      .addGap(12, 12, 12))
                              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                      .addComponent(labelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                      .addComponent(paneCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      layout.setVerticalGroup(
              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                      .addContainerGap()
                      .addComponent(labelSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(jLabel7)
                              .addComponent(labelTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                      .addGap(70, 70, 70)
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(labelClassroom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(jLabel2))
                      .addGap(70, 70, 70)
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(labelStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(jLabel5))
                      .addGap(70, 70, 70)
                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                              .addComponent(labelFinishDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addComponent(jLabel9))
                      .addGap(70, 70, 70)
                      .addComponent(buttonModify)
                      .addGap(54, 54, 54)
                      //                      .addComponent(buttonDelete)
                      .addContainerGap(103, Short.MAX_VALUE))
              .addComponent(paneCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );
    } catch (DBException ex) {
      Logger.getLogger(ConsultationSubjectGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      if (Intersection.willBeBusy(subject)) {
        javax.swing.JLabel jLabelUpdatestartDate = new javax.swing.JLabel("Data d'inici: ", javax.swing.JLabel.TRAILING);
        jLabelUpdatestartDate.setFont(new java.awt.Font("Dialog", 0, 12));
        javax.swing.JLabel jLabelUpdateFinishDate = new javax.swing.JLabel("Data de final: ", javax.swing.JLabel.TRAILING);
        jLabelUpdateFinishDate.setFont(new java.awt.Font("Dialog", 0, 12));

        jDateChooserStartDate = new com.toedter.calendar.JDateChooser();
        jDateChooserFinishDate = new com.toedter.calendar.JDateChooser();

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

        javax.swing.JPanel panel = new javax.swing.JPanel(new javax.swing.SpringLayout());

        panel.add(jLabelUpdatestartDate);
        jLabelUpdatestartDate.setLabelFor(jDateChooserStartDate);
        panel.add(jDateChooserStartDate);

        panel.add(jLabelUpdateFinishDate);
        jLabelUpdateFinishDate.setLabelFor(jDateChooserFinishDate);
        panel.add(jDateChooserFinishDate);

        SpringUtilities.makeCompactGrid(panel, 2, 2, 6, 6, 6, 6);

        String str[] = {"Reasig Dates", "Reasig Aul/Prof", "Cancel"};
        int response = JOptionPane.showOptionDialog(this, panel, "Modificacio", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, str, str[2]);
        if (response == 0) {
          if (ok(jDateChooserStartDate.getCalendar(), jDateChooserFinishDate.getCalendar())) {
            if (Intersection.availableChangeDates(subject, subject.getTeacher(), subject.getClassroom(), jDateChooserStartDate.getDate(), jDateChooserFinishDate.getDate())) {
              subject.setStartDate(jDateChooserStartDate.getDate());
              subject.setFinishDate(jDateChooserFinishDate.getDate());
              if (GUI.Main.connection.updateDatesSubject(subject)) {
                Dialogs.succesfulModSubject(this);
                Subject newS = GUI.Main.connection.loadSubject(subject.getId());
                body.consultationSubject(newS);
              } else {
                Dialogs.notSuccessfulMod(this, "L'Assignatura");
              }
            } else {
              Dialogs.notSuccesfulModSubject(this);
            }
          } else {
            GUI.Utils.Dialogs.wrongInputForm(this);
          }
        } else if (response == 1) {
          if (ok(jDateChooserStartDate.getCalendar(), jDateChooserFinishDate.getCalendar())) {
            subject.setStartDate(jDateChooserStartDate.getDate());
            subject.setFinishDate(jDateChooserFinishDate.getDate());
            if (GUI.Main.connection.updateDatesSubject(subject)) {
              body.showFormAssignationsSubject(subject, null, true);
            } else {
              Dialogs.notSuccessfulMod(this, "L'Assignatura");
            }
          } else {
            body.showFormAssignationsSubject(subject, null, true);
          }
        }
      } else {
        Dialogs.attemptDropBusy(this, "Assignatura");
      }
    } catch (DBException ex) {
      Logger.getLogger(ConsultationSubjectGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private boolean ok(Calendar startDate, Calendar finishDate) {
    return (startDate != null && finishDate != null && startDate.get(Calendar.DAY_OF_WEEK) != 7 && startDate.get(Calendar.DAY_OF_WEEK) != 1
            && finishDate.get(Calendar.DAY_OF_WEEK) != 7 && finishDate.get(Calendar.DAY_OF_WEEK) != 1 && startDate.compareTo(finishDate) <= 0);

  }

  private void jDateChooserStartDatePropertyChange(java.beans.PropertyChangeEvent evt) {
    if (jDateChooserStartDate.getCalendar() != null) {
      jDateChooserFinishDate.setEnabled(true);
    }
  }

  private CalendarPaneGUI paneCalendar;
//  private javax.swing.JButton buttonDelete;
  private javax.swing.JButton buttonModify;
  private javax.swing.JLabel labelSubject;
  private javax.swing.JLabel labelStartDate;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel labelClassroom;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel labelFinishDate;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JLabel labelTeacher;

  com.toedter.calendar.JDateChooser jDateChooserStartDate;
  com.toedter.calendar.JDateChooser jDateChooserFinishDate;

  private final BodyGUI body;

  private final Subject subject;

}
