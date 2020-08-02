/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DB.DBConn;

import java.sql.*;

/**
 *
 * @author Giuseppe & Falk
 */

public class Erinnerung {
    /*
    kann wohl weg
    private long id = 0;
    private String titel = "";
    private String inhalt = "";
    private Timestamp fällig = null;
    private boolean erledigt = false;
    private Datei datei = null;


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

    public static void erinnerungErstellen(String titel, String inhalt,  java.sql.Date sqldate , String datei){
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
            //id setzt db selbst, deshalb NULL
                PreparedStatement ps = null;
            String addquery = "INSERT INTO `erinnerungen`(`id`, `titel`, `inhalt`, `faellig`, `erledigt`, `dateiPfad`, `created_TMSTMP`) " + "VALUES (NULL,? ,?,?,"+false+",1,?,CURRENT_TIMESTAMP)";

                ps = con.prepareStatement(addquery);
                ps.setString(1, titel);
                ps.setString(2, inhalt);
                ps.setDate(3, sqldate);
                ps.setString(4, datei);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }



    public void dateiAnzeigen() {
        
    }
/*
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
    */

}
