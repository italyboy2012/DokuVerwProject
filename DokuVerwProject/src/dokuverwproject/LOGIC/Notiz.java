/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

/**
 *
 * @author Giuseppe
 */
public class Notiz {
    private long id = 0;
    private String titel = "";
    private String inhalt = "";
    private Datei datei = null;
    
    public Notiz(long id, String titel, String inhalt, Datei datei) {
        this.id = id;
        this.titel = titel;
        this.inhalt = inhalt;
        this.datei = datei;
    }
    
}
