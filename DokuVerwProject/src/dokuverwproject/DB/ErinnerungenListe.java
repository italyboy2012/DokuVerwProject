/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrame;
import dokuverwproject.LOGIC.Erinnerung;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 */
public class ErinnerungenListe {
    private long groesse = 0;
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ErinngerungenFrame

    public ErinnerungenListe(DefaultTableModel model) {
        this.model = model;
    }
    
    public void ansichtAktualisieren() {
        // ----------------------------------------- Wahrscheinlich nicht mehr benötigt
    }

    public Boolean erinnerungenLaden() {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if (con != null) {

            this.setGroesse(0);
            model.setRowCount(0);

            Object[] row = new Object[3];

            Statement stmt = null;
            String query = "SELECT * FROM `erinnerungen`";

            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    // --------------------------------- Änderung: wir brauchen nur nr, titel, fälligkeit
                    long id = rs.getLong(1);
                    String titel = rs.getString(2);
//                    String inhalt = rs.getString(3);
                    Date faellig = rs.getDate(4);  // --------------------------------- Änderung: Datum, nicht Timestamp
//                    Boolean erledigt = rs.getBoolean(5);  // --------------------------------- Änderung: Boolean (wird in der DB als tinyint gespeichert)
//                    String pfad = rs.getString(6);
                    //Timestamp stamp = rs.getTimestamp(7);  // --------------------------------- Änderung: Hier tritt eine Exception auf

                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));
//
//                    SimpleDateFormat sdfTime = new SimpleDateFormat("kk:mm");
//                    sdfTime.setTimeZone(TimeZone.getTimeZone("MEZ"));
//
//                    String s_stamp = sdfDate.format(stamp) + " " + sdfTime.format(stamp) + " Uhr";
                    String s_stamp = sdfDate.format(faellig);

                    row[0] = id;
                    row[1] = titel;
                    row[2] = s_stamp;

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


    public void erinnerungLöschen(Erinnerung erinnerung) {
        // lösche Referenz auf erinngerung und starte garbage collector
        erinnerung = null;
        System.gc();
    }
    
    public void erinnerungÄndern(Erinnerung erinnerung,Timestamp zeit, String inhalt) {
        // setze inhalt und zeit auf entsprechende Variablen von erinnerung
       //  erinnerung.setInhalt(inhalt);
        // erinnerung.setFällig(zeit);
    }
    
    public void aendereErledigt(Erinnerung erinnerung) {
        //
       // erinnerung.setErledigt( !erinnerung.getErledigt() );
    }

    public long getGroesse() {
        return this.groesse;
    }

    public void setGroesse(long groesse) {
        this.groesse = groesse;
    }


}
