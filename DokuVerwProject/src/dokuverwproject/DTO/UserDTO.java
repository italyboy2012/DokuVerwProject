/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DTO;

/**
 *
 * @author Giuseppe
 */
public class UserDTO {
    private long id = 0;
    private String name = "";
    private String prename = "";
    
    public UserDTO (long id, String name, String prename) {
        this.id = id;
        this.name = name;
        this.prename = prename;
    }
    
    @Override
    public String toString() {
        return "[" + id + "] : " + name + ", " + prename;
    }

    // Getter
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getPrename() {
        return prename;
    }
    
}
