/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.GUI;

import dokuverwproject.DB.ReminderDB;
import javax.swing.ImageIcon;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe &  Falk
 */
public class ReminderOverviewGUI extends javax.swing.JInternalFrame {
    private MainFrameGUI mfGUI = null; //Referenz zum hf, damit beim Schließen dieses Frames (eüf) die Referenz des hf zu eüf gelöscht wird
    private ReminderDB reminderDB = null; // MySQL-Logik der Erinnerungen
    
    public ReminderOverviewGUI(MainFrameGUI mfGUI) {
        initComponents();
        
        this.mfGUI = mfGUI;
        reminderDB = new ReminderDB((DefaultTableModel) jTable1.getModel());
        
        loadRemindersFromDB();
        
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                mfGUI.resetReferenceToReminderOverviewGUI();
            }
        });
        
    }

    /**
     * gibt den Befehl zum Laden aller Erinnerungen an die Klasse ReminderDB weiter
     */
    public void loadRemindersFromDB(){
        setStatus("Laden...");
        int height = jTable1.getRowHeight() - jTable1.getRowHeight()/10;
        if(reminderDB.loadReminders(-1, height, -1) != -2) { //-1, um alle Erinnerungen laden, 2. parameter in diesem Fall egal(dient zur skallierung der Icons in ThemengruppenFrame, 3. Parameter -1, da nach keiner Erinnerung geuscht wird
            setStatus(reminderDB.getSize() + " Erinnerungen geladen");
        } else {
            setStatus("Fehler");
        }
        return;
    }

    /**
     * gibt den Befehl zum löschen der Erinnerung hinter der markierten Zeile an die Klasse ReminderDB weiter
     */
    public void deleteReminder(){
        if(jTable1.getSelectedRow() != -1) {
            if(!reminderDB.deleteReminder(getIDOfSelectedRow())) {
                new NotifyFrameGUI("Fehler", "Der Datensatz konnte nicht gelöscht werden.");
            }
            loadRemindersFromDB();
            return;
        } else {
            new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
        return;
    }

    /**
     * öffnet einen CreadeAndEditReminderGUI für die markierte Erinnerung
     */
    public void editReminder() {
        if(jTable1.getSelectedRow() != -1) {
            new CreadeAndEditReminderGUI(this, getIDOfSelectedRow()); //ID der Erinnerung
        } else {
            new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }

    /**
     * gibt den Befehl zum Ändern des Erledigt-Status der markierten Erinnerung an die Klasse ReminderDB weiter
     */
    public void toggleDoneState(){
        if(jTable1.getSelectedRow() != -1) {
            if(reminderDB.toggleDoneState(getIDOfSelectedRow())) {
                loadRemindersFromDB();
                this.setStatus("Erinnerung bearbeitet.");
                return;
            } else {
                new NotifyFrameGUI("Fehler", "Der Erledigt-Status konnte nicht bearbeitet werden. Bitte Ansicht aktualisieren.");
                return;
            }
        } else {
            new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * Läd die ID und ThemengruppenID der Erinnerung der markierten Zeile und öffnet einen ThemengruppenFrame mit den geladenen Informationen
     */
    public void openSelectedRow() {
        if(jTable1.getSelectedRow() != -1) {
            long erinnerungsID = getIDOfSelectedRow(); // eigene Methode, nicht die Standard-Methode der Table
            long themengruppenID = reminderDB.getTGID(erinnerungsID);
            String pfad = reminderDB.loadText(getIDOfSelectedRow(), "pfad");
            long erID = getIDOfSelectedRow();
            TopicGroupGUI tgf = new TopicGroupGUI(themengruppenID, pfad,erID);
        } else {
            new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
        return;
    }

    // Getter und Setter
    
     /**
     * gibt ID der aktuell markierten Zeile zurück
     * 
     * @return gibt ID der aktuell markierten Zeile zurück
     */
    public long getIDOfSelectedRow(){
        long id = (long) jTable1.getValueAt(jTable1.getSelectedRow(),0);
        return id;
    }
    
    /**
     * Setzt den ihr übergebenen Status auf dem Textfeld des Frames.
     * 
     * @param status 
     */
    public void setStatus(String status) {
        textField1.setText(status);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        textField1 = new java.awt.TextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Erinnerungen");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(495, 350));
        setPreferredSize(new java.awt.Dimension(495, 576));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/edit-folder.png"))); // NOI18N
        jButton4.setToolTipText("bearbeiten");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/refresh.png"))); // NOI18N
        jButton9.setToolTipText("aktualisieren");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/cancel.png"))); // NOI18N
        jButton3.setToolTipText("löschen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setText("Erinnerungen");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/to-do-list.png"))); // NOI18N
        jButton2.setToolTipText("erledigt/offen setzen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/open.png"))); // NOI18N
        jButton5.setToolTipText("öffnen");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nr.", "Status", "Titel", "Fällig"
            }
        ) {

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return Long.class;
                    case 1: return ImageIcon.class;
                    default: return String.class;
                }
            }

            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

        });
        jTable1.setRowHeight(27);
        jTable1.setRowMargin(0);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator2)
                        .addComponent(jSeparator3)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addContainerGap())
        );

        textField1.setEditable(false);
        textField1.setText("textField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Erinnerung bearbeiten
        editReminder();
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        loadRemindersFromDB();
        // ansicht Aktualisieren.
    }//GEN-LAST:event_jButton9ActionPerformed
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteReminder();
        // Erinnerung Löschen
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Erinnerung auf erledigt
        toggleDoneState();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        openSelectedRow();
        // Datei öffnen
    }//GEN-LAST:event_jButton5ActionPerformed
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            openSelectedRow();
        }
    }//GEN-LAST:event_jTable1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}
