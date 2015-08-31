package GUI.Body;

import GUI.Body.Consultations.ConsultationSubjectGUI;
import GUI.Body.Consultations.ConsultationTeacherGUI;
import GUI.Body.Consultations.ConsultationStudyGUI;
import GUI.Body.Forms.Add.FormAddSubjectGUI;
import GUI.Body.Forms.Add.FormAssignationsSubjectGUI;
import GUI.Body.Forms.Add.FormAddClassroomGUI;
import GUI.Body.Forms.Add.FormAddStudyGUI;
import GUI.Body.Forms.Add.FormAddSpecialStudyGUI;
import GUI.Body.Forms.Add.FormAddTeacherGUI;
import GUI.Body.Forms.Add.FormAssignationsNormStudyGUI;
import GUI.Body.Forms.Drop.FormDropClassroomsGUI;
import GUI.Body.Forms.Drop.FormDropStudiesGUI;
import GUI.Body.Forms.Drop.FormDropTeachersGUI;
import Logic.NormStudy;
import Logic.SpeStudy;
import Logic.Subject;
import Logic.Teacher;

/**
 *
 * @author dobleme
 */
public class BodyGUI extends javax.swing.JPanel {

  public BodyGUI() {
    initComponents();
  }

  private void initComponents() {
    setMinimumSize(new java.awt.Dimension(1150, 670));
    setName("Body");
    setPreferredSize(new java.awt.Dimension(1150, 670));
    setBackground(java.awt.Color.PINK);
    setLayout(new java.awt.CardLayout());
    
    home = new HomeGUI();
    add(home, "HOME");

  }

  public void showHome() {
    home = new HomeGUI();
    add(home, "HOME");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "HOME");
  }

  public void consultationSubject(Subject s) {
    cSubject = new ConsultationSubjectGUI(this, s);
    add(cSubject, "cClassroomCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "cClassroomCard");
  }

  public void consultationTeacher(Teacher t) {
    cTeacher = new ConsultationTeacherGUI(this, t);
    add(cTeacher, "cTeacherCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "cTeacherCard");
  }

  public void consultationStudy(NormStudy ns, SpeStudy sp) {
    cStudy = new ConsultationStudyGUI(this, ns, sp);
    add(cStudy, "cStudyCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "cStudyCard");
  }

  public void showFormAddClassroom() {
    fAddClassroom = new FormAddClassroomGUI(this);
    add(fAddClassroom, "addClassroomCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "addClassroomCard");
  }

  public void showFormAddTeacher() {
    fAddTeacher = new FormAddTeacherGUI(this);
    add(fAddTeacher, "addTeacherCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "addTeacherCard");
  }

  public void showFormAddSubject() {
    fAddSubject = new FormAddSubjectGUI(this);
    add(fAddSubject, "addSubjectCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "addSubjectCard");
  }

  public void showFormAssignationsSubject(Subject s, SpeStudy speStudy, boolean mod) {
    fAssignationsSubject = new FormAssignationsSubjectGUI(this, s, speStudy, mod);
    add(fAssignationsSubject, "assignationsSubjectCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "assignationsSubjectCard");
    fAssignationsSubject.check();
  }

  public void showFormAssignationsNormStudy(NormStudy normStudy) {
    fAssignationsNormStudy = new FormAssignationsNormStudyGUI(this, normStudy);
    add(fAssignationsNormStudy, "assignationsStudyCard");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "assignationsStudyCard");
    fAssignationsNormStudy.check();
  }

  public void showFormAddStudy() {
    fAddStudy = new FormAddStudyGUI(this);
    add(fAddStudy, "addStudy");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "addStudy");
  }

  public void showFormAddSpecialStudy(SpeStudy speStudy) {
    fAddSpeStudy = new FormAddSpecialStudyGUI(this, speStudy);
    add(fAddSpeStudy, "addSpecialStudy");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "addSpecialStudy");
  }

  public void showDropClassrooms() {
    fDropClassrooms = new FormDropClassroomsGUI(this, 17);
    add(fDropClassrooms, "dropClassrooms");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "dropClassrooms");
    fDropClassrooms.check();
  }

  public void showDropTeachers() {
    fDropTeachers = new FormDropTeachersGUI(this, 17);
    add(fDropTeachers, "dropTeachers");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "dropTeachers");
    fDropTeachers.check();
  }

  public void showDropStudies() {
    fDropStudies = new FormDropStudiesGUI(this, 17);
    add(fDropStudies, "dropStudies");
    java.awt.CardLayout lay = (java.awt.CardLayout) getLayout();
    lay.show(this, "dropStudies");
    fDropStudies.check();
  }

  // Layout
  private java.awt.CardLayout layout;
  // Formularis d'alta
  private FormAddClassroomGUI fAddClassroom;
  private FormAddTeacherGUI fAddTeacher;
  private FormAddStudyGUI fAddStudy;
  private FormAddSubjectGUI fAddSubject;
  private FormAddSpecialStudyGUI fAddSpeStudy;
  private FormAssignationsSubjectGUI fAssignationsSubject;
  private FormAssignationsNormStudyGUI fAssignationsNormStudy;
//    // Formularis de baixa
  private FormDropClassroomsGUI fDropClassrooms;
  private FormDropTeachersGUI fDropTeachers;
  private FormDropStudiesGUI fDropStudies;
  // Consulta
  private ConsultationTeacherGUI cTeacher;
  private ConsultationSubjectGUI cSubject;
  private ConsultationStudyGUI cStudy;
  // HomeGUI
  private HomeGUI home;
}
