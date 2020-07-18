/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DATA.Benutzer;
import dokuverwproject.DATA.DBConn;
import dokuverwproject.GUI.MainFrame;
import dokuverwproject.GUI.NotifyFrame;
import java.sql.*;

/**
 *
 * @author Giuseppe
 */
public class Login {
    private String username = "";
    private String passwort = "";

    public Login(String un, String pw) {
        this.username = un;
        this.passwort = pw;
    }
    
//    public boolean login() {
//        if(!username.equals("") && !username.equals(null) && !passwort.equals("") && !passwort.equals(null)) {
//            if(username.equals("test") && passwort.equals("1234")) {
//                // Hier würde man in der DB schauen, ob der Nutzer existiert und wenn ja, würde man seine Daten laden
//                MainFrame mm = new MainFrame(new Benutzer("Nachname", "Vorname"));
//                return true;
//            }
//            return false;
//        }
//        return false;
//    }
    
    public boolean login() {
        if(!username.equals("") && !username.equals(null) && !passwort.equals("") && !passwort.equals(null)) {
            try {
                DBConn dbc = new DBConn();
                Connection con = dbc.getConnection();
                if(con != null) {
                    PreparedStatement ps = null;
                    String query = "SELECT * FROM `nutzer` WHERE `username` = ? AND `passwort` = ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ps.setString(2, passwort);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        if(rs.getString("username").equals(username) && rs.getString("passwort").equals(passwort)) {
                            MainFrame mm = new MainFrame(new Benutzer(rs.getString(4), rs.getString(5)));
                            return true;
                        }
                    } else {
                        NotifyFrame nf = new NotifyFrame("Fehler", "Bitte geben Sie eine gültige Nutzerkennung ein!");
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Bitte geben Sie eine vollständige Nutzerkennung ein!");
        }
        return false;
    }
    
}
