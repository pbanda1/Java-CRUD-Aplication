package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Campaign;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampaignRepo implements ICrud<Campaign, Integer> {

    /// SINGLETON  ////////
    private static final CampaignRepo INSTANCE = new CampaignRepo();

    private CampaignRepo() {
    }

    public static CampaignRepo getInstance() {
        return INSTANCE;
    }

    /// SQL ////////
    private static final String CAMPAIGN_FIND_ALL = """
            SELECT ID, CampaignTitle, Budget,Deadline FROM Campaign
            """;

    @Override
    public List<Campaign> findAll() {
        List<Campaign> campaigns = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(CAMPAIGN_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                campaigns.add(mapRow(resultSet));
            }

        } catch (SQLException e) {
            throw new RepoException("Can not find Campaigns", e);
        }
        return campaigns;
    }

    private Campaign mapRow(ResultSet resultSet) throws SQLException {
        return new Campaign(
                resultSet.getInt("ID"),
                resultSet.getString("CampaignTitle"),
                resultSet.getDouble("Budget"),
                resultSet.getString("Deadline")
        );
    }

    @Override
    public Optional<Campaign> findById(Integer id) {

        String sql = CAMPAIGN_FIND_ALL + "WHERE ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            // first argument -> ? placeholder, second argument -> real value that we are connecting
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Can not find Campaigns by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Campaign entity) throws AppException {

    }

    @Override
    public void deleteById(Integer integer) throws AppException {

    }

    @Override
    public void update(Campaign entity) throws RepoException {

    }
}
