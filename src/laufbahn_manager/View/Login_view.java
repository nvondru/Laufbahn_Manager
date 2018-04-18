/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 *
 * @author vmadmin
 */
public class Login_view {
    private VBox root;
    
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private static double SCREEN_WIDTH, SCREEN_HEIGHT;
    
    private Label title;
    private TextField tfUsername;
    private PasswordField pfPassword;
    private Button btnSubmit;
    
    private Separator sep;
    public Login_view(){
        SCREEN_WIDTH = primaryScreenBounds.getMaxX();
        SCREEN_HEIGHT = primaryScreenBounds.getMaxY();
        
        root = new VBox();
        root.setPrefSize(SCREEN_WIDTH / 3, SCREEN_HEIGHT / 4);
        root.setAlignment(Pos.CENTER);
        root.getStylesheets().add(getClass().getResource("style.css").toString());
        
        root.setPadding(new Insets(50));
        root.setSpacing(15);
        
        title = new Label("Laufbahn Manager");
        title.getStyleClass().add("loginTitle");
        
        sep = new Separator();
        
        tfUsername = new TextField();
        tfUsername.setPromptText("Benutzername");
        tfUsername.getStyleClass().add("tfUsername");
        
        pfPassword = new PasswordField();
        pfPassword.setPromptText("Passwort");
        pfPassword.getStyleClass().add("pfPassword");
        
        btnSubmit = new Button("Login");
        btnSubmit.getStyleClass().add("btnSubmit");
        
        // Layout
        root.getChildren().addAll(title, sep, tfUsername, pfPassword, btnSubmit);
        
        
    }
    
    public VBox getRoot(){
        return root;
    }

    public TextField getTfUsername() {
        return tfUsername;
    }

    public PasswordField getPfPassword() {
        return pfPassword;
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }
    
    
}
