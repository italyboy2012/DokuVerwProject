/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import dokuverwproject.GUI.NotifyFrameGUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 
 * @author Falk
 */
public class NoteDB {

    /**
     * Prüft ob eine NoteDB für eine Datei schon existiert und läd diese.
     * Falls diese noch nicht besteht, wird diese erstellt.
     * 
     * @param path - Pfad der Datei für die die NoteDB gespeichert werden soll.
     * @param tgID - Themengruppen ID, der Datei, für die die NoteDB erstellt werden soll.
     * @return - gibt den inhalt der NoteDB zurück
     */
    public String loadNote(String path, long tgID){
        String returnValue = ""; // initialisierung und definition des Ausgabeparameters

        // Erstellen der SQL-Verbindung
        ConnDB dbc = new ConnDB();
        Connection con = dbc.getConnection();

        // Testen ob Verbindung besteht
        if (con != null) {
            // initialisierung der für SQL benötigten Variablen
            Statement stmt = null;
            PreparedStatement ps = null;
            String query = "SELECT `inhalt` FROM `notizen` WHERE themengruppenID = ? AND `dateiPfad` = ?"; // Abfrage nach Notizinhalt der übergebenen Datei
            try {
                // Erstellen und Ausführen des injectionsicheren SQL-Befehls
                ps = con.prepareStatement(query);
                ps.setLong(1,tgID);
                ps.setString(2,path);
                ResultSet abfrage = ps.executeQuery();

                if(abfrage.next()) { //Test ob Rückgabewert nicht leer ist
                    // auslesen und zurückgeben des Notizeninhaltes
                    returnValue = abfrage.getString(1);
                    return returnValue;
                } else { // falls keine NoteDB für die Datei existiert wird hier eine neue leere erstellt
                    query = "INSERT INTO `notizen`(`id`, `inhalt`, `dateiPfad`, `themengruppenID`, `created_TMSTMP`) VALUES (NuLL , '',? ,?,CURRENT_TIMESTAMP)";
                    ps = con.prepareStatement(query);
                    ps.setString(1, path);
                    ps.setLong(2, tgID);
                    ps.executeUpdate();
                    ps.close();
                }
                return returnValue;
            } catch(Exception e){
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler beim lesen der Notizen.");
            }
        }
        return returnValue;
    }

    /**
     * Speichert den Text in die NoteDB mit dem Pfad
     * 
     * @param text - zu speichernder Text
     * @param path - Pfad der Datei, für die die NoteDB gespeichert werden soll
     * @param tgID - ThemengruppenID, damit die NoteDB der richtigen Themengruppe geladen wird
     * @return - gibt Rückmeldung, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean saveNote(String text,long tgID, String path) {
        String query = "UPDATE `notizen` SET `inhalt`= ? WHERE themengruppenID = ? AND dateipfad = ?";
        PreparedStatement ps = null;
        try {
            ConnDB dbc = new ConnDB();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, text);
            ps.setLong(2, tgID);
            ps.setString(3, path);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    /**
     * Wenn eine Themengruppe gelöscht wird, löscht diese Methode die Notizen der Dateien, die zu der TG gehören
     * 
     * @param tgID - ID der zu löschenden Themengruppe
     * @return - gibt Rückmeldung, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean deleteTGNotes(long tgID){
        String query = "DELETE FROM `notizen` WHERE `notizen`.`themengruppenID` = ?;";
        PreparedStatement ps = null;
        try {
            ConnDB dbc = new ConnDB();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setLong(1, tgID);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    /**
     * Wenn eine Datei gelöscht wird, löscht diese Methode die dazugehörige NoteDB
     * 
     * @param path - Pfad der gelöschten Datei
     * @return - gibt zurück, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean deleteNote(String path){
        String query = "DELETE FROM `notizen` WHERE `notizen`.`dateiPfad` = ?;";
        PreparedStatement ps = null;
        try {
            ConnDB dbc = new ConnDB();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, path);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }
    
    /**
     * Wenn ein File umbenannt wird, dann setzt diese Methode die Referenzen
     * der dazugehörenden NoteDB in der DB auf das neue File.
     * 
     * @param oldPath
     * @param newPath
     * @return 
     */
    public boolean resetReferenceToFile(String oldPath, String newPath) {
        String query = "UPDATE `notizen` SET `dateipfad`= ? WHERE `dateipfad` = ?";
        PreparedStatement ps = null;
        try {
            ConnDB dbc = new ConnDB();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, newPath);
            ps.setString(2, oldPath);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }
    
}


