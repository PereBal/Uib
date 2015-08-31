/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_3;

import dk.safl.beanstemc.BeanstemcException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author pere
 */
public abstract class Node {

  public static final int NND = (16 + 1);
  private final String id;
  private volatile String parent = null;
  private final JSONArray children;
  private volatile int inDef = 0;
  private volatile int outDef = 0;
  private volatile int pendingJobs = 0;
  private final int[] inDefa;
  private volatile dk.safl.beanstemc.Beanstemc conn;
  protected final String addr = "localhost";
  protected final int port = 15000;
  private volatile String deferred = null;
  private volatile boolean holding = false;
  private volatile String virtualParent = null;

  public Node(String id) throws IOException {
    this.inDefa = new int[NND];
    this.id = id;
    this.children = new JSONArray();
    for (int i = 0; i < NND; i++) {
      this.inDefa[i] = 0;
    }
    this.conn = new dk.safl.beanstemc.Beanstemc(this.addr, this.port);
  }

  public Node(String id, JSONArray children) throws IOException {
    this.inDefa = new int[NND];
    this.id = id;
    this.children = new JSONArray();
    this.children.addAll(children);
    for (int i = 0; i < NND; i++) {
      this.inDefa[i] = 0;
    }
    this.conn = new dk.safl.beanstemc.Beanstemc(this.addr, this.port);
  }

  public Node(String id, String parent, JSONArray children) throws IOException {
    this.inDefa = new int[NND];
    this.id = id;
    this.parent = parent;
    this.children = new JSONArray();
    this.children.addAll(children);
    for (int i = 0; i < NND; i++) {
      this.inDefa[i] = 0;
    }
    this.conn = new dk.safl.beanstemc.Beanstemc(this.addr, this.port);
  }

  public synchronized String getDeferred() {
    return this.deferred;
  }

  public synchronized boolean isHolding() {
    return this.holding;
  }

  public synchronized void setDeferred(String nd) {
    this.deferred = nd;
  }

  public synchronized void setHolding(boolean val) {
    this.holding = val;
  }

  public void setVirtualParent(String np) {
    this.virtualParent = np;
  }

  public synchronized String getVirtualParent() {
    return this.virtualParent;
  }

  public synchronized boolean isVirtualRoot() {
    return this.virtualParent == null;
  }

  /* SETTERS */
  public synchronized void setParent(String parent) {
    if (this.parent == null) {
      this.parent = parent;
    }
  }

  public synchronized void setChild(int i, String s) {
    this.children.set(i, s);
  }

  public synchronized void setChildren(JSONArray children) {
    this.children.addAll(children);
  }

  public synchronized void resetIDef() {
    this.inDef = 0;
  }

  public synchronized void resetODef() {
    this.outDef = 0;
  }

  public synchronized void incrODef() {
    this.outDef += 1;
  }

  public synchronized void decrODef() {
    if (this.outDef == 0) {
      System.err.println(this.toString() + " is trying to decrement a zero!!");
    }
    this.outDef -= 1;
  }

  public synchronized boolean receiveMessage(String src) {
    boolean hadParent = this.parent != null;
    if (this.parent == null) {
      this.parent = src;
    }
    this.inDefa[Integer.parseInt(src)]++;
    this.inDef++;
    return hadParent;
  }

  public synchronized void signalMessage(int src) {
    this.inDefa[src]--;
    this.inDef--;
  }

  public synchronized void restoreDefault() {
    if (this.inDef == 1) {
      this.inDefa[Integer.parseInt(this.parent)] = 0;
      this.inDef = 0;
      this.parent = null;
    } else {
      System.err.println("Restore default with inDef != 1");
    }
  }

  protected synchronized void decrPendingJobs() {
    this.pendingJobs--;
  }

  public synchronized void incrPendingJobs() {
    this.pendingJobs++;
  }

  /* GETTERS */
  public String getId() {
    return this.id;
  }

  public String getParent() {
    return this.parent;
  }

  public String getChild(int i) throws IOException {
    if (this.children.isEmpty()) {
      throw new java.io.IOException("Empty children array");
    }
    return this.children.get(i).toString();
  }

  public JSONArray getChildren() {
    return this.children;
  }

  public int getInDeficid() {
    return this.inDef;
  }

  public int getOutDeficid() {
    return this.outDef;
  }

  public dk.safl.beanstemc.Beanstemc getConnection() {
    if (this.conn == null) {
      throw new NullPointerException("Null connection field");
    }
    return this.conn;
  }

  public String getOwnTube() throws BeanstemcException {
    if (this.id == null) {
      throw new dk.safl.beanstemc.BeanstemcException("Tube not initialized");
    }
    return "t_" + this.id;
  }

  public String getParentTube() throws BeanstemcException {
    if (this.parent == null) {
      throw new dk.safl.beanstemc.BeanstemcException("Tube not initialized");
    }
    return "t_" + this.parent;
  }

  public String getTube(String elemId) throws BeanstemcException {
    if (Integer.parseInt(elemId) >= Node.NND) {
      throw new dk.safl.beanstemc.BeanstemcException("Tube not initialized");
    }
    return "t_" + elemId;
  }

  public synchronized String getNodeIdToSignal() {
    boolean found;
    int i = 0;
    do {
      found = (this.inDefa[i] > 1) || (this.inDefa[i] == 1 && !this.parent.equals("" + i));
      i++;
    } while (!found && i < NND);
    return "" + (i - 1);
  }

  public boolean hasParent() {
    return this.parent != null;
  }

  public boolean hasChildren() {
    return !this.children.isEmpty();
  }

  public boolean isRoot() {
    if (this.parent == null) {
      throw new NullPointerException("I can't decide wether this node is root or not");
    }
    return this.parent.equals("-1");
  }

  public boolean isRoot(String id) {
    return id.equals("-1");
  }

  public boolean hasFinished() {
    return this.pendingJobs == 0;
  }

  @Override
  public String toString() {
    return "id: " + this.id + ", children: " + this.children.toString() + ", parent: " + this.parent + ", inDef: " + this.inDef + ""
            + ", inDefa: " + Arrays.toString(this.inDefa) + ", outDef: " + this.outDef;
  }

  public final static JSONObject parse(String fn) throws IOException {
    BufferedReader br = Files.newBufferedReader(Paths.get(fn), StandardCharsets.UTF_8);
    if (br.readLine() == null) { //nos merendamos la primera linea de definicones + comprobamos que el fichero no este vacio
      br.close();
      throw new IOException("Empty File");
    }

    JSONObject obj = new JSONObject();
    String line, key, val, regexp = "[^0-9]+";
    String[] elem;
    JSONArray js;
    int lcount = 1;

    while (!(line = br.readLine()).equals("}")) { //el fichero acaba en '}' a fregar el ctrl de errores

      if ((elem = line.split(regexp)).length != 3) { //el fichero esta mal formateado 
        br.close();
        throw new IOException("File Bad formated at line: " + lcount);
      }

      // se podria añadir un control de errores pero supondremos que el formato es el correcto
      key = elem[1];
      val = elem[2];

      //Es mes eficient això que el merge
      js = (obj.containsKey(key)) ? ((JSONArray) obj.get(key)) : (new JSONArray());
      js.add(val);
      obj.put(key, js);

      lcount++;
    }

    br.close();
    return obj;
  }

  protected final void cleanup(dk.safl.beanstemc.Beanstemc p) throws IOException, BeanstemcException {
    System.err.println("CLEANING TUBES");
    for (int i = 0; i < NND; i++) {
      try {
        //System.err.println("Cleanup of t_" + i + " started...");
        p.watch(this.getTube("" + i));
        dk.safl.beanstemc.Job j;
        while (true) {
          //System.out.println(new String(j.getData()));
          j = p.reserve(1);
          p.delete(j);
        }
        //System.err.println("Cleanup of t_" + i + " finished!");
      } catch (BeanstemcException ex) {
        System.err.println("Cleanup of t_" + i + " finished!");
      } finally {
        p.ignore(this.getTube("" + i));
      }
    }
    System.err.println("TUBES CLEANED");
  }

  protected synchronized void sendMsg(dk.safl.beanstemc.Beanstemc c, String jobType, String dstId) throws org.json.simple.parser.ParseException {
    try {
      if (jobType.equals("TOK")) {
        //System.out.println("MSG{t: " + jobType + ", src: " + this.getId() + ", dst: " + dstId + "}");
      }
      dstId = this.getTube(dstId);
      c.use(dstId);
      String msg = Builder.build_Msg(new Object[]{"type", jobType, "src", this.getId()});
      //String msg = "{\"type\":\""+jobType+"\", \"src\":\""+this.getId()+"\"}";
      //System.err.println(msg + " goes to " + dstId);
      c.put(msg.getBytes(), dk.safl.beanstemc.Beanstemc.DEFAULT_PRIORITY, 0, dk.safl.beanstemc.Beanstemc.DEFAULT_TTR);
    } catch (IOException | BeanstemcException ex) {
      java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
  }

  protected synchronized void sendMsg(dk.safl.beanstemc.Beanstemc c, String jobType, String dstId, Object data) throws org.json.simple.parser.ParseException {
    try {
      if (jobType.equals("REQ") || jobType.equals("TOK")) {
        //System.out.println("MSG{t: " + jobType + ", src: " + this.getId() + ", dst: " + dstId + ", data: " + data.toString() + "}");
      }
      dstId = this.getTube(dstId);
      c.use(dstId);
      JSONObject msg = Builder.build_JSONObject(new Object[]{"type", jobType, "src", this.getId(), "data", data});
      //String msg = "{\"type\":\""+jobType+"\", \"src\":\""+this.getId()+"\", \"data\":\""+data.toString()+"\"}";
      //System.err.println(msg + " goes to " + dstId);
      c.put(msg.toString().getBytes(), dk.safl.beanstemc.Beanstemc.DEFAULT_PRIORITY, 0, dk.safl.beanstemc.Beanstemc.DEFAULT_TTR);
    } catch (IOException | BeanstemcException ex) {
      java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
  }

  protected synchronized void sendSig(dk.safl.beanstemc.Beanstemc c) throws org.json.simple.parser.ParseException {
    if (this.inDef > 1) {
      //System.err.println("Acl" + this.getId() + "::" + this.toString());
      String idn = this.getNodeIdToSignal();
      this.sendMsg(c, "SIG", idn);
      this.signalMessage(Integer.parseInt(idn));
      //System.out.println("SIG{nid: " + this.getId() + ", idef: " + this.getInDeficid() + ", odef: " + this.getOutDeficid() + "}");
    } else if (this.inDef == 1 && this.pendingJobs == 0 && this.outDef == 0) {
      //System.err.println("B" + this.getId() + "::" + this.toString());
      this.sendMsg(c, "SIG", this.getParent());
      this.restoreDefault();
      //System.out.println("SIG{nid: " + this.getId() + ", idef: " + this.getInDeficid() + ", odef: " + this.getOutDeficid() + "}");
    }
  }

  protected abstract JSONObject processArgs(String[] args) throws IOException, NullPointerException;

  protected abstract void receiver(JSONObject obj) throws org.json.simple.parser.ParseException;
}
