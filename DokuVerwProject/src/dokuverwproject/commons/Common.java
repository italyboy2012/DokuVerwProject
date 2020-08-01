/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.commons;

import dokuverwproject.DB.DBConn;
import dokuverwproject.GUI.NotifyFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.DesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author Giuseppe
 */
public class Common {
    private static ArrayList<JFrame> externalFrames = new ArrayList<JFrame>(); // Liste aller offener externen Fenster
    
    // DB Connection am Anfang und dauerhaft aufbauen
    public static Connection con = null; //DB Connection für Programm
    public static void establishSQLConnection() throws Exception {
        DBConn dbc = new DBConn();
        con = dbc.getConnection();
    }
    
    
    
    /**
     * Zentralisiert das ihr übergebene externe Frame und setzt das Frame-Icon auf den übergebenen Pfad.
     * 
     * @param frame
     * @param iconPaht 
     */
    public static void initExternalFrame (javax.swing.JFrame frame, String iconPaht) {
        //zentralisieren
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        
        //icon setzen
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource(iconPaht)));
        
        //Fenster der Liste aller offenen externen Fenster hinzufügen
        addExternalFrame(frame);
    }
    
    /**
     * Methode zentralisiert das ihr übergebene internalFrame und füegt es dem DesktopPane hinzu.
     * Anschließend wird das Fenster angezeigt.
     * 
     * @param frame - internes Frame
     * @param dp - DesktopPane
     * @param iconPaht - Pfad des Icons des internalFrame
     */
    public static void initInternalFrame(javax.swing.JInternalFrame frame, javax.swing.JDesktopPane dp, String iconPaht) {
        //zentralisieren
        //Dimension desktopSize = dp.getSize();
        //Dimension jInternalFrameSize = frame.getSize();
        //frame.setLocation((desktopSize.width - jInternalFrameSize.width)/2, (desktopSize.height- jInternalFrameSize.height)/2);
        
        //icon setzen
        frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource(iconPaht))));
        
        //internal Frame dem DesktopPane hinzufügen und anzeigen
        dp.add(frame);
        frame.setVisible(true);
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
     * Methode schließt alle offenen externen Fenster.
     * Diese Methode kann zum Beispiel beim Logout benutzt werden,
     * um sicherzustellen, dass alle sensiblen Fenster geschlossen sind.
     * 
     * @throws Exception 
     */
    public static void closeExternalFrames() throws Exception {
        for(int i = 0; i<= externalFrames.size()-1; i++) {
            externalFrames.get(i).dispose();
        }
    }
    
    /**
     * Methode schließt alle offenen internen Fenster des DesktopPane.
     * 
     * @param dp
     */
    public static void closeInternalFrames(javax.swing.JDesktopPane dp) {
        try {
            JInternalFrame frames[] = dp.getAllFrames();
            DesktopManager dm = dp.getDesktopManager();
            for (int i = 0 ; i < frames.length ; i ++) {
              dm.closeFrame(frames[i]);
              frames[i].setClosed(true);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim schließen der internen Fenster. Bitte manuell schließen.");
        }
    }
    
    /**
     * Methode fügt übergebenes Fenster der Liste offener Fenster zu.
     */
    public static void addExternalFrame(JFrame jf) {
        externalFrames.add(jf);
    }
    
}
