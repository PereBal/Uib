package GUI.Body.Forms.Drop;

import GUI.Utils.CheckListItem;
import GUI.Utils.DropCheckList;
import Logic.NormStudy;
import Logic.SpeStudy;
import Logic.Study;
import Utils.Calendar.Intersection;
import Utils.Exceptions.DBException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 *
 * @author dobleme
 */
public class FormDropStudiesGUI extends javax.swing.JPanel {

  public FormDropStudiesGUI(GUI.Body.BodyGUI body, int studiesToLoad) {
    this.body = body;
    this.studiesToLoad = studiesToLoad / 2;
    initComponents();
  }

  private void initComponents() {

    jLabelTitle = new javax.swing.JLabel();
    jSeparatorTitle = new javax.swing.JSeparator();
    jButtonDropStudies = new javax.swing.JButton();
    jButtonPrev = new javax.swing.JButton();
    jButtonNext = new javax.swing.JButton();
    jScrollPaneStudies = new DropCheckList(loadStudies());

    setBackground(new java.awt.Color(255, 255, 255));
    setMinimumSize(new java.awt.Dimension(1150, 670));

    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabelTitle.setForeground(new java.awt.Color(255, 51, 51));
    jLabelTitle.setText("Baixa Estudi/s");

    jSeparatorTitle.setForeground(new java.awt.Color(241, 241, 241));

    jButtonDropStudies.setBackground(new java.awt.Color(255, 102, 102));
    jButtonDropStudies.setText("Tramitar Baixa");
    jButtonDropStudies.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDropActionPerformed(evt);
      }
    });

    jScrollPaneStudies.setMinimumSize(new java.awt.Dimension(22, 400));
    jScrollPaneStudies.setPreferredSize(new java.awt.Dimension(100, 400));

    jButtonPrev.setBackground(new java.awt.Color(241, 241, 241));
    jButtonPrev.setText("<");
    jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonPrevActionPerformed(evt);
      }
    });

    jButtonNext.setBackground(new java.awt.Color(241, 241, 241));
    jButtonNext.setText(">");
    jButtonNext.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonNextActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButtonPrev)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButtonNext)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonDropStudies))
                            .addComponent(jScrollPaneStudies, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelTitle)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jSeparatorTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)))
                    .addGap(30, 30, 30))
    );
    layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparatorTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelTitle))
                    .addGap(30, 30, 30)
                    .addComponent(jScrollPaneStudies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(70, 70, 70)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonDropStudies)
                            .addComponent(jButtonPrev)
                            .addComponent(jButtonNext))
                    .addContainerGap(161, Short.MAX_VALUE))
    );
  }

  private void jButtonDropActionPerformed(java.awt.event.ActionEvent evt) {
    boolean attempt = false, futureAttempt = false;
    int i = 0;
    for (SpeStudy speStudy : listOfSpeStudies) {
      if (studiesItemList[i].isSelected()) {
        try {
          if (Intersection.isBusy(speStudy)) {
            GUI.Main.connection.deleteSpeStudy(speStudy);
          } else if (Intersection.willBeBusy(speStudy)) {
            GUI.Main.connection.deleteSpeStudy(speStudy);
            futureAttempt = true;
          } else {
            attempt = true;
          }
        } catch (DBException ex) {
          Logger.getLogger(FormDropStudiesGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      i++;
    }
    for (NormStudy normStudy : listOfNormStudies) {
      if (studiesItemList[i].isSelected()) {
        try {
          if (Intersection.isBusy(normStudy)) {
            GUI.Main.connection.deleteNormStudy(normStudy);
          } else if (Intersection.willBeBusy(normStudy)) {
            GUI.Main.connection.deleteNormStudy(normStudy);
            futureAttempt = true;
          } else {
            attempt = true;
          }
        } catch (DBException ex) {
          Logger.getLogger(FormDropStudiesGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      i++;
    }

    if (attempt) {
      GUI.Utils.Dialogs.attemptDropBusy(this, "Estudis");
      GUI.Utils.Dialogs.successfulStudyDrop(this, "Estudis");
    } else if (futureAttempt) {
      GUI.Utils.Dialogs.attemptStudyDropFutureBusy(this, "Estudis");
    } else {
      GUI.Utils.Dialogs.successfulStudyDrop(this, "Estudis");
    }

    body.showDropStudies();
  }

  private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {
    loadedSpeStudies -= studiesToLoad * 2;
    loadedNormStudies -= studiesToLoad * 2;
    jScrollPaneStudies.reInit(loadStudies());
  }

  private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {
    jScrollPaneStudies.reInit(loadStudies());
  }

  private JList loadStudies() {
    listOfSpeStudies = GUI.Main.connection.loadSpeStudies(loadedSpeStudies, studiesToLoad + 1);
    loadedSpeStudies += studiesToLoad;
    listOfNormStudies = GUI.Main.connection.loadNormStudies(loadedNormStudies, studiesToLoad + 1);
    loadedNormStudies += studiesToLoad;

    if (listOfNormStudies == null && listOfSpeStudies == null) {
      listOfSpeStudies = new ArrayList<>();
      listOfNormStudies = new ArrayList<>();
    } else if (listOfNormStudies != null && listOfSpeStudies == null) {
      listOfSpeStudies = new ArrayList<>();
    } else if (listOfNormStudies == null && listOfSpeStudies != null) {
      listOfNormStudies = new ArrayList<>();
    }

    if (listOfSpeStudies.size() > studiesToLoad && listOfNormStudies.size() > studiesToLoad) {
      jButtonNext.setEnabled(true);
      listOfSpeStudies.remove(studiesToLoad);
      listOfNormStudies.remove(studiesToLoad);
    } else if (listOfSpeStudies.size() > studiesToLoad && listOfNormStudies.size() <= studiesToLoad) {
      jButtonNext.setEnabled(true);
      listOfSpeStudies.remove(studiesToLoad);
    } else if (listOfSpeStudies.size() <= studiesToLoad && listOfNormStudies.size() > studiesToLoad) {
      jButtonNext.setEnabled(true);
      listOfNormStudies.remove(studiesToLoad);
    } else {
      jButtonNext.setEnabled(false);
    }

    if ((loadedSpeStudies + loadedNormStudies) > (studiesToLoad * 2)) {
      jButtonPrev.setEnabled(true);
    } else {
      jButtonPrev.setEnabled(false);
    }

    studiesItemList = new CheckListItem[listOfNormStudies.size() + listOfSpeStudies.size()];
    int i = 0;
    for (SpeStudy speStudy : listOfSpeStudies) {
      studiesItemList[i] = new CheckListItem(speStudy.getName() + ", " + speStudy.getTypeToString() + " | " + speStudy.getSpeAttr().getStartDate() + " -> " + speStudy.getSpeAttr().getFinishDate());
      i++;
    }
    for (NormStudy normStudy : listOfNormStudies) {
      try {
        studiesItemList[i] = new CheckListItem(normStudy.getName() + ", " + normStudy.getTypeToString() + " | " + normStudy.getStartDate() + " -> " + normStudy.getFinishDate());
      } catch (DBException ex) {
      }
      i++;
    }

    return new JList(studiesItemList);
  }

  public void check() {
    if (listOfSpeStudies.isEmpty() && listOfNormStudies.isEmpty()) {
      GUI.Utils.Dialogs.emptyElement(this, "Estudi");
      this.body.showHome();
    }
  }

  private javax.swing.JButton jButtonDropStudies;
  private javax.swing.JButton jButtonPrev;
  private javax.swing.JButton jButtonNext;
  private javax.swing.JLabel jLabelTitle;
  private DropCheckList jScrollPaneStudies;
  private javax.swing.JSeparator jSeparatorTitle;

  private final GUI.Body.BodyGUI body;

  private ArrayList<SpeStudy> listOfSpeStudies;
  private ArrayList<NormStudy> listOfNormStudies;
  private final int studiesToLoad;
  private int loadedSpeStudies = 0;
  private int loadedNormStudies = 0;
  //private int loadedTeachers = 0;
  private CheckListItem studiesItemList[];

}
