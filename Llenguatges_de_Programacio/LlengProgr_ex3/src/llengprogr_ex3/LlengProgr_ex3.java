/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package llengprogr_ex3;

/**
 *
 * @author pere
 */
public class LlengProgr_ex3 {

  final int maxAnimadors = 50;
  final int nifLen = 9;
  final int nomLen = 30;

  int lidx;
  int[] segonsAnimats;
  double[] costPerSegon;
  char[][] nifs; // files == indexAnimador, columnes == nif de l'animador
  char[][] noms; // files == indexAnimador, columnes == nom animador
  java.util.Scanner s;

  public LlengProgr_ex3() {
    lidx = 0;
    segonsAnimats = new int[maxAnimadors];
    costPerSegon = new double[maxAnimadors];
    nifs = new char[maxAnimadors][nifLen];
    noms = new char[maxAnimadors][nomLen];
    s = new java.util.Scanner(System.in);
  }

  private void afegirAnimador() {
    if (lidx == maxAnimadors) {
      System.err.println("El maxim d'animadors afegits");
      return;
    }
    System.out.println("Introdueixi els camps seguents::");
    System.out.println("Numero de segons:");
    segonsAnimats[lidx] = s.nextInt();
    System.out.println("Cost per segon:");
    costPerSegon[lidx] = s.nextDouble();
    System.out.println("Nom (max " + nomLen + " chars):");
    s.nextLine();
    String tmp = s.nextLine();
    int i;
    for (i = 0; i < nomLen && i < tmp.length(); i++) {
      noms[lidx][i] = tmp.charAt(i);
    }
    System.out.println("Nif (8 digits i una lletra):");
    tmp = s.nextLine();
    for (i = 0; i < nifLen && i < tmp.length(); i++) {
      nifs[lidx][i] = tmp.charAt(i);
    }
    lidx++;
  }

  private void nominaTotal() {
    double nomina = 0.0;
    for (int i = 0; i < lidx; i++) {
      nomina += segonsAnimats[i] * costPerSegon[i];
    }
    System.out.println("Nomina total: " + nomina);
  }

  private void prOpts() {
    System.out.println("Opcions: \na) Afegeix animador\nb) Calcula la nomina\nc) Surt del programa");
  }

  private void mainLoop() {
    String tmp;
    char c;
    do {
      prOpts();
      tmp = s.nextLine().toLowerCase();
      c = tmp.charAt(0);
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
    new LlengProgr_ex3().mainLoop();
  }

}
