/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.GUI;

import dokuverwproject.DB.ReminderDB;
import static dokuverwproject.commons.Common.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/**
 *
 * @author Falk
 */
public class CreadeAndEditReminderGUI extends javax.swing.JFrame {
    private TopicGroupGUI tgGUI = null; // Referenz, um Tabellenanzeige zu aktualisieren
    private ErinnerungsuebersichtFrame euf = null; // Gui der Erinnerungsübersicht, um die Anzeige zu aktualisieren
    
    private ReminderDB el = null; // DB-Logik der Erinnerungen
    
    private long themengruppenID = 0; // ID der Themengruppe, zu der die dAtei gehört, zu der wir eine Erinnerung erstellen
    private String file =""; // puffer für Übergabe des Pfades der in Themengruppe markierten Datei
    private long id = 0;
    private String buttonText = "erstellen";


    /**
     * Fenster zum Erstellen einer neuen Erinnerung
     * @param tgf Frame der Themengruppe, aus dem die Erinnerung erstellt werden soll
     * @param themengruppenID ID der Themengruppe
     * @param dateiPfad Pfad der Datei, für die diese Erinnerung erstellt wird
     */
    public CreadeAndEditReminderGUI(TopicGroupGUI tgf, long themengruppenID, String dateiPfad) {
        this.tgGUI = tgf;
        this.themengruppenID = themengruppenID;
        this.file = dateiPfad;
        el = new ReminderDB();
        initExternalFrame(this, "hourglass.png");
        initComponents();
        jDateChooser1.setDate(new Timestamp(System.currentTimeMillis())); // Datumsanzeige auf aktuelles Datum setzen
        this.setVisible(true);

    }

    /**
     * Fenster zum Bearbeiten einer bestehenden Erinnerung
     * @param euf Frame der Erinnerungsübersicht aus dem die Erinnerung erstellt wird.
     * @param id ID der zu ändernder Erinnerung
     */
    public CreadeAndEditReminderGUI(ErinnerungsuebersichtFrame euf, long id) {
        this.id = id;
        el = new ReminderDB();
        this.euf = euf;
        initComponents();

        initExternalFrame(this, "hourglass.png");
        
        jDateChooser1.setDate(el.loadDate(id,"faellig")); // Datumsanzeige auf aktuelles Datum setzen
        jTextField1.setText(el.loadText(id,"titel"));
        jTextArea1.setText(el.loadText(id,"inhalt"));
        this.setVisible(true);
        
        this.buttonText = "Speichern";
        this.setTitle("Erinnerung bearbeiten");
        jLabel1.setText("Erinnerung bearbeiten");
        
        this.setVisible(true);
    }

    /**
     * Fenster zum Bearbeiten einer bestehenden Erinnerung
     * @param tgf Frame der Themengruppe aus dem die Erinnerung erstellt wird.
     * @param id ID der zu ändernder Erinnerung
     */
    public CreadeAndEditReminderGUI(TopicGroupGUI tgf, long id) {
        this.id = id;
        el = new ReminderDB();
        this.tgGUI = tgf;
        //this.setTitle("Erinnerung ändern");
//        this.buttonText = "Speichern";
        initComponents();
        setTitle("Erinnerung bearbeiten");
        jLabel1.setText("Erinnerung bearbeiten");
        jButton1.setText("speichern");
        initExternalFrame(this, "hourglass.png");
        
        jDateChooser1.setDate(el.loadDate(id,"faellig")); // Datumsanzeige auf aktuelles Datum setzen
        jTextField1.setText(el.loadText(id,"titel"));
        jTextArea1.setText(el.loadText(id,"inhalt"));
        this.setVisible(true);
        
    }

    /**
     * Liest die einzelnen Felder des Fensters aus, und gibt diese Daten zum Speichern an die Klasse ErinnerungsListe weiter
     */
    public void speichern() {
        String titel = jTextField1.getText();
        String inhalt = jTextArea1.getText();
        String faelligkeitsDatum = "";
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            faelligkeitsDatum = formatter.format(jDateChooser1.getDate());
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        String datei = file;
        if (this.themengruppenID != 0 && this.id == 0) {
            if(!titel.equals("") && !titel.equals(null) && !inhalt.equals("") && !inhalt.equals(null) && !faelligkeitsDatum.equals("") && !faelligkeitsDatum.equals(null)) {
                if(!el.createReminder(titel, inhalt, faelligkeitsDatum, themengruppenID, datei)) {
                    NotifyFrame nf = new NotifyFrame("Fehler", "Fehler beim Erstellen des Datensatzes in der Datenbank.");
                } else {
                    tgGUI.ansichtAktualisieren();
                    this.dispose();
                }
            } else {
                NotifyFrame nf = new NotifyFrame("Fehler", "Bitte alle notwendigen Felder ausfüllen.");
            }
        } else if(this.themengruppenID == 0 && this.id != 0) {
            el.editReminder(this.id, titel, inhalt, faelligkeitsDatum);
            this.dispose();
        }
        if(euf != null) euf.erinnerungenAusDBLaden();
        if(tgGUI != null) tgGUI.ansichtAktualisieren();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Erinnerung erstellen");
        setAlwaysOnTop(true);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/get-ready.png"))); // NOI18N
        jLabel1.setText("Erinnerung erstellen");

        jLabel3.setText("Titel:");

        jLabel4.setText("Inhalt:");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/save.png"))); // NOI18N
        jButton1.setText("erstellen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel5.setText("Fällig:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE)
                        .addGap(80, 80, 80))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        speichern();
    }//GEN-LAST:event_jButton1ActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
