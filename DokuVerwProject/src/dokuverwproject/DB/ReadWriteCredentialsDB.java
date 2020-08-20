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
    private String nameDB = "";
    private String username = "";
    private String password = "";
    
    //Speicherort
    private String saveLocation = "";
    private String filename = "Dategt_DB_Cred.bin";

    /**
     * Konstruktor, um lokale Daten auszulesen
     */
    public ReadWriteCredentialsDB() {
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
        this.nameDB = nameDB;
        this.username = username;
        this.password = password;
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
            this.setNameDB(ld.getNameDB());
            this.setUsername(ld.getUsername());
            this.setPassword(ld.getPassword());
            this.setSaveLocation(ld.getSaveLocation());
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

    public String getNameDB() {
        return nameDB;
    }

    public void setNameDB(String nameDB) {
        this.nameDB = nameDB;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSaveLocation() {
        return saveLocation;
    }

    public void setSaveLocation(String saveLocation) {
        this.saveLocation = saveLocation;
    }

}
