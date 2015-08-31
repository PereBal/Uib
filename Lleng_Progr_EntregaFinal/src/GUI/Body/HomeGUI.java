/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Body;

import Logic.Classroom;
import Logic.NormStudy;
import Logic.SpeStudy;
import Logic.Teacher;
import java.util.ArrayList;

/**
 *
 * @author dobleme
 */
public class HomeGUI extends javax.swing.JPanel {

  /**
   * Creates new form Home
   */
  public HomeGUI() {
    initComponents();
    initStudies();
    initTeachers();
    initClassrooms();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    jSeparator1 = new javax.swing.JSeparator();
    jScrollPane1 = new javax.swing.JScrollPane();
    jListStudies = new javax.swing.JList();
    jScrollPane2 = new javax.swing.JScrollPane();
    jListClassrooms = new javax.swing.JList();
    jScrollPane3 = new javax.swing.JScrollPane();
    jListTeachers = new javax.swing.JList();
    jLabel1 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();

    setBackground(new java.awt.Color(255, 255, 255));
    setMinimumSize(new java.awt.Dimension(1150, 670));

    jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(255, 51, 51));
    jLabel2.setText("Home");

    jSeparator1.setForeground(new java.awt.Color(241, 241, 241));

    jListStudies.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jListStudies.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jListStudies.setEnabled(false);
    jListStudies.setMinimumSize(new java.awt.Dimension(300, 85));
    jListStudies.setPreferredSize(new java.awt.Dimension(300, 85));
    jScrollPane1.setViewportView(jListStudies);

    jListClassrooms.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jListClassrooms.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jListClassrooms.setEnabled(false);
    jListClassrooms.setMinimumSize(new java.awt.Dimension(300, 85));
    jListClassrooms.setPreferredSize(new java.awt.Dimension(300, 85));
    jScrollPane2.setViewportView(jListClassrooms);

    jListTeachers.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
    jListTeachers.setModel(new javax.swing.AbstractListModel() {
      String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
      public int getSize() { return strings.length; }
      public Object getElementAt(int i) { return strings[i]; }
    });
    jListTeachers.setEnabled(false);
    jListTeachers.setMinimumSize(new java.awt.Dimension(300, 85));
    jListTeachers.setPreferredSize(new java.awt.Dimension(300, 85));
    jScrollPane3.setViewportView(jListTeachers);

    jLabel1.setText("Estudis");

    jLabel3.setText("Professors");

    jLabel4.setText("Aules");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jSeparator1)
        .addGap(30, 30, 30))
      .addGroup(layout.createSequentialGroup()
        .addContainerGap(70, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addGap(51, 51, 51)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3))
        .addGap(52, 52, 52)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel4)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(70, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(30, 30, 30)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addGap(50, 50, 50)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(jLabel3)
          .addComponent(jLabel4))
        .addGap(9, 9, 9)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(98, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void initStudies() {
    int offsetNormStudies = 0;
    ArrayList<NormStudy> normStudies = new ArrayList<>();
    ArrayList<NormStudy> aux = GUI.Main.connection.loadNormStudies(offsetNormStudies, 20);
    while (aux != null) {
      normStudies.addAll(aux);
      offsetNormStudies += aux.size();
      aux = GUI.Main.connection.loadNormStudies(offsetNormStudies, 20);
    }

    int offsetSpeStudies = 0;
    ArrayList<SpeStudy> speStudies = new ArrayList<>();
    ArrayList<SpeStudy> aux2 = GUI.Main.connection.loadSpeStudies(offsetSpeStudies, 20);
    while (aux2 != null) {
      speStudies.addAll(aux2);
      offsetSpeStudies += aux2.size();
      aux2 = GUI.Main.connection.loadSpeStudies(offsetSpeStudies, 20);
    }

    String strings[];/* = new String[normStudies.size() + speStudies.size()];*/

    if (normStudies.isEmpty() && speStudies.isEmpty()) {
      strings = new String[1];
      strings[0] = "No hi ha cap estudi";
    } else {
      strings = new String[normStudies.size() + speStudies.size()];
    }

    int i = 0;
    for (; i < normStudies.size(); i++) {
      strings[i] = normStudies.get(i).getName() + " -> " + normStudies.get(i).getTypeToString();
    }
    for (SpeStudy speStudy : speStudies) {
      strings[i] = speStudy.getName() + " -> " + speStudy.getTypeToString();
      i++;
    }
    jListStudies.setModel(new javax.swing.AbstractListModel() {
      @Override
      public int getSize() {
        return strings.length;
      }

      @Override
      public Object getElementAt(int i) {
        return strings[i];
      }
    });
  }

  private void initTeachers() {
    int offsetTeachers = 0;

    ArrayList<Teacher> teachers = new ArrayList<>();
    ArrayList<Teacher> aux = GUI.Main.connection.loadTeachers(offsetTeachers, 20);
    while (aux != null) {
      teachers.addAll(aux);
      offsetTeachers += aux.size();
      aux = GUI.Main.connection.loadTeachers(offsetTeachers, 20);
    }

    String strings[]; /*= new String[teachers.size()];*/

    if (teachers.isEmpty()) {
      strings = new String[1];
      strings[0] = "No hi ha cap professor";
    } else {
      strings = new String[teachers.size()];
    }

    for (int i = 0; i < teachers.size(); i++) {
      strings[i] = teachers.get(i).getName() + " " + teachers.get(i).getFirstName() + " -> " + teachers.get(i).getNif();
    }
    jListTeachers.setModel(new javax.swing.AbstractListModel() {
      @Override
      public int getSize() {
        return strings.length;
      }

      @Override
      public Object getElementAt(int i) {
        return strings[i];
      }
    });
  }

  private void initClassrooms() {
    int offsetClassrooms = 0;

    ArrayList<Classroom> classrooms = new ArrayList<>();
    ArrayList<Classroom> aux = GUI.Main.connection.loadClassrooms(offsetClassrooms, 20);
    while (aux != null) {
      classrooms.addAll(aux);
      offsetClassrooms += aux.size();
      aux = GUI.Main.connection.loadClassrooms(offsetClassrooms, 20);
    }

    String strings[]; /*= new String[classrooms.size()];*/
    if (classrooms.isEmpty()) {
      strings = new String[1];
      strings[0] = "No hi ha cap aula";
    } else {
      strings = new String[classrooms.size()];
    }
    
    for (int i = 0; i < classrooms.size(); i++) {
      strings[i] = "Aula " + classrooms.get(i).getId() + " -> " + classrooms.get(i).getType() + "," + classrooms.get(i).getCapacity();
    }
    jListClassrooms.setModel(new javax.swing.AbstractListModel() {
      @Override
      public int getSize() {
        return strings.length;
      }

      @Override
      public Object getElementAt(int i) {
        return strings[i];
      }
    });
  }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JList jListClassrooms;
  private javax.swing.JList jListStudies;
  private javax.swing.JList jListTeachers;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JSeparator jSeparator1;
  // End of variables declaration//GEN-END:variables
}
