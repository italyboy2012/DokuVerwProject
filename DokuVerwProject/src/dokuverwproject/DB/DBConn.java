/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import dokuverwproject.GUI.NotifyFrame;
import java.sql.*;

/**
 *
 * @author Giuseppe
 */
public class DBConn {
    private String host = "";
    private String port = "";
    private String nameDatenbank = "";
    private String benutzername = "";
    private String passwort = "";
    
    /**
     * Konsturktor lädt beim Aufruf die Verbindungsvariablen zur DB
     * aus der .bin-Datei
     */
    public DBConn() {
        ReadWriteCredentials rwc = new ReadWriteCredentials();
        if(rwc.loadData()) {
            this.host = rwc.getHost();
            this.port = rwc.getPort();
            this.nameDatenbank = rwc.getNameDatenbank();
            this.benutzername = rwc.getBenutzername();
            this.passwort = rwc.getPasswort();
        }
    }
    
    /**
     * Konstruktor setzt die übergebenen Verbindungsvariablen als
     * Variablen für den Verbindungsaufbau zur DB
     * 
     * @param host
     * @param port
     * @param nameDatenbank
     * @param benutzername
     * @param passwort 
     */
    public DBConn(String host, String port, String nameDatenbank, String benutzername, String passwort) { //bekommt Verbindungsvariablen übergeben
        this.host = host;
        this.port = port;
        this.nameDatenbank = nameDatenbank;
        this.benutzername = benutzername;
        this.passwort = passwort;
    }
    
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Treiber für DB aus Libraries laden
            String DBurl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.nameDatenbank + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Connection con = DriverManager.getConnection(DBurl, benutzername, passwort);
            return con;
        } catch(Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Verbindungsversuch mit der Datenbank.");
        }
        return null;
    }
    
}
