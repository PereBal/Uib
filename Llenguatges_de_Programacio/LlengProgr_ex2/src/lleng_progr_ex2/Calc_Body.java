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
public class Calc_Body {

  private final double[] stack;
  private final char[] op_stack;
  private final boolean[] c_stack;
  private int sidx;
  private int sopidx;
  private int topIdx;
  private double ac;
  private boolean ac_mode;
  private int fact = 10;
  private boolean mustEmpty = false;

  protected static final double e = 2.718282;

  public Calc_Body() {
    op_stack = new char[2];
    stack = new double[3];
    c_stack = new boolean[3];
    sidx = 0;
    sopidx = 0;
    topIdx = sidx;
    stack[sidx] = 0;
    c_stack[sidx] = false;
    op_stack[sopidx] = ' ';
    ac = 0.0;
    ac_mode = false;
    mustEmpty = false;
  }

  public void clear() {
    sidx = 0;
    sopidx = 0;
    topIdx = sidx;
    stack[sidx] = 0;
    c_stack[sidx] = false;
    op_stack[sopidx] = ' ';
    ac = 0.0;
    ac_mode = false;
    mustEmpty = false;
  }

  public void clearStack() {
    sidx = 0;
    sopidx = 0;
    topIdx = sidx;
    stack[sidx] = 0;
    c_stack[sidx] = false;
    op_stack[sopidx] = ' ';
    mustEmpty = false;
  }

  public void acOn() {
    ac_mode = true;
  }

  public void actTermeEnter(int d) {
    stack[sidx] += d;
    stack[sidx] *= 10;
  }

  public void actTermeDecimal(int d) {
    stack[sidx] += (double) d / fact;
    fact *= 10;
  }

  public void beginDecPart() {
    decPart();
  }

  private void decPart() {
    if (!c_stack[sidx]) {
      stack[sidx] /= 10;
      c_stack[sidx] = true;
      fact = 10;
    }
  }

  public void push() {
    decPart();
    sidx++;
    stack[sidx] = 0.0;
    c_stack[sidx] = false;
  }

  public boolean push(char operation) {
    decPart();
    if (sidx < 2 && operation != '=' && !mustEmpty) { // x {+,-,*,/}  ||  x {+,-} x {+,-,*,/}
      mustEmpty = (operation == '*' || operation == '/' || operation == '%');
      op_stack[sopidx++] = operation;
      stack[++sidx] = 0.0;
      c_stack[sidx] = false;
      return false;
    } else if (operation == '=') { // x {} x =  ||   x {} x {} x = 
      mustEmpty = false;
      empty();
      topIdx = sidx;
      sopidx = 0;
    } else {
      empty();
      topIdx = sidx;
      stack[++sidx] = 0.0;
      c_stack[sidx] = false;
    }
    return true;
  }

  public double top() {
    return stack[topIdx];
  }

  public void e() {
    decPart();
    stack[sidx] = e;
  }

  public void empty() {
    int i;
    for (i = sopidx - 1; i >= 0; i--) {
      sidx--;
      switch (op_stack[i]) {
        case '+':
          stack[sidx] += stack[sidx + 1];
          break;
        case '-':
          stack[sidx] -= stack[sidx + 1];
          break;
        case '*':
          stack[sidx] *= stack[sidx + 1];
          break;
        case '/':
          stack[sidx] /= stack[sidx + 1];
          break;
        case '%':
          stack[sidx] = (stack[sidx + 1]*100) / stack[sidx];
          break;
      }
    }
  }
}
