package GUI.Body.Forms.Drop;

import GUI.Utils.CheckListItem;
import GUI.Utils.DropCheckList;
import Logic.Teacher;
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
public class FormDropTeachersGUI extends javax.swing.JPanel {

    public FormDropTeachersGUI(GUI.Body.BodyGUI body, int teachersToLoad) {
        this.body = body;
        this.teachersToLoad = teachersToLoad;
        initComponents();
    }

    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jSeparatorTitle = new javax.swing.JSeparator();
        jButtonDropTeachers = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jScrollPaneTeachers = new DropCheckList(loadTeachers());

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1150, 670));

        jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 51, 51));
        jLabelTitle.setText("Baixa Professor/s");

        jSeparatorTitle.setForeground(new java.awt.Color(241, 241, 241));

        jButtonDropTeachers.setBackground(new java.awt.Color(255, 102, 102));
        jButtonDropTeachers.setText("Tramitar Baixa");
        jButtonDropTeachers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDropActionPerformed(evt);
            }
        });

        jScrollPaneTeachers.setMinimumSize(new java.awt.Dimension(22, 400));
        jScrollPaneTeachers.setPreferredSize(new java.awt.Dimension(100, 400));

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
                                        .addComponent(jButtonDropTeachers))
                                .addComponent(jScrollPaneTeachers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelTitle)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jSeparatorTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 934, Short.MAX_VALUE)))
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
                        .addComponent(jScrollPaneTeachers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonDropTeachers)
                                .addComponent(jButtonPrev)
                                .addComponent(jButtonNext))
                        .addContainerGap(161, Short.MAX_VALUE))
        );
    }

    private void jButtonDropActionPerformed(java.awt.event.ActionEvent evt) {
        boolean attempt = false;
        int i = 0;
        for (Teacher teacher : listOfTeachers) {
            if (teachersItemList[i].isSelected()) {
                try {
                    if (!Intersection.isBusy(teacher) && !Intersection.willBeBusy(teacher)) {
                        GUI.Main.connection.deleteTeacher(teacher.getNif());
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
            GUI.Utils.Dialogs.attemptDropBusy(this, "Professor");
        } else {
            GUI.Utils.Dialogs.successfulDrop(this, "Professors");
        }

        body.showDropTeachers();
    }

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {
        loadedTeachers -= teachersToLoad * 2;
        jScrollPaneTeachers.reInit(loadTeachers());
    }

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {
        jScrollPaneTeachers.reInit(loadTeachers());
    }

    private JList loadTeachers() {
        listOfTeachers = GUI.Main.connection.loadTeachers(loadedTeachers, teachersToLoad + 1);
        loadedTeachers += teachersToLoad;

        if (listOfTeachers == null) {
            listOfTeachers = new ArrayList<>();
        } else {
            if (listOfTeachers.size() > teachersToLoad) {
                jButtonNext.setEnabled(true);
                listOfTeachers.remove(teachersToLoad);
            } else {
                jButtonNext.setEnabled(false);
            }

            if (loadedTeachers > teachersToLoad) {
                jButtonPrev.setEnabled(true);
            } else {
                jButtonPrev.setEnabled(false);
            }
        }

        teachersItemList = new CheckListItem[listOfTeachers.size()];
        int i = 0;
        for (Teacher t : listOfTeachers) {
            teachersItemList[i] = new CheckListItem(t.getName() + " " + t.getFirstName() + " - " + t.getNif());
            i++;
        }

        return new JList(teachersItemList);
    }

    public void check() {
        if (listOfTeachers.isEmpty()) {
            GUI.Utils.Dialogs.emptyElement(this, "Professor");
            this.body.showHome();
        }
    }

    private javax.swing.JLabel jLabelTitle;
    private DropCheckList jScrollPaneTeachers;
    private javax.swing.JSeparator jSeparatorTitle;
    private javax.swing.JButton jButtonDropTeachers;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JButton jButtonNext;

    private final GUI.Body.BodyGUI body;

    private ArrayList<Teacher> listOfTeachers;
    private final int teachersToLoad;
    private int loadedTeachers = 0;
    private CheckListItem teachersItemList[];

}
