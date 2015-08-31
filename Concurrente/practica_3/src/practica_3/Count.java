/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public class Count extends Work {

  private long work;
  private long res;
  private final java.nio.file.Path fp = Paths.get("../totales.txt");

  public Count(String id, Node ndi) {
    super(id, ndi);
  }

  @Override
  protected final long[] calcWork(String data) throws IOException {
    long diff = Long.parseLong(data), result = diff;
    if (!ndi.hasChildren() && ndi.isRoot()) {
      throw new IOException("El algorismo no puede funcionar con solo el nodo de entorno");
    }
    long[] res;
    if (ndi.isRoot()) {
      res = new long[ndi.getChildren().size()];
      result /= ndi.getChildren().size();
    } else {
      res = new long[ndi.getChildren().size() + 1]; // +1 == el nodo en si se cuenta a la hora de dividir el trabajo
      result /= ndi.getChildren().size() + 1; // +1 == el nodo en si se cuenta a la hora de dividir el trabajo
    }
    for (int i = 0, len = res.length; i < len; i++) {
      res[i] = result;
    }
    diff = diff % res.length;
    res[((int) (Math.floor(Math.random() * res.length)))] += diff; //añadimos el trabajo extra a un elemento aleatorio
    return res;
  }

  //msg = {type,src,data}
  @Override
  protected final void distributeWork(long[] valToSend) {
    if (ndi.hasParent()) {
      if (ndi.hasChildren()) {
        int idx = 0;
        String id;
        try {
          Iterator<String> it = ndi.getChildren().iterator();
          while (it.hasNext()) {
            id = it.next();
            String data = "" + valToSend[idx];
            valToSend[idx++] = 0; //el posam a zero perque despres mentre feim la iteracio no passi re
            //System.err.println(data+"-"+id+" ");
            ndi.sendMsg(this.getConnection(), "CNTWORK", id, (Object) data);
            ndi.incrODef();
          }
        } catch (ParseException ex) {
          Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    } else {
      System.err.println("ERROR");
    }
    this.work = 0;
    for (int i = 0; i < valToSend.length; i++) {
      this.work += valToSend[i];
    }
  }

  @Override
  protected final void save() throws InterruptedException {
    long value;
    this.enterCS();
    try {
      value = Integer.parseInt(new String(Files.readAllBytes(fp)));
      value += this.res;
      Files.write(fp, ("" + value).getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException ex) {
      Logger.getLogger(Count.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.exitCS();
  }

  @Override
  protected final void createStorageFile() {
    try {
      if (!Files.exists(fp)) {
        Files.createFile(fp);
        Files.write(fp, "0".getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
      } else {
        Files.write(fp, "0".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
      }
    } catch (IOException ex) {
      Logger.getLogger(Count.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public long readStoreFile() {
    try {
      return Integer.parseInt(new String(Files.readAllBytes(fp)));
    } catch (IOException ex) {
      Logger.getLogger(Count.class.getName()).log(Level.SEVERE, null, ex);
      return -1;
    }
  }

  @Override
  public void run() {
    try {
      res = 0;
      long decima = this.work / 10 + this.work % 10;
      while (res < decima) {
        res++;
      }
      save();
      while (res < this.work) {
        res++;
      }
      //save();
      this.end();
      System.out.println("Work for node " + ndi.getId() + " done, res=" + res);

    } catch (InterruptedException ex) {
      Logger.getLogger(Count.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
