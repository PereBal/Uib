/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lleng_progr_ex2;

/**
 *
 * @author pere
 */
public class Gest_Mouse extends java.awt.event.MouseAdapter {

  private final java.awt.Color New;
  private final java.awt.Color Old;
  private final java.awt.Color OnClick;

  public Gest_Mouse(java.awt.Color cn, java.awt.Color co, java.awt.Color onC) {
    super();
    this.New = cn;
    this.Old = co;
    this.OnClick = onC;
  }

  @Override
  public void mouseEntered(java.awt.event.MouseEvent e) {
    e.getComponent().setBackground(New);
  }

  @Override
  public void mouseExited(java.awt.event.MouseEvent e) {
    e.getComponent().setBackground(Old);
  }
  
  public java.awt.Color defaultColor() {
    return this.Old;
  }
}
