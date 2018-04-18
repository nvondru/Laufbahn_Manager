/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Controller;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import laufbahn_manager.Model.Besprechung;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author nicolasvondru
 */
public class DB_connector {
    private String jdbc_driver = "com.mysql.jdbc.Driver", db_url_loginLaufbahn = "jdbc:mysql://localhost/loginLaufbahn";
    
    private Connection connection;
    
    public DB_connector() throws ClassNotFoundException, SQLException{
        Class.forName(jdbc_driver);
        connection = DriverManager.getConnection(db_url_loginLaufbahn, "observer", DigestUtils.sha1Hex("obs_initPW"));
        
        System.out.println("Connection to LoginDB established!");
        
    }
   
    
    public void closeConnection(){
        if(connection != null){
            connection = null;
            System.out.println("Connection closed succesfully!");
        }
        else
        {
            System.out.println("No connection to close");
        }
    }
    
    // Inserts
    public void insertPerson(String name, String vorname, String beruf, String abteilung, String personalnummer) throws SQLException{
        PreparedStatement prepStatement = connection.prepareStatement("insert into person(name, vorname, beruf, abteilung, personalnummer) values (?,?,?,?,?);");
        prepStatement.setString(1, name);
        prepStatement.setString(2, vorname);
        prepStatement.setString(3, beruf);
        prepStatement.setString(4, abteilung);
        prepStatement.setString(5, personalnummer);
        
        prepStatement.execute();    
    }
    public void insertLaufbahn(int idOfPerson) throws SQLException{
        PreparedStatement prepStatement = connection.prepareStatement("insert into laufbahn(fk_id_person) values (?)");
        prepStatement.setInt(1, idOfPerson);
        
        prepStatement.execute();
    }
    public void insertBesprechung(int idOfPerson) throws SQLException{
        PreparedStatement prepStatement = connection.prepareStatement("insert into besprechung(datum, fk_id_person) values (?,?);");
        Calendar cal = Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        System.out.println(utilDate);
        Date sqlDate = new Date(utilDate.getTime());
        System.out.println(sqlDate);
        prepStatement.setDate(1, sqlDate);
        prepStatement.setInt(2, idOfPerson);
        
        prepStatement.execute();
    }
    public void insertDocument(File newFile, Besprechung besp) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into document(path, fk_id_besprechung) values (?,?);");
        preparedStatement.setString(1, newFile.toString());
        preparedStatement.setInt(2, besp.getId());
        preparedStatement.execute();
    }
    // Get Data
    public ResultSet getPersons() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from person order by name");
        ResultSet set = preparedStatement.executeQuery();
        return set;
    }    
    public ResultSet getPersons(String text) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from person where (name like ? or vorname like ? or beruf like ? or abteilung like ? or personalnummer like ?) "
                + "                                                         and (name like ? or vorname like ? or beruf like ? or abteilung like ? or personalnummer like ?) "
                + "                                                         and (name like ? or vorname like ? or beruf like ? or abteilung like ? or personalnummer like ?) "
                + "                                                         and (name like ? or vorname like ? or beruf like ? or abteilung like ? or personalnummer like ?)"
                + "                                                         and (name like ? or vorname like ? or beruf like ? or abteilung like ? or personalnummer like ?)"
                + "                                                         order by name;");
        String[] list = text.split(" ");
        ArrayList<String> filteredList = new ArrayList();
        
        
        //Gesplittete Einträge aus Array werden in ArrayList übernommen
        for(int x = 0; x < list.length; x++){
            filteredList.add(list[x]);
        }
        
        // Fehlende Einträge werden bis Index 4 mit "" ergänzt
        for(int x = 0; x < (5 - list.length); x ++){
            filteredList.add("");
        }        
        // Jeder Eintrag wird mit % ergänzt
        for(String x : filteredList){
            filteredList.set(filteredList.indexOf(x), x += "%");
        }
        
        
        preparedStatement.setString(1, filteredList.get(0));
        preparedStatement.setString(2, filteredList.get(0));
        preparedStatement.setString(3, filteredList.get(0));
        preparedStatement.setString(4, filteredList.get(0));
        preparedStatement.setString(5, filteredList.get(0));
        
        preparedStatement.setString(6, filteredList.get(1));
        preparedStatement.setString(7, filteredList.get(1));
        preparedStatement.setString(8,filteredList.get(1));
        preparedStatement.setString(9, filteredList.get(1));
        preparedStatement.setString(10, filteredList.get(1));
        
        preparedStatement.setString(11, filteredList.get(2));
        preparedStatement.setString(12, filteredList.get(2));
        preparedStatement.setString(13, filteredList.get(2));
        preparedStatement.setString(14, filteredList.get(2));
        preparedStatement.setString(15, filteredList.get(2));
        
        preparedStatement.setString(16, filteredList.get(3));
        preparedStatement.setString(17, filteredList.get(3));
        preparedStatement.setString(18, filteredList.get(3));
        preparedStatement.setString(19, filteredList.get(3));
        preparedStatement.setString(20, filteredList.get(3));
        
        preparedStatement.setString(21, filteredList.get(4));
        preparedStatement.setString(22, filteredList.get(4));
        preparedStatement.setString(23, filteredList.get(4));
        preparedStatement.setString(24, filteredList.get(4));
        preparedStatement.setString(25, filteredList.get(4));
        
        ResultSet set = preparedStatement.executeQuery();
        return set;
    }
    public int getIDofPerson(String name, String vorname, String personalnummer) throws SQLException{
        PreparedStatement prepStatement = connection.prepareStatement("select id from person where name = ? and vorname = ? and personalnummer = ?;");
        prepStatement.setString(1, name);
        prepStatement.setString(2, vorname);
        prepStatement.setString(3, personalnummer);
        ResultSet set = prepStatement.executeQuery();
        int id = 0;
        while (set.next()){
            id = set.getInt("id");
        }
        return id;
    
    }
    public ResultSet getLaufbahnOfPerson(int idOfPerson) throws SQLException{
        PreparedStatement prepStatement = connection.prepareStatement("select * from laufbahn where fk_id_person = ?;");
        prepStatement.setInt(1, idOfPerson);
        
        ResultSet set = prepStatement.executeQuery();
        return set;
    }
    public ResultSet getCredentials() throws SQLException {
        PreparedStatement prepStatement = connection.prepareStatement("select * from Credentials ");
        
        ResultSet set = prepStatement.executeQuery();
        
        return set;        
    }    
    public ResultSet getBesprechungenFromPerson(String id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from besprechung where fk_id_person = ? order by datum;");
        preparedStatement.setInt(1, Integer.valueOf(id));
        
        ResultSet set = preparedStatement.executeQuery();
        
        return set;
    }
    public ArrayList<String> getDocumentsOfBesprechung(Besprechung besp) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from document where fk_id_besprechung = ? order by id");
        preparedStatement.setInt(1, besp.getId());
        ResultSet set = preparedStatement.executeQuery();
        ArrayList<String> listPaths = new ArrayList<>();
        while(set.next()){
            listPaths.add(set.getString("path"));
        }
        return listPaths;
    }

    // Update Data
    public void updateLaufbahn(String schule_bez, String schule_comment, String lehre_bez, String lehre_comment, String skills_bez, String skills_comment, boolean ausland_checked, String ausland_comment, boolean weiterbildung_checked, String weiterbildung_comment, int id_pers) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("update laufbahn set schule_bez = ?, schule_comment = ?, lehre_bez = ? , lehre_comment = ?, skills_bez = ?, skills_comment = ?, ausland_checked = ?, ausland_comment = ?, weiterbildung_checked = ?, weiterbildung_comment = ? where fk_id_person = ?;");
    
        preparedStatement.setString(1, schule_bez);
        preparedStatement.setString(2, schule_comment);
        preparedStatement.setString(3, lehre_bez);
        preparedStatement.setString(4, lehre_comment);
        preparedStatement.setString(5, skills_bez);
        preparedStatement.setString(6, skills_comment);
        preparedStatement.setBoolean(7, ausland_checked);
        preparedStatement.setString(8, ausland_comment);
        preparedStatement.setBoolean(9, weiterbildung_checked);
        preparedStatement.setString(10, weiterbildung_comment);
        preparedStatement.setInt(11, id_pers);
        
        preparedStatement.execute();
    }
    
    
    public void updateBesprechung(Besprechung besp) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("update besprechung set title = ?, kommentar = ? where id = ?");
        
        preparedStatement.setString(1, besp.getTitle());
        preparedStatement.setString(2, besp.getKommentar());
        preparedStatement.setInt(3, besp.getId());
        
        preparedStatement.execute();
    }
    
    //Delete from DB
    public void deleteBesprechung(int id) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("delete from besprechung where id = ?");
        
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }

    


    

    
    
    
    
}
