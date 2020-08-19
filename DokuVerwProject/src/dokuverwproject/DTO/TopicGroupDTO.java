/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DTO;

import java.sql.Timestamp;

/**
 *
 * @author Giuseppe
 */
public class TopicGroupDTO {
    private long id = 0;
    private String title = "";
    private String path = "";
    private Timestamp creationTimeStamp = null;

    public TopicGroupDTO(long id, String title, String path, Timestamp creationTimeStamp) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.creationTimeStamp = creationTimeStamp;
    }
    
    // Getter und Setter
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Timestamp creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

}
