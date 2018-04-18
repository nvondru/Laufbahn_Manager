//Author: Nicolas Vondru
//Version: 1.6
//Date: 9.4.2018

//Tool zur Vwerwaltung von Notizen während eines Gesprächs zur Laufbahnberatung 
//Es werden persönliche Daten wie Namen, Vornamen, Personalnummern, Berufsgruppe erfasst
//Zusätzlich können Notizen zu einem Gespräch erfasst, und mit PDF-Dateien ergänzt werden

// Speziell benötigt werden:
// MySQL-Datenbank (auf localhost, zum Speichern jeglicher Daten und Pfade für Dateien)
//      PDF-Dateien werden lokal in einem Ordner abgepeichert (muss manuell erstellt werden)
// CommonsCodec (für Hashen von Passwörtern)

package laufbahn_manager;

import java.sql.SQLException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.security.auth.login.LoginContext;
import laufbahn_manager.Controller.DB_connector;
import laufbahn_manager.Controller.Login_controller;
import org.apache.commons.codec.digest.DigestUtils;


public class Laufbahn_Manager extends Application {
    
    
    private Login_controller login_controller;
    
    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
        
         
        login_controller = new Login_controller(this);
        login_controller.setSceneToStage(primaryStage);
        
     
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Close Request");
        });
        primaryStage.setTitle("Laufbahn Besprechungs-Tool"); 
       
        primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
