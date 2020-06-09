/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.commons;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Giuseppe
 */
public class Common {
    private static ArrayList<JFrame> openFrames = new ArrayList<JFrame>(); // Liste aller offener Fenster
    
    /**
     * Zentralisiert das ihr übergebene Frame und setzt das Frame-Icon auf den übergebenen Pfad.
     * 
     * @param frame
     * @param iconPaht 
     */
    public static void initFrame (javax.swing.JFrame frame, String iconPaht) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource(iconPaht)));
        
        addWindow(frame);
    }
    
    /**
     * Gibt die interne IP des Nutzers zurück.
     * 
     * @return - interne IP des Nutzers
     */
    public static String getInternalIP() {
        String rtn = "";
        try {
            rtn = InetAddress.getLocalHost().toString();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rtn;
    }
    
    /**
     * Methode schließt alle offenen Fenster.
     * Diese Methode kann zum Beispiel beim Logout benutzt werden,
     * um sicherzustellen, dass alle sensiblen Fenster geschlossen sind.
     * 
     * @throws Exception 
     */
    public static void closeWindows() throws Exception {
        for(int i = 0; i<= openFrames.size()-1; i++) {
            openFrames.get(i).dispose();
        }
    }
    
    /**
     * Methode fügt übergebenes Fenster der Liste offener Fenster zu.
     */
    public static void addWindow(JFrame jf) {
        openFrames.add(jf);
    }
    
}
