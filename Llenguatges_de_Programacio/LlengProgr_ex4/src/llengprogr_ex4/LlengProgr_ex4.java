/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package llengprogr_ex4;

/**
 *
 * @author pere
 */
public class LlengProgr_ex4 {

  private final int maxAnimadors = 50;
  private final Animador[] animadors;
  private int lidx;
  java.util.Scanner scanner;

  public LlengProgr_ex4() {
    animadors = new Animador[maxAnimadors];
    lidx = 0;
    scanner = new java.util.Scanner(System.in);
  }

  private void afegirAnimador() {
    if (lidx == maxAnimadors) {
      System.err.println("El maxim d'animadors afegits");
      return;
    }
    animadors[lidx] = Animador.llegirAnimador();
    if (animadors[lidx] != null) {
      lidx++;
    } else {
      System.err.println("S'ha produit un error, animador no afegit");
    }
  }

  private void nominaTotal() {
    double nomina = 0.0;
    for (int i = 0; i < lidx; i++) {
      nomina += animadors[i].getSa() * animadors[i].getCps();
    }
    System.out.println("Nomina total: " + nomina);
  }

  private void printOpts() {
    System.out.println("Opcions: \na) Afegeix animador\nb) Calcula la nomina\nc) Surt del programa");
  }

  private void mainLoop() {
    char c;
    do {
      printOpts();
      c = scanner.nextLine().toLowerCase().charAt(0);
      switch (c) {
        case 'a':
          afegirAnimador();
          break;
        case 'b':
          nominaTotal();
          break;
        case 'c':
          break;
        default:
          System.err.println("Invalid Option");
      }
    } while (c != 'c');
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    (new LlengProgr_ex4()).mainLoop();
  }

}
