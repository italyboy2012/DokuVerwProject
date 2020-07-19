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
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
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
    private javax.swing.JTable table = null; // Zugriff auf Tabelle in ThemengruppeFrame
    
    public Themengruppe(long id, javax.swing.JTable table) {
        this.id = id;
        this.table = table;
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
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        table.scrollRectToVisible(table.getCellRect(0,0, true)); 

        
        Object[] row = new Object[5];
        
        //jTextField2.setText(pfadNav);
        final File[] x = new File(pfad).listFiles();
        for (final File file : x) {
            
            ImageIcon img = (ImageIcon) javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon( file );
            row[0] = img;
            row[1] = file.getName();
            row[2] = "";
            row[3] = file.getTotalSpace();
            row[4] = file.getPath();
                
            model.addRow(row);
            
        }
        
        
        
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
