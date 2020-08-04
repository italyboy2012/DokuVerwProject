package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrame;

import java.sql.*;
public class Notiz {


    public Notiz() {
        // Konstruktor für Notizen
    }

    public String notizAusDBLaden(String pfad){
        String ausgabe = "";
        try {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        Statement stmt = null;
        PreparedStatement ps = null;

        String query = "SELECT `inhalt` FROM `notizen` WHERE `dateiPfad` LIKE '?'";
        ps = con.prepareStatement(query);
        stmt = con.createStatement();
        ps.setString(1,pfad);
        ResultSet abfrage = ps.executeQuery();

        if (abfrage != null){
            ausgabe = abfrage.getString(1);

        } else {
            query = "INSERT INTO `notizen`(`id`, `titel`, `inhalt`, `dateiPfad`, `created_TMSTMP`) VALUES (null,banane,\"\",?,CURRENT_TIMESTAMP)";
            ps = con.prepareStatement(query);
            ps.setString(1, pfad);
            ps.executeUpdate();
            ps.close();
        }
        } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
            }
        return ausgabe;
    }
        public boolean notizInDBSchreiben(String text, String pfad){
        String query = "UPDATE `notizen` SET `inhalt`= '?' WHERE dateipfad ='?'";
        PreparedStatement ps = null;
            try {
                DBConn dbc = new DBConn();
                Connection con = dbc.getConnection();
                ps = con.prepareStatement(query);
                ps.setString(1, text);
                ps.setString(2, pfad);
                ps.close();
                return true;
            } catch (Exception e) {
        System.out.println(e.toString());
        NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
    }
        return false;
        }


    }


