/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import java.sql.*;

/**
 *
 * @author Giuseppe
 */
public class ConnDB {
    private String host = "";
    private String port = "";
    private String nameDB = "";
    private String username = "";
    private String password = "";
    
    /**
     * Konsturktor lädt beim Aufruf die Verbindungsvariablen zur DB
     * aus der .bin-Datei
     */
    public ConnDB() {
        ReadWriteCredentialsDB rwc = new ReadWriteCredentialsDB();
        if(rwc.loadData()) {
            this.host = rwc.getHost();
            this.port = rwc.getPort();
            this.nameDB = rwc.getNameDB();
            this.username = rwc.getUsername();
            this.password = rwc.getPassword();
        }
    }
    
    /**
     * Konstruktor setzt die übergebenen Verbindungsvariablen als
     * Variablen für den Verbindungsaufbau zur DB
     * 
     * @param host - hostname
     * @param port - port
     * @param nameDB - Name der DB
     * @param username - Nutzername DB
     * @param password - Passwort DB
     */
    public ConnDB(String host, String port, String nameDB, String username, String password) { //bekommt Verbindungsvariablen übergeben
        this.host = host;
        this.port = port;
        this.nameDB = nameDB;
        this.username = username;
        this.password = password;
    }

    /**
     * Methode erstellt eine Connection zur DB und gibt diese zurück
     * 
     * @return - Connection zur DB
     */
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Treiber für DB aus Libraries laden
            String DBurl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.nameDB + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Connection con = DriverManager.getConnection(DBurl, username, password);
            return con;
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    /**
     * Methode erstellt eine Verbindung zum MySQL-Server und prüft, ob eine Datenbank
     * mit den ihr übergebenen Parametern erreichbar ist.
     * Ist sie es nicht, dann wird eine DB angelegt.
     * 
     * @return boolean; true = DB vorhanden oder angelegt; false = fehler
     */
    public boolean createDBIfNotExists() {
        try {
            Class.forName("com.mysql.jdbc.Driver"); //Treiber für DB aus Libraries laden
            String url = "jdbc:mysql://" + this.host + ":" + this.port + "/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Connection con = DriverManager.getConnection(url, username, password);
            Statement s = con.createStatement();
            int Result = s.executeUpdate("CREATE DATABASE IF NOT EXISTS "+ nameDB + " CHARACTER SET utf8mb4");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
