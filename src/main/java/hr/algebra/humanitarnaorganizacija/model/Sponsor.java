package hr.algebra.humanitarnaorganizacija.model;

import hr.algebra.humanitarnaorganizacija.model.enums.DonatorType;

public class Sponsor extends Person implements Comparable<Sponsor> {


    private DonatorType donatorType;

    //FILL WITH GET + SET (XML/JSON)
    public Sponsor() {
    }

    //READ FROM DB -

    public Sponsor(int ID, String name, String surName, DonatorType donatorType) {
        super(ID, name, surName);
        this.donatorType = donatorType;
    }

    //MAKE NEW
    public Sponsor(String name, String surName, DonatorType donatorType) {
        super(name, surName);
        this.donatorType = donatorType;
    }

    //GETTERS + SETTERS
    public DonatorType getDonatorType() {
        return donatorType;
    }

    public void setDonatorType(DonatorType donatorType) {
        this.donatorType = donatorType;
    }

    //OVERRIDE ABSTRACT
    @Override
    public String getClassRole() {
        return getClass().getSimpleName();
    }

    //COMPARE BY NAME
    @Override
    public int compareTo(Sponsor o) {
        return this.getName().compareTo(o.getName());
    }

    //TO STRING FROM PERSON
    @Override
    public String toString() {
        return super.toString() + " " + donatorType;
    }

    //HASH CODE + EQUALS
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
