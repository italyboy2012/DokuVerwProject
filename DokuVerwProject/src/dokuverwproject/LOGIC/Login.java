/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DTO.Benutzer;
import dokuverwproject.DB.LoginDB;
import dokuverwproject.GUI.HauptFrame;

/**
 *
 * @author Giuseppe
 */
public class Login {
    private String username = "";
    private String password = "";

    public Login(String un, String pw) {
        this.username = un;
        this.password = pw;
    }

    /**
     * Methode ruft login() in LoginDB mit den ihr übergebenen Credentials auf.
     * 
     * @return - true = Benutzer existerit; false = Benutzer existiert nicht
     */
    public boolean login() {
        if(!username.equals("") && !username.equals(null) && !username.equals("") && !password.equals(null)) {
            Benutzer b = new LoginDB().login(username, password);
            
            if(b != null) {
                HauptFrame mm = new HauptFrame(b);
                return true;
            }
        }
        return false;
    }
    
}
