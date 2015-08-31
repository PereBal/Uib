/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package llengprogr_ex1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author pere
 */
public class Gestor {

  private RandomAccessFile raf = null;
  private final java.nio.file.Path fp;
  private long codigo;

  // Para optimizar las lecturas
  private final byte[] b = new byte[Contacte.eSize];
  private final int ageSeek = Long.BYTES;
  private final int nameSeek = ageSeek + Integer.BYTES;
  private final int addressSeek = nameSeek + Contacte.nSize;
  private final int emailSeek = addressSeek + Contacte.dSize;
  private final int phoneSeek = emailSeek + Contacte.mSize;

  public Gestor(String filename) throws IOException {
    fp = java.nio.file.Paths.get(filename);
    if (!java.nio.file.Files.exists(fp)) {
      java.nio.file.Files.createFile(fp);
    }
    raf = new RandomAccessFile(fp.toFile(), "rw");
    codigo = (raf.length() / Contacte.eSize);
  }

  public void appendElem(Contacte c) throws IOException {
    raf.seek(codigo * Contacte.eSize);
    raf.writeLong(codigo++);
    raf.writeInt(c.getEdad());
    raf.writeChars((c.getPaddedNombre() + c.getPaddedDireccion() + c.getPaddedEmail() + c.getPaddedTelefono()));
  }

  // Que es mas correcto, reescribir solo los campos alterados o reescribirlo todo de una sola tirada?
  public void writeElem(Contacte n) throws IOException {
    if (validateCode(n.getCodigo())) {
      raf.seek(n.getCodigo() * Contacte.eSize);
      raf.writeLong(n.getCodigo());
      raf.writeInt(n.getEdad());
      raf.writeChars(n.getPaddedNombre() + n.getPaddedDireccion() + n.getPaddedEmail() + n.getPaddedTelefono());
    } else {
      System.err.println("Err, El elemento no ha sido actualizado");
    }
  }

  public void clean() throws IOException {
    raf.setLength(0);
    codigo = 0;
  }
  
  public Contacte readElem(long c) throws IOException {
    return (validateCode(c)) ? readElem(c,true) : null;
  }

  private Contacte readElem(long c, boolean seek) throws IOException {
    if (seek) {
      raf.seek(c * Contacte.eSize);
    }
    raf.readFully(b);
    return new Contacte(
            c,
            ByteBuffer.wrap(b, ageSeek, Integer.BYTES).getInt(),
            new String(b, nameSeek, Contacte.nSize, StandardCharsets.UTF_16),
            new String(b, addressSeek, Contacte.dSize, StandardCharsets.UTF_16),
            new String(b, emailSeek, Contacte.mSize, StandardCharsets.UTF_16),
            new String(b, phoneSeek, Contacte.tSize, StandardCharsets.UTF_16)
    );
  }

  public void listElems() throws IOException {
    long i = 0;
    if (i < codigo) {
      System.out.println("\n" + readElem(i, true));
    }
    for (i = 1; i < codigo; i++) {
      System.out.println("\n" + readElem(i, false));
    }
  }

  private boolean validateCode(long c) {
    if (c >= 0 && c < codigo) {
      return true;
    }
    System.err.println("Err, Elemento no encontrado");
    return false;
  }

  public long getCodigo() {
    return codigo;
  }

  public void close() throws IOException {
    raf.close();
  }

}
