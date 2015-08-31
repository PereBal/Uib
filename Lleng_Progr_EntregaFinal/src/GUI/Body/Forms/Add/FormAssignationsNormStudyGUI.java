package GUI.Body.Forms.Add;

import static GUI.Main.connection;
import GUI.Utils.DropCheckList;
import GUI.Utils.CheckListItem;
import Logic.Course;
import Logic.NormStudy;
import Logic.Subject;
import Utils.Calendar.CalendarComposition;
import Utils.Calendar.Intersection;
import Utils.Exceptions.DBException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 *
 * @author dobleme
 */
public class FormAssignationsNormStudyGUI extends javax.swing.JPanel {

  public FormAssignationsNormStudyGUI(GUI.Body.BodyGUI body, NormStudy normStudy) {
    this.body = body;
    this.normStudy = normStudy;
    this.subjectInitList = new ArrayList<>();
    initComponents();
  }

  private void initComponents() {
    jLabelTitle = new javax.swing.JLabel();
    jSeparatorTitle = new javax.swing.JSeparator();
    jButtonDropSubjects = new javax.swing.JButton();
    jScrollPaneSubjects = new DropCheckList(loadSubjects());

    setBackground(new java.awt.Color(255, 255, 255));
    setMinimumSize(new java.awt.Dimension(1150, 670));

    jLabelTitle.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabelTitle.setForeground(new java.awt.Color(255, 51, 51));
    jLabelTitle.setText("Assignació Assignatures");

    jSeparatorTitle.setForeground(new java.awt.Color(241, 241, 241));

    jButtonDropSubjects.setBackground(new java.awt.Color(0, 214, 121));
    jButtonDropSubjects.setText("Tramitar Alta");
    jButtonDropSubjects.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonAssignateActionPerformed(evt);
      }
    });

    jScrollPaneSubjects.setMinimumSize(new java.awt.Dimension(22, 400));
    jScrollPaneSubjects.setPreferredSize(new java.awt.Dimension(100, 400));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButtonDropSubjects))
                            .addComponent(jScrollPaneSubjects, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(jScrollPaneSubjects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(70, 70, 70)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonDropSubjects))
                    .addContainerGap(161, Short.MAX_VALUE))
    );
  }

  private void jButtonAssignateActionPerformed(java.awt.event.ActionEvent evt) {
    LinkedList<Subject> aux = new LinkedList<>();
    subjectItemList.stream().forEach((item) -> {
      subjectInitList.stream().forEach((s) -> {
        String str = s.getName() + " " + s.getStartDate() + " -> " + s.getFinishDate();
        if (item.toString().equals(str) && item.isSelected()) {
          aux.add(s);
        }
      });
    });
    try {
      // A les subjects els hi hem d'assignar l'estudi
      Course c = connection.storeCourse(normStudy.getCourse());
      NormStudy newS = new NormStudy(connection, normStudy.getName(), normStudy.getType(), c, aux, null);
      newS = connection.storeNormStudy(newS);

      for (Subject s : aux) {
        s.setStudy(newS);
        connection.updateStudySubject(s);
      }
    } catch (DBException ex) {
      Logger.getLogger(FormAssignationsNormStudyGUI.class.getName()).log(Level.SEVERE, null, ex);
    }

    body.showFormAddStudy();
  }

  private void jCheckListMouseClicked(java.awt.event.MouseEvent evt) {
    JList oldList = (JList) evt.getSource();
    int index = oldList.locationToIndex(evt.getPoint());
    CheckListItem item = (CheckListItem) oldList.getModel().getElementAt(index);
    item.setSelected(!item.isSelected());

    for (int i = 0; i < subjectItemList.size(); i++) {
      if (subjectItemList.get(i).isSelected()) {
        int j = 0, ii = 0;
        while (!showItem[ii]) {
          ii++;
        }
        while (j != i) {
          if (showItem[ii]) {
            j++;
          }
          ii++;
        }

        Subject subjectSelected = subjectInitList.get(ii);
        for (int jj = 0; jj < subjectInitList.size(); jj++) {
          if (subjectSelected.getId() != subjectInitList.get(jj).getId()) {
            try {
              showItem[jj] = !Intersection.overlapedSubjects(subjectSelected, subjectInitList.get(jj));
            } catch (DBException ex) {
              Logger.getLogger(FormAssignationsNormStudyGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
    }

    subjectItemList = new ArrayList<>();
    for (int i = 0; i < subjectInitList.size(); i++) {
      if (showItem[i]) {
        subjectItemList.add(new CheckListItem(subjectInitList.get(i).getName() + " " + subjectInitList.get(i).getStartDate() + " -> " + subjectInitList.get(i).getFinishDate()));
      }
    }

    for (int i = 0; i < oldList.getModel().getSize(); i++) {
      for (CheckListItem newItem : subjectItemList) {
        CheckListItem oldItem = (CheckListItem) oldList.getModel().getElementAt(i);
        if (newItem.toString().equals(oldItem.toString())) {
          newItem.setSelected(oldItem.isSelected());
        }
      }
    }

    JList list = new JList(subjectItemList.toArray(new CheckListItem[subjectItemList.size()]));
    list.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent event) {
        jCheckListMouseClicked(event);
      }
    });

    jScrollPaneSubjects.reInit(list);
  }

  private JList loadSubjects() {
    boolean notNull = true;
    ArrayList<Subject> aux = new ArrayList<>();
    int offsetSubjects = 0;
    while (aux.size() == offsetSubjects && notNull) {
      ArrayList<Subject> aux2 = GUI.Main.connection.loadSubjects(offsetSubjects, 20);
      if (aux2 == null) {
        notNull = false;
      } else {
        aux.addAll(aux2);
      }
      offsetSubjects += 20;
    }

    subjectItemList = new ArrayList<>();
    int i = 0;
    CalendarComposition c = null;
    try {
      if (normStudy.hasSubjects()) {
        c = normStudy.getCalendar();
      }
      boolean allWeek[][] = Intersection.mapOfCalendars(c, normStudy.getStartDate(), normStudy.getFinishDate());
      for (Subject s : aux) {
        if (!Intersection.isSpeStudy(s)) {
          s.getClassroom();
          s.getStudy();
          s.getTeacher();
          if (!s.hasStudy() && s.hasClassroom() && s.hasTeacher() && Intersection.availableTime(allWeek, s)
                  && normStudy.getStartDate().compareTo(s.getStartDate()) < 1 && normStudy.getFinishDate().compareTo(s.getFinishDate()) > -1) {
            subjectItemList.add(new CheckListItem(s.getName() + " " + s.getStartDate() + " -> " + s.getFinishDate()));
            subjectInitList.add(s);
          }
          i++;
        }
      }
    } catch (DBException ex) {
    }

    showItem = new boolean[subjectInitList.size()];
    for (int j = 0; j < subjectInitList.size(); j++) {
      showItem[j] = true;
    }

    JList list = new JList(subjectItemList.toArray(new CheckListItem[subjectItemList.size()]));
    list.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent event) {
        jCheckListMouseClicked(event);
      }
    });
    return list;
  }

  public void check() {
    if (subjectItemList.isEmpty()) {
      GUI.Utils.Dialogs.emptyElement(this, "Assignatura");
      this.body.showFormAddStudy();
    }
  }

  private javax.swing.JLabel jLabelTitle;
  private DropCheckList jScrollPaneSubjects;
  private javax.swing.JSeparator jSeparatorTitle;
  private javax.swing.JButton jButtonDropSubjects;

  private final GUI.Body.BodyGUI body;
  private final NormStudy normStudy;

  private final ArrayList<Subject> subjectInitList;
  private boolean showItem[];
  private ArrayList<CheckListItem> subjectItemList;
}
