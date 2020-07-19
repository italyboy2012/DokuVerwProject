/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DATA.Benutzer;
import dokuverwproject.DATA.DBConn;
import dokuverwproject.GUI.HauptFrame;
import dokuverwproject.GUI.NotifyFrame;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe
 */
public class Themengruppe {
    private long id = 0;
    private String titel = "";
    private String pfad = "";
    private Timestamp erstellungsdatum = null;
    private long größe = 0;
    private ArrayList<Datei> dateien = new ArrayList<>();
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ThemengruppeFrame
    
    public Themengruppe(long id, DefaultTableModel model) {
        this.id = id;
        this.model = model;
    }
    
    @Override
    public String toString() {
     return this.id + " - " + this.titel;   
    }
    
    public boolean loadFromDB() {
        if(id != 0) {
            try {
                DBConn dbc = new DBConn();
                Connection con = dbc.getConnection();
                if(con != null) {
                    PreparedStatement ps = null;
                    String query = "SELECT * FROM `themengruppen` WHERE `id` = ?";
                    ps = con.prepareStatement(query);
                    ps.setLong(1, id);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        if(rs.getLong(1) == id) {
                            this.titel = rs.getString(2);
                            this.pfad = rs.getString(3);
                            this.erstellungsdatum = rs.getTimestamp(4);
                            return true;
                        }
                    } else {
                        NotifyFrame nf = new NotifyFrame("Fehler", "Die gefundene Themengruppen-ID stimmt nicht mit der intern übergebenen ID überein.");
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Ein interner Übertragrungsfehler der Themengruppen-ID ist aufgetreten.");
        }
        return false;
    }
    
    public void ansichtAktualisieren() {
        
    }
    
    public boolean dateienIndexieren() {
        return false;
    }
    
    public void neueDateiHinzufügen(String pfad) {
        
    }
    
    public void dateiLöschen(long dateiId) {
        
    }
    
    public void dateiUmbenennen(long dateiId) {
        
    }
    
    public void dateiÖffnen(long dateiId) {
        
    }
    
    public void setGröße(int größe) {
        this.größe = größe;
    }
}
