/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.HostServices;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import laufbahn_manager.Laufbahn_Manager;
import laufbahn_manager.Model.Besprechung;
import laufbahn_manager.Model.Person;
import laufbahn_manager.View.Detail_view;

/**
 *
 * @author vmadmin
 */
public class Detail_controller {
    private Overview_controller overview_controller;
    private Laufbahn_Manager master;
    
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private Stage mainStage;
    private Detail_view detail_view;
    private Scene scene;
    private DB_connector mainConnector;
    private Person actPers;
    private ArrayList<Besprechung> listBesprechungen = new ArrayList<>();
    private Popup popUpStage;
    private final double SCREEN_WIDTH, SCREEN_HEIGHT;
    
    // Document
    private boolean saved = true;
    private HBox boxDocuments;
    private Path rootDir = Paths.get("C:\\Users\\Jens Helfenstein\\Documents\\Laufbahn_Manager");
    private Path personsDir;
    private File selectedFile, newFile;
    private FileChooser chooser = new FileChooser();
    private HostServices services;
    
    public Detail_controller(Person actPers, Laufbahn_Manager master) throws ClassNotFoundException, SQLException{
        this.master = master;
        services = master.getHostServices();
        SCREEN_WIDTH = primaryScreenBounds.getMaxX();
        SCREEN_HEIGHT = primaryScreenBounds.getMaxY();
        this.actPers = actPers;
        detail_view = new Detail_view(actPers);
        scene = new Scene(detail_view.getRoot());
        mainConnector = new DB_connector();
        updateBoxBesprechungen();
        
        initActionHandlers();
    }
    
    public void setSceneToStage(Stage mainStage){
        this.mainStage = mainStage;
        this.mainStage.setScene(scene);
        this.mainStage.setMaximized(true);
        addHandler_closeRequest();

        
    }

    private void initActionHandlers() {
        
        addHandler_btnAddBesprechung();
        addHandler_btnBack();
        
        
    }
    // Action Handlers
    private void addHandler_btnBack() {
        detail_view.getBtnBack().setOnAction((event) -> {
            
            // Daten werden in DB gespeichert
            try {
                saveBesprechungsData();
                saveLaufbahnData();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            if(popUpStage != null) popUpStage.hide();     
            // Neues Overview Fenster wird geladen und gezeigt
            try {
                overview_controller = new Overview_controller(master);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            overview_controller.setSceneToStage(mainStage);
        });
    }
    private void addHandler_closeRequest() {
            mainStage.setOnCloseRequest(event -> {
                // Daten werden in DB gespeichert
                try {
                    saveBesprechungsData();
                    saveLaufbahnData();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    private void addHandler_btnAddBesprechung() {
           detail_view.getBtnAddBesprechung().setOnAction((event) -> {

               if(detail_view.getBoxBesprechungen().getChildren().size() <= 9){                
                   // Neue Besprechung wird in DB erstellt und Programm aktualisiert
                   try {
                       mainConnector = new DB_connector();
                       mainConnector.insertBesprechung(Integer.valueOf(actPers.getId()));
                       mainConnector.closeConnection();
                       updateBoxBesprechungen();
                       
                       // 
                       detail_view.getRightPart().getChildren().clear();
                       detail_view.getRightPart().getChildren().add(createBesprechungPage(listBesprechungen.get(listBesprechungen.size() -1))); 
                       detail_view.getBoxBesprechungen().getChildren().get(detail_view.getBoxBesprechungen().getChildren().size() -1).getStyleClass().add("selectedBesp");
                       

                   } catch (ClassNotFoundException ex) {
                       Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (SQLException ex) {
                       Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                   }

               }
               else
                   // Popup mit Fehler wird angezeigt
               {
                   popUpStage = new Popup();

                   VBox popUpRoot = new VBox();
                   popUpRoot.getStyleClass().add("popUpBackground");
                   popUpRoot.setPrefSize(400, 250);
                   popUpRoot.setAlignment(Pos.CENTER);


                   Label lblErrorTitle = new Label("Fehler");
                   lblErrorTitle.getStyleClass().addAll("whiteLabel", "title");
                   Separator separator = new Separator();
                   Label lblError = new Label("Maximale Anzahl der Besprechungen liegt bei 10!");
                   lblError.getStyleClass().add("whiteLabel");
                   Label lblError2 = new Label("Löschen Sie zunächst eine Besprechung");
                   lblError2.getStyleClass().add("whiteLabel");
                   Button btnOk = new Button("Schliessen");
                   btnOk.setOnAction(event2 -> {
                       popUpStage.hide();
                   });

                   popUpRoot.getChildren().addAll(lblErrorTitle, separator, lblError, lblError2, btnOk);
                   popUpRoot.setSpacing(20);
                   popUpRoot.getStylesheets().addAll(detail_view.getRoot().getStylesheets());

                   Scene scenePopUp = new Scene(popUpRoot);
                   popUpStage.getContent().addAll(popUpRoot);                
                   popUpStage.show(mainStage);
               }


           });
       }

    private VBox createBesprechungPage(Besprechung besp) throws SQLException, ClassNotFoundException {
        VBox root = new VBox();
        
        root.setPadding(new Insets(30));
        root.setSpacing(15);
        
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(150);
        TextField tfBespTitle = new TextField();
        
        tfBespTitle.getStyleClass().add("titleBesp");
        tfBespTitle.textProperty().bindBidirectional(besp.titleProperty());
        
        Label lblDate = new Label();
        lblDate.getStyleClass().add("dateLbl");
        lblDate.setText(String.valueOf(besp.getDatum()));
        
        titleBox.getChildren().addAll(tfBespTitle, lblDate);
        
        Separator sep = new Separator();
        
        TextArea taKommentar = new TextArea();
        taKommentar.getStyleClass().add("rounded");
        taKommentar.textProperty().bindBidirectional(besp.kommentarProperty());
        
        Separator sep2 = new Separator();
        
        boxDocuments = new HBox();
        boxDocuments.getStyleClass().add("backgroundField");
        boxDocuments.setPadding(new Insets(20));
        
        Button btnAddDocument = new Button();
        ImageView btnAddBespImg = new ImageView(detail_view.getClass().getResource("Images/add.png").toExternalForm());
        btnAddBespImg.setFitWidth( SCREEN_WIDTH / 60);
        btnAddBespImg.setFitHeight( SCREEN_WIDTH / 60);
        btnAddDocument.setPadding(new Insets(10));
        btnAddDocument.setGraphic(btnAddBespImg);
        btnAddDocument.setOnAction(event -> {
            
            FileChooser.ExtensionFilter fileExtensions =  new FileChooser.ExtensionFilter("PDF's", "*.pdf");     
            chooser.getExtensionFilters().add(fileExtensions);
            
            selectedFile = chooser.showOpenDialog(mainStage);
            personsDir = Paths.get(rootDir.toString() + "\\" + actPers.getName() + "_" + actPers.getVorname() + "_" + actPers.getPersonalnummer());
            newFile = new File(personsDir.toString() + "\\" + selectedFile.getName());
            
            
            try {
                mainConnector = new DB_connector();
                mainConnector.insertDocument(newFile, besp);
                mainConnector.closeConnection();
                updateDocumentsBox(besp);
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                Files.copy(selectedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
            } catch (IOException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
        
        root.getChildren().addAll(titleBox, sep, taKommentar, sep2, boxDocuments, btnAddDocument);
        tfBespTitle.selectAll();
        updateDocumentsBox(besp);
        return root;
    } 
    
    private void saveLaufbahnData() {
        try {
                mainConnector = new DB_connector();
                
                TextField schule_comment, lehre_comment, skills_comment, ausland_comment;
                TextArea  weiterbildung_comment;
                CheckBox skills_checked, ausland_checked, weiterbildung_checked;
                ComboBox schule_bez, lehre_bez;
                
                schule_bez = (ComboBox)detail_view.getListSkills().get(0).getChildren().get(0);
                lehre_bez = (ComboBox)detail_view.getListSkills().get(1).getChildren().get(0);
                
                schule_comment = (TextField)detail_view.getListSkills().get(0).getChildren().get(1);
                lehre_comment = (TextField)detail_view.getListSkills().get(1).getChildren().get(1);
                skills_comment = (TextField)detail_view.getListSkills().get(2).getChildren().get(1);
                ausland_comment = (TextField)detail_view.getListSkills().get(3).getChildren().get(1);
                weiterbildung_comment = (TextArea)detail_view.getListSkills().get(4).getChildren().get(1);
                
                skills_checked = (CheckBox)detail_view.getListSkills().get(2).getChildren().get(0);
                ausland_checked = (CheckBox)detail_view.getListSkills().get(3).getChildren().get(0);
                weiterbildung_checked = (CheckBox)detail_view.getListSkills().get(4).getChildren().get(0);
                
                mainConnector.updateLaufbahn(String.valueOf(schule_bez.getValue()), schule_comment.getText(), String.valueOf(lehre_bez.getValue()), lehre_comment.getText(), skills_checked.isSelected(), skills_comment.getText(), ausland_checked.isSelected(), ausland_comment.getText(), weiterbildung_checked.isSelected(), weiterbildung_comment.getText(), Integer.valueOf(actPers.getId()));
                mainConnector.closeConnection();
                
                
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    private void saveBesprechungsData() throws ClassNotFoundException, SQLException{
        mainConnector = new DB_connector();
        
        for(Besprechung besp : listBesprechungen){
            mainConnector.updateBesprechung(besp);
        }
        
        mainConnector.closeConnection();
    }
    
    private void updateBoxBesprechungen() throws SQLException, ClassNotFoundException {
        saveBesprechungsData();
        
        mainConnector = new DB_connector();
        ResultSet set = mainConnector.getBesprechungenFromPerson(actPers.getId());
        mainConnector.closeConnection();
        
        listBesprechungen.clear();
        detail_view.getBoxBesprechungen().getChildren().clear();
        while(set.next()){
            
            String title = set.getString("title");
            Date date = set.getDate("datum");
            String kommentar = set.getString("kommentar");
            int id = set.getInt("id");
            
            Besprechung besp = new Besprechung(title, date, kommentar, id);
            listBesprechungen.add(besp);            
            
        }
        
        for(Besprechung besp : listBesprechungen){
            VBox box = new VBox();
            box.getStyleClass().add("bespBox");
            box.setOnMouseReleased((event) -> {
                detail_view.getRightPart().getChildren().clear();
                try { 
                    detail_view.getRightPart().getChildren().add(createBesprechungPage(besp));
                } catch (SQLException ex) {
                    Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(Node boxie : detail_view.getBoxBesprechungen().getChildren()){
                    boxie.getStyleClass().remove("selectedBesp");
                }
                box.getStyleClass().add("selectedBesp");
                
            });
            box.setSpacing(10);
            box.getStyleClass().add("backgroundField");
            box.setPadding(new Insets(10));
            
            Label lblTitle = new Label();
            lblTitle.textProperty().bind(besp.titleProperty());
            
            HBox boxBottom = new HBox();
            boxBottom.setAlignment(Pos.CENTER_LEFT);
            boxBottom.setSpacing(20);
            
            Label lblDate = new Label(String.valueOf(besp.getDatum()));
            
            
            Button btnDeleteBesp = new Button();
            
            ImageView imgBtnDelete = new ImageView(detail_view.getClass().getResource("Images/close.png").toString());
            btnDeleteBesp.setGraphic(imgBtnDelete);
            btnDeleteBesp.getStyleClass().clear();
            btnDeleteBesp.getStyleClass().add("noBackground");
            btnDeleteBesp.setOnAction(event2 -> {

                popUpStage = new Popup();
                VBox popUpRoot = new VBox();
                popUpRoot.getStyleClass().add("popUpBackground");
                popUpRoot.setPrefSize(400, 250);
                popUpRoot.setAlignment(Pos.CENTER);
                
                Label lblErrorTitle = new Label("Besprechung Löschen");
                lblErrorTitle.getStyleClass().addAll("whiteLabel", "title");
                Separator separator = new Separator();
                Label lblError = new Label("Sie sind dabei diese Besprechung zu löschen");
                lblError.getStyleClass().add("whiteLabel");
                Label lblError2 = new Label("Sicher dass Sie forfahren möchten?");
                lblError2.getStyleClass().add("whiteLabel");
                HBox btnBox = new HBox();
                btnBox.setAlignment(Pos.CENTER);
                btnBox.setSpacing(20);

                Button btnOk = new Button("Ja");
                btnOk.setOnAction(event3 -> {
                    try {
                        mainConnector = new DB_connector();
                        mainConnector.deleteBesprechung(besp.getId());
                        mainConnector.closeConnection();
                        updateBoxBesprechungen();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(Detail_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    popUpStage.hide();
                    if(detail_view.getBoxBesprechungen().getChildren().isEmpty()){
                        detail_view.getRightPart().getChildren().clear();
                    }
                    else
                    {
                        Event.fireEvent(detail_view.getBoxBesprechungen().getChildren().get(detail_view.getBoxBesprechungen().getChildren().size() -1), new MouseEvent(MouseEvent.MOUSE_RELEASED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, saved, saved, saved, saved, saved, saved, saved, saved, saved, saved, null));

                    }
                });

                Button btnNo = new Button("Nein");
                btnNo.setOnAction(event3 -> {
                    popUpStage.hide();                    
                });

                popUpRoot.getChildren().addAll(lblErrorTitle, separator, lblError, lblError2, btnBox);
                btnBox.getChildren().addAll(btnOk, btnNo);
                popUpRoot.setSpacing(20);
                popUpRoot.getStylesheets().addAll(detail_view.getRoot().getStylesheets());
                popUpStage.getContent().add(popUpRoot);
                popUpStage.show(mainStage);
                    
         
            });
            
            box.getChildren().addAll(lblTitle, boxBottom);
                boxBottom.getChildren().addAll(lblDate, btnDeleteBesp);
            
            detail_view.getBoxBesprechungen().getChildren().add(box);
        }
       
    }
    private void updateDocumentsBox(Besprechung besp) throws SQLException, ClassNotFoundException {
        mainConnector = new DB_connector();
        ArrayList<String> setPaths = mainConnector.getDocumentsOfBesprechung(besp);
        mainConnector.closeConnection();

        boxDocuments.getChildren().clear();

        for(String path : setPaths){
            File newFile = new File(path);
            Button btn = new Button(newFile.getName());
            btn.setOnAction(event2 -> {
                
                services.showDocument(newFile.toString());                
                
            });
            boxDocuments.getChildren().add(btn);
        }    
    }
    
}
