/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.GUI;

import dokuverwproject.DB.ReminderDB;
import dokuverwproject.DB.NoteDB;
import dokuverwproject.DB.TopicGroupDB;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Giuseppe & Falk
 */
public class TopicGroupOverviewGUI extends javax.swing.JInternalFrame {
    private MainFrameGUI mfGUI = null; //Referenz zum hf, damit beim Schließen dieses Frames (tgüf) die Referenz des hf zu tgüf gelöscht wird
    private TopicGroupDB tgDB = null; // Logik dieser Klasse
    private NoteDB nDB = null;
    private ReminderDB rDB = null;

    /**
     * Konstruktor übergibt der Logikklasse das TableModel,
     * damit diese die Daten in die Tabelle laden und anzeigen kann.
     * 
     * Außerdem wird eine Methode aufgerufen, welche die Logikklasse zum
     * Laden der Themengruppen aufruft.
     */
    public TopicGroupOverviewGUI(MainFrameGUI mf) {
        initComponents();
        
        this.mfGUI = mf;
        this.tgDB = new TopicGroupDB((DefaultTableModel)jTable1.getModel());
        this.rDB = new ReminderDB();
        this.nDB = new NoteDB();
        
        loadTopicGroupsFromDB();
        
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                mf.resetReferenceToTopicGroupOverviewGUI();
            }
        });
        
    }
    
    /**
     * Methode ruft eine Methode der Logikklasse auf,
     * welche die Themengruppen aus der DB lädt und anzeigt.
     */
    public void loadTopicGroupsFromDB() {
        setStaturs("Laden...");
        if(tgDB.loadFromDB()) {
            setStaturs(tgDB.getSize() + " Themengruppen geladen");
        } else {
            setStaturs("Fehler");
        }
    }
    
    /**
     * Methode öffnet den ausgewählten Datensatz aus der Tabelle.
     * Für den Datensatz wird eine neue Instanz der Klasse TopicGroupGUI erstellt.
     * Ihr wird die ID der Themengruppe übergeben. Die restlichen Daten
     * der Themengruppe werden in Echtzeit aus der Datenbank gezogen.
     */
    public void openSelectedRow() {
        if(jTable1.getSelectedRow() != -1) {
            long selectedRowId = (long) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            TopicGroupGUI tgf = new TopicGroupGUI(selectedRowId,"",-1);
        } else {
            NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * Methode löscht den ausgewählten Datensatz aus der Tabelle.
     * Die ID der ausgewählten Themengruppe wird der Logikklasse übergeben,
     * welche den Datensatz dann löscht.
     */
    public void deleteSelectedRow() {
        if(jTable1.getSelectedRow() != -1) {
            setStaturs("Löschen...");
            long selectedRowId = (long) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            if(!rDB.deleteTGReminders(selectedRowId)){
                NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es ist ein Fehler beim Löschen der Erinnerungen aufgetreten.");
                setStaturs("Fehler beim Löschen... Bitte aktualisieren.");
                return;
            }
            if(!nDB.deleteTGNotes(selectedRowId)){
                NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es ist ein Fehler beim Löschen der Notizen aufgetreten.");
                setStaturs("Fehler beim Löschen... Bitte aktualisieren.");
                return;
            }
            if(!tgDB.deleteTG(selectedRowId)) {
                NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es ist ein Fehler beim Löschen der Themengruppe aufgetreten.");
                setStaturs("Fehler beim Löschen... Bitte aktualisieren.");
                return;
            }
            loadTopicGroupsFromDB();
        } else {
            NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * Methode bearbeitet den ausgewählten Datensatz aus der Tabelle.
     * Es wird ein neues Fenster geöffnet und diesem wird die Logikklasse und diese Klasse übergeben.
     * Über die Referenz zur Logikklasse kann dieser Datensatz gelöscht werden.
     * Über die Referenz zu dieser Klasse kann die tabellarische Ansicht gelöscht werden.
     */
    public void editSelectedRow() {
        if(jTable1.getSelectedRow() != -1) {
            setStaturs("Warten auf Eingabe...");
            long selectedRowId = (long) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
            String titel = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            String pfad = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            EditTopicGroupGUI tgb = new EditTopicGroupGUI(selectedRowId, titel, pfad, tgDB, this);
        } else {
            NotifyFrameGUI nf = new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * MEthode veranlasst das Erstellen einer Themengruppe
     */
    public void createTopicGroup() {
        setStaturs("Warten auf Eingabe...");
        CreateTopicGroupGUI tgef = new CreateTopicGroupGUI(tgDB, this);
    }
    
    /**
     * Setzt den ihr übergebenen Status auf dem Textfeld des Frames.
     * 
     * @param status 
     */
    public void setStaturs(String status) {
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
        setTitle("Themengruppen");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(600, 350));

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
        jLabel7.setText("Themengruppen");

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/add.png"))); // NOI18N
        jButton2.setToolTipText("neu");
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
                "Nr.", "Titel", "Pfad", "Anlagedatum"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(100);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(1).setMinWidth(250);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(3).setMinWidth(200);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(200);
        }

        textField1.setEditable(false);
        textField1.setText("textField1");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        editSelectedRow();
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        loadTopicGroupsFromDB();
    }//GEN-LAST:event_jButton9ActionPerformed
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteSelectedRow();
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        createTopicGroup();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        openSelectedRow();
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
