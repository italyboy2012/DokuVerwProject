/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject;

import dokuverwproject.DB.ReadWriteCredentialsDB;
import dokuverwproject.GUI.SetupAssistantGUI;
import dokuverwproject.GUI.LoginFrameGUI;

/**
 *
 * @author Giuseppe
 */
public class Main {

    public static void main(String args[]) {

        // set design to Windows-Style
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginFrameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display LoginForm */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                ReadWriteCredentialsDB rwc = new ReadWriteCredentialsDB();
        
                if(!rwc.loadData()) {
                    new SetupAssistantGUI();
                    return;
                }
                    
                new LoginFrameGUI();
                
            }
        });
    }
    
    
}
