/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DATA;

/**
 *
 * @author Giuseppe
 */
public class Benutzer {
    private String name = "";
    private String prename = "";
    
    public Benutzer (String n, String pn) {
        this.name = n;
        this.prename = pn;
    }
    
    @Override
    public String toString() {
        return name + ", " + prename;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getPrename() {
        return prename;
    }
    
}
