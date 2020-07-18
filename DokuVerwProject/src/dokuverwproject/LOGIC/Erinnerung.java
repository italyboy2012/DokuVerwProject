/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import java.sql.Timestamp;

/**
 *
 * @author Giuseppe
 */
public class Erinnerung {
    private long id = 0;
    private String titel = "";
    private String inhalt = "";
    private Timestamp fällig = null;
    private boolean erledigt = false;
    private Datei datei = null;

    public Erinnerung(long id, String titel, String inhalt, Timestamp fällig, boolean erledigt, Datei datei) {
        this.id = id;
        this.titel = titel;
        this.inhalt = inhalt;
        this.fällig = fällig;
        this.erledigt = erledigt;
        this.datei = datei;
    }
    
    public void dateiAnzeigen() {
        
    }
    
}
