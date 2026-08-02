package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Mission;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MissionRepo implements ICrud<Mission, Integer> {

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
            throw new RepoException("Error while fetching Missions", e);
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
            throw new RepoException("Error while finding Missions by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Mission entity) throws AppException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_SAVE_TO_DB)) {
            preparedStatement.setString(1, entity.getMissionTitle());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Can not save Mission", e);
        }
    }

    @Override
    public void deleteById(Integer integer) throws AppException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_DELETE_BY_ID)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepoException("Can not delete Mission, possible invalid ID", e);
        }
    }

    @Override
    public void update(Mission entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(MISSION_UPDATE_BY_ID)) {
            preparedStatement.setString(1, entity.getMissionTitle());
            preparedStatement.setInt(2, entity.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Can not update Mission", e);
        }
    }
}
