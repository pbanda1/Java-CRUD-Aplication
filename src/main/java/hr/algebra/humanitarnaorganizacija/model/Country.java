package hr.algebra.humanitarnaorganizacija.model;

public class Country implements Identifiable {

    private int ID;
    private String stateName;

    public Country() {
    }

    //READ FROM DB
    //cita redak iz baze (tablica) i pretvara ga u java objekt
    public Country(int ID, String stateName) {
        this.ID = ID;
        this.stateName = stateName;
    }
     //ADD TO DB via API call
    public Country(String stateName) {
        this.stateName = stateName;
    }

    //GETTER + SETTER
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }


}
