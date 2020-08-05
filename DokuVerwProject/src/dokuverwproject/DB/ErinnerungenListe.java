/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrame;
import dokuverwproject.LOGIC.Erinnerung;
import java.io.File;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 * ChangeLog
 * Falk @ 05.08.2020
 * Einführung der Methoden textLaden und datumLaden.
 *  beide Methoden benötigen eine long ID und einen String Typ. Dabei wird je nach String der entsprechende
 *  Wert zurückgeliefert
 * Umbenennen der Methode erinnerungLoeschen in erinnerungenLoeschen,
 *      da diese alle Erinnerungen der übergebenen TG löscht
 * Einführung der Methode erinnerungLoeschen
 *      diese löscht die Erinnerung mit der übergebenen ID.
 * Einführung der Methode getTGID
 *      diese gibt die ThemengruppenID der übergebenen Erinnerung zurück
 *
 */
public class ErinnerungenListe {
    private long groesse = 0;
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ErinngerungenFrame
    public ErinnerungenListe() { // Konstruktor, um Sachen in die DB zu schreiben
    }


    
    public ErinnerungenListe(DefaultTableModel model) { //Konstruktor, um Tabelle eines Frames mit Inhalt zu füllen
        this.model = model;
    }
    
    public void ansichtAktualisieren() {
        // ----------------------------------------- Wahrscheinlich nicht mehr benötigt
    }
    
    public Boolean erinnerungErstellen(String titel, String inhalt, String date, long tgID, String dateiPfad) {
    // Erinnerungen werden in der DB gespeichert
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if ( con != null){
            try {
                PreparedStatement ps = null;
                String addquery = "INSERT INTO `erinnerungen`(`id`, `titel`, `inhalt`, `faellig`, `erledigt`, `themengruppenID`, `dateiPfad`, `created_TMSTMP`) "
                                                    + "VALUES (NULL, ?, ?, ?, "+false+", ?, ?, CURRENT_TIMESTAMP)";

                ps = con.prepareStatement(addquery);
                ps.setString(1, titel);
                ps.setString(2, inhalt);
                ps.setString(3, date);
                ps.setLong(4, tgID);
                ps.setString(5, dateiPfad);
                ps.executeUpdate();
                ps.close();
                
                return true;
            } catch (Exception e) {
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Speichern der Erinnerung in der DB.");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }

    public Boolean erinnerungenLaden(long tgID) { // wenn tgID = -1, dann aus Hauptframe, sonst aus Themengruppen-Frame
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if (con != null) {
            this.setGroesse(0);
            model.setRowCount(0);

            Object[] row = null;
            
            Statement stmt = null;
            String query = "";
            
            if(tgID == -1) {
                query = "SELECT * FROM `erinnerungen`";
                row = new Object[3];
            } else {
                query = "SELECT * FROM `erinnerungen` where `themengruppenID` = " + tgID;
                row = new Object[4]; 
            }
            
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    // --------------------------------- Änderung: wir brauchen nur nr, titel, fälligkeit
                    long id = rs.getLong(1);
                    String titel = rs.getString(2);
                    //String inhalt = rs.getString(3);
                    Date faellig = rs.getDate(4);  // --------------------------------- Änderung: Datum, nicht Timestamp
                    Boolean erledigt = rs.getBoolean(5);  // --------------------------------- Änderung: Boolean (wird in der DB als tinyint gespeichert)
                    //String pfad = rs.getString(6);
                    //Timestamp stamp = rs.getTimestamp(7);  // --------------------------------- Änderung: Hier tritt eine Exception auf

                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    //SimpleDateFormat sdfTime = new SimpleDateFormat("kk:mm");
                    //sdfTime.setTimeZone(TimeZone.getTimeZone("MEZ"));

                    //String s_stamp = sdfDate.format(stamp) + " " + sdfTime.format(stamp) + " Uhr";
                    String s_stamp = sdfDate.format(faellig);
                        
                    if(tgID == -1) { // Wenn für Hauptframe geladen wird
                        row[0] = id;
                        row[1] = titel;
                        row[2] = s_stamp;
                    } else { // wenn für Themengruppen-Frame geladen wird
                        // mehr infos laden --> "Nr.", "Symbol", "Titel", "Fällig" --> Symbol für Fälligkeitsanzeige
                        row[0] = id;
                        
                        //ImageIcon img = (ImageIcon) new ImageIcon((ErinnerungenListe.class.getResource("../img/green.png").getFile()));
                        ImageIcon img = null;
                        
                        if(erledigt) {
                            img = (ImageIcon) new ImageIcon((ErinnerungenListe.class.getResource("../img/green.png").getFile()));
                        } else {
                            img = (ImageIcon) new ImageIcon((ErinnerungenListe.class.getResource("../img/red.png").getFile()));
                        }
                        
                        row[1] = img;
                        row[2] = titel;
                        row[3] = s_stamp;
                    }
                    model.addRow(row);
                    this.setGroesse(this.getGroesse() + 1);
                }
                stmt.close();
                return true;
            } catch (Exception e) {
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Laden der Erinnerungenliste.");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank");
        }
        return false;
    }


    public String textLaden(long id, String typ){
        String query = "SELECT * FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        String ausgabe = "";
            if (con != null){
                try {
                    ps = con.prepareStatement(query);
                    //ps.setObject(1, "inhalt");
                    ps.setLong(1, id);
                    ResultSet result = ps.executeQuery();
                    if(result.next()){
                        if(typ.equals("inhalt")){
                        ausgabe = result.getString(3);
                        } else if (typ.equals("titel")){
                        ausgabe = result.getString(2);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.toString());
                    NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
                }
            }
        return ausgabe;
    }
    public Date datumLaden(long id, String typ){
        String query = "SELECT * FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        Date ausgabe = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if (con != null){
            ausgabe = new Date(System.currentTimeMillis());
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ResultSet result = ps.executeQuery();
                if(result.next()){
                    if(typ.equals("faellig")){
                        ausgabe = result.getDate(4);
                    } else if (typ.equals("erstellt")){
                        ausgabe = result.getDate(8);
                    }
                }
                return ausgabe;
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank Datum.");
            }
        }
        return ausgabe;
    }





    public boolean erinnerungenLoeschen(long tgID){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`themengruppenID` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, tgID);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }
    public boolean erinnerungLoeschen(long id){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }


    public boolean erinnerungLoeschen(String pfad){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`dateiPfad` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setString(1, pfad);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }

    public long getTGID(long id){
        String query = "SELECT `themengruppenID` FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        long ausgabe = 0;
        if (con != null){
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ResultSet result = ps.executeQuery();
                if(result.next()){
                    ausgabe = result.getLong(1);
                    }

            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return ausgabe;
    }




    
    public boolean erinnerungBearbeiten(long id,String titel, String text, String datum) {
        String query = "UPDATE `erinnerungen` SET `titel`= ?, `inhalt`= ?, `faellig` = ? WHERE id = ?";
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        PreparedStatement ps = null;
        if (con != null)
        try {
            ps = con.prepareStatement(query);
            ps.setString(1,titel);
            ps.setString(2, text);
            ps.setString(3, datum);
            ps.setLong(4, id);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    
    public void setzeErledigt(long id) {
        String query = "UPDATE `erinnerungen` SET `erledigt`= ? WHERE id = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con != null)
        try {
            ps = con.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setLong(2, id);
            ps.executeUpdate();
            ps.close();
            return;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return;

    }

    public long getGroesse() {
        return this.groesse;
    }

    public void setGroesse(long groesse) {
        this.groesse = groesse;
    }


}
