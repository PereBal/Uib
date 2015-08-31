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
public class Animador {

  private final static int nifLen = 9;
  private final int sa; // segons animats
  private final float cps; // cost per segon
  private final String nif;
  private final String nom;

  public Animador(String nom, String nif, int sa, float cps) {
    this.sa = sa;
    this.cps = cps;
    this.nom = nom;
    this.nif = nif;
  }

  public int getSa() {
    return sa;
  }

  public float getCps() {
    return cps;
  }

  public String getNif() {
    return nif;
  }

  public String getNom() {
    return nom;
  }

  @Override
  public String toString() {
    return "Animador{" + "segonsAnimats=" + sa + ", costPerSegon=" + cps + ", nif=" + nif + ", nom=" + nom + '}';
  }

  public static Animador llegirAnimador() {
    java.util.Scanner scanner = new java.util.Scanner(System.in);
    System.out.println("Introdueixi els camps seguents::");
    System.out.println("Numero de segons:");
    int sa = scanner.nextInt();
    System.out.println("Cost per segon:");
    float cps = scanner.nextFloat();
    System.out.println("Nom:");
    scanner.nextLine();
    String nom = scanner.nextLine();
    System.out.println("Nif (8 digits i una lletra):");
    String nif = scanner.nextLine();
    if (nif.length() != nifLen) {
      System.err.println("Longitut inadecuada del nif");
      return null;
    } else {
      return new Animador(nom, nif, sa, cps);
    }
  }
}
