/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import java.util.ArrayList;

/**
 *
 * @author Giuseppe
 */
public class Themengruppe {
    private long id = 0;
    private String titel = "";
    private String pfad = "";
    private long größe = 0;
    private ArrayList<Datei> dateien = new ArrayList<>();
    
    public Themengruppe(long id, String titel, String pfad) {
        this.id = id;
        this.titel = titel;
        this.pfad = pfad;
    }
    
    public void ansichtAktualisieren() {
        
    }
    
    public void dateienIndexieren() {
        
    }
    
    public void neueDateiHinzufügen(String pfad) {
        
    }
    
    public void dateiLöschen(long dateiId) {
        
    }
    
    public void dateiUmbenennen(long dateiId) {
        
    }
    
    public void dateiÖffnen(long dateiId) {
        
    }
    
    public void setGröße(int größe) {
        this.größe = größe;
    }
}
