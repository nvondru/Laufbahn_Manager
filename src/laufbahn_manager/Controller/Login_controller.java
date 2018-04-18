/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import laufbahn_manager.Laufbahn_Manager;
import laufbahn_manager.View.Login_view;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author vmadmin
 */
public class Login_controller {
    
    private Stage mainStage;
    
    private Overview_controller overview_controller;
    private Login_view login_view;   
    private Scene scene;
    private DB_connector initialConnector;
    private Laufbahn_Manager master;
    
    
    public Login_controller(Laufbahn_Manager master){
        this.master = master;
        login_view = new Login_view();
        scene = new Scene(login_view.getRoot());
        
        initActionHandlers();

        
    }
    
    public void setSceneToStage(Stage mainStage){
        this.mainStage = mainStage;
        this.mainStage.setScene(scene);
    }

    private void initActionHandlers() {
        
        addBtnSubmitAction();
        
    }
    private void addBtnSubmitAction() {
        login_view.getRoot().setOnKeyPressed((event) -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                try {
                
                System.out.println(DigestUtils.sha1Hex(login_view.getPfPassword().getText()));
                if(checkLoginCredentials(login_view.getTfUsername().getText(), DigestUtils.sha1Hex(login_view.getPfPassword().getText()))){
                    
                
                    System.out.println("We are logged in!");
                    overview_controller = new Overview_controller(master);
                    overview_controller.setSceneToStage(mainStage);
                }                
                else
                {
                    System.out.println("We're not logged in!");
                    login_view.getTfUsername().getStyleClass().add("wrong");
                    login_view.getPfPassword().getStyleClass().add("wrong");
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Login_controller.class.getName()).log(Level.SEVERE, null, ex);
            } 
            initialConnector.closeConnection();
            }
        });
        login_view.getBtnSubmit().setOnAction(event -> {
                        
            try {
                
                System.out.println(DigestUtils.sha1Hex(login_view.getPfPassword().getText()));
                if(checkLoginCredentials(login_view.getTfUsername().getText(), DigestUtils.sha1Hex(login_view.getPfPassword().getText()))){
                    
                
                    System.out.println("We are logged in!");
                    overview_controller = new Overview_controller(master);
                    overview_controller.setSceneToStage(mainStage);
                }                
                else
                {
                    System.out.println("We're not logged in!");
                    login_view.getTfUsername().getStyleClass().add("wrong");
                    login_view.getPfPassword().getStyleClass().add("wrong");
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Login_controller.class.getName()).log(Level.SEVERE, null, ex);
            } 
            initialConnector.closeConnection();
            
        });
    }

    private boolean checkLoginCredentials(String username, String password) throws ClassNotFoundException, SQLException {
        boolean validation = false;
        
        initialConnector = new DB_connector();
        
        ResultSet resultSet = initialConnector.getCredentials();
        
        while(resultSet.next() && validation == false){
            if(resultSet.getString("password").equals(password) && resultSet.getString("username").equals(username)){
                validation = true;
            }
        }        
        return validation;
    }

    
    
    
    
    
}
