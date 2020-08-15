/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrame;

import java.sql.*;

public class Notiz {
    /**
     *
     * @author Falk
     * ChangeLog
     * 04.08.2020
     * notizAusDBLaden: überprüft Existenz einer Notiz für übergebene Datei.
     *      falls ja   -> gib Notiztext zurück
     *      falls nein -> erstelle Notiz und gib leeren String zurück
     *
     * notizInDBSchreiben: Schreibt übergebenen Notiztext in Notiz der übergebenen Datei
     *
     * notizLoeschen: Löscht eine Notiz für übergebene ThemengruppenID
     *
     * 05.08.2020
     * notizLoeschen umbenannt in themengruppenNotizenLoeschen
     * notizLoeschen löscht die Notiz mit dem Übergebenen Pfad
     */
    /**
     * Konstruktor für Notizen
     */
    public Notiz() {
    }

    /**
     * Prüft ob eine Notiz für eine Datei schon existiert und läd diese. Falls diese noch nicht besteht, wird diese erstellt.
     * @param pfad Pfad der Datei für die die Notiz gespeichert werden soll.
     * @param tgid Themengruppen ID, der Datei, für die die Notiz erstellt werden soll.
     * @return gibt den inhalt der Notiz zurück
     */
    public String notizAusDBLaden(String pfad, long tgid){
        String ausgabe = ""; // initialisierung und definition des Ausgabeparameters

        // Erstellen der SQL-Verbindung
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();

        // Testen ob Verbindung besteht
            if (con != null) {
                // initialisierung der für SQL benötigten Variablen
                Statement stmt = null;
                PreparedStatement ps = null;
                String query = "SELECT `inhalt` FROM `notizen` WHERE `dateiPfad` = ?"; // Abfrage nach Notizinhalt der übergebenen Datei
                try {
                    // Erstellen und Ausführen des injectionsicheren SQL-Befehls
                    ps = con.prepareStatement(query);
                    ps.setString(1,pfad);
                    ResultSet abfrage = ps.executeQuery();

                   if(abfrage.next()) //Test ob Rückgabewert nicht leer ist
                   {
                        // auslesen und zurückgeben des Notizeninhaltes
                        ausgabe = abfrage.getString(1);
                        return ausgabe;

                } else {
                        // falls keine Notiz für die Datei existiert wird hier eine neue leere erstellt
                       query = "INSERT INTO `notizen`(`id`, `inhalt`, `dateiPfad`, `themengruppenID`, `created_TMSTMP`) VALUES (NuLL , '',? ,?,CURRENT_TIMESTAMP)";
                       ps = con.prepareStatement(query);
                       ps.setString(1, pfad);
                       ps.setLong(2, tgid);
                       ps.executeUpdate();
                       ps.close();}

                    return ausgabe;
                } catch(Exception e){
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim lesen der Notizen.");
            }
        }
        return ausgabe;
    }

    /**
     * Speichert den Text in die Notiz mit dem Pfad
     * @param text zu speichernder Text
     * @param pfad Pfad der Datei, für die die Notiz gespeichert werden soll
     * @return gibt Rückmeldung, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean notizInDBSchreiben(String text, String pfad) {
        String query = "UPDATE `notizen` SET `inhalt`= ? WHERE dateipfad = ?";
        PreparedStatement ps = null;
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, text);
            ps.setString(2, pfad);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    /**
     * Wenn eine Themengruppe gelöscht wird, löscht diese Methode die Notizen der Dateien, die zu der TG gehören
     * @param tgID ID der zu löschenden Themengruppe
     * @return gibt Rückmeldung, ob die Methode erfolgreich durchlaufen wurde
     */

    public boolean themengruppenNotizenLoeschen(long tgID){
        String query = "DELETE FROM `notizen` WHERE `notizen`.`themengruppenID` = ?;";
        PreparedStatement ps = null;
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setLong(1, tgID);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    /**
     * Wenn eine Datei gelöscht wird, löscht diese Methode die dazugehörige Notiz
     * @param pfad Pfad der gelöschten Datei
     * @return gibt zurück, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean notizLoeschen(String pfad){
        String query = "DELETE FROM `notizen` WHERE `notizen`.`dateiPfad` = ?;";
        PreparedStatement ps = null;
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, pfad);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }
    
    /**
     * Wenn ein File umbenannt wird, dann setzt diese Methode die Referenzen
     * der dazugehörenden Notiz in der DB auf das neue File.
     * 
     * @param dateiPfadAlt
     * @param dateiPfadNeu
     * @return 
     */
    public boolean resetReferenceToFile(String dateiPfadAlt, String dateiPfadNeu) {
        String query = "UPDATE `notizen` SET `dateipfad`= ? WHERE `dateipfad` = ?";
        PreparedStatement ps = null;
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, dateiPfadNeu);
            ps.setString(2, dateiPfadAlt);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }
    
}


