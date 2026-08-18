package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Mission;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionRepo implements ICrud<Mission, Integer> {

    /// LOGGING MECHANISM ////
    private static final Logger log = LoggerFactory.getLogger(MissionRepo.class);

    /// SINGLETON //////////
    private static final MissionRepo INSTANCE = new MissionRepo();

    private MissionRepo() {
    }

    public static MissionRepo getInstance() {
        return INSTANCE;
    }

    /// SQL STATEMENT//////
    private static final String MISSION_FIND_ALL = """
               SELECT ID, MissionTitle FROM Mission
            """;
    private static final String MISSION_SAVE_TO_DB = """
            INSERT INTO Mission (MissionTitle)
            VALUES (?); 
            """;
    private static final String MISSION_DELETE_BY_ID = """
            DELETE FROM Mission 
            WHERE Mission.ID = ?; 
            """;
    private static final String MISSION_UPDATE_BY_ID = """
            UPDATE MISSION 
            SET MissionTitle = ? 
            WHERE Mission.ID = ?; 
            """;

    /// METHODS /////
    @Override
    public List<Mission> findAll() {
        List<Mission> missions = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                missions.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            String msg = "Error while fetching Missions";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return missions;

    }

    private Mission mapRow(ResultSet resultSet) throws SQLException {
        return new Mission(
                resultSet.getInt("ID"),
                resultSet.getString("MissionTitle")
        );
    }

    @Override
    public Optional<Mission> findById(Integer id) {

        String sql = MISSION_FIND_ALL + " WHERE ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            String msg = "Error while finding Missions by ID";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Mission entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_SAVE_TO_DB)) {
            preparedStatement.setString(1, entity.getMissionTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not save Mission";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void deleteById(Integer integer) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_DELETE_BY_ID)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            String msg = "Can not delete Mission, possible invalid ID";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void update(Mission entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_UPDATE_BY_ID)) {
            preparedStatement.setString(1, entity.getMissionTitle());
            preparedStatement.setInt(2, entity.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not update Mission";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }
}
