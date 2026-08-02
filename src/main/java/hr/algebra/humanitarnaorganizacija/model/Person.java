package hr.algebra.humanitarnaorganizacija.model;

import java.util.Objects;

public abstract class Person implements Identifiable {

    protected int ID;
    protected String name;
    protected String surName;

    //pozivam defaultni konstruktor jer podklasa poziva prvo roditeljski dio
    protected Person() {
    }

    //konstruktor kada povlačim osobu iz baze - tj. podklase koje naslijeđuju ovaj konstruktor
    protected Person(int ID, String name, String surName) {
        this.ID = ID;
        this.name = name;
        this.surName = surName;
    }

    //konstruktor kada dodajem osobu u bazu - tj. sve izvedenice ove klase
    protected Person(String name, String surName) {
        this.name = name;
        this.surName = surName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public abstract String getClassRole();  //vraćam uloge svake osobe

    //HASH-CODE-METHOD
    @Override
    public int hashCode() {
        return Objects.hash(ID);   // hash po Id-u, ne po identitetu
    }

    //EQUALS METHOD
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false; //usporedujem instance a ne Person u childsima
        Person person = (Person) obj; //castanje u Person
        return ID == person.ID;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                '}';
    }
}
