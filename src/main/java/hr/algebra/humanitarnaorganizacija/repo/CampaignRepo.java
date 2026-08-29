package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.interface_.ICrud;
import hr.algebra.humanitarnaorganizacija.model.Campaign;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampaignRepo implements ICrud<Campaign, Integer> {

    /// LOGGER MECHANISM ////
    private static final Logger log = LoggerFactory.getLogger(CampaignRepo.class);

    /// SINGLETON  ////////
    private static final CampaignRepo INSTANCE = new CampaignRepo();

    private CampaignRepo() {
    }

    public static CampaignRepo getInstance() {
        return INSTANCE;
    }

    /// SQL STATEMENTS ////////
    private static final String CAMPAIGN_FIND_ALL = """
            SELECT ID, CampaignTitle, Budget,Deadline FROM Campaign
            """;
    private static final String CAMPAIGN_SAVE_TO_DB = """
            INSERT INTO Campaign (CampaignTitle, Budget, Deadline) 
            VALUES (?,?,?); 
            """;
    private static final String CAMPAIGN_DELETE_BY_ID = """
            DELETE FROM Campaign
            WHERE Campaign.ID = ?
            """;
    private static final String CAMPAIGN_UPDATE = """
            UPDATE Campaign
            SET 
            CampaignTitle = ?, 
            Budget = ?, 
            Deadline = ?
            WHERE ID = ?
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
            String msg = "Can not find Campaigns";
            log.error(msg, e);
            throw new RepoException(msg, e);
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
            String msg = "Can not find Campaigns by ID";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Campaign entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(CAMPAIGN_SAVE_TO_DB)) {
            preparedStatement.setString(1, entity.getCampaignTitle());
            preparedStatement.setDouble(2, entity.getBudget());
            preparedStatement.setString(3, entity.getDeadLine());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not insert Campaigns";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void deleteById(Integer integer) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(CAMPAIGN_DELETE_BY_ID)) {
              preparedStatement.setInt(1, integer);
              preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not delete Campaign";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void update(Campaign entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(CAMPAIGN_UPDATE)) {
            preparedStatement.setString(1, entity.getCampaignTitle());
            preparedStatement.setDouble(2, entity.getBudget());
            preparedStatement.setString(3, entity.getDeadLine());
            preparedStatement.setInt(4, entity.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not update Campaign";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }
}





