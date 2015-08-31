/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import dk.safl.beanstemc.Beanstemc;
import dk.safl.beanstemc.BeanstemcException;
import dk.safl.beanstemc.Job;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public class Client extends Node {

  private volatile Beanstemc c = null;
  //private final LinkedList<Work> wList = new LinkedList();
  private volatile LinkedBlockingQueue<JSONObject> wList = null;
  //private final LinkedList<String> unusedTubes = new LinkedList();
  private volatile Work currentWork = null;
  private volatile boolean end = false;
  private volatile Semaphore innerEnd = null;
  private volatile Semaphore ready = null;
  // Este semaforo sirve para garantizar que debido al asincronismo del receiver al hacer pop de un trabajo de la queue
  // y recuperar el estado del anterior trabajo, se produzca un cambio no deseado.
  private volatile Semaphore mutex = null;
  private volatile boolean firstCNT = true, firstSPT = true, firstIMG = true, askForReady = false;

  public Client(String id) throws IOException {
    super(id);
  }

  public Client(String id, JSONArray children) throws IOException {
    super(id, children);
  }

  public Client(String id, String parent, JSONArray children) throws IOException {
    super(id, parent, children);
  }

  private class Slave extends Thread {

    private final Client node;

    private Slave(Client c) {
      this.node = c;
    }

    @Override
    public void run() {
      try {
        /*
         * El follon de fr es para garantizar que el valor que contienen las
         * variables parent, deferred y holding son validas hasta que se llega al
         * metodo run() del nuevo trabajo.
         * La idea es que a partir de la 1ª iteracion el semaforo se mantiene siempre
         * que se pueda continuar de inmediato con el siguiente trabajo de la queue
         */
        innerEnd.acquire();
        JSONObject cw;
        while (!end) {
          cw = wList.take();
          mutex.acquire();
          switch (cw.get("type").toString()) {
            case "CNTWORK": //msg = {type,src,data}              
              currentWork = new Count(node.getId(), node);
              currentWork.distributeWork(currentWork.calcWork(cw.get("data").toString()));
              if (askForReady) {
                ready.release();
                askForReady = false;
              }
              mutex.release();

              currentWork.run();
              break;

            case "SPTWORK":
              currentWork = new StoreST(node.getId(), node);
              currentWork.distributeWork(null);
              if (askForReady) {
                ready.release();
                askForReady = false;
              }
              mutex.release();

              currentWork.run();
              break;

            case "IMGWORK":
              JSONObject imgData = (JSONObject) cw.get("data");
              currentWork = new Image(node.getId(), node, imgData.get("file").toString());
              currentWork.distributeWork(currentWork.calcWork(imgData.toString()));
              if (askForReady) {
                ready.release();
                askForReady = false;
              }
              mutex.release();

              currentWork.run();
              break;

            case "END":
              System.err.println("Thread" + this.getId() + " ended");
              //end == true
              break;
          }
        }
        firstCNT = true;
        firstSPT = true;
        firstIMG = true;
        System.out.println("Released " + currentWork);
        innerEnd.release();
      } catch (InterruptedException e) {
      } catch (IOException ex) {
        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  protected final JSONObject processArgs(String[] args) throws IOException, NullPointerException {
    try {
      if (args.length > 3 || args.length < 2) {
        throw new IOException("USAGE: java -jar file.jar { (-r|--remote) NDID FILE }");
      }
      JSONObject rem = Builder.build_JSONObject(new Object[]{"remote", "F", "id", "", "children", "[]"});
      if (args.length == 3) {
        if (args[0].equals("-r") || args[0].equals("--remote")) {
          rem.put("remote", "T");
          rem.put("id", args[1]); //no further error control
          JSONObject obj = parse(args[2]);
          rem.put("children", obj.get(rem.get("id")));
        } else {
          throw new IOException("USAGE: java -jar file.jar { (-r|--remote) NDID FILE }");
        }
      }
      return rem;
    } catch (ParseException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }

  @Override
  protected final void receiver(JSONObject obj) throws ParseException {
    try {
      initClient();
      try {
        Slave slv = new Slave(this);
        slv.start();
        JSONParser parser = new JSONParser();

        Job j;
        String s, s1;
        Work w = null;
        end = false;
        while (!end) {
          //job -> toString
          j = c.reserve();
          c.delete(j);
          s = new String(j.getData());

          //JSONString -> JSONObject
          obj = (JSONObject) parser.parse(s);
          if (obj.get("type").toString().endsWith("WORK")) {
            System.out.println("C" + this.getId() + " receive: " + s + "::" + currentWork + "::W::" + w);
          }

          //System.err.println(obj.toString());
          s = obj.get("type").toString();
          if (s.endsWith("WORK")) {

            s = s.substring(0, 3);
            s1 = obj.get("src").toString();
            mutex.acquire();
            this.receiveMessage(s1);

            //if (s.startsWith("CNT")) {
            switch (s) {
              case "CNT":
                this.incrPendingJobs();
                if (firstCNT) {
                  this.setVirtualParent(s1);
                  firstCNT = false;
                }
                wList.add(obj);
                break;

              case "SPT":
                //}else if(s.startsWith("SPT")){
                if (firstSPT) {
                  this.incrPendingJobs();
                  this.setVirtualParent(s1);
                  firstSPT = false;
                  wList.add(obj);
                }
                break;

              case "IMG":
                this.incrPendingJobs();
                if (firstIMG) {
                  this.setVirtualParent(s1);
                  firstIMG = false;
                }
                wList.add(obj);
                break;
            }
            mutex.release();
            this.sendSig(c);

          } else {

            switch (s) {
              case "REQ": //{type, src, data} data == originator
                if (currentWork == null) {
                  System.err.println("Current work null on REQ");
                  askForReady = true;
                  ready.acquire();
                }
                mutex.acquire();
                //synchronized (this) {
                currentWork.processRequest(obj, obj.get("data").toString());
                //}
                mutex.release();
                break;

              // los trabajos no tienen id, simplemente concedemos a alguno que lo haya solicitado el token siguiendo una politica RR sobre las solicitudes.
              case "TOK": //{type,src, data} data == originator}
                if (currentWork == null) {
                  System.err.println("Current work null on TOK");
                  askForReady = true;
                  ready.acquire();
                }
                mutex.acquire();
                //synchronized (this) {
                currentWork.receiveToken();
                //currentWork.processRequest(obj, obj.get("src").toString());
                //}
                mutex.release();
                break;

              case "SIG": //{type,src} 
                //System.err.println("SIG received from: " + obj.toString());
                this.decrODef();
                this.sendSig(c);
                break;

              case "END":
                stopThread(slv);
                end = true;
                break;

              default:
                end = true;
                throw new IOException("Unrecognized message type");
            }
          }
        }
        if (!this.hasFinished()) {
          throw new IOException(this.getId() + " has reached last code statement without finishing all pending jobs");
        }
        end();
        slv.interrupt();
      } catch (BeanstemcException ex) {
        c.quit();
        throw new IOException(ex);
      }
    } catch (IOException | InterruptedException | BeanstemcException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      System.gc();
    }
  }

  private void initClient() throws IOException, BeanstemcException {
    c = this.getConnection();
    c.watch(this.getOwnTube());
    this.innerEnd = new Semaphore(1);
    this.mutex = new Semaphore(1);
    this.ready = new Semaphore(0);
    this.wList = new LinkedBlockingQueue();
  }

  private synchronized void stopThread(Slave slv) throws ParseException {
    try {
      //System.err.println("Stopping " + slv);
      end = true;
      wList.add(Builder.build_JSONObject(new Object[]{"type", "END"}));
      innerEnd.acquire();
      end = false;
      innerEnd.release();
    } catch (InterruptedException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void end() throws IOException, BeanstemcException {
    try {
      // Las hojas no entraran en ninguno de los bucles i responderan aqui. 
      if (this.getChildren().size() != 0) {
        Iterator<String> it = this.getChildren().iterator();
        while (it.hasNext()) {
          String id = it.next();
          this.sendMsg(c, "END", id);
        }
      }
    } catch (ParseException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args) throws ParseException, IOException {
    Client cn = new Client("-2"); //foo var.. i don't want to add a setter to the id -_-
    JSONObject pargs = cn.processArgs(args);
    JSONParser parser = new JSONParser();
    if (pargs.get("remote").equals("T")) {
      cn = new Client(pargs.get("id").toString(), (JSONArray) pargs.get("children"));
    } else {
      cn = new Client(args[0], (JSONArray) parser.parse(args[1]));
    }
    cn.receiver(pargs);
  }
}
