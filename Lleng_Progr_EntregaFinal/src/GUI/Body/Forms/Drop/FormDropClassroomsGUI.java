package GUI.Body.Forms.Drop;

import GUI.Utils.DropCheckList;
import GUI.Utils.CheckListItem;
import Logic.Classroom;
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
public class FormDropClassroomsGUI extends javax.swing.JPanel {

  public FormDropClassroomsGUI(GUI.Body.BodyGUI body, int classroomsToLoad) {
    this.body = body;
    this.classroomsToLoad = classroomsToLoad;
    initComponents();
  }

  private void initComponents() {
    jLabelTitle = new javax.swing.JLabel();
    jSeparatorTitle = new javax.swing.JSeparator();
    jButtonDropClassrooms = new javax.swing.JButton();
    jButtonPrev = new javax.swing.JButton();
    jButtonNext = new javax.swing.JButton();
    jScrollPaneClassrooms = new DropCheckList(loadClassrooms());

    setBackground(new java.awt.Color(255, 255, 255));
    setMinimumSize(new java.awt.Dimension(1150, 670));

    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabelTitle.setForeground(new java.awt.Color(255, 51, 51));
    jLabelTitle.setText("Baixa Aula/s");

    jSeparatorTitle.setForeground(new java.awt.Color(241, 241, 241));

    jButtonDropClassrooms.setBackground(new java.awt.Color(255, 102, 102));
    jButtonDropClassrooms.setText("Tramitar Baixa");
    jButtonDropClassrooms.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonDropClassroomsActionPerformed(evt);
      }
    });

    jScrollPaneClassrooms.setMinimumSize(new java.awt.Dimension(22, 400));
    jScrollPaneClassrooms.setPreferredSize(new java.awt.Dimension(100, 400));

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
                                    .addComponent(jButtonDropClassrooms))
                            .addComponent(jScrollPaneClassrooms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabelTitle)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jSeparatorTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)))
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
                    .addComponent(jScrollPaneClassrooms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(70, 70, 70)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonDropClassrooms)
                            .addComponent(jButtonPrev)
                            .addComponent(jButtonNext))
                    .addContainerGap(161, Short.MAX_VALUE))
    );
  }

  private void jButtonDropClassroomsActionPerformed(java.awt.event.ActionEvent evt) {
    boolean attempt = false;
    int i = 0;
    for (Classroom classroom : listOfClassrooms) {
      if (classroomItemList[i].isSelected()) {
        try {
          if (!Intersection.isBusy(classroom) && !Intersection.willBeBusy(classroom)) {
            GUI.Main.connection.deleteClassroom(classroom.getId());
          } else {
                        // Opcio de remplaçament
            // if (Interseciotn.willBeBusy(classroom)) {
            //} else {
            attempt = true;
            //}
          }
        } catch (DBException ex) {
          Logger.getLogger(FormDropTeachersGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      i++;
    }

    if (attempt) {
      GUI.Utils.Dialogs.attemptDropBusy(this, "Aula");
    } else {
      GUI.Utils.Dialogs.successfulDrop(this, "Aules");
    }

    body.showDropClassrooms();
  }

  private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {
    loadedClassrooms -= classroomsToLoad * 2;
    jScrollPaneClassrooms.reInit(loadClassrooms());
  }

  private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {
    jScrollPaneClassrooms.reInit(loadClassrooms());
  }

  private JList loadClassrooms() {
    listOfClassrooms = GUI.Main.connection.loadClassrooms(loadedClassrooms, classroomsToLoad + 1);
    loadedClassrooms += classroomsToLoad;

    if (listOfClassrooms == null) {
      listOfClassrooms = new ArrayList<>();
    } else {
      if (listOfClassrooms.size() > classroomsToLoad) {
        jButtonNext.setEnabled(true);
        listOfClassrooms.remove(classroomsToLoad);
      } else {
        jButtonNext.setEnabled(false);
      }

      if (loadedClassrooms > classroomsToLoad) {
        jButtonPrev.setEnabled(true);
      } else {
        jButtonPrev.setEnabled(false);
      }
    }

    classroomItemList = new CheckListItem[listOfClassrooms.size()];
    int i = 0;
    for (Classroom c : listOfClassrooms) {
      classroomItemList[i] = new CheckListItem("Aula " + c.getId() + " -> " + c.getCapacity() + ", " + c.getType());
      i++;
    }

    return new JList(classroomItemList);
  }

  public void check() {
    if (listOfClassrooms.isEmpty()) {
      GUI.Utils.Dialogs.emptyElement(this, "Aula");
      this.body.showHome();
    }
  }

  private javax.swing.JLabel jLabelTitle;
  private DropCheckList jScrollPaneClassrooms;
  private javax.swing.JSeparator jSeparatorTitle;
  private javax.swing.JButton jButtonDropClassrooms;
  private javax.swing.JButton jButtonPrev;
  private javax.swing.JButton jButtonNext;

  private final GUI.Body.BodyGUI body;

  private ArrayList<Classroom> listOfClassrooms;
  private final int classroomsToLoad;
  private int loadedClassrooms = 0;
  private CheckListItem classroomItemList[];
}
