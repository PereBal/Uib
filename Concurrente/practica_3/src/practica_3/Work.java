/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import dk.safl.beanstemc.Beanstemc;
import dk.safl.beanstemc.BeanstemcException;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pere
 */
public abstract class Work {

  private String workId = null;
  protected final Node ndi;
  private volatile Beanstemc c;

  private volatile Semaphore mutex = new Semaphore(1);
  private volatile Semaphore token = new Semaphore(0);

  public Work(String id, Node ndi) {
    this.workId = id;
    this.ndi = ndi;
    try {
      this.c = new Beanstemc(ndi.addr, ndi.port);
    } catch (IOException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Only client nodes call this method
  public synchronized void end() {
    ndi.decrPendingJobs();
    try {
      this.ndi.sendSig(c);
    } catch (ParseException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public synchronized void setId(String newId) {
    this.workId = newId;
  }

  public String getId() {
    return this.workId;
  }

  public Beanstemc getConnection() {
    return this.c;
  }

  public void receiveToken() {
    this.token.release();
    //System.err.println("TOKEN released on " + this.workId);
  }

  public void adquireToken() throws InterruptedException {
    token.acquire();
  }

  public void enterCS() throws InterruptedException {
    mutex.acquire();
    try {
      //System.err.println(this.ndi.getId() + " is going to request the token" + this.toString());
      if (!ndi.isHolding()) {
        //System.err.println(this.ndi.getId() + " requests the token whit parent " + ndi.getVirtualParent());
        ndi.sendMsg(c, "REQ", ndi.getVirtualParent(), (Object) this.workId/*Builder.build_JSONObject(new Object[]{"origin", this.workId, "csid", csid.toString()})*/);
        ndi.setVirtualParent(null);
        mutex.release();
        token.acquire();
        mutex.acquire();
      }
      ndi.setHolding(false);
    } catch (ParseException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      mutex.release();
    }
  }

  public void exitCS() throws InterruptedException {
    mutex.acquire();
    try {
      if (ndi.getDeferred() != null) {
        this.ndi.sendMsg(c, "TOK", ndi.getDeferred());
        ndi.setDeferred(null);
      } else {
        ndi.setHolding(true);
      }
    } catch (ParseException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      mutex.release();
    }
  }

  public void processRequest(org.json.simple.JSONObject obj, String origin) throws ParseException, BeanstemcException {
    try {
      //System.err.println("Before(" + System.nanoTime() + "): " + this + " s=" + s);
      mutex.acquire();
      if (ndi.isVirtualRoot()) {
        if (ndi.isHolding()) {
          this.ndi.sendMsg(c, "TOK", origin);
          ndi.setHolding(false);
        } else {
          //System.err.println("Set Deferred-" + this.workId + "-" + origin);
          ndi.setDeferred(origin);
        }
      } else {
        //System.err.println("Resend-" + this.workId + "-" + origin);
        this.ndi.sendMsg(c, "REQ", ndi.getVirtualParent(), origin);
      }
      ndi.setVirtualParent(obj.get("src").toString());
      mutex.release();
      //System.err.println("After(" + System.nanoTime() + "): " + this);
    } catch (InterruptedException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected abstract long[] calcWork(String data) throws java.io.IOException;

  protected abstract void distributeWork(long[] valToSend);

  protected abstract void save() throws InterruptedException;

  protected abstract void createStorageFile();

  protected abstract void run();

  @Override
  public String toString() {
    return "W[" + this.workId + "]: parent->" + /*Arrays.toString(*/ ndi.getVirtualParent()/*)*/ + ", deferred->" + /*Arrays.toString(*/ ndi.getDeferred()/*)*/ + ", holding->" + /*Arrays.toString(*/ ndi.isHolding()/*)*/;
  }
}
