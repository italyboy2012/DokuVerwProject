/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Giuseppe
 */
public class Datei {
    private long id = 0;
    private String titel = "";
    private String pfad = "";
    private Timestamp änderungsdatum = null;
    private String typ = "";
    private String größe = "";
    private Themengruppe themengruppe = null;
    private ArrayList<Erinnerung> erinnerungen = new ArrayList<>();
    private Notiz notiz = null;
    
    public Datei(String pfad, Themengruppe themengruppe) {
        this.pfad = pfad;
        this.themengruppe = themengruppe;
    }
    
    public void getAndSetMetaData() {
        
    }
    
    public void notizLaden() {
        
    }
    
    public void notizErstellen() {
        
    }
    
    public void notizBearbeiten() {
        
    }
    
    public void notizLöschen() {
        
    }
    
    public void erinnerungenLaden() {
        
    }
    
    public void erinnerungErstellen() {
        
    }
    
    public void erinnerungBearbeiten() {
        
    }
    
    public void erinnerungLöschen() {
        
    }
    
    public void erinnerungErledigtSetzen() {
        
    }
}
