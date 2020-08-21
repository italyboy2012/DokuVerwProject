/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.GUI;

import dokuverwproject.DB.ReminderDB;
import dokuverwproject.DB.NoteDB;
import dokuverwproject.LOGIC.TopicGroupLOGIC;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import static dokuverwproject.commons.Common.*;

/**
 *
 * @author Giuseppe & Falk
 */
public class TopicGroupGUI extends javax.swing.JFrame {
    private ReminderDB rDB = null; // MySQL-Logik der Erinnerungen
    private long selectedRowId = 0; //Ausgewählte Spalten-ID aus ThemengruppenübersichtFrame
    private TopicGroupLOGIC tgLOGIC = null; //Logik von TopicGroupGUI
    private NoteDB nDB = null; //Logik von NoteDB
    private SearchFileGUI sfGUI = null; //Fenster zum Suchen; damit max. 1 Fenster pro TopicGroupLOGIC genutzt werden kann,
                                        //wird hier eine Referenz zwischengespeichert.
    private final int NOTE_MAX_LENGTH = 60000;
    
    /**
     * Der Konstruktor bekommt die ID der ausgewählten TopicGroupLOGIC und eine Referenz zur
     * Tabelle des Frames übergeben. Die ID wird der Logikklasse TopicGroupLOGIC übergeben.
     * Diese lädt alle Daten der TopicGroupLOGIC aus der DB und zeigt alle indexierten Dateien an.
     * 
     * @param selectedRowId
     * @param path
     * @param rID 
     */
    public TopicGroupGUI(long selectedRowId, String path, long rID) {
        this.selectedRowId = selectedRowId;
        
        initComponents();
        initExternalFrame(this, "open.png");
        
        tgLOGIC = new TopicGroupLOGIC(this.selectedRowId, jTable1, this.jTextField2, this);
        rDB = new ReminderDB((DefaultTableModel) jTable2.getModel());
        nDB = new NoteDB();
        
        this.addWindowListener(new WindowAdapter() { // Schließt beim Schließen des Frames das SuchenFrame mit
            public void windowClosing(WindowEvent event) {
                if(sfGUI != null) sfGUI.dispose();
            }
        });
        
        this.setVisible(true);
        reloadAndRerenderContent(path, rID);
    }
    
    /**
     * Methode Veranlasst das Neuladen des Inhalts mittels der Methode reloadAndRerenderContent().
     * Sie ist eine Hilfsmethode, damit beim Aktualisieren keine unnötigen Parameter übergeben werden müssen.
     */
    public void refreshView(){
        reloadAndRerenderContent("", -1);
    }

    /**
     * Methode lädt Details der TopicGroupLOGIC und indexiert anschließend alle Dateien des OS innerhalb dieser TopicGroupLOGIC.
     * Dafür werden MEthoden der Logikklasse TopicGroupLOGIC verwendet.
     */
    public void reloadAndRerenderContent(String path, long rID) {
        jTable1.clearSelection();
        loadTopicGroup(path);
        jTable2.clearSelection();
        int hoehe = jTable2.getRowHeight() - jTable2.getRowHeight()/10;

        try {
            int reminderRow = rDB.loadReminders(selectedRowId, hoehe, rID);
            if (rID >= 0) {
                jTable2.setRowSelectionInterval(reminderRow, reminderRow);
            } else {
                emptyAndDisableTextField();
            }
        } catch (Exception e){
            System.out.println("keine Erinnerung ausgewählt");
        }

        //leereSperreTextfeld1(); // NoteDB aus TextFeld löschen, da nach aktualisieren keine Zeile mehr ausgewählt
        if(sfGUI != null) sfGUI.setTopicTitle(tgLOGIC.toString()); // Wenn ein SuchenFrame geöffnet ist,
                                                                  // dann dort den Titel der TopicGroupLOGIC anzeigen,
                                                                  // damit der user weiß, dass dieses SuchenFrame
                                                                  // in dieser TopicGroupLOGIC sucht.
                                                                  // Beim Aktualisieren wird hier der aktuelle Name
                                                                  // aus der DB geladen und angezeigt.
    }
    
    /**
     * Lädt eine Themengruppe und die dazugehörigen Erinnerungen aus der DB
     * 
     * @param path 
     */
    public void loadTopicGroup(String path){
        textField1.setText("Laden...");
        if(tgLOGIC.loadFromDB()) {
            int selectedRow = tgLOGIC.indexFiles(path);
            textField1.setText("Daten aus Datenbank geladen. Indexiere Dateien...");
            jLabel1.setText(tgLOGIC.toString()); //Titelleiste mit Themengruppenwerten setzen
            this.setTitle(tgLOGIC.toString()); //Fenstertitel mit Themengruppenwerten setzen

            if(selectedRow!=-2) {

                textField1.setText("Dateien indexiert und geladen.");
                if (selectedRow >= 0){
                    jTable1.setRowSelectionInterval(selectedRow,selectedRow);
                    loadNote();
                }
                return;
            } else {
                textField1.setText("Fehler beim Indexieren der Dateien auf dem OS");
            }
        } else {
            textField1.setText("Fehler beim Laden aus der Datenbank.");
        }
    }
    
    /**
     * Methode veranlasst das ERstellen einer Erinnerung zur markierten Datei
     */
    public void createReminder() {
        if(jTable1.getSelectedRow() != -1) {
            String selectedRowPath = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2); //Pfad der ausgewählten Datei
            CreateAndEditReminderGUI eef = new CreateAndEditReminderGUI(this, selectedRowId, selectedRowPath); //ID der TopicGroupLOGIC und Pfad der Datei
        } else {
            errorChooseFileFirst();
        }
    }
    
    /**
     * Methode veranlasst das Bearbeiten einer ausgewählten Erinnerung
     */
    public void editReminder() {
        if(jTable2.getSelectedRow() != -1) {
            new CreateAndEditReminderGUI(this, (long) jTable2.getValueAt(jTable2.getSelectedRow(),0));
        } else {
            new NotifyFrameGUI("Fehler", "Es wurde kein Datensatz aus der Tabelle ausgewählt.");
        }
    }
    
    /**
     * Methode toggled den Erledigt-Status einer Erinnerung
     */
    public void toggleDoneState(){
        if(jTable2.getSelectedRow() != -1) {
            if(rDB.toggleDoneState((long) jTable2.getValueAt(jTable2.getSelectedRow(),0))) {
                refreshView();
                return;
            } else {
                new NotifyFrameGUI("Fehler", "Der Erledigt-Status konnte nicht bearbeitet werden. Bitte Ansicht aktualisieren.");
            }
        } else {
            new NotifyFrameGUI("Fehler", "Bitte wählen Sie einen Datensatz aus der rechten Tabelle aus.");
        }
    }

    /**
     * Methode lädt die Notiz der ausgewählten Datei und zeigt diese an
     */
    public void loadNote(){
        String test = (String) jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2);
        long themengruppenID = tgLOGIC.getId();
        String ausgabe = nDB.loadNote(test, themengruppenID); //Notiz wird immer erstellt, auch wenn die Datei nur ausgewählt wird
        jTextArea1.setText(ausgabe);
        setCharCountNote(jTextArea1.getText().length());
        enableTextField(); //auch wenn keine NoteDB enthalten ist, muss das Textfeld entsperrt werden, um eine neue zu erstellen
    }

    /**
     * Methode speichert den Inhalt des TextFelds, Also den Inhalt der Motiz, in der DB
     */
    public void saveNote(){
        toggleEditableTable(false);
        String notizText = jTextArea1.getText();
        String pfad = (String) jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 2);
        if(!nDB.saveNote(notizText, this.selectedRowId ,pfad)){
            new NotifyFrameGUI("Fehler", "Fehler beim Speichern der Notiz.");
        }
        toggleEditableTable(true);
    }
    
    /**
     * Methode leert und sperrt das Notiz-Textfeldt
     */
    public void emptyAndDisableTextField(){
        jTextArea1.setText("");
        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new Color(244,247,252));
        setCharCountNote(0);
    }
    
    /**
     * Methode entsperrt das Notiz-Textfeldt
     */
    public void enableTextField() {
        // Text aus DB laden
        jTextArea1.setEditable(true);
        jTextArea1.setBackground(new Color(255,255,255));
    }
    
    /**
     * Methode sperrt und entsperrt das Anklicken der Tabelle entsprechned des 
     * übergebenen Wahrheitswertes
     * 
     * @param b - wahrheitswert
     */
    public void toggleEditableTable(Boolean b) {
        jTable1.setRowSelectionAllowed(b);
    }

    /**
     * Methode wird aufgerufen, wenn "Löschen"-Butten angeklickt wird.
     * Sie schaut, ob eine Datei oder eine Erinnerung ausgewählt ist.
     * Entsprechend der Auswahl veranlasst sie das weitere Vorgehen zum Löschen
     * der Datei oder der Erinnerung
     */
    public void deleteFileOrReminder() {
        if(jTable1.getSelectedRow() != -1 && jTable2.getSelectedRow() != -1) { //Erinnerung löschen, wenn beides ausgewählt ist
            int hoehe = jTable2.getRowHeight() - jTable2.getRowHeight()/10;
            long erinnerungenID = (long) jTable2.getValueAt(jTable2.getSelectedRow(),0);
            rDB.deleteReminder(erinnerungenID);
            rDB.loadReminders(selectedRowId, hoehe,-1);
            refreshView();
            return;
        } else if (jTable1.getSelectedRow() != -1) { //Datei löschen
            String selectedRowPath = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2); //Pfad der ausgewählten Datei
            deleteFile(selectedRowPath);
            emptyAndDisableTextField();
            refreshView();
            return;
        } else {
            new NotifyFrameGUI("Fehler", "wählen Sie bitte etwas zum Löschen aus.");
        }
    }

    /**
     * Methode veranlasst das Löschen der ausgewählten Datei
     * 
     * @param selectedRowPath - Dateipfad des ausgewählten Files
     */
    public void deleteFile(String selectedRowPath){
        new DeleteFileGUI(this, this.tgLOGIC, this.nDB, this.rDB, selectedRowPath);
    }
    
    /**
     * Methode öffnet einen Filechooser, welcher dem User die Möglichkeit bietet,
     * eine Datei oder ein Verzeichnis auszuwählen.
     * Dieses wird dann in das aktuell angezeigte Verzeichnis der Themengruppe verschoben
     */
    public void addFileFromOs() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
        fc.setDialogTitle("Datei/Verzeichnis hinzufügen - DATEGT");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);
        int response = fc.showOpenDialog(this);
        if(response == JFileChooser.APPROVE_OPTION) {
            // Datei/Verzeichnis verschieben
            if(!tgLOGIC.addFile(fc.getSelectedFile())) {
                new NotifyFrameGUI("Fehler", "Die Datei konnte nicht in die Themengruppe verschoben werden. Eventluell existiert der Dateiname bereits.");
                return;
            }
            refreshView();
            return;
        } else {
            System.out.println("The operation was cancelled.");
        }
    }

    /**
     * Methode löscht die Referenz zum SearchFileGUI-Frame
     */
    public void resetReferenceToSearchFileGUI() {
        this.sfGUI = null;
    }
    
    /**
     * Methode markiert die ihr übergebene Zeile in der Dokumenten-Tabelle
     * 
     * @param row - Zeilennummer, die zu markieren ist
     */
    private void highlightRowInTable1(int row){
        jTable1.setRowSelectionInterval(row,row);
    }
    
    /**
     * Fehlermeldung: Bitte Datei asuwählen
     */
    public void errorChooseFileFirst(){
        new NotifyFrameGUI("Fehler", "Bitte wählen Sie eine Datei aus der linken Tabelle aus.");
    }
    
    // Getter und Setter
    
    /**
     * Methode zeigt einen Counter oberhalt des Textfelds der Notizen an,
     * damit der Nutzer weiß, wieviele Zeichen noch eingegeben werdne dürfen
     * 
     * @param i - Anzahl der bereits vorhandenen Zeichen
     */
    public void setCharCountNote(int i) {
        jLabel4.setText(i + " / " + NOTE_MAX_LENGTH);
    }
    
    /**
     * Methode gibt die Zeilennummer des an ihr übergebenen Pfads zurück
     * 
     * @param pfad
     * @return 
     */
    private int getRowOfPath(String pfad){
        int i = 0;
        while (i <= jTable1.getRowCount()){
            if (pfad.equals(jTable1.getValueAt(i,2))) {
                return i;
            }
            i++;
        }
        return -1;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        textField1 = new java.awt.TextField();
        jSeparator2 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButton14 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Themengruppe");
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("0000 - Themengruppe");

        jTextField3.setEditable(false);
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Pfad:");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setText("jTextField2");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/left-arrow.png"))); // NOI18N
        jButton8.setText("zurück");
        jButton8.setToolTipText("zurück");
        jButton8.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Symbol", "Titel", "Pfad", "Änderungsdatum", "Größe"
            }
        ) {

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0: return ImageIcon.class;
                    default: return String.class;
                }
            }

            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTable1MouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        textField1.setEditable(false);
        textField1.setText("textField1");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/trash.png"))); // NOI18N
        jButton3.setToolTipText("löschen");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/refresh.png"))); // NOI18N
        jButton9.setToolTipText("aktualisieren");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/root-directory.png"))); // NOI18N
        jButton4.setToolTipText("Datei erstellen");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/hourglass.png"))); // NOI18N
        jButton6.setToolTipText("Erinnerung erstellen");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMaximumSize(null);

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable2.setRowHeight(27);
        jTable2.setRowMargin(0);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable2MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jLabel2.setText("Erinnerungen:");

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/edit-folder.png"))); // NOI18N
        jButton13.setToolTipText("bearbeiten");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/to-do-list.png"))); // NOI18N
        jButton2.setToolTipText("erledigt/offen setzen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/rename.png"))); // NOI18N
        jButton7.setToolTipText("umbenennen");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/add.png"))); // NOI18N
        jButton10.setToolTipText("Verzeichnis erstellen");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setMaximumSize(null);

        jLabel3.setText("Notizen:");

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextArea1MouseClicked(evt);
            }
        });
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextArea1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(jTextArea1);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/search.png"))); // NOI18N
        jButton12.setToolTipText("Datei Suchen");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/dokuverwproject/IMG/movement.png"))); // NOI18N
        jButton14.setToolTipText("Datei/Verzeichnis hinzufügen");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                                .addGap(15, 15, 15))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jTextField2)
                                .addGap(0, 0, 0)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(13, 13, 13)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(textField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator3)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("Fenster");

        jMenuItem1.setText("schließen");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        tgLOGIC.goBackNAV();
    }//GEN-LAST:event_jButton8ActionPerformed
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        emptyAndDisableTextField();
        if (evt.getClickCount() == 2) {
            tgLOGIC.openSelectedFile(); // Datei oder Verzeichnis öffnen
        }
        if (jTable1.getSelectedRow() != -1){
            loadNote(); // Methode entsperrt nach Laden TextFeld wieder
        }
    }//GEN-LAST:event_jTable1MouseClicked
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
    }//GEN-LAST:event_jTextField3ActionPerformed
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteFileOrReminder();
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        refreshView();
    }//GEN-LAST:event_jButton9ActionPerformed
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            tgLOGIC.openSelectedFile();
        } else {
            errorChooseFileFirst();
        }
    }//GEN-LAST:event_jButton5ActionPerformed
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        new CreateFileGUI(jTextField2.getText(), this, tgLOGIC); //TextField2 = aktuelles Verzeichnis
    }//GEN-LAST:event_jButton4ActionPerformed
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        createReminder();
    }//GEN-LAST:event_jButton6ActionPerformed
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
    }//GEN-LAST:event_jTable2MouseClicked
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        if(jTable1.getSelectedRow() != -1) {
            String selectedRowPath = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2); //Pfad der ausgewählten Datei
            RenameFileGUI duf = new RenameFileGUI(selectedRowPath, this, tgLOGIC);
        } else {
            errorChooseFileFirst();
        }
    }//GEN-LAST:event_jButton7ActionPerformed
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new CreateDirGUI(jTextField2.getText(), this, tgLOGIC); //TextField2 = aktuelles Verzeichnis
    }//GEN-LAST:event_jButton10ActionPerformed
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        if(sfGUI == null) sfGUI = new SearchFileGUI(this, tgLOGIC);
    }//GEN-LAST:event_jButton12ActionPerformed
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        editReminder();
    }//GEN-LAST:event_jButton13ActionPerformed
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        toggleDoneState();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        jTable2.clearSelection();
    }//GEN-LAST:event_jTable1MouseReleased
    private void jTable2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseReleased
        jTable1.clearSelection();
        long erID = 0;
        erID = (long) jTable2.getValueAt(jTable2.getSelectedRow(),0);
        String pfad = rDB.loadText(erID,"pfad");
        int markierendeZeile = getRowOfPath(pfad);
        if (markierendeZeile != -1){ highlightRowInTable1(markierendeZeile);loadNote();}
    }//GEN-LAST:event_jTable2MouseReleased
    private void jTable1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseEntered
    }//GEN-LAST:event_jTable1MouseEntered
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        addFileFromOs();
    }//GEN-LAST:event_jButton14ActionPerformed
    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
    }//GEN-LAST:event_jTextArea1MouseClicked
    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyPressed
    }//GEN-LAST:event_jTextArea1KeyPressed
    private void jTextArea1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyReleased
        int length = jTextArea1.getText().length();
        setCharCountNote(length);
        
        if(length <= NOTE_MAX_LENGTH) {
            saveNote();
        } else {
            new NotifyFrameGUI("Fehler", "Die Notiz ist zu lang. Bitte beachten Sie die Anzeige der maximal erlaubten Zeichen."
                    + "\nIhre Änderungen wurdne nicht gespeichert!");
        }
    }//GEN-LAST:event_jTextArea1KeyReleased
    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
    }//GEN-LAST:event_jTextArea1KeyTyped
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private java.awt.TextField textField1;
    // End of variables declaration//GEN-END:variables
}
