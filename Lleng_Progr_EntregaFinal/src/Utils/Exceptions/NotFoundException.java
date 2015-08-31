/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.Exceptions;

/**
 *
 * @author pere
 */
public class NotFoundException extends Exception {
  
  public NotFoundException(String objName) {
    super(objName+" no trobat");
  }  
}
