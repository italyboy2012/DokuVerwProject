/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.commons;

import dokuverwproject.GUI.NotifyFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.InetAddress;
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
    
    /**
     * Zentralisiert das ihr übergebene externe Frame und setzt das Frame-Icon auf den übergebenen Pfad.
     * 
     * @param frame - externe Frame
     * @param iconFileName - fileName des Icons
     */
    public static void initExternalFrame (javax.swing.JFrame frame, String iconFileName) {
        //zentralisieren
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        
        //icon setzen
        try{
            String filePathCompatibleWithJarFile = "/dokuverwproject/IMG/" + iconFileName;
            frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Common.class.getResource(filePathCompatibleWithJarFile)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //Fenster der Liste aller offenen externen Fenster hinzufügen
        addExternalFrame(frame);
    }
    
    /**
     * Methode zentralisiert das ihr übergebene internalFrame und füegt es dem DesktopPane hinzu.
     * Anschließend wird das Fenster angezeigt.
     * 
     * @param frame - internes Frame
     * @param dp - DesktopPane
     * @param iconFileName - Pfad des Icons des internalFrame
     */
    public static void initInternalFrame(javax.swing.JInternalFrame frame, javax.swing.JDesktopPane dp, String iconFileName) {
        //icon setzen
        try {
            String filePathCompatibleWithJarFile = "/dokuverwproject/IMG/" + iconFileName;
            frame.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource(filePathCompatibleWithJarFile))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //internal Frame dem DesktopPane hinzufügen und anzeigen
        dp.add(frame);
        frame.setVisible(true);
    }
    
    /**
     * Methode reposizioniert ein ihr übergebenes internes Fenster anhand der ihr übergebenen Parameter
     * 
     * @param frame - internes Frame, welches umposizioniert werden muss
     * @param dp - DesktopPane, auf welches das interne Frame gerendert wird
     * @param achtel - Anteil an einem Achten (x/8) - wenn z.B. 2 übergeben wird, dann wird die Breite auf 2/8 des dp gesetzt
     * @param rechts - boolean; true = Frame oben rechts positionieren; false = Frame oben links positionieren
     */
    public static void resizeAndRepositionInternalFrame(javax.swing.JInternalFrame frame, javax.swing.JDesktopPane dp, int achtel, boolean rechts) {
        //Frames Größe setzen
        int width = (dp.getWidth()) / 8 * achtel;
        int heigth = dp.getHeight();
        frame.setSize(width, heigth);
        
        //Frames Position setzen
        int x = 0; //horizontal
        int y = 0; //vertikal
        if(rechts) {
            x = (int) dp.getWidth() - width;
        }
        frame.setLocation(x, y);
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
     * @param dp - DesktopPane, auf welchem die internen Frames gerendert wurden
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
