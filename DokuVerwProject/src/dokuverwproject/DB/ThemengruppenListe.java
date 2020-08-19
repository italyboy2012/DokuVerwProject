/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import dokuverwproject.GUI.NotifyFrame;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe
 */
public class ThemengruppenListe {
    private long size = 0; // Anzahl der geladenen TG
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ThemengruppenübersichtFrame
    
    /**
     * 
     * @param model - Tabellenmodell der Tabelle eines Frames, um die Daten in ihr rendern zu können
     */
    public ThemengruppenListe(DefaultTableModel model) {
        this.model = model;
    }
    
    /**
     * Läd alle Themengruppen aus der Datenbank in eine Tabelle
     * 
     * @return - gibt zurück, ob die Methode erfolgreich druchlaufen wurde
     */
    public boolean loadFromDB() {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        
        if(con != null) {
            this.setSize(0);
            model.setRowCount(0);

            Object[] row = new Object[4];

            Statement stmt = null;
            String query = "SELECT * FROM `themengruppen`";
            
            try{
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    long id = rs.getLong(1);
                    String title = rs.getString(2);
                    String path = rs.getString(3);
                    Timestamp tmstp = rs.getTimestamp(4);

                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    SimpleDateFormat sdfTime = new SimpleDateFormat("kk:mm");
                    sdfTime.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    String anlagedatum = sdfDate.format(tmstp) + " " + sdfTime.format(tmstp) + " Uhr";

                    row[0] = id;
                    row[1] = title;
                    row[2] = path;
                    row[3] = anlagedatum;

                    model.addRow(row);
                    
                    this.setSize(this.getSize()+1);
                };
                stmt.close();
                return true;
            } catch(Exception e){
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Laden der Themengruppenliste.");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }
    
    /**
     * Läd die Themengruppen aus der Datenbank, zu der die ID passt
     * 
     * @return - gibt zurück, ob die Methode erfolgreich druchlaufen wurde
     */
    public boolean loadFromDB(int id) {
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
                            this.title = rs.getString(2);
                            this.path = rs.getString(3);
                            if(pfadsNavIndex == 0) {
                                this.pfadNav = new String(this.path); // Pfad für Navigation
                                //Der Pfad wird nur dann auf das Hauptverzeichnis der Themengruppe gesetzt,
                                //wenn keine Unterordner geöffnet sind.
                                //Sind unterordner geöffnet, wird deren aktueller Pfad
                                //so nicht überschrieben.
                            }
                            this.creationTimeStamp = rs.getTimestamp(4);
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

    /**
     *  Erstellt eine Themengruppe mit den ihr übergebenen Attributen
     * 
     * @param title
     * @param path
     * @return - gibt zurück, ob die Methode erfolgreich druchlaufen wurde
     */
    public boolean createTG(String title, String path) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "INSERT INTO `themengruppen` (`id`, `titel`, `pfad`, `created_TMSTMP`) VALUES (NULL, ?, ?, CURRENT_TIMESTAMP);";
                ps = con.prepareStatement(query);
                ps.setString(1, title);
                ps.setString(2, path);
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

    /**
     * Bearbeitet eine TG in der DB mit den ihr übergebenen Attributen
     * 
     * @param id
     * @param title
     * @param path
     * @return - gibt zurück, ob die Methode erfolgreich druchlaufen wurde
     */
    public boolean editTG(long id, String title, String path) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "UPDATE `themengruppen` SET `titel` = ?, `pfad` = ?, `created_TMSTMP` = CURRENT_TIMESTAMP WHERE `themengruppen`.`id` = ?;";
                ps = con.prepareStatement(query);
                ps.setString(1, title);
                ps.setString(2, path);
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

    /**
     * Methode löscht eine Themengruppe mit der ihr übergenenen ID
     * 
     * @param id
     * @return - gibt zurück, ob die Methode erfolgreich druchlaufen wurde
     */
    public boolean deleteTG(long id) {
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
    
    // Getter und Setter

    public long getSize() {
        return this.size;
    }
    
    public void setSize(long groesse) {
        this.size = groesse;
    }
    
}
