/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DATA;

import dokuverwproject.GUI.NotifyFrame;
import java.sql.*;

/**
 *
 * @author Giuseppe
 */
public class DBConn {
    private String host = "localhost";
    private String port = "3306";
    private String nameDatenbank = "dategt_dokuverw";
    private String benutzername = "root";
    private String passwort = "";
    
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
