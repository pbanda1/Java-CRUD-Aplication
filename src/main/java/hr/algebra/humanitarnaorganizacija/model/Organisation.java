package hr.algebra.humanitarnaorganizacija.model;

import hr.algebra.humanitarnaorganizacija.interface_.Identifiable;

import java.util.Objects;

public class Organisation implements Comparable<Organisation>, Identifiable {

    //PROPS
    private int ID;
    private String title;
    private int yearEstablishment;
    private int numOfEmployees;
    private double yearlyBudget;
    private String endGoal;
    private String Logo;

    //CONNECTED ENTITIES
        private Country country; //from api
        private Mission mission;
        private Volunteer volunteer;
        private Sponsor sponsor;
        private Campaign campaign;

    //CONSTRUCTORS

    public Organisation() {
    }

        // -> read from db
    public Organisation(int ID, String title, int yearEstablishment,
                        int numOfEmployees, double yearlyBudget,
                        String mission, String logo,
                        Country country, Mission mission1,
                        Volunteer volunteer, Sponsor sponsor, Campaign campaign) {
        this.ID = ID;
        this.title = title;
        this.yearEstablishment = yearEstablishment;
        this.numOfEmployees = numOfEmployees;
        this.yearlyBudget = yearlyBudget;
        endGoal = mission;
        Logo = logo;
        this.country = country;
        this.mission = mission1;
        this.volunteer = volunteer;
        this.sponsor = sponsor;
        this.campaign = campaign;
    }


    //GETTERS + SETTERS
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getYearEstablishment() {
        return yearEstablishment;
    }
    public void setYearEstablishment(int yearEstablishment) {
        this.yearEstablishment = yearEstablishment;
    }

    public int getNumOfEmployees() {
        return numOfEmployees;
    }
    public void setNumOfEmployees(int numOfEmployees) {
        this.numOfEmployees = numOfEmployees;
    }

    public double getYearlyBudget() {
        return yearlyBudget;
    }
    public void setYearlyBudget(double yearlyBudget) {
        this.yearlyBudget = yearlyBudget;
    }

    public String getEndGoal() {
        return endGoal;
    }
    public void setEndGoal(String endGoal) {
        this.endGoal = endGoal;
    }

    public String getLogo() {
        return Logo;
    }
    public void setLogo(String logo) {
        Logo = logo;
    }

    public Country getCountry() {
        return country;
    }
    public void setCountry(Country country) {
        this.country = country;
    }


    public Mission getMission() {
        return mission;
    }
    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }
    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }
    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Campaign getCampaign() {
        return campaign;
    }
    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }


    //COMPARE TO
    @Override
    public int compareTo(Organisation o) {
        return this.title.compareTo(o.title);
    }

    //EQ + HC
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof Organisation that)) return false;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    //toStr

    @Override
    public String toString() {
        return "Organisation{" +
                "title='" + title + '\'' +
                '}';
    }
}
