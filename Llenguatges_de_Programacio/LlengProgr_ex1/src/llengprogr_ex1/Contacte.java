/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package llengprogr_ex1;

/**
 *
 * @author pere
 */
public class Contacte {

  protected static final int nLen = 32, dLen = 256, mLen = 64, tLen = 9;
  protected static final int nSize = nLen * Character.BYTES, dSize = dLen * Character.BYTES, mSize = mLen * Character.BYTES, tSize = tLen * Character.BYTES;
  protected static final int eSize = (1 * Long.BYTES + 1 * Integer.BYTES + (nLen + dLen + mLen + tLen) * Character.BYTES);

  private final long codigo;
  private int edad;
  private String nombre;
  private String direccion;
  private String email;
  private String telefono;

  // Este constructor se usa para crear Contactes "dummy's" para que las clases que no tengan que escribir en el fichero directamente puedan crear y
  // modificar los campos del Contacte (excepto el codigo)
  public Contacte() {
    codigo = -1;
  }

  // Este constructor solo se usa cuando se han leido elementos del fichero, por lo tanto tendran un padding que eliminar a fin de
  // reducir el overhead en memoria principal
  public Contacte(long codigo, int edad, String nombre, String direccion, String email, String telefono) {
    this.codigo = codigo;
    this.edad = edad;
    this.nombre = removePadding(nombre);
    this.direccion = removePadding(direccion);
    this.email = removePadding(email);
    this.telefono = removePadding(telefono);
  }

  @Override
  public String toString() {
    return "\nCodigo: " + codigo
            + "\nEdad: " + edad
            + "\nNombre: " + nombre
            + "\nDirección: " + direccion
            + "\nEmail: " + email
            + "\nTelefono: " + telefono;
  }

  public long getCodigo() {
    return codigo;
  }

  public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public String getNombre() {
    return nombre;
  }

  public String getPaddedNombre() {
    return returnPaddedSubstring(nombre, nLen);
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDireccion() {
    return direccion;
  }

  public String getPaddedDireccion() {
    return returnPaddedSubstring(direccion, dLen);
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getEmail() {
    return email;
  }

  public String getPaddedEmail() {
    return returnPaddedSubstring(email, mLen);
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public String getPaddedTelefono() {
    return returnPaddedSubstring(telefono, tLen);
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  private String returnPaddedSubstring(String s, int len) {
    if (s.length() > len) {
      return s.substring(0, len);
    } else {
      return s.concat(new String(new char[len - s.length()])); // padding made with nulls x)
    }
  }

  private String removePadding(String a) {
    // split on null aka \u0000. de todos modos el MIN_VALUE garantiza que aunque se cambie el estandard de condificacion, 
    // esta chapuza seguira funcionando
    String[] s = a.split(Character.MIN_VALUE + "+$");
    return (s.length == 0) ? a : s[0];
  }
}
