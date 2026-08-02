package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.*;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganisationRepo implements ICrud<Organisation, Integer> {
    //SINGLETON PATTERN
    private static final OrganisationRepo INSTANCE = new OrganisationRepo();
    private OrganisationRepo(){};

    public static OrganisationRepo getInstance() {return INSTANCE;}


    private static final String ORGANISATION_FIND_ALL = """
            SELECT
                o.ID as OrgID,
                o.Title,
                o.EstablishmentYear,
                o.NumOfEmployees,
                o.YearlyBudget,
                o.EndGoal,
                o.Logo,
                country.ID as CountryID, country.StateName,
                mission.ID as MissionID, mission.MissionTitle,
   
                volunteer.ID as VolunteerID,
                volunteer.Name as VName,
                volunteer.Surname as VSurname,
                volunteer.Specialisation,
                volunteer.HoursNum,
                volunteer.Status,
           
                sponsor.ID as SponsorID,
                sponsor.Name as SName,
                Sponsor.Surname as SSurName,
                sponsor.DonatorType,
           
                campaign.ID as CampaignID,
                campaign.CampaignTitle,
                campaign.Budget,
                campaign.Deadline
            
            FROM Organisation  o
            JOIN Country country ON o.CountryID = country.ID
            JOIN Mission mission ON o.MissionID = mission.ID
            JOIN Volunteer volunteer ON o.VolunteerID = volunteer.ID
            JOIN Sponsor sponsor ON o.SponsorID = sponsor.ID
            JOIN Campaign campaign ON o.CampaignID = campaign.ID
            
            """;


    @Override
    public List<Organisation> findAll() {
        List<Organisation> organisation = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(ORGANISATION_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                organisation.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RepoException("Error while searching organisations", e);
        }
        return organisation;
    }

    private Organisation mapRow(ResultSet rs) throws SQLException {
        return new Organisation(
                rs.getInt("OrgID"),
                rs.getString("Title"),
                rs.getInt("EstablishmentYear"),
                rs.getInt("NumOfEmployees"),
                rs.getDouble("YearlyBudget"),
                rs.getString("EndGoal"),
                rs.getString("Logo"),
                new Country(rs.getInt("CountryID"), rs.getString("StateName")),
                new Mission(rs.getInt("MissionID"), rs.getString("MissionTitle")),
                new Volunteer(
                        rs.getInt("VolunteerID"),
                        rs.getString("VName"),
                        rs.getString("VSurname"),
                        rs.getString("Specialisation"),
                        rs.getInt("HoursNum"),
                        Volunteer.VolunteerStatus.valueOf(rs.getString("Status"))
                ),
                new Sponsor(
                        rs.getInt("SponsorID"),
                        rs.getString("SName"),
                        rs.getString("SSurName"),
                        Sponsor.DonatorType.valueOf(rs.getString("DonatorType"))
                ),

                new Campaign(
                        rs.getInt("CampaignID"),
                        rs.getString("CampaignTitle"),
                        rs.getDouble("Budget"),
                        rs.getString("Deadline")
                )
        );

    };

    @Override
    public Optional<Organisation> findById(Integer id) {
        String sql = ORGANISATION_FIND_ALL + " WHERE o.ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Error while fetching Organisations by id", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Organisation entity) throws AppException {

    }

    @Override
    public void deleteById(Integer integer) throws AppException {

    }

    @Override
    public void update(Organisation entity) throws RepoException {

    }
}
