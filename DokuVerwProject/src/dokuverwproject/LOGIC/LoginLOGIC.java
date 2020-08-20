/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DTO.UserDTO;
import dokuverwproject.DB.LoginDB;
import dokuverwproject.GUI.MainFrameGUI;

/**
 *
 * @author Giuseppe
 */
public class LoginLOGIC {
    private String username = "";
    private String password = "";

    public LoginLOGIC(String un, String pw) {
        this.username = un;
        this.password = pw;
    }

    /**
     * Methode ruft login() in LoginDB mit den ihr übergebenen Credentials auf.
     * 
     * @return - true = UserDTO existerit; false = UserDTO existiert nicht
     */
    public boolean login() {
        if(!username.equals("") && !username.equals(null) && !username.equals("") && !password.equals(null)) {
            UserDTO b = new LoginDB().login(username, password);
            
            if(b != null) {
                MainFrameGUI mm = new MainFrameGUI(b);
                return true;
            }
        }
        return false;
    }
    
}
