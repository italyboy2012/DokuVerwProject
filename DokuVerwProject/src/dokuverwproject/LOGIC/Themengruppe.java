/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.LOGIC;

import dokuverwproject.DB.DBConn;
import dokuverwproject.DB.ErinnerungenListe;
import dokuverwproject.DB.Notiz;
import dokuverwproject.GUI.NotifyFrame;
import dokuverwproject.GUI.ThemengruppeFrame;
import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 *
 */
public class Themengruppe {
    private long id = 0;
    private String title = "";
    private String path = "";
    private Timestamp creationTimeStamp = null;
    
    private javax.swing.JTable table = null; // Zugriff auf Tabelle in ThemengruppeFrame
    private ThemengruppeFrame tgf = null; // Zugriff auf GUI dieser Logik
    
    private Notiz note = new Notiz(); //Zugriff auf DB-Schicht der Notizen
    private ErinnerungenListe reminder = new ErinnerungenListe(); //Zugriff auf DB-Schicht der Erinnerungen
    
    // FÜR DIE NAVIGATION
    private javax.swing.JTextField displayPath = null; //Pfadanzeige auf Frame für Themengruppe
    private String pfadNav = ""; // aktueller Pfad der Navigation
    private ArrayList<String> pfadsNav = new ArrayList<String>(); // Liste aller aufgerufenen Unterpfade innerhalb einer Themengruppe
    private int pfadsNavIndex = 0; // Speichert die Tiefe, also wie oft ein Unterordner aufgerufen wurde
    
    public Themengruppe(long id, javax.swing.JTable table, javax.swing.JTextField displayPath, ThemengruppeFrame tgf) {
        this.id = id;
        this.table = table;
        this.displayPath = displayPath;
        this.tgf = tgf;
    }

    
    public boolean loadFromDB() {
        if(id != 0) {
            try {
                DBConn dbc = new DBConn();
                Connection con = dbc.getConnection();
                if(con != null) {
                    PreparedStatement ps = null;
                    String query = "SELECT * FROM `themengruppen` WHERE `id` = ?";
                    ps = con.prepareStatement(query);
                    ps.setLong(1, id);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        if(rs.getLong(1) == id) {
                            this.title = rs.getString(2);
                            this.path = rs.getString(3);
                            if(pfadsNavIndex == 0) {
                                this.pfadNav = new String(this.path); // Pfad für Navigation
                                //Der Pfad wird nur dann auf das Hauptverzeichnis der Themengruppe gesetzt,
                                //wenn keine Unterordner geöffnet sind.
                                //Sind unterordner geöffnet, wird deren aktueller Pfad
                                //so nicht überschrieben.
                            }
                            this.creationTimeStamp = rs.getTimestamp(4);
                            return true;
                        }
                    } else {
                        NotifyFrame nf = new NotifyFrame("Fehler", "Die gefundene Themengruppen-ID stimmt nicht mit der intern übergebenen ID überein.");
                    }
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Zugriff auf die Datenbank.");
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Ein interner Übertragrungsfehler der Themengruppen-ID ist aufgetreten.");
        }
        return false;
    }
    
    /**
     * Methode listet alle Files des aktuellen pfadNavs und übergibt diese
     * der Methode renderFilesOnTable(), welche die Dateien rendert
     * 
     * @param reminderPath - Pfad der Erinnerung; wenn "", dann kein Reminder gesucht
     * @return - true = erstellt; false = nicht erstellt
     */
    public int indexFiles(String reminderPath) {
            displayPath.setText(pfadNav);
            File f = new File(pfadNav);
            if(!f.exists()) return -2;
            return renderFilesOnTable(f.listFiles(), reminderPath);
    }
    
    /**
     * Methode sucht im aktuellen pfadNAv Files, dessen Name den searchString entahlten.
     * Gefundene Dateien werden in einem Array referenziert und es wird die
     * Methode renderFilesOnTable() aufgerufen, welche die Dateien rendert.
     * 
     * @param searchString - Suchstring
     */
    public void indexSearchedFiles(String searchString) {
        //alle dateien des aktuell angezeigten Orts werden nach gesuchten Suchwort durchsucht
        File[] allFilesInCurrentNavPath = new File(pfadNav).listFiles();
        if(allFilesInCurrentNavPath!=null) {
            ArrayList<File> foundFiles = new ArrayList<>();
            for (File file : allFilesInCurrentNavPath) {
                if (file.getName().toLowerCase().contains(searchString.toLowerCase())){
                    foundFiles.add(file);
                }
            }
            // da "(File[]) foundFiles.toArray()" nicht funktioniert, muss
            // die Umwandlung zum Array hier händisch erfolgen
            File[] x = new File[foundFiles.size()];
            for (int i = 0; i < x.length; i++) {
                x[i] = foundFiles.get(i);
            }
            renderFilesOnTable(x,"");
        }
    }
    
    /**
     * Methode bekommt ein Array vom Typ Files übergeben und zeigt diese in der Tabelle des
     * Frames an. Wird ein reminderPath übergenen, dann wird sich gemerkt, welche Datei du dem Reminder gehört.
     * Diese wird dann gehighlightet, um den Nutzer anzuzeigen, welche Datei zur ausgewählten Erinnerung gehört.
     * 
     * @param files - Dateien
     * @param reminderPath - Pfad der Erinnerung; wenn "", dann kein Reminder gesucht
     * @return -2 == false
     *         -1 == true
     *         ab 0 == Suchergebnis
     */
    public int renderFilesOnTable(File[] files, String reminderPath) {
        int returnValue = -1;
        int counter = 0;
        try {
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.setRowCount(0);
            table.scrollRectToVisible(table.getCellRect(0,0, true)); 
            Object[] row = null;

            for (final File file : files) {
                row = new Object[5];
                ImageIcon img = (ImageIcon) javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon(file);
                row[0] = img;
                row[1] = file.getName();
                row[2] = file.getPath();
                row[3] = readableDate(file.lastModified());
                row[4] = readableFileSize(file, file.length()); //größe
                if (row[2].equals(reminderPath)){
                    returnValue = counter;
                }
                counter++;
                model.addRow(row);
            }
            return returnValue;
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return -2;
    }
    
    /**
     * Falls ein Unterverzeichnis geöffnet ist und der Nutzer auf goBack klickt,
     * dann wird der vorherige Pfad geladen und die dort befindlichen Dateien
     * werden indexiert.
     */
    public void goBackNAV() {
        if(this.pfadsNavIndex >= 1) {
            this.pfadsNavIndex--;
            this.pfadNav = pfadsNav.get(this.pfadsNavIndex);
            pfadsNav.remove(this.pfadsNavIndex);
            indexFiles("");
        } else {
            try {
                //new Thread(new MediaPlayer(ErinnerungenListe.class.getResource("/dokuverwproject/IMG/butcher.wav").getFile())).start();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
    /**
     * Methode öffnen ausgewähltes File aus der Tabelle.
     * Ist es ein Verzeichnis, wird es geöffnet und die darin befindlichen Dateien
     * werden neu indexiert.
     * 
     */
    public void openSelectedFile() {
        if(table.getSelectedRow() != -1) {
            try {
                String filePath = (String) table.getValueAt(table.getSelectedRow(), 2);
                Desktop desktop = Desktop.getDesktop();
                File file = new File(filePath);
                
                if(!file.exists()) {
                    NotifyFrame nf = new NotifyFrame("Fehler", "Die Datei ist evtl. nicht mehr vorhanden. Bitte Ansicht aktualisieren.");
                    return;
                }
                
                if(file.isDirectory()) {
                    this.pfadsNav.add(pfadNav);
                    pfadsNavIndex++;
                    
                    this.pfadNav = file.getPath();
                    this.indexFiles("");
                    return;
                } else if(file.isFile()) {
                    if(file.exists()) desktop.open(file);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else {
            NotifyFrame nf = new NotifyFrame("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * Mehtode erstellt ein neues Verzeichnis.
     * 
     * @param name - Name des Verzeichnisses
     * @param cuttentNavPath - aktuell angezeigter Pfad in der Themengruppe
     * 
     * @return - true = erstellt; false = nicht erstellt
     */
    public boolean createDir(String name, String cuttentNavPath) {
        try {
            File f = new File(cuttentNavPath);
            if(!f.exists()) return false;

            File f2 = new File(cuttentNavPath + File.separator + name);
            if(f2.exists()) return false;

            if(f2.mkdirs()) return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * File in aktuell angezeigtes Verzeichnis (pfadNav) verschieben
     * 
     * @param file
     * @return  - true = erledigt; false = fehler
     */
    public boolean addFile(File file) {
        //
        if(file.renameTo(new File(pfadNav + File.separator + file.getName()))) {
            return true;
        }
        return false;
    }
    
    /**
     * Methode erstellt eine Datei
     * 
     * @param name - Name der Datei
     * @param cuttentNavPath - aktuell angezeigter Pfad in der Themengruppe
     * 
     * @return - true = erstellt; false = nicht erstellt
     */
    public boolean createFile(String name, String cuttentNavPath) {
        try {
            File f = new File(cuttentNavPath);
            if(!f.exists()) return false;

            File f2 = new File(cuttentNavPath + File.separator + name);
            if(f2.exists()) return false;

            if(f2.createNewFile()) return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Methode löscht die Datei, dessen Pfad ihr übergeben wird.
     * 
     * @param path - Pfad der zu löschenden Datei
     * @return - true = gelöscht; false = nicht gelöscht
     */
    public boolean deleteFile(String path) {
        File f = new File(path);
        
        if(!f.exists()) {
            new NotifyFrame("Fehler", "Die Datei ist evtl. nicht mehr vorhanden. Bitte Ansicht aktualisieren.");
            return false;
        } else {
            if(f.delete()) return true;
        }
        return false;
    }
    
    /**
     * Methode benännt eine Datei/ein Verzeichnis um.
     * Sie veranlasst auch, dass die Referentz  der
     * zu dieser Datei gehörende Notizen und Erinnerungen in der DB
     * geändert werden, damit das umbenannte File immer noch
     * auf die Erinnerungen und auf die Notizen zugreifen kann.
     * 
     * @param newName - Name der neuen Datei
     * @param currentPath - Pfad des umzubenennenen Files
     * @return 
     */
    public boolean renameFile(String newName, String currentPath) {
        File f = new File(currentPath); // File (or directory) with old name
        if(!f.exists()) return false;
        
        String parentPath = f.getParent(); //Pathname des Vorverzeichnises (Pfad ohne Dateiname)
        
        File f2 = new File(parentPath + File.separator + newName); // File (or directory) with new name
        if(f2.exists()) return false;
        
        if(f.renameTo(f2)) {
            // Referent Notizen und Erinnerungen in DB neu setzen
            if(!note.resetReferenceToFile(currentPath, f2.getAbsolutePath())) { //Notizen
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim setzen der neuen Referenz zur Notiz in der DB.");
            }
            if(!reminder.resetReferenceToFile(currentPath, f2.getAbsolutePath())) { //Erinnerungen
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim setzen der neuen Referenz zu den Erinnerungen in der DB.");
            }
            return true;
        }
        return false;
    }
    
    /**
     * Methode wandelt das ihr übergebene Datum als Long in ein lesbares Format um
     * 
     * @param lastModified - Änderungsdatum in long-Format
     * 
     * @return - lesbares Datum
     */
    public String readableDate(long lastModified) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	return(sdf.format(lastModified));
    }
    
    /**
     * Methode wandelt die ihr übergebenen FileInfos in einer lesbaren FileSize-Info um
     * 
     * @param file
     * @param size - Filesize
     * 
     * @return - lesbar formattierte Filegröße
     */
    public String readableFileSize(File file, long size) {
        if(file.exists() && file.isFile()) {
            if(size <= 0) return "0 B";
            final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
            int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
            return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        } else {
            return "";
        }
    }
    
    @Override
    public String toString() {
        return "[" + this.id + "] : " + this.title;   
    }
    
    // Getter und Setter
    
    public long getId(){
        return this.id;
    }
    
}
