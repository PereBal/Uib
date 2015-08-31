/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public class StoreST extends Work {

  public static final java.nio.file.Path stfp = java.nio.file.Paths.get("../spaningtree.dot");

  public StoreST(String id, Node ndi) {
    super(id, ndi);
  }

  @Override
  protected long[] calcWork(String data) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected void distributeWork(long[] valToSend) {
    try {
      if (this.ndi.hasParent()) {
        if (this.ndi.hasChildren()) {
          java.util.Iterator<String> it = this.ndi.getChildren().iterator();
          String childID;
          while (it.hasNext()) {
            childID = it.next();
            this.ndi.sendMsg(this.getConnection(), "SPTWORK", childID);
            this.ndi.incrODef();
          }
        }
      }
    } catch (ParseException ex) {
      Logger.getLogger(StoreST.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected final void save() throws InterruptedException {
    try {
      //System.out.println("StoreST:" + "\n\t" + this.ndi.getParent() + " -> " + this.getId());
      this.enterCS();
      Files.write(stfp, ("\n\t" + this.ndi.getParent() + " -> " + this.getId()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
      /*try (RandomAccessFile raf = new RandomAccessFile(stfp.toFile(), "rw")) {
       raf.seek(raf.length() - 3);
       raf.writeChars("\n\t" + this.ndi.getParent() + " -> " + this.getId() + "\n}");
       raf.close();
       //Files.write(stfp, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
       */
      this.exitCS();
    } catch (IOException ex) {
      Logger.getLogger(StoreST.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected final void saveLast() throws InterruptedException {
    try {
      Files.write(stfp, ("\n}").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    } catch (IOException ex) {
      Logger.getLogger(StoreST.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void createStorageFile() {
    try {
      //System.out.println("SST inner block");
      if (!Files.exists(stfp)) {
        Files.createFile(stfp);
      }
      //Los espacios extra son para la chapuza de la reescritura
      Files.write(stfp, "digraph G {".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void run() {
    try {
      this.save();
      this.end();
      System.out.println("Work for node " + this.getId() + " done");
    } catch (InterruptedException ex) {
    }
  }

}
