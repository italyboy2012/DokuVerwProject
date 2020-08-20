/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;
import dokuverwproject.GUI.NotifyFrameGUI;
import java.awt.Image;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 */
public class ReminderDB {
    private long size = 0; // Anzahl der gefundenen Erinnerungen
    private DefaultTableModel model = null; // Zugriff auf Tabelle in ErinngerungenFrame

    public ReminderDB() {
    }

    /**
     * Konstruktor, um Tabelle eines Frames mit Inhalt zu füllen
     * 
     * @param model 
     */
    public ReminderDB(DefaultTableModel model) {
        this.model = model;
    }
    
    /**
     * Skalliert das übergebene Bild auf die übergebene höhe und breite
     * 
     * @param pic - Bild
     * @param width
     * @param height
     * @return
     */
    private ImageIcon resizeImageIcon(ImageIcon pic, int width, int height){
        Image image = pic.getImage(); // transform it
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon ausgabe = new ImageIcon(newimg);  // transform it back
        return ausgabe;
    }

    /**
     * Zwei Datumswerte werden verglichen, ob sie unter einer bestimmten Distanz voneinander liegen.
     * 
     * @param current_date - aktuelles Datum
     * @param faellig - Fälligkeitsdatum
     * @param range - übergebene Distanz in Tagen
     * @return - boolean: differenz Überfälligkeitsdatum kleiner als übergene Distanz?
     */
    private boolean inDays(Date current_date, Date faellig,int range){ 
        long diffInMillies = Math.abs(faellig.getTime() - current_date.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return (diff < range);
    }

    /**
     * Schreibt die übergebenen Daten als Erinnerung in die Datenbank
     * 
     * @param title
     * @param content
     * @param date
     * @param tgID
     * @param filePath
     * @return
     */
    public Boolean createReminder(String title, String content, String date, long tgID, String filePath) {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if ( con != null){
            try {
                PreparedStatement ps = null;
                String addquery = "INSERT INTO `erinnerungen`(`id`, `titel`, `inhalt`, `faellig`, `erledigt`, `themengruppenID`, `dateiPfad`, `created_TMSTMP`) "
                                                    + "VALUES (NULL, ?, ?, ?, " + false + ", ?, ?, CURRENT_TIMESTAMP)";

                ps = con.prepareStatement(addquery);
                ps.setString(1, title);
                ps.setString(2, content);
                ps.setString(3, date);
                ps.setLong(4, tgID);
                ps.setString(5, filePath);
                ps.executeUpdate();
                ps.close();
                
                return true;
            } catch (Exception e) {
                new NotifyFrameGUI("Fehler", "Fehler beim Speichern der Erinnerung in der DB.");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            new NotifyFrameGUI("Fehler", "Fehler beim Zugriff auf die Datenbank.");
        }
        return false;
    }

    /**
     * Läd Erinnerungen abhängig aus der DB
     * 
     * @param tgID - wenn tgID = -1, dann aus Hauptframe (also alle Erinerungen laden),
     * sonst aus Themengruppen-Frame (nur die Laden, die zu dieser tgID gehören)
     * @param erID - ist die ID der zu suchenden Erinnerung
     *             - wenn -1 wird nach keiner Erinnerung gesucht
     * @param height - für das Rendern der Icons in den Spalten
     * @return - returnValue; -2 = Methode wurde nicht erfolgreich beendet
     *         -1 = Methode wurde erfolgreich beendet, wenn keine Erinnerung gesucht wurde
     *         ab 0 = zeile der gesuchten Erinnerung in der Tabelle, sofern eine Erinnerung gesucht wurde
     */
    public int loadReminders(long tgID, int height, long erID) {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        int returnValue = -1;

        if (con != null) {
            this.setSize(0);
            model.setRowCount(0);
            int counter = 0;
            Object[] row = new Object[4];
            
            Statement stmt = null;
            String query = "";
            
            if(tgID == -1) {
                query = "SELECT * FROM `erinnerungen` ORDER BY `faellig` ASC";
            } else {
                query = "SELECT * FROM `erinnerungen` where `themengruppenID` = " + tgID + " ORDER BY `faellig` ASC";
            }
            
            try {
                stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    long id = rs.getLong(1);
                    String title = rs.getString(2);
                    Date dueTo = rs.getDate(4);
                    Boolean done = rs.getBoolean(5);
                    
                    //Datum umformattieren
                    SimpleDateFormat sdfDate = new SimpleDateFormat("E, dd.MM.yyyy");
                    sdfDate.setTimeZone(TimeZone.getTimeZone("MEZ"));
                    Date current_date = new Date(System.currentTimeMillis());
                    String s_stamp = sdfDate.format(dueTo);

                    //Array befüllen (1 Array wird zu einer Zeile in der Tabelle)
                    row[0] = id;

                    ImageIcon img = null;
                    if(done) {
                        img = (ImageIcon) new ImageIcon((ReminderDB.class.getResource("/dokuverwproject/IMG/tick.png")));
                    } else if (dueTo.before(current_date)) {
                        img = (ImageIcon) new ImageIcon((ReminderDB.class.getResource("/dokuverwproject/IMG/cross.png")));
                    } else if (inDays(current_date,dueTo,4)){
                        img = (ImageIcon) new ImageIcon((ReminderDB.class.getResource("/dokuverwproject/IMG/pin.png")));
                        //new Thread(new MediaPlayer(ReminderDB.class.getResource("/dokuverwproject/IMG/butcher.wav").getFile())).start();
                    } else {
                        img = (ImageIcon) new ImageIcon((ReminderDB.class.getResource("/dokuverwproject/IMG/working.png")));
                    }
                    img = resizeImageIcon(img, height, height);

                    row[1] = img;
                    row[2] = title;
                    row[3] = s_stamp;

                    if (row[0].equals(erID)){
                        returnValue = counter;
                    }
                    
                    model.addRow(row);
                    this.setSize(this.getSize() + 1);
                    counter++;
                }
                stmt.close();
                return returnValue;
            } catch (Exception e) {
                new NotifyFrameGUI("Fehler", "Fehler beim Laden der Erinnerungenliste.");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        } else {
            new NotifyFrameGUI("Fehler", "Fehler beim Zugriff auf die Datenbank");
        }
        return -2;
    }

    /**
     * Lädt inhalt, Titel oder pfad einer Erinnerung
     * 
     * @param id - ID der gewünschten Erinnerung
     * @param type - zu landender Text-Typ
     * @return - gibt den in der DB gespeicherten Text abhängig vom Typ zurück
     */
    public String loadText(long id, String type) {
        String query = "SELECT * FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        String returnValue = "";
        
        if (con != null){
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ResultSet result = ps.executeQuery();
                if(result.next()){
                    if(type.equals("inhalt")){
                        returnValue = result.getString(3);
                    } else if (type.equals("titel")){
                        returnValue = result.getString(2);
                    }
                    else if(type.equals("pfad")){
                        returnValue = result.getString(7);
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return returnValue;
    }

    /**
     * Läd Erstellungs- oder Fälligkeitsdatum einer Erinnerung
     * 
     * @param id - ID der Erinnerung, von der geladen werden soll
     * @param type - Typ des zu ladenden Datums ("fällig" oder "erstellt")
     * @return - gibt das geladenene Datum abhängiy von typ zurück
     */
    public Date loadDate(long id, String type){
        String query = "SELECT * FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        Date returnValue = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        
        if (con != null){
            returnValue = new Date(System.currentTimeMillis());
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ResultSet result = ps.executeQuery();
                if(result.next()){
                    if(type.equals("faellig")){
                        returnValue = result.getDate(4);
                    } else if (type.equals("erstellt")){
                        returnValue = result.getDate(8);
                    }
                }
                return returnValue;
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank Datum.");
            }
        }
        return returnValue;
    }

    /**
     * Wenn eine Themengruppe gelöscht wird, löscht diese Methode
     * die Erinnerungen, die an Dateien dieser TG hängen
     * 
     * @param tgID - ID der Themengruppe
     * @return - gibt zurück, ob die Methode erfolgreich durchgelaufen wurde
     */
    public boolean deleteTGReminders(long tgID){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`themengruppenID` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, tgID);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }

    /**
     * Löscht die Erinnerung mit der übergebenen ID
     * 
     * @param id - ID der zu löschenden Erinnerung
     * @return - gibt zurück, ob die Methoe erfolgreich durchlaufen wurde
     */
    public boolean deleteReminder(long id){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }

    /**
     * Wenn eine Datei gelöscht wird, löscht diese Methode die zu der Datei gehörenden Erinnerungen
     * 
     * @param pfad - Pfad der zu löschenden Datei
     * @return - Gibt zurück, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean deleteFileReminders(String pfad){
        String query = "DELETE FROM `erinnerungen` WHERE `erinnerungen`.`dateiPfad` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        if(con!=null) {
            try {
                ps = con.prepareStatement(query);
                ps.setString(1, pfad);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }

    /**
     * Gibt die ThemengruppenID einer Erinnerung zurück
     * 
     * @param id - ID der Erinnerung, deren Themengruppen ID benötigt wird
     * @return - ThemengruppenID der Erinnerung
     */
    public long getTGID(long id){
        String query = "SELECT `themengruppenID` FROM `erinnerungen` WHERE `id` = ?";
        PreparedStatement ps = null;
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        long ausgabe = 0;
        if (con != null){
            try {
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                ResultSet result = ps.executeQuery();
                if(result.next()){
                    ausgabe = result.getLong(1);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return ausgabe;
    }
    
    /**
     * Speichert die Daten Titel, text, Datum in der Erinnerung mit der übergebenen id
     * 
     * @param id - ID der Erinnerung
     * @param title - Titel der Erinnerung
     * @param content - Inhalt der Erinnerung
     * @param date - neues Fälligkeitsdatum der Erinnerung
     * @return - gibt zurück, ob die Methode erfolgreich durchlaufen wurde.
     */
    public boolean editReminder(long id,String title, String content, String date) {
        String query = "UPDATE `erinnerungen` SET `titel`= ?, `inhalt`= ?, `faellig` = ? WHERE id = ?";
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();
        PreparedStatement ps = null;
        if (con != null) {
            try {
                ps = con.prepareStatement(query);
                ps.setString(1,title);
                ps.setString(2, content);
                ps.setString(3, date);
                ps.setLong(4, id);
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }

    /**
     * Ändert den Erinnerungsstatus einer Erinnerung
     * 
     * @param id - ID der anzupassenden Erinnerung
     * @return gibt zurück, ob die Methode erfolgreich durchlaufen wurde
     */
    public boolean toggleDoneState(long id) {
        DBConn dbc = new DBConn();
        Connection con = dbc.getConnection();

        if(con != null) {
            try {
                String query = "UPDATE `erinnerungen` SET `erledigt`= !`erledigt` WHERE id = ?";
                PreparedStatement ps = null;
                ps = con.prepareStatement(query);
                ps.setLong(1, id);
                if(ps.executeUpdate() != 0) {
                    ps.close();
                    return true;
                }
                ps.close();
            } catch (Exception e) {
                System.out.println(e.toString());
                new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
            }
        }
        return false;
    }
    
    /**
     * Wenn ein File umbenannt wird, dann setzt diese Methode die Referenzen
     * der dazugehörenden Erinnerungen in der DB auf das neue File.
     * 
     * @param oldPath
     * @param newPath
     * @return 
     */
    public boolean resetReferenceToFile(String oldPath, String newPath) {
        String query = "UPDATE `erinnerungen` SET `dateiPfad`= ? WHERE `dateiPfad` = ?";
        PreparedStatement ps = null;
        try {
            DBConn dbc = new DBConn();
            Connection con = dbc.getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, newPath);
            ps.setString(2, oldPath);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            new NotifyFrameGUI("Fehler", "Fehler bei der Verbindung zur Datenbank.");
        }
        return false;
    }

    // Getter und Setter
    
    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }


}
