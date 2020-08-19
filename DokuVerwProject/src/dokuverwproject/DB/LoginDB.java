/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import dokuverwproject.GUI.NotifyFrame;
import dokuverwproject.DTO.Benutzer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Giuseppe
 */
public class LoginDB {
    
    /**
     * Methode prüft, ob ein Nutzer mit den ihr übergebenen Credentials existiert.
     * 
     * @param username
     * @param password
     * @return - Benutzer != null, falls Benutzer existerit
     *         - Benutzer == null, falls Benutzer nicht existiert
     */
    public Benutzer login(String username, String password) {
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            if(con != null) {
                PreparedStatement ps = null;
                String query = "SELECT * FROM `nutzer` WHERE `username` = ? AND `passwort` = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    if(rs.getString("username").equals(username) && rs.getString("passwort").equals(password)) {
                        return new Benutzer(rs.getLong(1), rs.getString(4), rs.getString(5));
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
        return null;
    }
    
}
