package hr.algebra.humanitarnaorganizacija.model;

import java.util.Objects;

public class Mission implements Comparable<Mission>, Identifiable {
    //PROPS
    private int ID;
    private String missionTitle;

    //CONSTRUCTORS
        // -> fill via set
    public Mission() {
    }
        // -> read from db
    public Mission(int ID, String missionTitle) {
        this.ID = ID;
        this.missionTitle = missionTitle;
    }

    //GETTER + SETTER
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMissionTitle() {
        return missionTitle;
    }

    public void setMissionTitle(String missionTitle) {
        this.missionTitle = missionTitle;
    }

    //COMPARE TO
    @Override
    public int compareTo(Mission o) {
        return this.missionTitle.compareTo(o.missionTitle);
    }

    //TO STRING
    @Override
    public String toString() {
        return "Mission{" +
                "missionTitle='" + missionTitle + '\'' +
                '}';
    }

    //EQUALS + HASHCODE
            // --> eq
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mission )) return false;
        return ID == ((Mission) obj).ID;
    }
            // --> hc
    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}
