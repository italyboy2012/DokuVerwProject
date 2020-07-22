/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;
import java.sql.Timestamp;
/**
 *
 * @author Giuseppe & Falk
 */
public class ErinnerungenListe {
    
    public void ansichtAktualisieren() {

    }

    public void erinnerungLöschen(Erinnerung erinnerung) {
        // lösche Referenz auf erinngerung und starte garbage collector
        erinnerung = null;
        System.gc();
    }
    
    public void erinnerungÄndern(Erinnerung erinnerung,Timestamp zeit, String inhalt) {
        // setze inhalt und zeit auf entsprechende Variablen von erinnerung
        erinnerung.setInhalt(inhalt);
        erinnerung.setFällig(zeit);
    }
    
    public void aendereErledigt(Erinnerung erinnerung) {
        //
        erinnerung.setErledigt( !erinnerung.getErledigt() );
    }
    
}
