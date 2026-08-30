package hr.algebra.humanitarnaorganizacija.model;

public class Volunteer extends Person implements Comparable<Volunteer> {

    private String specialisation;
    private int hoursNum;


    public enum VolunteerStatus {
        Active,
        Inactive
    }

    private VolunteerStatus volunteerStatus;

    //SET VIA SETTERS
    public Volunteer() {
    }

    //DATABASE RETRIEVAL
    public Volunteer(int ID, String name, String surName, String specialisation, int hoursNum, VolunteerStatus volunteerStatus) {
        super(ID, name, surName);
        this.specialisation = specialisation;
        this.hoursNum = hoursNum;
        this.volunteerStatus = volunteerStatus;
    }

    //DATABASE INSERT
    public Volunteer(String name, String surName, String specialisation, int hoursNum, VolunteerStatus volunteerStatus) {
        super(name, surName);
        this.specialisation = specialisation;
        this.hoursNum = hoursNum;
        this.volunteerStatus = volunteerStatus;
    }

    //GETTERS + SETTERS
    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public int getHoursNum() {
        return hoursNum;
    }

    public void setHoursNum(int hoursNum) {
        this.hoursNum = hoursNum;
    }

    public VolunteerStatus getVolunteerStatus() {
        return volunteerStatus;
    }

    public void setVolunteerStatus(VolunteerStatus volunteerStatus) {
        this.volunteerStatus = volunteerStatus;
    }


    //COMPARABLE METHOD by Name
    @Override
    public int compareTo(Volunteer other) {
        return getName().compareTo(other.getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String getClassRole() {
        return this.getClass().getSimpleName();
    }
}
