/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DATA.User;
import dokuverwproject.GUI.MainMenu;
import static dokuverwproject.commons.Common.*;

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
    
    public boolean login() {
        if(!username.equals("") && !username.equals(null) && !password.equals("") && !password.equals(null)) {
            if(username.equals("test") && password.equals("1234")) {
                // Hier würde man in der DB schauen, ob der Nutzer existiert und wenn ja, würde man seine Daten laden
                MainMenu mm = new MainMenu(new User("Nachname", "Vorname"));
                return true;
            }
            return false;
        }
        return false;
    }
    
}