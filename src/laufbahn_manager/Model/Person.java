/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laufbahn_manager.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author vmadmin
 */
public class Person {

    private final StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }
    private final StringProperty beruf = new SimpleStringProperty();

    public String getBeruf() {
        return beruf.get();
    }

    public void setBeruf(String value) {
        beruf.set(value);
    }

    public StringProperty berufProperty() {
        return beruf;
    }

    
    private final StringProperty vorname = new SimpleStringProperty();

    public String getVorname() {
        return vorname.get();
    }

    public void setVorname(String value) {
        vorname.set(value);
    }

    public StringProperty vornameProperty() {
        return vorname;
    }
    private final StringProperty abteilung = new SimpleStringProperty();

    public String getAbteilung() {
        return abteilung.get();
    }

    public void setAbteilung(String value) {
        abteilung.set(value);
    }

    public StringProperty abteilungProperty() {
        return abteilung;
    }
    private final StringProperty personalnummer = new SimpleStringProperty();

    public String getPersonalnummer() {
        return personalnummer.get();
    }

    public void setPersonalnummer(String value) {
        personalnummer.set(value);
    }

    public StringProperty personalnummerProperty() {
        return personalnummer;
    }
    private final StringProperty id = new SimpleStringProperty();

    public String getId() {
        return id.get();
    }

    public void setId(String value) {
        id.set(value);
    }

    public StringProperty idProperty() {
        return id;
    }
    

    public Person(String name, String vorname, String beruf, String abteilung, String personalnummer, String id) {
        this.name.set(name);
        this.vorname.set(vorname);
        this.beruf.set(beruf);
        this.abteilung.set(abteilung);
        this.personalnummer.set(personalnummer);
        this.id.set(id);
    }
    
    
    
 
    
    
    
}
