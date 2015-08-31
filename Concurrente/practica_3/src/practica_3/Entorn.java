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
import java.lang.ProcessBuilder.Redirect;
import java.util.Iterator;
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
public class Entorn extends Node {

  private Beanstemc p;
  private boolean remote;

  public Entorn(String id) throws IOException {
    super(id);
    initServer();
  }

  public Entorn(String id, JSONArray children) throws IOException {
    super(id, children);
    initServer();
  }

  public Entorn(String id, String parent, JSONArray children) throws IOException {
    super(id, parent, children);
    initServer();
  }

  @Override
  protected final JSONObject processArgs(String[] args) throws IOException, NullPointerException {
    try {
      if (args.length > 2) {
        throw new IOException("USAGE: java -jar file.jar {-r|--remote} FILE");
      }
      String fn = null; //temporal

      JSONObject rem = Builder.build_JSONObject(new Object[]{"remote", "F", "file", ""});
      for (int i = 0, len = args.length; i < len; i++) {
        if (args[i].equals("-r") || args[i].equals("--remote")) {
          rem.put("remote", "T");
        } else {
          rem.put("file", args[i]);
          fn = args[i]; //temporal
        }
      }
      if (fn == null) { //solo para testear
        rem.put("file", "/home/pere/Documents/Uib/CURS_2014-2015/PC/entrega_3/graph.dot");
      }
      return rem;
    } catch (ParseException ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }

  private void countWork() {
    try {
      JSONObject obj;
      JSONParser parser = new JSONParser();
      Job j;
      String msg;
      System.out.println("Entorn starts at: " + System.currentTimeMillis());
      Count w = new Count("0", this);
      this.setHolding(true); // holds Token, not in CS
      this.setVirtualParent(null);//isRoot
      w.createStorageFile();
      w.distributeWork(w.calcWork("1000000"));
      while (this.getOutDeficid() > 0) {
        //job -> toString
        j = p.reserve();
        p.delete(j);
        msg = new String(j.getData());
        System.out.println("C" + this.getId() + " receive: " + msg);

        //JSONString -> JSONObject
        obj = (JSONObject) parser.parse(msg);

        switch (obj.get("type").toString()) {
          case "REQ": //{type, src, data} data == {workId}
            w.processRequest(obj, obj.get("data").toString());
            break;

          case "TOK": //{type, src}
            w.receiveToken();
            break;

          case "SIG": //{type,src}
            this.decrODef();
            break;

          default:
            p.quit();
            throw new IOException("Unknown message type: " + msg);
        }
      }
      System.out.println("Entorn ends at: " + System.currentTimeMillis() + " whit totales.txt value as " + w.readStoreFile());
    } catch (IOException | BeanstemcException | ParseException ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void storeSpanningTreeWork() {
    try {
      JSONObject obj;
      JSONParser parser = new JSONParser();
      Job j;
      String msg;
      StoreST w = new StoreST("0", this);
      this.setHolding(true); // holds Token not in CS
      this.setVirtualParent(null); //isRoot
      w.distributeWork(null);
      w.createStorageFile();

      while (this.getOutDeficid() > 0) {
        //job -> toString
        j = p.reserve();
        p.delete(j);
        msg = new String(j.getData());
        System.out.println("C" + this.getId() + " receive: " + msg);

        //JSONString -> JSONObject
        obj = (JSONObject) parser.parse(msg);

        switch (obj.get("type").toString()) {
          case "REQ": //{type, src, data} data == {workId}
            w.processRequest(obj, obj.get("data").toString());
            break;

          case "TOK":
            w.receiveToken();
            break;

          case "SIG": //{type,src}
            this.decrODef();
            break;

          default:
            p.quit();
            throw new IOException("Unknown message type: " + msg);
        }
      }
      w.saveLast();
      System.out.println("Entorn ends has stored the Spamming three");
    } catch (IOException | BeanstemcException | ParseException | InterruptedException ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void ImgWork(String filename) {
    try {
      JSONObject obj;
      JSONParser parser = new JSONParser();
      Job j;
      String msg;

      Image w = new Image("0", this, filename);
      this.setHolding(true); // holds Token, not in CS
      this.setVirtualParent(null);//isRoot
      w.distributeWork(w.calcWork(Builder.build_Msg(new Object[]{"y", 0, "height", w.getHeight()})));
      w.createStorageFile();
      
      while (this.getOutDeficid() > 0) {
        //job -> toString
        j = p.reserve();
        p.delete(j);
        msg = new String(j.getData());
        //System.out.println("C" + this.getId() + " receive: " + msg);

        //JSONString -> JSONObject
        obj = (JSONObject) parser.parse(msg);

        switch (obj.get("type").toString()) {
          case "REQ": //{type, src, data} data == {workId}
            w.processRequest(obj, obj.get("data").toString());
            break;

          case "TOK": //{type, src}
            w.receiveToken();
            break;

          case "SIG": //{type,src}
            this.decrODef();
            break;

          default:
            p.quit();
            throw new IOException("Unknown message type: " + msg);
        }
      }
      System.out.println("Entorn ends image stored as " + filename + ".out");
    } catch (IOException | BeanstemcException | ParseException ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  protected void receiver(JSONObject obj) throws ParseException {
    try {
      try {
        //countWork();
        //storeSpanningTreeWork();
        ImgWork("../img/amon_amarth_deluxe.png");
        end();
      } catch (BeanstemcException ex) {
        p.quit();
        Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (IOException ex) {
      Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      System.gc();
    }
  }

  private void launch(JSONObject obj) throws IOException {
    ProcessBuilder builder;
    for (int i = 1; i < NND; i++) {
      JSONArray jsonArry = (JSONArray) obj.get("" + i);
      if (jsonArry == null) {
        jsonArry = new JSONArray();
      }
      //builder = new ProcessBuilder("java", "-jar", "../Testing_Client/dist/Testing_Client.jar", "" + i, jsonArry.toString());
      //TEMPORAL! per a l'entrega final fer un .jar a ma incloent el classpath com toca
      builder = new ProcessBuilder("java", "-classpath", "./build/classes/:./lib/CopyLibs/Beanstemc.jar:./lib/CopyLibs/json-simple-1.1.1.jar", "practica_3.Client", "" + i, jsonArry.toString());
      builder.redirectError(Redirect.INHERIT); //en lugar de hacer el Redirect.PIPE por defecto
      builder.redirectOutput(Redirect.INHERIT);
      builder.start();
    }
  }

  private void end() throws IOException, BeanstemcException {
    try {
      Iterator it = this.getChildren().iterator();
      while (it.hasNext()) {
        String id = it.next().toString();
        this.sendMsg(p, "END", id);
      }
    } catch (Exception ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void initServer() throws IOException {
    try {
      p = this.getConnection();
      this.cleanup(p);
      p.watch(this.getOwnTube());
    } catch (BeanstemcException ex) {
      Logger.getLogger(Entorn.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args) throws InterruptedException, ParseException, IOException, BeanstemcException {
    Entorn en = new Entorn("0");
    en.remote = en.processArgs(args).get("remote").toString().equals("T");
    JSONObject obj = parse(en.processArgs(args).get("file").toString());
    en.launch(obj);
    en.setParent("-1");
    en.setChildren((JSONArray) obj.get("0"));
    en.receiver(obj);
  }

}
