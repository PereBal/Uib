/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public class Image extends Work {

  private int width = 0;
  // Difuminaremos por filas
  private int height = 0;
  private int y = 0;
  private BufferedImage cimg;
  private BufferedImage img;
  private final Path inf;
  private final Path outf;

  public Image(String id, Node ndi, String fn) throws IOException {
    super(id, ndi);
    inf = Paths.get(fn);
    outf = Paths.get(fn + ".out");
    if (!Files.exists(inf)) {
      throw new IOException("File " + fn + "not found");
    }
    img = ImageIO.read(inf.toFile());
    cimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
  }

  @Override
  protected final long[] calcWork(String data) throws IOException {
    try {
      if (!ndi.hasChildren() && ndi.isRoot()) {
        throw new IOException("El algorismo no puede funcionar con solo el nodo de entorno");
      }
      long[] res;
      JSONParser parser = new JSONParser();
      JSONObject obj = (JSONObject) parser.parse(data);
      int pYidx = 0, hidx = 1;
      int posY, height, yres;
      int len = this.ndi.getChildren().size();
      if (!this.ndi.isRoot()) { //El nodo se tiene q tener en cuenta a la hora de trabajar
        len++;
      }
      res = new long[len * 2];
      posY = Integer.parseInt(obj.get("y").toString());
      height = Integer.parseInt(obj.get("height").toString());

      yres = height / len;

      System.err.println(this.getId() + " py: " + posY + "h:" + yres + ", H:" + height + ", L:" + len);

      for (int i = 0; i < len; i++) {
        res[pYidx] = posY;
        res[hidx] = yres;

        // La nueva posY = posY anterior + alto/len
        posY += yres;

        pYidx = hidx + 1;
        hidx = pYidx + 1;
      }

      // Añadimos lo que sobra
      res[(len*2)-1] += height % len;
      System.out.println(Arrays.toString(res));

      return res;
    } catch (ParseException ex) {
      throw new IOException(ex);
    }
  }

  @Override
  protected final void distributeWork(long[] valToSend) {
    try {
      int pYidx, hidx;
      // Empezamos desde la 2a pareja.

      if (this.ndi.hasChildren()) {
        if (this.ndi.isRoot()) {
          pYidx = 0;
          hidx = 1;
        } else {
          pYidx = 2;
          hidx = 3;
        }
        java.util.Iterator<String> it = this.ndi.getChildren().iterator();
        String childID;
        JSONObject obj;

        while (it.hasNext()) {
          childID = it.next();
          obj = Builder.build_JSONObject(new Object[]{"y", valToSend[pYidx], "height", valToSend[hidx], "file", this.inf.toString()});
          //valToSend[pYidx] = 0;
          pYidx = hidx + 1;
          hidx = pYidx + 1;
          this.ndi.sendMsg(this.getConnection(), "IMGWORK", childID, obj);
          this.ndi.incrODef();
        }
        this.y = (int) valToSend[0];
        this.height = (int) valToSend[1];

      } else {
        // Guardamos la {lx,y} maxima teniendo en cuenta que la X es constante
        hidx = 1;
        this.y = (int) valToSend[0];
        this.height = (int) valToSend[1];
        for (int i = 1, max = valToSend.length / 2; i < max; i++) {
          //if (this.height < valToSend[hidx]) {
          this.height += (int) valToSend[hidx];
          //}
          hidx += 2;
        }
      }
    } catch (ParseException ex) {
      Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void run() {
    try {
      this.width = img.getWidth();
      int npixel;
      for (int lx = 0; lx < width; lx++) {
        for (int ly = y; ly < (height + y); ly++) {
          // Optimo de la hostia xD
          npixel = img.getRGB(lx, ly) * 4;
          if (lx + 1 < width) {
            npixel += img.getRGB(lx + 1, ly);
          }
          if (ly + 1 < (height + this.y)) {
            npixel += img.getRGB(lx, ly + 1);
          }
          if (lx - 1 > 0) {
            npixel += img.getRGB(lx - 1, ly);
          }
          if (ly - 1 > 0) {
            npixel += img.getRGB(lx, ly - 1);
          }
          npixel /= 8;
          cimg.setRGB(lx, ly, npixel);
        }
      }
      save();
      this.end();
    } catch (InterruptedException ex) {
      Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected final void save() throws InterruptedException {
    try {
      this.enterCS();
      System.out.println(ndi.getId() + " " + System.currentTimeMillis() + " stores image: " + this.y + " " + this.height);
      BufferedImage oimg = ImageIO.read(outf.toFile());
      oimg.setData(cimg.getData(new java.awt.Rectangle(0, y, width, height)));
      ImageIO.write(oimg, "png", outf.toFile());
      this.exitCS();
    } catch (IOException ex) {
      Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected final void createStorageFile() {
    try {
      ImageIO.write(cimg, "png", outf.toFile());
    } catch (IOException ex) {
      Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public int getImgY() {
    return this.y;
  }

  public int getHeight() {
    return this.img.getHeight();
  }
}
