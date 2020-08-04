package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrame;

import java.sql.*;
public class Notiz {
    /**
     *
     * @author Falk
     * ChangeLog 04.08.2020
     * notizAusDBLaden: überprüft Existenz einer Notiz für übergebene Datei.
     *      falls ja   -> gib Notiztext zurück
     *      falls nein -> erstelle Notiz und gib leeren String zurück
     *
     * notizInDBSchreiben: Schreibt übergebenen Notiztext in Notiz der übergebenen Datei
     */

    public Notiz() {
        // Konstruktor für Notizen
    }


    public String notizAusDBLaden(String pfad){
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
                       query = "INSERT INTO `notizen`(`id`, `titel`, `inhalt`, `dateiPfad`, `created_TMSTMP`) VALUES (null,'created','',?,CURRENT_TIMESTAMP)";
                       ps = con.prepareStatement(query);
                       ps.setString(1, pfad);
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

        public boolean notizInDBSchreiben(String text, String pfad){
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


    }

