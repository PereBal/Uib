/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package llengprogr_ex1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pere
 */
public class LlengProgr_ex1 {

  private static Gestor gest;
  // Uso el Br en lugar de java.util.Scanner debido a: http://stackoverflow.com/questions/8135903/java-memory-usage-for-scanner
  private static java.io.BufferedReader br;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      gest = new Gestor("./file.raf");
      br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
      boolean cont = true;
      int answ;
      while (cont) {
        System.out.println("\nElija una opción: ");
        answ = readInt(0, 5, "\n\t0-Salir\n\t1-Añadir contacto\n\t2-Visualizar un contacto"
                + "\n\t3-Modificar un contacto\n\t4-Listar todos los contactos\n\t5-Limpiar el fichero\n\t6-Limpiar la pantalla\n\n");
        switch (answ) {
          case 0:
            cont = false;
            gest.close();
            break;
          case 1:
            addContact();
            break;
          case 2:
            printContact();
            break;
          case 3:
            modContact();
            break;
          case 4:
            listContacts();
            break;
          case 5:
            gest.clean();
            break;
          case 6:
            System.out.println("\n\n\n\n\n\n\n\n\n\n");
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(LlengProgr_ex1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void addContact() {
    try {
      String s;
      Contacte c = new Contacte();
      System.out.println("Id del contacto = " + gest.getCodigo());
      c.setEdad(readInt(0, 150, "Introduzca la edad del contacto: "));
      System.out.print("Introduzca el nombre del contacto (máximo " + Contacte.nLen + " caracteres):\n==> ");
      c.setNombre(br.readLine());
      System.out.print("Introduzca la dirección del contacto (máximo " + Contacte.dLen + " caracteres):\n==> ");
      c.setDireccion(br.readLine());
      c.setEmail(readString("Introduzca el nuevo email del contacto (máximo " + Contacte.mLen + " caracteres):",
              "\nEl email introducido no cumple el formato estándar (xxx@xxx.xxx)\n", ".*@.*\u002E.*")); //\u002E == unicode character '.'
      c.setTelefono(readString("Introduzca el teléfono del contacto (máximo " + Contacte.tLen + " caracteres):",
              "\nEl telefono introducido no cumple el formato estándar (dígito x 9)\n", "[0-9]+"));
      gest.appendElem(c);
    } catch (IOException ex) {
      Logger.getLogger(LlengProgr_ex1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void printContact() {
    try {
      Contacte c = readContacto();
      if (c != null) {
        System.out.println(c);
      }
    } catch (IOException ex) {
      Logger.getLogger(LlengProgr_ex1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void modContact() {
    try {
      Contacte c = readContacto();
      int answ;
      boolean cont = true;
      while (cont) {
        answ = readInt(0, 5, "Que opción desea usar?"
                + "\n\t0. Escribir elemento\n\t1. Modif. Edad\n\t2. Modif. Nombre\n\t3. Modif. Dirección\n\t4. Modif. Email\n\t5. Modif. Teléfono\n");
        switch (answ) {
          case 0:
            cont = false;
            break;
          case 1:
            c.setEdad(readInt(0, 150, "\nIntroduzca la nueva edad: "));
            break;
          case 2:
            System.out.print("\nIntroduzca el nuevo nombre del contacto (máximo " + Contacte.nLen + " caracteres):\n==> ");
            c.setNombre(br.readLine());
            break;
          case 3:
            System.out.println("\nIntroduzca la nueva dirección del contacto (máximo " + Contacte.dLen + " caracteres):\n==> ");
            c.setDireccion(br.readLine());
            break;
          case 4:
            c.setEmail(readString("Introduzca el nuevo email del contacto (máximo " + Contacte.mLen + " caracteres): ",
                    "\nEl email introducido no cumple el formato estándar (xxx@xxx.xxx)\n", ".*@.*\u002E.*")); //\u002E == unicode character '.'
            break;
          case 5:
            c.setTelefono(readString("Introduzca el teléfono del contacto (máximo " + Contacte.tLen + " caracteres): ",
                    "\nEl telefono introducido no cumple el formato estándar (dígito x 9)\n", "[0-9]+"));
        }
      }
      gest.writeElem(c);
    } catch (IOException ex) {
      Logger.getLogger(LlengProgr_ex1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void listContacts() {
    try {
      gest.listElems();
    } catch (IOException ex) {
      Logger.getLogger(LlengProgr_ex1.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static int readInt(int linf, int lsup, String msg) {
    return (int) readLong(linf, lsup, msg);
  }

  private static long readLong(long linf, long lsup, String msg) {
    long i = linf - 1;
    while (i < linf) {
      try {
        System.out.print(msg+"\n==> ");
        i = Integer.parseInt(br.readLine());
        if (i > lsup) {
          System.err.println("Err, introduzca un número entero en el rango [" + linf + "-" + lsup + "]");
          i = linf - 1;
        }
      } catch (IOException | NumberFormatException e) {
        System.err.println("Err, introduzca un número entero");
      }
    }
    return i;
  }

  private static Contacte readContacto() throws IOException {
    Contacte c = null;
    boolean cont = true;
    long code;
    String s;
    while (cont) {
      code = readLong(0, gest.getCodigo(), "Por favor, introduzca el código del contacto que desea buscar: ");
      c = gest.readElem(code);
      if (cont = (c == null)) {
        System.out.println("Desea volver a intentarlo? [SI/no]");
        s = br.readLine().toLowerCase();
        if (s.equals("n") || s.equals("no")) {
          cont = false;
        }
      }
    }
    return c;
  }

  private static String readString(String msg, String emsg, String regexp) throws IOException {
    System.out.print(msg+"\n==> ");
    String s = br.readLine();
    while (!s.matches(regexp)) {
      System.err.println(emsg);
      System.out.print(msg);
      s = br.readLine();
    }
    return s;
  }
}
