package hr.algebra.humanitarnaorganizacija.model;

import hr.algebra.humanitarnaorganizacija.model.enums.Role;

public class AppUser extends Person implements Comparable<AppUser> {

    private String userName;
    private String passWord;
    private Role role;

    //SET VIA SETTERS
    public AppUser() {
    }

    // READ FROM DB,
    // Konstrukcija objekta na temelju pročitanog redka iz baze
    // baza čuva tekst kao VARCHAR - rs.GetRole
    // vraća string "Admin  || User" i.e. String Role
    public AppUser(int ID, String name, String surName, String userName, String passWord, String role) {
        super(ID, name, surName);
        this.userName = userName;
        this.passWord = passWord;
        this.role = Role.valueOf(role);
    }

    // CODE -
    // stvara potpuni objekt korisnika unutar Java koda,
    // kad imam pripremljen Role umjesto teksta
    public AppUser(int ID, String name, String surName, String userName, String passWord, Role role) {
        super(ID, name, surName);
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    //CHOOSE userName || admin
    public AppUser(String userName, String passWord, Role role) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    //REGISTRACIJA KORISNIKA
    public AppUser(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.role = Role.User;
    }


    //GETTERS + SETTERS
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }


    @Override
    public String getClassRole() {
        return getClass().getSimpleName();
    }

    //COMPARE USERNAMES REDUNDANCY
    @Override
    public int compareTo(AppUser other) {
        return userName.compareTo(other.userName);
    }

    //EQUALS AND HASH CODE INHERIT
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    //TO STRING

    @Override
    public String toString() {
        return super.toString() + "role: " + this.role;
    }
}
