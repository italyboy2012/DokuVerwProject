/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.GUI.MainMenu;

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
                MainMenu mm = new MainMenu();
                mm.setVisible(true);
                return true;
            }
            return false;
        }
        return false;
    }
    
}
