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
public class ReadWriteCredentials implements Serializable{
    String filename = "Dategt_DB_Cred.bin";
    
    /**
     * Datenverbindung
     */
    private String host = "";
    private String port = "";
    private String nameDatenbank = "";
    private String benutzername = "";
    private String passwort = "";
    
    /**
     * Speicherort
     */
    private String pfadSpeicher = "";

    public ReadWriteCredentials(String host, String port, String nameDatenbank, String benutzername, String passwort) { //Daten speichern
        this.host = host;
        this.port = port;
        this.nameDatenbank = nameDatenbank;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }
    
    public ReadWriteCredentials() { //Daten auslesen
    }
    
    
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
    
    public boolean loadData() {
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            ReadWriteCredentials ld = (ReadWriteCredentials) is.readObject();
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
    
    /**
     * Datenverbindung
     */
    
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

    /**
     * Speicherort
     */
    public String getPfadSpeicher() {
        return pfadSpeicher;
    }

    public void setPfadSpeicher(String pfadSpeicher) {
        this.pfadSpeicher = pfadSpeicher;
    }

}
