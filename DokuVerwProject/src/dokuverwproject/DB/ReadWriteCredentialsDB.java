/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Giuseppe
 */
public class ReadWriteCredentialsDB implements Serializable {
    // Datenverbindung
    private String host = "";
    private String port = "";
    private String nameDatenbank = "";
    private String benutzername = "";
    private String passwort = "";
    
    //Speicherort
    private String pfadSpeicher = "";
    String filename = "Dategt_DB_Cred.bin";

    /**
     * Konstruktor, um lokale Daten auszulesen
     */
    public ReadWriteCredentialsDB() { //Daten auslesen
    }
    
    /**
     * Konstruktor, um Daten lokal zu speichern
     * 
     * @param host
     * @param port
     * @param nameDB
     * @param username
     * @param password 
     */
    public ReadWriteCredentialsDB(String host, String port, String nameDB, String username, String password) {
        this.host = host;
        this.port = port;
        this.nameDatenbank = nameDB;
        this.benutzername = username;
        this.passwort = password;
    }
    
    /**
     * Methode speichert die im Kontruktor 2 übergebenen Datein lokal ab
     * 
     * @return - true = gepsichert; false = fehler
     */
    public boolean saveData() {
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(this);
            os.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }
    
    /**
     * Methode lädt lokal gespeicherte Daten
     * 
     * @return - true = gepsichert; false = fehler
     */
    public boolean loadData() {
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            ReadWriteCredentialsDB ld = (ReadWriteCredentialsDB) is.readObject();
            is.close();
            this.setHost(ld.getHost());
            this.setPort(ld.getPort());
            this.setNameDatenbank(ld.getNameDatenbank());
            this.setBenutzername(ld.getBenutzername());
            this.setPasswort(ld.getPasswort());
            this.setPfadSpeicher(ld.getPfadSpeicher());
            return true;
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }
    
    // Getter und Setter
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getNameDatenbank() {
        return nameDatenbank;
    }

    public void setNameDatenbank(String nameDatenbank) {
        this.nameDatenbank = nameDatenbank;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
    
    public String getPfadSpeicher() {
        return pfadSpeicher;
    }

    public void setPfadSpeicher(String pfadSpeicher) {
        this.pfadSpeicher = pfadSpeicher;
    }

}
