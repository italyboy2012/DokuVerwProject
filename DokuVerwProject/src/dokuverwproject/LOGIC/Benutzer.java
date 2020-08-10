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
public class Benutzer {
    private long id = 0;
    private String name = "";
    private String vorname = "";
    
    public Benutzer (long id, String n, String pn) {
        this.id = id;
        this.name = n;
        this.vorname = pn;
    }
    
    @Override
    public String toString() {
        return "[" + id + "] : " + name + ", " + vorname;
    }

    // Getter
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getVorname() {
        return vorname;
    }
    
}
