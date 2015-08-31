package GUI.Body.Consultations;

import GUI.Body.BodyGUI;
import GUI.Body.Calendar.CalendarPaneGUI;
import static GUI.Main.connection;
import GUI.Utils.Dialogs;
import GUI.Utils.ListFrame;
import Logic.NormStudy;
import Logic.SpeStudy;
import Logic.Subject;
import Utils.Calendar.Intersection;
import Utils.Exceptions.DBException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dobleme
 */
public class ConsultationStudyGUI extends javax.swing.JPanel {

  public ConsultationStudyGUI(BodyGUI body, NormStudy ns, SpeStudy sp) {
    this.normStudy = ns;
    this.speStudy = sp;
    this.body = body;
    try {
      initComponents();
    } catch (DBException ex) {
      Logger.getLogger(ConsultationStudyGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void initComponents() throws DBException {
    String startDate = "";
    String finishDate = "";
    String name = "";
    String type = "";
    if (normStudy != null) {
      name = normStudy.getName();
      try {
        startDate = normStudy.getStartDate().toLocaleString().split(" ")[0];
        finishDate = normStudy.getFinishDate().toLocaleString().split(" ")[0];
      } catch (DBException ex) {
        Logger.getLogger(ConsultationStudyGUI.class.getName()).log(Level.SEVERE, null, ex);
      }
      type = normStudy.getTypeToString();
    } else {
      name = speStudy.getName();
      startDate = speStudy.getSpeAttr().getStartDate().toLocaleString().split(" ")[0];
      finishDate = speStudy.getSpeAttr().getFinishDate().toLocaleString().split(" ")[0];
      type = speStudy.getTypeToString();
    }

    jButtonDrop = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    if (normStudy != null) {
      if (normStudy.getSubjects() == null) {
        cal = new CalendarPaneGUI(null, normStudy.getCalendar(), 1);
      } else {
        cal = new CalendarPaneGUI(normStudy.getSubjects(), normStudy.getCalendar(), 1);
      }
    } else {
      cal = new CalendarPaneGUI(null, speStudy.getCalendar(), 1);
    }
    jLabel11 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jLabel9 = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setMinimumSize(new java.awt.Dimension(1150, 670));
    setPreferredSize(new java.awt.Dimension(1150, 670));

    jButtonDrop.setBackground(new java.awt.Color(255, 51, 51));
    jButtonDrop.setText("Tramitar Baixa");
    jButtonDrop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDropActionPerformed(evt);
      }
    });

    jButton2.setBackground(new java.awt.Color(241, 241, 241));
    jButton2.setText("Assignatures");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonListSubjectsActionPerformed(evt);
      }
    });
//    if (normStudy == null) {
    jButton2.setVisible(false);
//    }

    cal.setBackground(new java.awt.Color(255, 255, 255));
    cal.setMinimumSize(new java.awt.Dimension(850, 670));
    cal.setPreferredSize(new java.awt.Dimension(850, 670));

    jLabel11.setText(startDate);
    jLabel11.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabel11.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabel11.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel5.setText("Inici:");

    jLabel12.setText(type);
    jLabel12.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabel12.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabel12.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabel7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel7.setText("Tipus:");

    jLabel13.setText(finishDate);
    jLabel13.setMaximumSize(new java.awt.Dimension(184, 15));
    jLabel13.setMinimumSize(new java.awt.Dimension(1, 15));
    jLabel13.setName(""); // NOI18N
    jLabel13.setPreferredSize(new java.awt.Dimension(184, 15));

    jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jLabel9.setText("Final:");

    jLabel1.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("<html><p align = center>" + name + "</p></html>");
    jLabel1.setMaximumSize(new java.awt.Dimension(3999, 3999));
    jLabel1.setMinimumSize(new java.awt.Dimension(276, 99));
    jLabel1.setPreferredSize(new java.awt.Dimension(276, 99));

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
                                                    .addComponent(jButtonDrop))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(100, 100, 100)
                                                    .addComponent(jButton2))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addGap(49, 49, 49)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel5)
                                                            .addComponent(jLabel9)
                                                            .addComponent(jLabel7))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(12, 12, 12))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                    .addComponent(cal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(90, 90, 90)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                    .addGap(90, 90, 90)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                    .addGap(90, 90, 90)
                    .addComponent(jButton2)
                    .addGap(54, 54, 54)
                    .addComponent(jButtonDrop)
                    .addContainerGap(128, Short.MAX_VALUE))
            .addComponent(cal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
  }

  private void jButtonListSubjectsActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      ArrayList<Subject> aux = new ArrayList<>();
      while (normStudy.getSubjects().hasNext()) {
        aux.add(normStudy.getSubjects().next());
      }
      if (aux.isEmpty()) {
        Dialogs.emptyElement(this, "Assignatures");
      } else {
        String strs[] = new String[aux.size()];
        for (int i = 0; i < strs.length; i++) {
          strs[i] = aux.get(i).getName();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
            new ListFrame(strs).setVisible(true);
          }
        });
      }
    } catch (DBException ex) {
      Logger.getLogger(ConsultationStudyGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButtonDropActionPerformed(java.awt.event.ActionEvent evt) {
    try {
      if (normStudy != null) {
        if (Intersection.isBusy(normStudy)) {
          Dialogs.attemptDropBusy(this, "Estudi");
        } else {
          connection.deleteNormStudy(normStudy);
          Dialogs.successfulUniqueDrop(this, "Estudi");
          body.showHome();
        }
      } else {
        if (Intersection.isBusy(speStudy)) {
          Dialogs.attemptDropBusy(this, "Estudi");
        } else {
          connection.deleteSpeStudy(speStudy);
          Dialogs.successfulUniqueDrop(this, "Estudi");
          body.showHome();
        }
      }
    } catch (DBException ex) {
      Dialogs.notSuccessfulDrop(this, "Estudi");
      //Logger.getLogger(ConsultationTeacherGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private javax.swing.JButton jButtonDrop;
  private javax.swing.JButton jButton2;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel13;
  private CalendarPaneGUI cal;

  private final NormStudy normStudy;
  private final SpeStudy speStudy;

  private final BodyGUI body;
}
