/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.View;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import laufbahn_manager.Controller.DB_connector;
import laufbahn_manager.Model.Person;

/**
 *
 * @author nicolasvondru
 */
public class Detail_view {
    
    private Person actPers;
    
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private double SCREEN_WIDTH, SCREEN_HEIGHT;
    private HBox root;
    private VBox leftPart, middlePart, rightPart;
    private VBox boxPersonData, boxPersonSkills, boxBesprechungen;
    private Label dataTitle ,lblName, lblVorname, lblBeruf, lblAbteilung, lblPersonalnummer;
    private Separator sep1, sep2, sep3, sep4, sep5, sep6, sep7;
    private ArrayList<VBox> listSkills = new ArrayList<>();
    private Button btnSaveSkill, btnAddBesprechung, btnBack;
    
    private HBox boxBtnAddSkill, boxBtnAddBesprechung, boxBtnAddDocument, boxBtnAddErinnerung;
    
    
    
    public Detail_view(Person actPers) throws ClassNotFoundException, SQLException{
        
        this.actPers = actPers;
        
        // Initialisierung
        SCREEN_WIDTH = primaryScreenBounds.getMaxX();
        SCREEN_HEIGHT = primaryScreenBounds.getMaxY();
        root = new HBox();
        root.getStylesheets().add(getClass().getResource("style.css").toString());
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //Linke Seite
        leftPart = new VBox();
        leftPart.setPrefWidth((SCREEN_WIDTH / 10) * 3.5);
        leftPart.setPadding(new Insets(10));
        leftPart.setSpacing(10);
        
        btnBack = new Button();
        ImageView btnBackImg = new ImageView(getClass().getResource("Images/back.png").toExternalForm());
        btnBackImg.setFitWidth( SCREEN_WIDTH / 60);
        btnBackImg.setFitHeight( SCREEN_WIDTH / 60);
        btnBack.setPadding(new Insets(10));
        btnBack.setGraphic(btnBackImg);
        
        // Person Data
        boxPersonData = new VBox();
        boxPersonData.setPadding(new Insets(10, 25, 10, 25));
        boxPersonData.setSpacing(5);
        boxPersonData.getStyleClass().add("backgroundField");
        
        HBox boxDataTitle = new HBox();
        boxDataTitle.setSpacing(SCREEN_WIDTH / 10);
        
        dataTitle = new Label("Personalien");
        dataTitle.getStyleClass().add("titlePersonData");
        lblName = new Label("Name: ");
        lblVorname = new Label("Vorname: ");
        lblBeruf = new Label("Beruf: ");
        lblAbteilung = new Label("Abteilung: ");
        lblPersonalnummer = new Label("Personalnummer: ");
        
        sep3 = new Separator();
        
        //Skills
        boxPersonSkills = new VBox();
        boxPersonSkills.setPadding(new Insets(10));
        boxPersonSkills.setSpacing(10);        
        boxPersonSkills.setAlignment(Pos.TOP_CENTER);
        boxPersonSkills.getStyleClass().add("backgroundField");
        
        // Schule
        VBox boxSchule = new VBox();  
        boxSchule.setSpacing(15);
        boxSchule.setAlignment(Pos.CENTER);
        boxSchule.setPadding(new Insets(5, 35, 5, 35));
        boxSchule.getStyleClass().add("skillBox");
        listSkills.add(boxSchule);
        ComboBox cbSchule = new ComboBox();
        cbSchule.getItems().addAll("Schule" ,"Berufsschule (ohne BMS)", "Berufsschule (mit BMS)");
        TextField tfSchule = new TextField();
        
        // Ausbildung
        VBox boxAusbildungsstatus = new VBox(); 
        boxAusbildungsstatus.setAlignment(Pos.CENTER);
        boxAusbildungsstatus.setPadding(new Insets(5, 35, 5, 35));
        boxAusbildungsstatus.setSpacing(15);
        boxAusbildungsstatus.getStyleClass().add("skillBox");
        listSkills.add(boxAusbildungsstatus);
        ComboBox cbAusbildungsstatus = new ComboBox();
        cbAusbildungsstatus.getItems().addAll("Ausbildungsstatus","In Ausbildung (1. Lehrjahr)", "In Ausbildung (2. Lehrjahr)", "In Ausbildung (3. Lehrjahr)", "In Ausbildung (4. Lehrjahr)", "Ausbildung abgeschlossen");
        TextField tfAusbildungsstatus = new TextField();
        
        // Skills
        VBox boxSkills = new VBox();
        boxSkills.setAlignment(Pos.CENTER);
        boxSkills.setSpacing(15);
        boxSkills.getStyleClass().add("skillBox");
        boxSkills.setPadding(new Insets(5, 35, 5, 35));
        ComboBox cbSkills = new ComboBox();
        cbSkills.getItems().addAll("Skills", "Regionalmeisterschaft", "Swiss Skills", "World Skills");
        TextField commentSkills = new TextField();        
        listSkills.add(boxSkills);
        
        // Auslandeinsatz
        VBox boxAusland = new VBox();
        boxAusland.setAlignment(Pos.CENTER);
        boxAusland.setSpacing(15);
        boxAusland.getStyleClass().add("skillBox");
        boxAusland.setPadding(new Insets(5, 35, 5, 35));
        CheckBox checkboxAusland = new CheckBox("War im Auslandeinsatz");
        TextField commentAusland = new TextField();        
        listSkills.add(boxAusland);
        
        // Weiterbildungen / Projekte
        VBox boxWeiterbildung = new VBox();
        boxWeiterbildung.setAlignment(Pos.CENTER);
        boxWeiterbildung.setSpacing(15);
        boxWeiterbildung.getStyleClass().add("skillBox");
        boxWeiterbildung.setPadding(new Insets(5, 35, 5, 35));
        CheckBox checkboxWeiterbildung = new CheckBox("Aus- & Weiterbildungen / Projekte");
        TextArea commentWeiterbildung = new TextArea();
        commentWeiterbildung.setPrefHeight(SCREEN_HEIGHT / 14);
        listSkills.add(boxWeiterbildung);
        
        sep4 = new Separator();       
        
        
        // Mittlerer Part
        sep1 = new Separator(Orientation.VERTICAL);
         
        middlePart = new VBox();
        middlePart.setPrefWidth((SCREEN_WIDTH / 10 * 1.5));
        middlePart.setPadding(new Insets(10));
        boxBesprechungen = new VBox();
        boxBesprechungen.setPrefHeight(SCREEN_HEIGHT / 10 * 9);
        boxBesprechungen.setSpacing(5);
        
        
        boxBtnAddBesprechung = new HBox();
        boxBtnAddBesprechung.setAlignment(Pos.CENTER);
        btnAddBesprechung = new Button();
        ImageView btnAddBespImg = new ImageView(getClass().getResource("Images/add.png").toExternalForm());
        btnAddBespImg.setFitWidth( SCREEN_WIDTH / 60);
        btnAddBespImg.setFitHeight( SCREEN_WIDTH / 60);
        btnAddBesprechung.setPadding(new Insets(10));
        btnAddBesprechung.setGraphic(btnAddBespImg);
        
        
        sep2 = new Separator(Orientation.VERTICAL);
        
        rightPart = new VBox();
        rightPart.setPrefWidth(SCREEN_WIDTH / 2);
        
        //Layout
        root.getChildren().addAll(leftPart, sep1, middlePart, sep2, rightPart);
            leftPart.getChildren().add(boxPersonData);
                boxPersonData.getChildren().addAll(boxDataTitle, lblName, lblVorname, lblBeruf, lblAbteilung, lblPersonalnummer);
                    boxDataTitle.getChildren().addAll(dataTitle, btnBack);
            leftPart.getChildren().add(sep3);
            leftPart.getChildren().add(boxPersonSkills);
                boxPersonSkills.getChildren().add(boxSchule);
                    boxSchule.getChildren().addAll(cbSchule, tfSchule);
                boxPersonSkills.getChildren().add(boxAusbildungsstatus);
                    boxAusbildungsstatus.getChildren().addAll(cbAusbildungsstatus, tfAusbildungsstatus);
                boxPersonSkills.getChildren().add(boxSkills);
                    boxSkills.getChildren().addAll(cbSkills, commentSkills);
                boxPersonSkills.getChildren().add(boxAusland);
                    boxAusland.getChildren().addAll(checkboxAusland, commentAusland);
                boxPersonSkills.getChildren().add(boxWeiterbildung);
                    boxWeiterbildung.getChildren().addAll(checkboxWeiterbildung, commentWeiterbildung);
                    

            middlePart.getChildren().add(boxBesprechungen);
            middlePart.getChildren().add(boxBtnAddBesprechung);
                boxBtnAddBesprechung.getChildren().add(btnAddBesprechung);
            
                
                
        initData();
    }
    
    //Methods   
    
    //Getter / Setter
    public HBox getRoot(){
        return root;
    }

    private void initData() throws ClassNotFoundException, SQLException {
        
        bindPersonData();        
        fillLaufbahnFelder();   
        
    }
    
    
    private void fillLaufbahnFelder() throws ClassNotFoundException, SQLException{
      DB_connector mainConnector = new DB_connector();
        
        ResultSet laufbahn = mainConnector.getLaufbahnOfPerson(Integer.valueOf(actPers.getId()));
        mainConnector.closeConnection();
        while(laufbahn.next()){
            String schule_bez = laufbahn.getString("schule_bez");
            String schule_comment = laufbahn.getString("schule_comment");
            String lehre_bez = laufbahn.getString("lehre_bez");
            String lehre_comment = laufbahn.getString("lehre_comment");
            String skills_bez = laufbahn.getString("skills_bez");
            String skills_comment = laufbahn.getString("skills_comment");
            boolean ausland_checked = laufbahn.getBoolean("ausland_checked");
            String ausland_comment = laufbahn.getString("ausland_comment");
            boolean weiterbildung_checked = laufbahn.getBoolean("weiterbildung_checked");
            String weiterbildung_comment = laufbahn.getString("weiterbildung_comment");
            
            ComboBox cbSchule = (ComboBox)listSkills.get(0).getChildren().get(0);
            cbSchule.setValue(schule_bez);
            TextField tfSchule = (TextField)listSkills.get(0).getChildren().get(1);
            tfSchule.setText(schule_comment);
            
            ComboBox cbAusbildung = (ComboBox)listSkills.get(1).getChildren().get(0);
            cbAusbildung.setValue(lehre_bez);
            TextField tfAusbildung = (TextField)listSkills.get(1).getChildren().get(1);
            tfAusbildung.setText(lehre_comment);
            
            ComboBox cbSkills = (ComboBox)listSkills.get(2).getChildren().get(0);
            cbSkills.setValue(skills_bez);
            TextField tfSkills = (TextField)listSkills.get(2).getChildren().get(1);
            tfSkills.setText(skills_comment);
            
            CheckBox cbAusland = (CheckBox)listSkills.get(3).getChildren().get(0);
            cbAusland.setSelected(ausland_checked);
            TextField tfAusland = (TextField)listSkills.get(3).getChildren().get(1);
            tfAusland.setText(ausland_comment);
            
            CheckBox cbWeiterbildung = (CheckBox)listSkills.get(4).getChildren().get(0);
            cbWeiterbildung.setSelected(weiterbildung_checked);
            TextArea tfWeiterbildung = (TextArea)listSkills.get(4).getChildren().get(1);
            tfWeiterbildung.setText(weiterbildung_comment);
        }  
    }
    private void bindPersonData(){
        lblName.textProperty().bind(Bindings.concat("Name: ").concat(actPers.nameProperty()));
        lblVorname.textProperty().bind(Bindings.concat("Vorname: ").concat(actPers.vornameProperty()));
        lblBeruf.textProperty().bind(Bindings.concat("Beruf: ").concat(actPers.berufProperty()));
        lblAbteilung.textProperty().bind(Bindings.concat("Abteilung: ").concat(actPers.abteilungProperty()));
        lblPersonalnummer.textProperty().bind(Bindings.concat("Personalnummer: ").concat(actPers.personalnummerProperty()));
    }
    
    //Getetr / Setter

    public Button getBtnAddBesprechung() {
        return btnAddBesprechung;
    }

    public VBox getBoxBesprechungen() {
        return boxBesprechungen;
    }

    public Button getBtnBack() {
        return btnBack;
    }

    public VBox getRightPart() {
        return rightPart;
    }

    public ArrayList<VBox> getListSkills() {
        return listSkills;
    }

    public Button getBtnSaveSkill() {
        return btnSaveSkill;
    }
    
    
    
    
}
