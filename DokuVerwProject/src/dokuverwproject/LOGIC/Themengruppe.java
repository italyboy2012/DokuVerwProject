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
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 * ChangeLog
 * Falk @ 05.08.2020
 * Funktion dateiLoeschen:
 *      verschiebt übergebene Datei in den Papierkorb
 *      löscht Notizen und Erinnerungen, die mit der Datei verknüpft sind.
 */
public class Themengruppe {
    private long id = 0;
    private String titel = "";
    private String pfad = "";
    private Timestamp erstellungsdatum = null;
    //private long größe = 0; NICHT MEHT BENÖTIGT -------------------------------------------------------------
    //private ArrayList<Datei> dateien = new ArrayList<>(); NICHT MEHT BENÖTIGT -------------------------------------------------------------
    private javax.swing.JTable table = null; // Zugriff auf Tabelle in ThemengruppeFrame
    private ThemengruppeFrame tgf = null; // Zugriff auf Gui dieser Logik
    
    private Notiz n = new Notiz(); //Zugriff auf DB-Schicht der Notizen
    private ErinnerungenListe e = new ErinnerungenListe(); //Zugriff auf DB-Schicht der Erinnerungen
    
    //FÜR DIE NAVIGATION
    private javax.swing.JTextField pfadAnzeige = null; //Pfadanzeige auf Frame für Themengruppe
    private String pfadNav = "";
    private ArrayList<String> pfadsNav = new ArrayList<String>();
    private int pfadsNavIndex = 0;
    
    public Themengruppe(long id, javax.swing.JTable table, javax.swing.JTextField pfadAnzeige, ThemengruppeFrame tgf) {
        this.id = id;
        this.table = table;
        this.pfadAnzeige = pfadAnzeige;
        this.tgf = tgf;
    }

    public long getId(){
        return this.id;
    }
    
    @Override
    public String toString() {
     return this.id + " - " + this.titel;   
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
                            this.titel = rs.getString(2);
                            this.pfad = rs.getString(3);
                            if(pfadsNavIndex == 0) {
                                this.pfadNav = new String(this.pfad); // Pfad für Navigation
                                //Der Pfad wird nur dann auf das Hauptverzeichnis der Themengruppe gesetzt,
                                //wenn keine Unterordner geöffnet sind.
                                //Sind unterordner geöffnet, wird deren aktueller Pfad
                                //so nicht überschrieben.
                            }
                            this.erstellungsdatum = rs.getTimestamp(4);
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
    
    public boolean dateienIndexieren() {
//        try {
//            DefaultTableModel model = (DefaultTableModel)table.getModel();
//            model.setRowCount(0);
//            table.scrollRectToVisible(table.getCellRect(0,0, true)); 

//            Object[] row = null;

            pfadAnzeige.setText(pfadNav);
            File f = new File(pfadNav);
            if(!f.exists()) return false;
//            final File[] x = f.listFiles();
            return dateienInTabelleAnzeigen(f.listFiles());
//            for (final File file : x) {
//                row = new Object[5];
//                ImageIcon img = (ImageIcon) javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon(file);
//                row[0] = img;
//                row[1] = file.getName();
//                row[2] = file.getPath();
//                row[3] = readableDate(file.lastModified());
//                row[4] = readableFileSize(file, file.length()); //größe
//
//                model.addRow(row);
//            }
//        } catch(Exception e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//        }
    }
    
    public void gesuchteDateienIndexieren(String suchwort) {
        //alle dateien des aktuell angezeigten Orts werden nach gesuchten Suchwort durchsucht
        File[] allFilesInCurrentNavPath = new File(pfadNav).listFiles();
        if(allFilesInCurrentNavPath!=null) {
            ArrayList<File> foundFiles = new ArrayList<>();
            for (File file : allFilesInCurrentNavPath) {
                if (file.getName().toLowerCase().contains(suchwort.toLowerCase())){
                    foundFiles.add(file);
                }
            }
            // da "(File[]) foundFiles.toArray()" nicht funktioniert, muss
            // die Umwandlung zum Array hier händisch erfolgen
            File[] x = new File[foundFiles.size()];
            for (int i = 0; i < x.length; i++) {
                x[i] = foundFiles.get(i);
            }
            dateienInTabelleAnzeigen(x);
        }
    }
    
    public boolean dateienInTabelleAnzeigen(File[] x) {
        try {
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.setRowCount(0);
            table.scrollRectToVisible(table.getCellRect(0,0, true)); 
            Object[] row = null;

            for (final File file : x) {
                row = new Object[5];
                ImageIcon img = (ImageIcon) javax.swing.filechooser.FileSystemView.getFileSystemView().getSystemIcon(file);
                row[0] = img;
                row[1] = file.getName();
                row[2] = file.getPath();
                row[3] = readableDate(file.lastModified());
                row[4] = readableFileSize(file, file.length()); //größe

                model.addRow(row);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return false;
    }
    
    public String readableDate(long lastModified) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	return(sdf.format(lastModified));
    }
    
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
    
    public void goBackNAV() {
        if(this.pfadsNavIndex >= 1) {
            this.pfadsNavIndex--;
            this.pfadNav = pfadsNav.get(this.pfadsNavIndex);
            pfadsNav.remove(this.pfadsNavIndex);
            dateienIndexieren();
        } else {
            try {
                //Clip clip = AudioSystem.getClip();
                //clip.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("sounds/WindowsError.wav")));
                //clip.start();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
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
                    this.dateienIndexieren();
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
    
    public boolean dateiHinzufuegen(File file) {
        //Datei in aktuell angezeigtes Verzeichnis (pfadNav) schieben
        if(file.renameTo(new File(pfadNav + File.separator + file.getName()))) {
            return true;
        }
        return false;
    }
    
    public boolean dateiErstellen(String name, String cuttentNavPath) {
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
    
    public boolean verzeichnisErstellen(String name, String cuttentNavPath) {
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
    
    public void dateiLoeschen(String dateiPfad) {
        File f = new File(dateiPfad);
        Desktop desktop = Desktop.getDesktop();
        
        if(!f.exists()) {
            NotifyFrame nf = new NotifyFrame("Fehler", "Die Datei ist evtl. nicht mehr vorhanden. Bitte Ansicht aktualisieren.");
            return;
        } else {
            try {
                //desktop.moveToTrash(f);
                //
                //// --------- Notiz_NICHTBENUTZT für datei noch mit löschen.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Methode benännt eine Datei/ein Verzeichnis um.
     * Sie veranlasst auch, dass die Referentz  der
     * zu dieser Datei gehörende Notizen und Erinnerungen in der DB
     * geändert werden, damit das umbenannte File immer noch
     * auf die Erinnerungen und auf die Notizen zugreifen kann.
     * 
     * @param neuerName - Name der neuen Datei
     * @param dateiPfad - Pfad des umzubenennenen Files
     * @return 
     */
    public boolean dateiUmbenennen(String neuerName, String dateiPfad) {
        // File (or directory) with old name
        File f = new File(dateiPfad);
        if(!f.exists()) return false;
        
        //Pathname des Vorverzeichnises (Pfad ohne Dateiname)
        String parentPath = f.getParent(); 

        // File (or directory) with new name
        File f2 = new File(parentPath + File.separator + neuerName);
        if(f2.exists()) return false;
        
        if(f.renameTo(f2)) {
            // Referent Notizen und Erinnerungen in DB neu setzen
            if(!n.resetReferenceToFile(dateiPfad, f2.getAbsolutePath())) { //Notizen
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim setzen der neuen Referenz zur Notiz in der DB.");
            }
            if(!e.resetReferenceToFile(dateiPfad, f2.getAbsolutePath())) { //Erinnerungen
                NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim setzen der neuen Referenz zu den Erinnerungen in der DB.");
            }
            return true;
        }
        return false;
    }
    
//    public void dateiÖffnen(long dateiId) {
//        
//    }
    
//    public void setGröße(int größe) {
//        this.größe = größe;
//    }
}
