package hr.algebra.humanitarnaorganizacija.model;

import java.util.Objects;

public class Campaign implements Comparable<Campaign>, Identifiable {

    private int ID;
    private String campaignTitle;
    private double budget;
    private String deadLine;

    //CONSTRUCTORS
        // -> fill via set
    public Campaign() {
    }
        // -> read from db
    public Campaign(int ID, String campaignTitle, double budget, String deadLine) {
        this.ID = ID;
        this.campaignTitle = campaignTitle;
        this.budget = budget;
        this.deadLine = deadLine;
    }


    //GETTER + SETTER
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }


    //COMPARE BY TITLE
    @Override
    public int compareTo(Campaign other) {
        return this.campaignTitle.compareTo(other.campaignTitle);
    }

    //EQUALS + HASH CODE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Campaign campaign)) return false;
        return ID == campaign.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    //TO STRING
    @Override
    public String toString() {
        return "Campaign{" +
                "campaignTitle='" + campaignTitle + '\'' +
                ", budget=" + budget +
                ", deadLine='" + deadLine + '\'' +
                '}';
    }
}
