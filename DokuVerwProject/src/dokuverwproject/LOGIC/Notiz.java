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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public Datei getDatei() {
        return datei;
    }

    public void setDatei(Datei datei) {
        this.datei = datei;
    }
}
