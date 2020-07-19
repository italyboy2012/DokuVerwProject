/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DATA.DBConn;
import dokuverwproject.GUI.NotifyFrame;
import java.net.InetAddress;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe
 */
public class ThemengruppenListe {
    private long größe = 0;
    //private ArrayList<Themengruppe> themengruppen = null;
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ThemengruppenübersichtFrame
    
    public ThemengruppenListe(DefaultTableModel model) {
        this.model = model;
    }
    
    public void ansichtAktualisieren() {
        // METHODE ÜBERFLÜSSIG -------------------------------------------------
    }
    
    public boolean themenAusDBLaden() {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        
        if(con != null) {
            this.setGröße(0);
            //this.themengruppen = new ArrayList<>();
            model.setRowCount(0);

            Object[] row = new Object[4];

            Statement stmt = null;
            String query = "SELECT * FROM `themengruppen`";
            
            try{
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    long id = rs.getLong(1);
                    String titel = rs.getString(2);
                    String pfad = rs.getString(3);
                    Timestamp tmstp = rs.getTimestamp(4);

                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    SimpleDateFormat sdfTime = new SimpleDateFormat("kk:mm");
                    sdfTime.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    String anlagedatum = sdfDate.format(tmstp) + " " + sdfTime.format(tmstp) + " Uhr";

                    row[0] = id;
                    row[1] = titel;
                    row[2] = pfad;
                    row[3] = anlagedatum;

                    model.addRow(row);
                    
                    this.setGröße(this.getGröße()+1);
                    
                    //themengruppen.add(new Themengruppe(id, titel, pfad, tmstp, null));

                };
                stmt.close();
                return true;
            } catch(Exception e){
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }
    
    public boolean themaErstellen(String titel, String pfad) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "INSERT INTO `themengruppen` (`id`, `titel`, `pfad`, `created_TMSTMP`) VALUES (NULL, ?, ?, CURRENT_TIMESTAMP);";
                ps = con.prepareStatement(query);
                ps.setString(1, titel);
                ps.setString(2, pfad);
                ps.executeUpdate();
                ps.close();
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }
    
    public boolean themaBearbeiten(long id, String titel, String pfad) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "UPDATE `themengruppen` SET `titel` = ?, `pfad` = ?, `created_TMSTMP` = CURRENT_TIMESTAMP WHERE `themengruppen`.`id` = ?;";
                ps = con.prepareStatement(query);
                ps.setString(1, titel);
                ps.setString(2, pfad);
                ps.setLong(3, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }
    
    public boolean themaLöschen(long id) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "DELETE FROM `themengruppen` WHERE `themengruppen`.`id` = ?";
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }
    
    public long getGröße() {
        return this.größe;
    }
    
    public void setGröße(long größe) {
        this.größe = größe;
    }
    
}
