/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Model;

import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author vmadmin
 */
public class Besprechung {

    private final ObjectProperty<Date> datum = new SimpleObjectProperty<>();
    private final StringProperty kommentar = new SimpleStringProperty();
    private final StringProperty title = new SimpleStringProperty();
    private int id;

    public Besprechung(String title, Date date, String kommentar, int id){
        this.title.set(title);
        this.datum.set(date);
        this.kommentar.set(kommentar);
        this.id = id;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }
    
    
    
    public Date getDatum() {
        return datum.get();
    }

    public void setDatum(Date value) {
        datum.set(value);
    }

    public ObjectProperty datumProperty() {
        return datum;
    }
    

    public String getKommentar() {
        return kommentar.get();
    }

    public void setKommentar(String value) {
        kommentar.set(value);
    }

    public StringProperty kommentarProperty() {
        return kommentar;
    }
    public int getId(){
        return id;
    }
    
    
    
    
}
