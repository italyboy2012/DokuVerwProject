/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DATA.DBConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Giuseppe & Falk
 */

public class Erinnerung {
    private long id = 0;
    private String titel = "";
    private String inhalt = "";
    private Timestamp fällig = null;
    private boolean erledigt = false;
    private Datei datei = null;
/*
    kann wohl weg
    public Erinnerung(long id, String titel, String inhalt, Timestamp fällig, boolean erledigt, Datei datei) {
        this.id = id;
        this.titel = titel;
        this.inhalt = inhalt;
        this.fällig = fällig;
        this.erledigt = erledigt;
        this.datei = datei;
    }
*/
    public Erinnerung(){

    };

    public void erinnerungErstellen(String titel, String inhalt, Timestamp faellig, Datei datei){
    // Erinnerungen werden in der DB gespeichert
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if ( con != null){
            Statement stmt = null;
            String query = "SELECT id FROM `erinnerungen`";
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()){
                    long id = rs.getLong(1);
                }
            String addquery = "INSERT INTO `erinnerungen`(`id`, `titel`, `inhalt`, `faellig`, `erledigt`, `dateiPfad`, `created_TMSTMP`) " + "VALUES ("+id+","+ titel +","+inhalt+","+faellig+","+false+","+datei+",CURRENT_TIMESTAMP)";

            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }



    public void dateiAnzeigen() {
        
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public Timestamp getFällig() {
        return fällig;
    }

    public void setFällig(Timestamp fällig) {
        this.fällig = fällig;
    }

    public boolean getErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    public Datei getDatei() {
        return datei;
    }

    public void setDatei(Datei datei) {
        this.datei = datei;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
