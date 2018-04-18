/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import laufbahn_manager.Laufbahn_Manager;
import laufbahn_manager.Model.Person;
import laufbahn_manager.View.Overview_view;

/**
 *
 * @author vmadmin
 */
public class Overview_controller {
    private Laufbahn_Manager master;
    private Path rootDir = Paths.get("C:\\Users\\vmadmin\\Documents\\Laufbahn_Manager");
    private Path personsDir;
    
    private Detail_controller detail_controller;
    private Overview_view overview_view;
    private Scene scene;
    private Stage mainStage;
    private ObservableList<Person> listPersons = FXCollections.observableArrayList();
    private DB_connector mainConnector;

    public Overview_controller(Laufbahn_Manager master) throws ClassNotFoundException, SQLException{
        this.master = master;
        overview_view = new Overview_view(this, master);
        scene = new Scene(overview_view.getRoot());
        initActionHandlers();
        overview_view.getPersonsTable().setItems(listPersons);
        updatePersonsTable();
        TableView personsTable = overview_view.getPersonsTable();
        
        personsTable.setFixedCellSize(60);
        personsTable.prefHeightProperty().bind(personsTable.fixedCellSizeProperty().multiply(Bindings.size(personsTable.getItems()).add(0.56)));
        
    }
    
    // Methods
    public void setSceneToStage(Stage mainStage){
        this.mainStage = mainStage;
        System.out.println(this.mainStage);
        this.mainStage.setScene(scene);
//        this.mainStage.setX(Screen.getPrimary().getVisualBounds().getMinX());
//        this.mainStage.setY(Screen.getPrimary().getVisualBounds().getMinY());
//        this.mainStage.setFullScreen(true);
        this.mainStage.setMaximized(true);

    }

    private void initActionHandlers() {
        addBtnAddHandler();
        addSearchFieldHandler();
    }

    private void addBtnAddHandler() {
        overview_view.getBtnAdd().setOnAction(event -> {
            // Textfelder werden auf Vollständigkeit überprüft
            if(checkTextFields()){
                String name = overview_view.getTfName().getText();
                String vorname = overview_view.getTfVorname().getText();
                String beruf = overview_view.getTfBeruf().getText();
                String abteilung = overview_view.getTfAbteilung().getText();
                String personalnummer = overview_view.getTfPersonalnummer().getText();
                
                
                // Ordner für die neue person wird in 'Documents\Laufbahnmanager' erstellt
                personsDir = Paths.get(rootDir.toString() + "\\" + name + "_" + vorname + "_" + personalnummer);
                System.out.println(personsDir);
                try {
                    Files.createDirectory(personsDir);
                } catch (IOException ex) {
                    Logger.getLogger(Overview_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //Textfelder werden geleert
                for(Node node : overview_view.getBoxCreateNewPerson().getChildren()){
                    if(node instanceof TextField){
                        TextField field = (TextField)node;
                        field.clear();
                    }
                }
                try {
                    // Person wird erstellt und in DB gespeichert
                    mainConnector = new DB_connector();
                    mainConnector.insertPerson(name, vorname, beruf, abteilung, personalnummer);
                    mainConnector.insertLaufbahn(mainConnector.getIDofPerson(name, vorname, personalnummer));
                    mainConnector.closeConnection(); 
                    
                    // PersonenTable wird geupdated
                    updatePersonsTable();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Overview_controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Overview_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                
                
            }
        });
    }
    private void addSearchFieldHandler() {
        overview_view.getSearchField().setOnKeyReleased(event -> {
            
            String[] list = overview_view.getSearchField().getText().split(" ");
        for(int x = 0; x < list.length; x++){
            System.out.println(list[x]);
        }
            System.out.println("--");
            
            try {
                mainConnector = new DB_connector();
                ResultSet set = mainConnector.getPersons(overview_view.getSearchField().getText());
                System.out.println(overview_view.getSearchField().getText());
                listPersons.clear();
                while(set.next()){
                    String name = set.getString("name");
                    String vorname = set.getString("vorname");
                    String beruf = set.getString("beruf");
                    String abteilung = set.getString("abteilung");
                    String personalnummer = set.getString("personalnummer");
                    String id = String.valueOf(set.getInt("id"));
                    Person persTemp = new Person(name, vorname, beruf, abteilung, personalnummer, id);
                    listPersons.add(persTemp);
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Overview_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Overview_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }

    private boolean checkTextFields() {
        boolean allFilled = true;
        
        if(overview_view.getTfName().getText().isEmpty()){
            allFilled = false;
        }
        else if(overview_view.getTfVorname().getText().isEmpty())
        {
            allFilled = false;
        }
        else if(overview_view.getTfBeruf().getText().isEmpty()){
            allFilled = false;
        }
        else if(overview_view.getTfAbteilung().getText().isEmpty()){
            allFilled = false;
        }
        else if(overview_view.getTfPersonalnummer().getText().isEmpty()){
            allFilled = false;
        }
        return allFilled;
        
    }

    private void updatePersonsTable() throws ClassNotFoundException, SQLException {
        // Vorheriger Content wird gelöscht
        listPersons.clear();
        
        // aktuelle Personen werden aus DB abgefragt
        mainConnector = new DB_connector();        
        ResultSet set = mainConnector.getPersons();
        mainConnector.closeConnection();
        
        
        while(set.next()){
            String name = set.getString("name");
            String vorname = set.getString("vorname");
            String beruf = set.getString("beruf");
            String abteilung = set.getString("abteilung");
            String personalnummer = set.getString("personalnummer");
            String id = String.valueOf(set.getInt("id"));
            Person persTemp = new Person(name, vorname, beruf, abteilung, personalnummer, id);
            listPersons.add(persTemp);
        }
    }
    
    //Getter / Setter
    public void setDetail_controller(Detail_controller detail_controller){
        this.detail_controller = detail_controller;
    }
    public Detail_controller getDetail_controller(){
        return detail_controller;
    }

    public Stage getMainStage() {
        return this.mainStage;
    }
    public Laufbahn_Manager getMaster(){
        return master;
    }

    
    
}
