/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.View;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import laufbahn_manager.Controller.Detail_controller;
import laufbahn_manager.Controller.Overview_controller;
import laufbahn_manager.Laufbahn_Manager;
import laufbahn_manager.Model.Person;

/**
 *
 * @author vmadmin
 */
public class Overview_view {
    private VBox root;
    private Overview_controller overview_controller;
    private Laufbahn_Manager master;
    
    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private static double SCREEN_WIDTH, SCREEN_HEIGHT;
    
    private HBox boxSearchField;
    private TextField searchField;
    private ImageView searachIcon;
    
    private TableView personsTable;
    private TableColumn colName, colVorname, colBeruf, colAbteilung, colPersonalnummer, colDetail;
    
    private HBox boxCreateNewPerson;
    private TextField tfName, tfVorname, tfBeruf, tfAbteilung, tfPersonalnummer;
    private Button btnAdd;
    
    public Overview_view(Overview_controller overview_controller, Laufbahn_Manager master){
        this.master = master;
        this.overview_controller = overview_controller;
        SCREEN_WIDTH = primaryScreenBounds.getMaxX();
        SCREEN_HEIGHT = primaryScreenBounds.getMaxY();
        
        root = new VBox();
        root.getStylesheets().add(getClass().getResource("style.css").toString());
        root.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.setSpacing(30);
        root.setPadding(new Insets(50));
        
        boxSearchField = new HBox();
        boxSearchField.setSpacing(20);
        boxSearchField.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        // Search-Icon designed by 'http://www.simpleicon.com' from www.flaticon.com
        searachIcon = new ImageView(getClass().getResource("Images/scope.png").toString());
        searachIcon.setFitWidth(SCREEN_WIDTH / 35);
        searachIcon.setFitHeight(SCREEN_WIDTH / 35);
                
        
        Separator sep1 = new Separator();
        
        personsTable = new TableView();
        personsTable.setPrefHeight(SCREEN_HEIGHT / 2);       
        personsTable.setMaxHeight(SCREEN_HEIGHT / 2);
        personsTable.setColumnResizePolicy(personsTable.CONSTRAINED_RESIZE_POLICY);
        
        colName = new TableColumn("Nachname");
        colVorname = new TableColumn("Vorname");
        colBeruf = new TableColumn("Beruf");
        colAbteilung = new TableColumn("Abteilung");
        colPersonalnummer = new TableColumn("Personalnummer");
        colDetail = new TableColumn("Details");
        
        
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colVorname.setCellValueFactory(new PropertyValueFactory("vorname"));
        colBeruf.setCellValueFactory(new PropertyValueFactory("beruf"));
        colAbteilung.setCellValueFactory(new PropertyValueFactory("abteilung"));
        colPersonalnummer.setCellValueFactory(new PropertyValueFactory("personalnummer"));
        
        Callback<TableColumn<Person, String>, TableCell<Person, String>> detailCell
                = //
                new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell call(final TableColumn<Person, String> param) {
                final TableCell<Person, String> detailCell = new TableCell<Person, String>() {

                    Button btnGo = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btnGo.setOnAction(event -> {
                                int row = getTableRow().getIndex();
                                Person pers = (Person)personsTable.getItems().get(row);
                                try {
                                    overview_controller.setDetail_controller(new Detail_controller(pers, master));
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Overview_view.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (SQLException ex) {
                                    Logger.getLogger(Overview_view.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                Stage mainStage = overview_controller.getMainStage();
                                System.out.println(mainStage);
                                overview_controller.getDetail_controller().setSceneToStage(mainStage);
                                System.out.println("Halloo Welt");
                            });
                            btnGo.getStyleClass().add("btnGo");
                            ImageView btnGoImg = new ImageView(getClass().getResource("Images/next.png").toExternalForm());
                            btnGoImg.setFitWidth( SCREEN_WIDTH / 60);
                            btnGoImg.setFitHeight( SCREEN_WIDTH / 60);
                            btnGo.setGraphic(btnGoImg);
                            btnGo.setPadding(new Insets(10,10,10,10));                
                            
                            setGraphic(btnGo);
                            setText(null);
                        }
                    }
                };
                return detailCell;
            }
        };
        colDetail.setCellFactory(detailCell);
        colDetail.getStyleClass().add("centeredCol");
        personsTable.getColumns().addAll(colName, colVorname, colBeruf, colAbteilung, colPersonalnummer, colDetail);
        
        Separator sep2 = new Separator();
        
        boxCreateNewPerson = new HBox();
        boxCreateNewPerson.setSpacing(55);
        boxCreateNewPerson.setAlignment(Pos.CENTER_LEFT);
        
        tfName = new TextField();
        tfVorname = new TextField();
        tfBeruf = new TextField();
        tfAbteilung = new TextField();
        tfPersonalnummer = new TextField();
        
        tfName.setPromptText("Nachname");
        tfVorname.setPromptText("Vorname");
        tfBeruf.setPromptText("Beruf");
        tfAbteilung.setPromptText("Abteilung");
        tfPersonalnummer.setPromptText("Personalnummer");
        
        btnAdd = new Button();
        ImageView btnAddImg = new ImageView(getClass().getResource("Images/add.png").toExternalForm());
        btnAddImg.setFitWidth( SCREEN_WIDTH / 60);
        btnAddImg.setFitHeight( SCREEN_WIDTH / 60);
        btnAdd.setPadding(new Insets(10));
        btnAdd.setGraphic(btnAddImg);
        
        
        
        
        root.getChildren().add(boxSearchField);
            boxSearchField.getChildren().addAll(searachIcon, searchField);
        root.getChildren().add(sep1);
        root.getChildren().add(personsTable);
        root.getChildren().add(sep2);
        root.getChildren().add(boxCreateNewPerson);
            boxCreateNewPerson.getChildren().addAll(tfName, tfVorname, tfBeruf, tfAbteilung, tfPersonalnummer, btnAdd);
        
        
        
        
    }
    
    
    // Getter / Setter
    public VBox getRoot(){
        return root;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public TextField getTfName() {
        return tfName;
    }

    public TextField getTfVorname() {
        return tfVorname;
    }

    public TextField getTfBeruf() {
        return tfBeruf;
    }

    public TextField getTfAbteilung() {
        return tfAbteilung;
    }

    public TextField getTfPersonalnummer() {
        return tfPersonalnummer;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public HBox getBoxCreateNewPerson() {
        return boxCreateNewPerson;
    }

    public TableView getPersonsTable() {
        return personsTable;
    }
    
    
}
