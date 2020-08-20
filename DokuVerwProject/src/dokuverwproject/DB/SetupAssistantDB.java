/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dokuverwproject.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Giuseppe
 */
public class SetupAssistantDB {
    // Datenverbindung
    private String db_host = "";
    private String db_port = "";
    private String db_name = "";
    private String db_username = "";
    private String db_password = "";
    DBConn db = null;
    // DB-Tables
    private String[][] tables = { //2d Array mit Tabellennamen und SQL-Code, um sie einzurichten
        {"erinnerungen", "CREATE TABLE `erinnerungen` (`id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,`titel` text NOT NULL,`inhalt` text NOT NULL,`faellig` date NOT NULL,`erledigt` tinyint(1) NOT NULL,`themengruppenID` int(11) NOT NULL,`dateiPfad` text NOT NULL,`created_TMSTMP` timestamp NOT NULL DEFAULT current_timestamp()) AUTO_INCREMENT = 30001 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"},
        {"notizen", "CREATE TABLE `notizen` (`id` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY ,`inhalt` text NOT NULL,`dateiPfad` text NOT NULL,`themengruppenID` bigint(11) NOT NULL,`created_TMSTMP` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()) AUTO_INCREMENT = 60001 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"},
        {"nutzer", "CREATE TABLE `nutzer` (`id` bigint(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,`username` text NOT NULL,`passwort` text NOT NULL,`name` text NOT NULL,`vorname` text NOT NULL,`created_TMSTMP` timestamp NOT NULL DEFAULT current_timestamp()) AUTO_INCREMENT = 80001 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"},
        {"themengruppen", "CREATE TABLE `themengruppen` (`id` bigint(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,`titel` text NOT NULL,`pfad` text NOT NULL,`created_TMSTMP` timestamp NOT NULL DEFAULT current_timestamp()) AUTO_INCREMENT = 10001 ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"}
    };

    /**
     * Konstruktor legt eine DB-Verbindung an
     * 
     * @param db_host
     * @param db_port
     * @param db_name
     * @param db_username
     * @param db_password 
     */
    public SetupAssistantDB(String db_host, String db_port, String db_name, String db_username, String db_password) {
        this.db_host = db_host;
        this.db_port = db_port;
        this.db_name = db_name;
        this.db_username = db_username;
        this.db_password = db_password;
        db = new DBConn(db_host, db_port, db_name, db_username, db_password);
    }
    
    /**
     * Methode prüft die mit den ihr übergebenen Credentials übereinstimmende DB.
     * Ist diese noch nicht vorhanden, dann wird sie angelegt.
     * 
     * @return  0 = erfolgreich erstellt/geprüft
     *          1 = Fehler beim Erstellen/Prüfen der DB
     *          2 = Fehler beim Verbinden mit der DB
     */
    public int testDBConnection() {
        if(!db.createDBIfNotExists()) return 1;
        
        Connection con = db.getConnection();
        if(con == null) return 2;
        
        return 0;
    }
    
    /**
     * Methode prüft in der DB, ob die benötigten Tabellen vorhanden sind.
     * 
     * @return true = DB Struktur in Ordnung; false = DB Struktur fehlerhaft
     */
    public boolean checkDBStructure() {
        int foundTablesInDB = 0; // Anzahl der in der DB gefundenen Tabellen
        
        Connection con = db.getConnection();
        Statement stmt = null;
        
        try{
            DatabaseMetaData dbm = con.getMetaData();
            
            for (int i = 0; i < tables.length; i++) {
                ResultSet table = dbm.getTables(null, null, tables[i][0], null);
                if (table.next()) { //if table exists
                    foundTablesInDB ++;
                }
            }
            
        } catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
        
        if(foundTablesInDB == tables.length) return true;
        return false;
    }
    
    /**
     * Methode prüft die DB Struktur und legt nicht vorhandene Tabellen an
     * 
     * @return true = DB Struktur in Ordnung/ertsellt; false = DB Struktur fehlerhaft/nicht erstellbar
     */
    public boolean createDBStructure() {
        Connection con = db.getConnection();
        Statement stmt = null;
        
        try{
            DatabaseMetaData dbm = con.getMetaData();
            
            for (int i = 0; i < tables.length; i++) {
                ResultSet tableAccounts = dbm.getTables(null, null, tables[i][0], null);
                if (!tableAccounts.next()) { //if table does not exist
                    try{
                        stmt = con.createStatement();
                        stmt.executeUpdate(tables[i][1]); //Tabelle erstellen
                        stmt.close();
                    } catch(Exception e){
                        System.out.println(e.toString());
                        return false;
                    }
                }
            }
            return true;
        } catch(Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    /**
     * Methode erstellt einen User mit den ihr übergebenen Credentials
     * 
     * @param usr_username
     * @param usr_password
     * @param user_lastname
     * @param user_prename
     * 
     * @return  0 = erfolgreicht erstellt
     *          1 = Benutzername bereits vorhanden
     *          2 = Fehler
     */
    public int createRootUser(String usr_username, String usr_password, String user_lastname, String user_prename) {
        try{
            Connection con = db.getConnection();

            PreparedStatement ps = null;
            String queryCheckAccount = "SELECT * FROM `nutzer` WHERE `username` = ?";
            ps = con.prepareStatement(queryCheckAccount);
            ps.setString(1, usr_username);
            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()){ // keine Einträge mit dem selben Benutzernamen
                ps = null;
                String addUserQuery = "INSERT INTO `nutzer` (`id`, `username`, `passwort`, `name`, `vorname`, `created_TMSTMP`) VALUES (NULL, ?, ?, ?, ?, CURRENT_TIMESTAMP);";

                ps = con.prepareStatement(addUserQuery);
                ps.setString(1, usr_username);
                ps.setString(2, usr_password);
                ps.setString(3, user_lastname);
                ps.setString(4, user_prename);

                ps.execute();
                ps.close();
                return 0;

            }
            ps.close();
            return 1;
        } catch(Exception e) {
            System.out.println(e.toString());
            return 2;
        }
    }
    
}
